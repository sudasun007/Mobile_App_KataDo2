package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.interfaces.DeleteCallBack;
import com.example.finalproject.models.DeleteAcc;
import com.example.finalproject.models.DeleteResponse;
import com.example.finalproject.models.LogoutRequest;
import com.example.finalproject.models.User;
import com.example.finalproject.services.AuthService;

public class ViewProfileActivity extends AppCompatActivity {
    private ImageView logoImageView;
    private TextView appTitleTextView;
    private ImageView homeButton;
    private Button logoutButton;
    private Button Edit;
    private Button Delete;
    private DBHelper dbHelper;
    private String currentUserEmail;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView rank;
    private TextView initials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        // Initialize UI components
        logoImageView = findViewById(R.id.imageView2);
        appTitleTextView = findViewById(R.id.textView9);
        homeButton = findViewById(R.id.homebtn);
        logoutButton = findViewById(R.id.button5);
        Edit = findViewById(R.id.btnedit);
        Delete = findViewById(R.id.btndelete);
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        email = findViewById(R.id.useremail);
        rank = findViewById(R.id.rank);
        initials=findViewById(R.id.initials_text);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);
        currentUserEmail = getCurrentUserEmail();
        if (currentUserEmail == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            // Redirect to login activity
            Intent intent = new Intent(ViewProfileActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return; // Exit the onCreate method
        }
        populateUserData();

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete account button click
                showDeleteAccountDialog();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to home activity
                Intent intent = new Intent(ViewProfileActivity.this, HomepageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthService authservice=new AuthService();

                // Update active status to 0 in the database
                String userEmail = getCurrentUserEmail();
                LogoutRequest logoutRequest=new LogoutRequest();
                logoutRequest.setEmail(userEmail);

                authservice.logout(logoutRequest, new DeleteCallBack() {
                    @Override
                    public void onSuccess(DeleteResponse deleteResponse) {
                        Toast.makeText(ViewProfileActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();

                        // Clear session or any relevant data here
                        clearSessionData(userEmail);

                        // Navigate to login activity and clear the back stack
                        Intent intent = new Intent(ViewProfileActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                });

                // Perform logout operations

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateUserData();
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteAccount())
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteAccount() {
        AuthService authService=new AuthService();
        DeleteAcc deleteAcc=new DeleteAcc();
        deleteAcc.setEmail(getCurrentUserEmail());
        authService.deleteAcc(deleteAcc, new DeleteCallBack() {
            @Override
            public void onSuccess(DeleteResponse deleteResponse) {
                Toast.makeText(ViewProfileActivity.this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                // Redirect to login activity
                Intent intent = new Intent(ViewProfileActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(ViewProfileActivity.this, "Failed to delete account", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private String getCurrentUserEmail() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.getString("user_email", null); // Return null if email not found
    }
    private boolean getActiveStatus() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.getBoolean("active_status",false); // Return null if email not found
    }

    private void populateUserData() {

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            firstName.setText(preferences.getString("f_name", null));
            lastName.setText(preferences.getString("l_name", null));
            email.setText(preferences.getString("user_email", null));
            rank.setText("6"); // Ensure rank column exists in the result table
            initials.setText(preferences.getString("f_name", null).substring(0,1).toUpperCase()+preferences.getString("l_name", null).substring(0,1).toUpperCase());

    }

    // Method to clear session data
    private void clearSessionData(String userEmail) {
        // Add code to clear session data, such as shared preferences or any other session management


            SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
    }
}
