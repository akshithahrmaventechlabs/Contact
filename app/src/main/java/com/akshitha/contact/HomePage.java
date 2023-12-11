package com.akshitha.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class HomePage extends AppCompatActivity {

    private static final int REQUEST_ADD_CONTACT = 1;

    private TextInputEditText firstNameEt, lastNameEt, phoneMobileEt;
    private FloatingActionButton fabSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        firstNameEt = findViewById(R.id.firstNameEt);
        lastNameEt = findViewById(R.id.lastNameEt);
        phoneMobileEt = findViewById(R.id.phoneMobileEt);

        fabSave = findViewById(R.id.fabSave);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    // Input is valid, perform the save operation or any other action

                    // Create an Intent to start the ContactListActivity
                    Intent intent = new Intent(HomePage.this, ContactListActivity.class);

                    // Pass user-entered data as extras to the intent
                    intent.putExtra("FIRST_NAME", firstNameEt.getText().toString().trim());
                    intent.putExtra("LAST_NAME", lastNameEt.getText().toString().trim());
                    intent.putExtra("PHONE_NUMBER", phoneMobileEt.getText().toString().trim());

                    // Start the ContactListActivity and wait for the result
                    startActivityForResult(intent, REQUEST_ADD_CONTACT);
                }
            }
        });
    }

    // Add your validation logic here
    private boolean validateInput() {
        // Your validation logic goes here
        return true; // Change this based on your validation criteria
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_CONTACT && resultCode == RESULT_OK) {
            // Contact added successfully
            Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show();
        }
    }
}