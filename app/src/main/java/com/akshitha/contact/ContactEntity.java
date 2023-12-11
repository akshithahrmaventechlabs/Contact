package com.akshitha.contact;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Declare a class named ContactEntity and mark it as an Entity for Room database
@Entity(tableName = "contacts")
public class ContactEntity {

    // Declare a variable to represent the primary key in the database, set to auto-generate
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Declare variables to store contact information
    private String firstName;
    private String lastName;
    private String phoneNumber;

    // Constructors, getters, and setters

    // Constructor
    public ContactEntity(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}