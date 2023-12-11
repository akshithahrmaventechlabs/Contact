//package com.akshitha.contact;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//public class ContactRepository {
//
//    //This line declares a private static variable instance
//    // of type ContactRepository. It will hold a single instance of the ContactRepository class.
//    private static ContactRepository instance;
//    //This line declares a private variable contacts of type List<Contact>.
//    // It will store a list of Contact objects.
//    private List<Contact> contacts;
//
//    private ContactRepository() {
//
//        contacts = new ArrayList<>();
//    }
//
//    //This method is a static method that provides a way to
//    //get an instance of the ContactRepository class.
//    // It follows the Singleton design pattern, ensuring that only one
//    // instance of ContactRepository exists.
//    // The synchronized keyword ensures that the method is thread-safe.
//    public static synchronized ContactRepository getInstance() {
//        if (instance == null) {
//            instance = new ContactRepository();
//        }
//        return instance;
//    }
//
//    //This method adds a Contact object to the list of contacts.
//    public void addContact(Contact contact) {
//        contacts.add(contact);
//    }
//
//    //This method returns the list of contacts stored in the repository.
//    public List<Contact> getContacts() {
//        return contacts;
//    }
//
//    public boolean deleteContact(String firstName, String lastName, String contactNumber) {
//        Iterator<Contact> iterator = contacts.iterator();
//        while (iterator.hasNext()) {
//            Contact contact = iterator.next();
//            if (contact.equals(new Contact(firstName, lastName, contactNumber))) {
//                iterator.remove(); // Safely remove the contact
//                return true; // Contact deleted successfully
//            }
//        }
//        return false; // Contact not found or deletion failed
//    }
//}
////This method attempts to delete a contact based on the provided
//// first name, last name, and contact number.
//// It iterates through the list of contacts, and if a
//// match is found, the contact is removed from the list, and the method returns true.
//// If no matching contact is found, or if the deletion fails, it returns false.







package com.akshitha.contact;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactRepository {

    private ContactDao contactDao;
    private LiveData<List<ContactEntity>> allContacts;

    public ContactRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        contactDao = database.contactDao();
        allContacts = contactDao.getAllContacts();
    }

    public LiveData<List<ContactEntity>> getAllContacts() {
        return allContacts;
    }

    public void insert(ContactEntity contact) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            contactDao.insertContact(contact);
        });
    }

    public void update(ContactEntity contact) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            contactDao.updateContact(contact);
        });
    }

    public void delete(ContactEntity contact) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            contactDao.deleteContact(contact);
        });
    }
}