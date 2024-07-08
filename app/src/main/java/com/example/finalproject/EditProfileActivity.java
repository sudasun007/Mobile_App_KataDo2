package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.interfaces.ResponseCallBack;
import com.example.finalproject.models.LoginResponse;
import com.example.finalproject.models.User;
import com.example.finalproject.services.AuthService;

public class EditProfileActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;

    private Button saveButton;
    private User user;

    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize the views
        firstNameEditText = findViewById(R.id.fname_id);
        lastNameEditText = findViewById(R.id.lname_id);


        saveButton = findViewById(R.id.signup_btn);

        user=new User();
        // Fetch the current user's email
        currentUserEmail = getCurrentUserEmail();


        // Check if email is null, if so handle it (maybe redirect to login)
//        if (currentUserEmail == null) {
//            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
//            // Redirect to login activity
//            Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//            return; // Exit the onCreate method
//        }
        populateUserData();
        // Set click listener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private String getCurrentUserEmail() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.getString("user_email", null); // Return null if email not found
    }
    private void populateUserData() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        firstNameEditText.setText(preferences.getString("f_name", null));
        lastNameEditText.setText(preferences.getString("l_name", null));

        user.setF_name(preferences.getString("f_name", null));
        user.setL_name(preferences.getString("l_name", null));
        user.setEmail(preferences.getString("user_email", null));

    }

    private void saveProfile() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();


        // Validate the inputs
        if (TextUtils.isEmpty(firstName)) {
            firstNameEditText.setError("First name is required");
            return;
        }
        if (TextUtils.isEmpty(lastName)) {
            lastNameEditText.setError("Last name is required");
            return;
        }

        user.setF_name(firstName);
        user.setL_name(lastName);

        AuthService authService=new AuthService();
        authService.update(user, new ResponseCallBack() {
            @Override
            public void onSuccess(LoginResponse loginResponse) {
                Toast.makeText(EditProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                // Optionally update the currentUserEmail if email is changed
                SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("f_name", firstName);
                editor.putString("l_name", lastName);
                editor.apply();

            }


            @Override
            public void onError(Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Failed to update profile!", Toast.LENGTH_SHORT).show();
            }
        });





    }
}
