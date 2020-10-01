package contact_app.data;

import javafx.beans.property.SimpleStringProperty;

public class Contact {
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty notes;

    public Contact(String firstName, String lastName, String phoneNumber) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.notes = new SimpleStringProperty("");
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public String getNotes() {
        return notes.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public SimpleStringProperty notesProperty() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }
}
