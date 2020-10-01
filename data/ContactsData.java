package contact_app.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ContactsData {

    private static ContactsData instance = new ContactsData();

    private ObservableList<Contact> contactList;
    private String fileName;
    private String separator;

    private ContactsData() {
        // Private Constructor, supposed to have only one instance
        contactList = FXCollections.observableArrayList();
        fileName = "contactsData.txt";
        separator = "WASMIT\3\3";
    }

    public static ContactsData getInstance() {
        return instance;
    }

    public void load() {
        Path path = Paths.get(fileName);
        if (Files.exists(path)) {
            try (BufferedReader br = new BufferedReader(Files.newBufferedReader(path))) {
                String line = br.readLine();
                while (line != null) {
                    String[] splitedLine = line.split(separator);
                    Contact contact = new Contact(splitedLine[0], splitedLine[1], splitedLine[2]);
                    if (splitedLine.length > 3) {
                        contact.setNotes(splitedLine[3]);
                    }
                    contactList.add(contact);
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void store() {
        Path path = Paths.get(fileName);
        try (BufferedWriter bw = new BufferedWriter(Files.newBufferedWriter(path))) {
            for (Contact contact : contactList) {
                bw.write(String.format("%s%s%s%s%s%s%s",
                        contact.getFirstName(), separator, contact.getLastName(),
                        separator, contact.getPhoneNumber(), separator, contact.getNotes()));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Contact> getContactsList() {
        return contactList;
    }

    public void addContact(Contact toAdd) {
        this.contactList.add(toAdd);
    }

    public void removeContact(Contact toRemove) {
        this.contactList.remove(toRemove);
    }

    public void replaceContact(Contact toRemove, Contact toAdd) {
        contactList.set(contactList.indexOf(toRemove), toAdd);
    }
}
