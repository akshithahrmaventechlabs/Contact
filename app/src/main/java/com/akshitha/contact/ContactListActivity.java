package com.akshitha.contact;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactListActivity extends AppCompatActivity implements ContactAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private FloatingActionButton fabAddContact;
    private FloatingActionButton fabRefreshContact;
    private FloatingActionButton fabLogout;
    private List<Contact> contacts;
    private static final int REQUEST_EDIT_CONTACT_CODE = 1;


    private static final String SHARED_PREFS_NAME = "MyPrefs";
    private static final String CONTACTS_KEY = "contacts";
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FIRST_LOGIN = "firstLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list_activity);

        loadContacts();

        recyclerView = findViewById(R.id.recyclerViewContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactAdapter = new ContactAdapter(contacts, this);
        recyclerView.setAdapter(contactAdapter);

        // Check if data is coming from HomePage
        if (getIntent().hasExtra("FIRST_NAME") && getIntent().hasExtra("LAST_NAME") && getIntent().hasExtra("PHONE_NUMBER")) {
            String firstName = getIntent().getStringExtra("FIRST_NAME");
            String lastName = getIntent().getStringExtra("LAST_NAME");
            String phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");

            Contact newContact = new Contact(firstName, lastName, phoneNumber);
            contacts.add(newContact);

            saveContacts(); // Save the updated list

            Collections.sort(contacts, new Comparator<Contact>() {
                @Override
                public int compare(Contact contact1, Contact contact2) {
                    return contact1.getFirstName().compareToIgnoreCase(contact2.getFirstName());
                }
            });

            contactAdapter.notifyDataSetChanged();
        } else {
            // No data from HomePage, handle as needed
        }

        fabAddContact = findViewById(R.id.fabAddContact);
        fabAddContact.setOnClickListener(view -> openHomePage());

        fabRefreshContact = findViewById(R.id.fabrefreshtContact);
        fabRefreshContact.setOnClickListener(view -> refreshContactsWithBlink());

        TextView textViewMessage = findViewById(R.id.textViewMessage);
        textViewMessage.setVisibility(View.GONE);

        fabLogout = findViewById(R.id.fablogout);
        fabLogout.setOnClickListener(view -> showLogoutConfirmation());

        SearchView searchViewContacts = findViewById(R.id.searchViewContacts);
        searchViewContacts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search submission if needed
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the adapter when text changes in the search bar
                contactAdapter.getFilter().filter(newText);//getFilter method is used in adapter
                return false;
            }
        });
    }

    private void showLogoutConfirmation() {
        Snackbar.make(findViewById(android.R.id.content),
                        "Are you sure you want to log out?",
//Snackbars provide lightweight feedback about an operation.
// They show a brief message at the bottom of the screen on mobile and lower left on larger devices.
// Snackbars appear above all other elements on screen and only one can be displayed at a time.
                        Snackbar.LENGTH_LONG)
                .setAction("Logout", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        performLogout();
                    }
                })
                .show();

    }

    private void performLogout() {
        // Add your logout logic here
        // For example, clearing user data, navigating to the login screen, etc.

        // Update the message at the top
        TextView textViewMessage = findViewById(R.id.textViewMessage);
        textViewMessage.setText("Logged out successfully");
        textViewMessage.setVisibility(View.VISIBLE);

        // Clear user data
        clearUserData();

        // After logging out, navigate to the login screen
        Intent intent = new Intent(ContactListActivity.this, MainActivity.class);

        // Start the MainActivity with a new task
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Start the MainActivity
        startActivity(intent);
    }
    private void clearUserData() {
        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).edit();

        // Remove only the user-related data
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_FIRST_LOGIN);

        editor.apply();
    }
    private void loadContacts() {
        // Access the SharedPreferences using the name "MyPrefs" and set the mode to private
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        // Retrieve the stored JSON string representing the contacts using the key "contacts"
        String contactsJson = sharedPreferences.getString(CONTACTS_KEY, "");

        // Check if the retrieved JSON string is not empty
        if (!contactsJson.isEmpty()) {
            // If not empty, parse the JSON string into a List<Contact> using the fromJsonListString method
            contacts = Contact.fromJsonListString(contactsJson);
        } else {
            // If the JSON string is empty, initialize the contacts list as a new ArrayList
            contacts = new ArrayList<>();
        }

        // Sort the contacts list based on the first name, ignoring case
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact contact1, Contact contact2) {
                return contact1.getFirstName().compareToIgnoreCase(contact2.getFirstName());
            }
        });
    }

    private void refreshContactsWithBlink() {
        // Add a blinking effect by toggling visibility with a delay
        blinkView(findViewById(android.R.id.content));

        // Reload contacts from SharedPreferences
        loadContacts();

        // Update the RecyclerView
        contactAdapter.notifyDataSetChanged();
    }

    private void blinkView(View view) {
        // Toggle visibility with a delay for a blinking effect
        view.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(() -> view.setVisibility(View.VISIBLE), 100); // 100 milliseconds (0.1 second) delay
    }

    private void saveContacts() {
        // Access the SharedPreferences using the name "MyPrefs" and set the mode to private
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        // Obtain an editor to modify the SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Create a Gson instance to convert the contacts list to a JSON string
        Gson gson = new Gson();
        String contactsJson = gson.toJson(contacts);

        // Put the JSON string into the SharedPreferences with the key "contacts"
        editor.putString(CONTACTS_KEY, contactsJson);
        // Apply the changes to the SharedPreferences
        editor.apply();
    }

    private void openHomePage() {
        saveContacts(); // Save contacts before navigating to HomePage
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
    private void openViewDetailsActicity(){
        Intent intent=new Intent(this,ViewDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        Contact selectedContact = contacts.get(position);
        Intent intent = new Intent(ContactListActivity.this, ViewDetailsActivity.class);
        intent.putExtra("CONTACT_FIRST_NAME", selectedContact.getFirstName());
        intent.putExtra("CONTACT_LAST_NAME", selectedContact.getLastName());
        intent.putExtra("CONTACT_NUMBER", selectedContact.getPhoneNumber());
        startActivity(intent);
    }
    // This method handles the result returned from EditContactActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_CONTACT_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                // Extract updated contact data
                Contact updatedContact = data.getParcelableExtra("UPDATED_CONTACT");

                // Update your contact list or database with the updated contact
                // For example, update the contact at its position in the list
                int position = findContactPosition(updatedContact);
                if (position != -1) {
                    contacts.set(position, updatedContact);

                    // Notify your adapter if using RecyclerView
                    contactAdapter.notifyItemChanged(position);

                    // Save the updated contacts list
                    saveContacts();
                }
            }
        }
    }

    private int findContactPosition(Contact targetContact) {
        for (int i = 0; i < contacts.size(); i++) {
            Contact currentContact = contacts.get(i);
            if (currentContact.equals(targetContact)) {
                return i;  // Return the position if the contact is found
            }
        }
        return -1; // Return -1 if the contact is not found
    }
}




//
////roomdata code
//package com.akshitha.contact;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.View;
//import android.widget.SearchView;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.LiveData;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.room.Room;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;
//
//import java.util.List;
//
//public class ContactListActivity extends AppCompatActivity implements ContactAdapter.OnItemClickListener {
//
//    private RecyclerView recyclerView;
//    private ContactAdapter contactAdapter;
//    private FloatingActionButton fabAddContact;
//    private FloatingActionButton fabRefreshContact;
//    private FloatingActionButton fabLogout;
//    private LiveData<List<ContactEntity>> contacts;
//    private static final int REQUEST_EDIT_CONTACT_CODE = 1;
//
//    private static final String SHARED_PREFS_NAME = "MyPrefs";
//    private static final String PREF_NAME = "MyPrefs";
//    private static final String KEY_USERNAME = "username";
//    private static final String KEY_FIRST_LOGIN = "firstLogin";
//
//    private AppDatabase appDatabase;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.contact_list_activity);
//
//        // Initialize Room database
//        appDatabase = Room.databaseBuilder(getApplicationContext(),
//                AppDatabase.class, "contacts-database").build();
//
//        // Get the ContactDao from the database
//        ContactDao contactDao = appDatabase.contactDao();
//
//        // Use LiveData to observe changes in the contact data
//        contacts = contactDao.getAllContacts();
//
//        recyclerView = findViewById(R.id.recyclerViewContacts);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Update the way you handle contacts using LiveData
//        contacts.observe(this, contactEntities -> {
//            // Update your RecyclerView adapter with the new list of contactEntities
//            if (contactEntities != null) {
//                contactAdapter = new ContactAdapter(contactEntities, this);
//                recyclerView.setAdapter(contactAdapter);
//            }
//        });
//
//        fabAddContact = findViewById(R.id.fabAddContact);
//        fabAddContact.setOnClickListener(view -> openHomePage());
//
//        fabRefreshContact = findViewById(R.id.fabrefreshtContact);
//        fabRefreshContact.setOnClickListener(view -> refreshContactsWithBlink());
//
//        TextView textViewMessage = findViewById(R.id.textViewMessage);
//        textViewMessage.setVisibility(View.GONE);
//
//        fabLogout = findViewById(R.id.fablogout);
//        fabLogout.setOnClickListener(view -> showLogoutConfirmation());
//
//        SearchView searchViewContacts = findViewById(R.id.searchViewContacts);
//        searchViewContacts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // Handle search submission if needed
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // Filter the adapter when text changes in the search bar
//                contactAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//    }
//
//    private void showLogoutConfirmation() {
//        Snackbar.make(findViewById(android.R.id.content),
//                        "Are you sure you want to log out?",
//                        Snackbar.LENGTH_LONG)
//                .setAction("Logout", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        performLogout();
//                    }
//                })
//                .show();
//    }
//
//    private void performLogout() {
//        TextView textViewMessage = findViewById(R.id.textViewMessage);
//        textViewMessage.setText("Logged out successfully");
//        textViewMessage.setVisibility(View.VISIBLE);
//
//        clearUserData();
//
//        Intent intent = new Intent(ContactListActivity.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//    }
//
//    private void clearUserData() {
//        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).edit();
//        editor.remove(KEY_USERNAME);
//        editor.remove(KEY_FIRST_LOGIN);
//        editor.apply();
//    }
//
//    private void refreshContactsWithBlink() {
//        blinkView(findViewById(android.R.id.content));
//        // No need to reload contacts, LiveData handles updates automatically
//    }
//
//    private void blinkView(View view) {
//        view.setVisibility(View.INVISIBLE);
//        new Handler().postDelayed(() -> view.setVisibility(View.VISIBLE), 100);
//    }
//
//    private void openHomePage() {
//        // No need to manually save contacts, LiveData handles updates automatically
//        Intent intent = new Intent(this, HomePage.class);
//        startActivity(intent);
//    }
//
//    @Override
//    public void onItemClick(int position) {
//        ContactEntity selectedContact = contacts.getValue().get(position);
//        Intent intent = new Intent(ContactListActivity.this, ViewDetailsActivity.class);
//        intent.putExtra("CONTACT_FIRST_NAME", selectedContact.getFirstName());
//        intent.putExtra("CONTACT_LAST_NAME", selectedContact.getLastName());
//        intent.putExtra("CONTACT_NUMBER", selectedContact.getPhoneNumber());
//        startActivity(intent);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_EDIT_CONTACT_CODE && resultCode == RESULT_OK) {
//            if (data != null) {
//                ContactEntity updatedContact = data.getParcelableExtra("UPDATED_CONTACT");
//                int position = findContactPosition(updatedContact);
//                if (position != -1) {
//                    // Room handles updates automatically, no need to manually update the list
//                }
//            }
//        }
//    }
//
//    private int findContactPosition(ContactEntity targetContact) {
//        List<ContactEntity> contactList = contacts.getValue();
//        if (contactList != null) {
//            for (int i = 0; i < contactList.size(); i++) {
//                ContactEntity currentContact = contactList.get(i);
//                if (currentContact.getId() == targetContact.getId()) {
//                    return i;
//                }
//            }
//        }
//        return -1;
//    }
//}