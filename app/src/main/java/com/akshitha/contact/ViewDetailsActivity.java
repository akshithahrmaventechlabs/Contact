package com.akshitha.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewDetailsActivity extends AppCompatActivity {

    private FloatingActionButton buttonDelete;
    private Contact contact;
    private FloatingActionButton buttonEdit;

    // Request code for editing contact
    private static final int REQUEST_EDIT_CONTACT_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        // Retrieve contact details from the intent
        String firstName = getIntent().getStringExtra("CONTACT_FIRST_NAME");
        String lastName = getIntent().getStringExtra("CONTACT_LAST_NAME");
        String contactNumber = getIntent().getStringExtra("CONTACT_NUMBER");

        // Create a Contact object with the retrieved information
        contact = new Contact(firstName, lastName, contactNumber);

        // Display contact details in TextViews
        TextView contactNameTextView = findViewById(R.id.contactNameTextView);
        contactNameTextView.setText(firstName + " " + lastName);

        TextView numberTextView = findViewById(R.id.numberTextView);
        numberTextView.setText(contactNumber);

        buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(view -> deleteContact());

        buttonEdit = findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(view -> editContact());
    }

    private void deleteContact() {
        if (contact != null) {
            Intent intent = new Intent();
            intent.putExtra("DELETED_CONTACT", contact);
            setResult(RESULT_OK, intent);
            finish();

            showToast("Contact deleted successfully");
        } else {
            showToast("No contact to delete");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void editContact() {
        // Create an Intent to navigate to EditContactActivity
        Intent intent = new Intent(this, EditContactActivity.class);

        // Pass the current contact details to the EditContactActivity
        intent.putExtra("CONTACT_FIRST_NAME", contact.getFirstName());
        intent.putExtra("CONTACT_LAST_NAME", contact.getLastName());
        intent.putExtra("CONTACT_NUMBER", contact.getPhoneNumber());

        // Start the EditContactActivity and wait for the result
        startActivityForResult(intent, REQUEST_EDIT_CONTACT_CODE);
    }

    // Handle the result from EditContactActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_CONTACT_CODE && resultCode == RESULT_OK) {
            // Contact edited successfully
            if (data != null) {
                Contact updatedContact = data.getParcelableExtra("UPDATED_CONTACT");

                // Update the contact object and display the updated data
                if (updatedContact != null) {
                    contact = updatedContact;
                    updateContactDetails();
                    showToast("Contact edited successfully");
                }
            }
        }
    }

    private void updateContactDetails() {
        TextView contactNameTextView = findViewById(R.id.contactNameTextView);
        contactNameTextView.setText(contact.getFirstName() + " " + contact.getLastName());

        TextView numberTextView = findViewById(R.id.numberTextView);
        numberTextView.setText(contact.getPhoneNumber());
    }
}