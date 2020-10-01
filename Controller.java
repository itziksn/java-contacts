package contact_app;

import contact_app.data.Contact;
import contact_app.data.ContactsData;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {
    @FXML
    private BorderPane borderPane;
    @FXML
    private TableView<Contact> contactsTableView;
    @FXML
    private TableColumn<Contact, String> firstNameColumn;
    @FXML
    private TableColumn<Contact, String> lastNameColumn;
    @FXML
    private TableColumn<Contact, String> phoneNumberColumn;
    @FXML
    private TableColumn<Contact, String> notesColumn;
    @FXML
    private TextField searchField;

    private ContextMenu contextMenu;

    private Dialog<ButtonType> dialog = new Dialog<>();
    private FXMLLoader loader = new FXMLLoader();

    // Keep them global, in case we want to have an option to filter contactTableView
    private FilteredList<Contact> filteredList;
    private Predicate<Contact> showAll;

    @FXML
    private void initialize() throws IOException {
        dialog = new Dialog<>();
        loader.setLocation(getClass().getResource("addContact.fxml"));
        DialogPane root = loader.load();
        dialog.setDialogPane(root);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        // Initializing cells value factory for each column
        firstNameColumn.setCellValueFactory((cell)-> cell.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory((cell)-> cell.getValue().lastNameProperty());
        phoneNumberColumn.setCellValueFactory((cell)-> cell.getValue().phoneNumberProperty());
        notesColumn.setCellValueFactory(cell -> cell.getValue().notesProperty());
        // Initializing contextMenu and adding to each row
        contextMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        MenuItem edit = new MenuItem("Edit");
        delete.setOnAction(event -> deleteContact());
        edit.setOnAction(event -> editContact());
        contextMenu.getItems().addAll(delete, edit);
        contactsTableView.setRowFactory(param -> new TableRow<>() {
            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && (!empty)) {
                    setContextMenu(contextMenu);
                } else {
                    setContextMenu(null);
                }
            }
        });
        // Initialize the predicate for the filteredList
        showAll = contact -> true;
        // Initialize the filteredList to the ObservableList from our Singleton, with the default predicate
        filteredList = new FilteredList<>(ContactsData.getInstance().getContactsList(), showAll);
        // Initialize the lastName comparator
        Comparator<Contact> byLastName = Comparator.comparing(Contact::getLastName);
        Comparator<Contact> byFirstName = Comparator.comparing(Contact::getFirstName);
        // Initialize the sorted list to be based on the filtered list
        SortedList<Contact> sortedList = new SortedList<>(filteredList, byLastName);
        sortedList.comparatorProperty().bind(contactsTableView.comparatorProperty());
        SortedList<Contact> another = new SortedList<>(filteredList, byFirstName);
        another.comparatorProperty().bind(contactsTableView.comparatorProperty());
        // Initialize the TableView to the sortedList;
        contactsTableView.setItems(sortedList);
        contactsTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//        contactsTableView.getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldValue, newValue) -> {
//                    if (newValue != null) {
//
//                    }
//                });

    }

    private void editContact() {
        if (dialog.getOwner() == null) {
            dialog.initOwner(borderPane.getScene().getWindow());
        }
        Contact toEdit = contactsTableView.getSelectionModel().getSelectedItem();
        dialog.setTitle("Edit contact: " + toEdit.getFirstName() + " " + toEdit.getLastName());
        dialog.setHeaderText("Use this dialog to edit a Contact in your phone book:");

        AddContactController controller = loader.getController();
        controller.editing(toEdit);
        Optional<ButtonType> optional = dialog.showAndWait();
        if (optional.isPresent()&&optional.get().equals(ButtonType.OK)) {
            Contact contact = controller.processDialog();
            if (contact!=null) {
                ContactsData.getInstance().replaceContact(toEdit, contact);
            }
        }
    }

    @FXML
    private void addContact() {
        if (dialog.getOwner() == null) {
            dialog.initOwner(borderPane.getScene().getWindow());
        }
        dialog.setTitle("Add new contact");
        dialog.setHeaderText("Use this dialog to add a Contact to your phone book:");
        AddContactController controller = loader.getController();
        controller.adding();
        Optional<ButtonType> optional = dialog.showAndWait();
        if (optional.isPresent()&&optional.get().equals(ButtonType.OK)) {
            Contact contact = controller.processDialog();
            if (contact!=null) {
                ContactsData.getInstance().addContact(contact);
            }
        }
    }

    @FXML
    private void deleteContact() {
        Contact selected = contactsTableView.getSelectionModel().getSelectedItem();
        if (selected!=null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            String fullName = selected.getFirstName() + " " + selected.getLastName();
            alert.setTitle("Delete contact " + fullName);
            alert.setHeaderText("You are about to delete Contact: " + fullName);
            alert.setContentText("Are you sure?");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.isPresent()&&optional.get().equals(ButtonType.OK)) {
                ContactsData.getInstance().removeContact(selected);
            }
        }
    }

    @FXML
    private void searchHandler() {
        String query = searchField.getText();
        if (query!=null && (!query.equals(""))) {
            filteredList.setPredicate(contact -> contact.getFirstName().contains(query)
                                                || contact.getLastName().contains(query)
                                                || contact.getPhoneNumber().contains(query));
        } else {
            filteredList.setPredicate(contact -> true);
        }
    }
}
