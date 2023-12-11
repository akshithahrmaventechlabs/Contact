package com.akshitha.contact;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {

    //This method is annotated with @Query and defines a SQL query to retrieve
    // all contacts from the "contacts" table in the database, ordered by the "firstName" column.
    // The return type is LiveData<List<ContactEntity>>, indicating that the result is wrapped in a
    // LiveData object to observe changes.
    @Query("SELECT * FROM contacts ORDER BY firstName")
    LiveData<List<ContactEntity>> getAllContacts();


    //This method is annotated with @Insert and is used to insert a new contact into the database.
    // The ContactEntity parameter represents the contact to be inserted.
    @Insert
    void insertContact(ContactEntity contact);

    //This method is annotated with @Update and is used to update an existing contact in the database.
    // The ContactEntity parameter represents the contact to be updated.
    @Update
    void updateContact(ContactEntity contact);
    //This method is annotated with @Delete and is used to delete a contact from the database.
    // The ContactEntity parameter represents the contact to be deleted.

    @Delete
    void deleteContact(ContactEntity contact);
}