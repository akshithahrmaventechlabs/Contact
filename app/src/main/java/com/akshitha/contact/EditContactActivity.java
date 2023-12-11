package com.akshitha.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditContactActivity extends AppCompatActivity {

    private EditText editFirstName;
    private EditText editLastName;
    private EditText editPhoneNumber;

    private Contact contact;
    private FloatingActionButton fabSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);

        // Retrieve contact details from the intent
        String firstName = getIntent().getStringExtra("CONTACT_FIRST_NAME");
        String lastName = getIntent().getStringExtra("CONTACT_LAST_NAME");
        String contactNumber = getIntent().getStringExtra("CONTACT_NUMBER");

        // Create a Contact object with the retrieved information
        contact = new Contact(firstName, lastName, contactNumber);

        // Populate EditText fields with existing contact information
        editFirstName.setText(contact.getFirstName());
        editLastName.setText(contact.getLastName());
        editPhoneNumber.setText(contact.getPhoneNumber());

        fabSave = findViewById(R.id.fabSave);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    // Input is valid, perform the save operation or any other action
                    saveContact();
                }
            }
        });
    }

    // Add your validation logic here
    private boolean validateInput() {
        // Your validation logic goes here
        return true; // Change this based on your validation criteria
    }

    private void saveContact() {
        // Update the contact object with edited data
        contact.setFirstName(editFirstName.getText().toString());
        contact.setLastName(editLastName.getText().toString());
        contact.setPhoneNumber(editPhoneNumber.getText().toString());

        // Create an Intent to send back the updated contact data
        Intent resultIntent = new Intent();
        resultIntent.putExtra("UPDATED_CONTACT", contact);
        setResult(RESULT_OK, resultIntent);

        // Finish the activity to return to ViewDetailsActivity
        finish();
    }

}