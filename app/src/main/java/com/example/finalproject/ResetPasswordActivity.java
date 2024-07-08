package com.example.finalproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button submitButton;
    DBHelper dbHelper;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        dbHelper = new DBHelper(this);

        newPasswordEditText = findViewById(R.id.editTextTextPassword2);
        confirmPasswordEditText = findViewById(R.id.editTextTextPassword);
        submitButton = findViewById(R.id.button);

        email = getIntent().getStringExtra("email");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePasswordReset();
            }
        });
    }

    private void handlePasswordReset() {
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(newPassword)) {
            newPasswordEditText.setError("New password is required");
            newPasswordEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordEditText.setError("Confirm password is required");
            confirmPasswordEditText.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            confirmPasswordEditText.requestFocus();
            return;
        }

        if (dbHelper.updatePassword(email, newPassword)) {
            Toast.makeText(this, "Password reset successful", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Password reset failed", Toast.LENGTH_SHORT).show();
        }
    }
}
