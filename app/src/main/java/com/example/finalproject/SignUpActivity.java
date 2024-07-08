package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.interfaces.ResponseCallBack;
import com.example.finalproject.models.LoginResponse;
import com.example.finalproject.models.User;
import com.example.finalproject.services.AuthService;

public class SignUpActivity extends AppCompatActivity {
    Button signup;
    DBHelper dbHelper;
    EditText fnameEditText, lnameEditText, emailEditText, passwordEditText, conformPwEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);

        fnameEditText = findViewById(R.id.fname_id);
        lnameEditText = findViewById(R.id.lname_id);
        emailEditText = findViewById(R.id.email_id);
        passwordEditText = findViewById(R.id.password_id);
        conformPwEditText = findViewById(R.id.conformpw_id);

        signup = findViewById(R.id.signup_btn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s_fname = fnameEditText.getText().toString();
                String s_lname = lnameEditText.getText().toString();
                String s_email = emailEditText.getText().toString();
                String s_password = passwordEditText.getText().toString();
                String s_conformpw = conformPwEditText.getText().toString();
                User user=new User();
                user.setF_name(s_fname);
                user.setL_name(s_lname);
                user.setEmail(s_email);
                user.setPassword(s_password);

                if (s_fname.isEmpty() || s_lname.isEmpty() || s_email.isEmpty() || s_password.isEmpty() || s_conformpw.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(s_email).matches()) {
                    Toast.makeText(SignUpActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!s_password.equals(s_conformpw)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Additional password strength checks can be added here if needed
                AuthService authService=new AuthService();
                authService.signup(user, new ResponseCallBack() {
                    @Override
                    public void onSuccess(LoginResponse loginResponse) {
                        Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        saveUserEmail(s_email);
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(SignUpActivity.this, "Error in Registering", Toast.LENGTH_SHORT).show();
                    }
                });
                //Boolean checkInsertData = dbHelper.addNewMember(s_email, s_fname, s_lname, s_password);
            }
        });
    }

    private void saveUserEmail(String email) {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_email", email);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
