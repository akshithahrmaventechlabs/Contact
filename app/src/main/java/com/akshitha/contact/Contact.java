package com.akshitha.contact;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
//
//public class Contact {
//    private String firstName;
//    private String lastName;
//    private String phoneNumber;
//
//    public Contact(String firstName, String lastName, String phoneNumber) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.phoneNumber = phoneNumber;
//    }
//
//    public ContactEntity toContactEntity() {
//        ContactEntity contactEntity = new ContactEntity();
//        contactEntity.firstName = this.firstName;
//        contactEntity.lastName = this.lastName;
//        contactEntity.phoneNumber = this.phoneNumber;
//        return contactEntity;
//    }
//
//    public static Contact fromContactEntity(ContactEntity contactEntity) {
//        return new Contact(contactEntity.firstName, contactEntity.lastName, contactEntity.phoneNumber);
//    }
//}


public class Contact implements Parcelable {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String ownerUsername;

    public Contact(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    // Parcelable implementation

    protected Contact(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        phoneNumber = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(phoneNumber);
    }

    // Gson conversion methods

    public static String toJsonString(Contact contact) {
        Gson gson = new Gson();
        return gson.toJson(contact);
    }

    public static Contact fromJsonString(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Contact.class);
    }

    public static List<Contact> fromJsonListString(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Contact>>() {}.getType();
        return gson.fromJson(jsonString, type);
    }

    // Setter methods

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}