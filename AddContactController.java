package contact_app;

import contact_app.data.Contact;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddContactController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextArea notesArea;

    Contact processDialog(){
        String first = firstNameField.getText();
        String last = lastNameField.getText();
        String phone = phoneNumberField.getText().trim();
        String notes = notesArea.getText();
        if ((!first.equals("")) &&(!last.equals("")) &&(!phone.equals(""))) {
            Contact newContact = new Contact(first, last, phone);
            if (!notes.equals("")) {
                newContact.setNotes(notes);
            }
            return newContact;
        }
        return null;
    }

    void editing(Contact toEdit) {
        firstNameField.setText(toEdit.getFirstName());
        lastNameField.setText(toEdit.getLastName());
        phoneNumberField.setText(toEdit.getPhoneNumber());
        notesArea.setText(toEdit.getNotes());
    }

    void adding() {
        firstNameField.clear();
        lastNameField.clear();
        phoneNumberField.clear();
        notesArea.clear();
    }
}
