package com.akshitha.contact;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {

    //This line declares a private variable repository of type ContactRepository.
    // The repository is responsible for handling data operations.
    private ContactRepository repository;
    //This line declares a private variable allContacts of type LiveData<List<ContactEntity>>.
    // It holds a list of ContactEntity objects and is observable, meaning it can be observed for changes.
    private LiveData<List<ContactEntity>> allContacts;

//This line defines a constructor for the ContactViewModel class that takes an Application parameter.
// The constructor initializes the repository and sets up the allContacts LiveData.
    public ContactViewModel(Application application) {
        super(application);
        repository = new ContactRepository(application);
        allContacts = repository.getAllContacts();
    }

    public LiveData<List<ContactEntity>> getAllContacts() {
        return allContacts;
    }

//This line declares a public method insertContact() that takes a ContactEntity parameter.
// This method is used to insert a new contact into the repository.
    public void insertContact(ContactEntity contact) {
        repository.insert(contact);
    }

    public void updateContact(ContactEntity contact) {
        repository.update(contact);
    }

    public void deleteContact(ContactEntity contact) {
        repository.delete(contact);
    }
}