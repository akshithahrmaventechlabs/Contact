package com.akshitha.contact;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private Button loginButton;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FIRST_LOGIN = "firstLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.editTextUsername);
        loginButton = findViewById(R.id.buttonLogin);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Check if the user has already logged in
        if (isLoggedIn()) {
            openContactListActivity();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();

                if (isValidUsername(username)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_USERNAME, username);
                    editor.apply();

                    // Set the flag to indicate that the user has logged in
                    setLoggedInFlag();

                    openContactListActivity();
                }
            }
        });
    }

    private boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_FIRST_LOGIN, false);
    }

    private void setLoggedInFlag() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_FIRST_LOGIN, true);
        editor.apply();
    }

    private boolean isValidUsername(String username) {
        // Check if the username length is between 6 and 18 characters
        if (username.length() < 6 || username.length() > 18) {
            showToast("Invalid username length. Must be between 6 and 18 characters.");
            return false;
        }

        // Check if the username contains only alphanumeric characters and underscores
        if (!username.matches("[a-zA-Z0-9_]+")) {
            showToast("Invalid username format. Only alphanumeric characters and underscores are allowed.");
            return false;
        }

        // Check if the username contains at least one uppercase letter
        if (!username.matches(".*[A-Z].*")) {
            showToast("Invalid username. Must contain at least one uppercase letter.");
            return false;
        }

        return true;
    }

    private void openContactListActivity() {
        Intent intent = new Intent(this, ContactListActivity.class);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}













//
//
//
//
////package com.akshitha.contact;
////
////import android.content.Context;
////import android.content.Intent;
////import android.content.SharedPreferences;
////import android.os.Bundle;
////import android.view.View;
////import android.widget.Button;
////import android.widget.EditText;
////import android.widget.Toast;
////import androidx.appcompat.app.AppCompatActivity;
////
////public class MainActivity extends AppCompatActivity {
////
////    private EditText usernameEditText;
////    private Button loginButton;
////
////    private SharedPreferences sharedPreferences;
////    private static final String PREF_NAME = "MyPrefs";
////    private static final String KEY_USERNAME = "username";
////    private static final String KEY_FIRST_LOGIN = "firstLogin";
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////
////        usernameEditText = findViewById(R.id.editTextUsername);
////        loginButton = findViewById(R.id.buttonLogin);
////
////        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
////
////        // Check if the user has already logged in
////        if (isLoggedIn()) {
////            openContactListActivity();
////            finish();  // Finish the current activity to prevent going back to it.
////        }
////
////        loginButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                String username = usernameEditText.getText().toString().trim();
////
////                if (!username.isEmpty()) {
////                    SharedPreferences.Editor editor = sharedPreferences.edit();
////                    editor.putString(KEY_USERNAME, username);
////                    editor.apply();
////
////                    // Set the flag to indicate that the user has logged in
////                    setLoggedInFlag();
////
////                    openContactListActivity();
////                    finish();
////                } else {
////                    showToast("Please enter a username");
////                }
////            }
////        });
////    }
////
////    private boolean isLoggedIn() {
////        return sharedPreferences.getBoolean(KEY_FIRST_LOGIN, false);
////    }
////
////    private void setLoggedInFlag() {
////        SharedPreferences.Editor editor = sharedPreferences.edit();
////        editor.putBoolean(KEY_FIRST_LOGIN, true);
////        editor.apply();
////    }
////
////    private void openContactListActivity() {
////        Intent intent = new Intent(this, ContactListActivity.class);
////        startActivity(intent);
////    }
////
////    private void showToast(String message) {
////        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
////    }
////}