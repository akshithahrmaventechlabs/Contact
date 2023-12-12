package com.akshitha.contact;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ViewDetailsActivity extends AppCompatActivity {

    private FloatingActionButton buttonDelete;
    private Contact contact;
    private FloatingActionButton buttonEdit;
    private FloatingActionButton callBtn;

    private FloatingActionButton shareBtn;


    private static final int PERMISSION_CODE = 100;



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

        callBtn = findViewById(R.id.buttonphone);
        callBtn.setOnClickListener(v -> makePhoneCall());


        shareBtn = findViewById(R.id.buttonshare);
        shareBtn.setOnClickListener(v -> shareContact());

        FloatingActionButton messageBtn = findViewById(R.id.buttonmessage);
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }




    private void sendMessage() {
        // Get the contact's phone number
        String phoneNumber = ((TextView) findViewById(R.id.numberTextView)).getText().toString();

        // Create an intent to open the messaging app
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
        intent.putExtra("sms_body", "Hello, let's chat!"); // Optional: Set a default message

        try {
            startActivity(intent);
        } catch (Exception e) {
            // Handle exception if there's no messaging app installed
            showToast("No messaging app installed.");
        }
    }

    private void shareContact() {
        String contactName = ((TextView) findViewById(R.id.contactNameTextView)).getText().toString();
        String contactNumber = ((TextView) findViewById(R.id.numberTextView)).getText().toString();

        String shareMessage = "Contact: " + contactName + "\nNumber: " + contactNumber;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

        startActivity(Intent.createChooser(shareIntent, "Share contact details"));
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
        startActivityForResult(intent, 2);
    }

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(ViewDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Check if the user has previously denied the permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(ViewDetailsActivity.this, Manifest.permission.CALL_PHONE)) {
                // Explain to the user why the app needs the CALL_PHONE permission
                showToast("This app requires the CALL_PHONE permission to make phone calls.");

                // Now request the permission
                ActivityCompat.requestPermissions(ViewDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
            } else {
                // Request the permission without explanation
                ActivityCompat.requestPermissions(ViewDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
            }
        } else {
            // Permission is already granted, make the phone call
            String phoneNumber = ((TextView) findViewById(R.id.numberTextView)).getText().toString();
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }
    }

    // Handle the result from EditContactActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK) {
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