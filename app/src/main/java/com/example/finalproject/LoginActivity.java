package com.example.finalproject;

import android.content.Context;
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

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordTextView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);

        emailEditText = findViewById(R.id.editTextTextEmailAddress2);
        passwordEditText = findViewById(R.id.editTextTextPassword5);
        loginButton = findViewById(R.id.button4);
        forgotPasswordTextView = findViewById(R.id.textView8);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin(getBaseContext());
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleForgotPassword();
            }
        });
    }

    private void handleLogin(Context context) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        User user=new User();
        user.setEmail(email);
        user.setPassword(password);


        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }
        AuthService authService=new AuthService();
        authService.login(user, new ResponseCallBack() {
            @Override
            public void onSuccess(LoginResponse loginResponse){
                // Handle successful login
                if (loginResponse != null) {
                    // e.g., update UI, store user info, etc.
                    saveUserEmail(loginResponse.player);
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                    intent.putExtra("user_email", loginResponse.player.getEmail()); // Pass user email to profile activity
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable t) {
                // Handle error
                // e.g., show error message to the user
                Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleForgotPassword() {
        Intent intent = new Intent(LoginActivity.this, EmailValidationActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Update active status to 0 (logged out) when the activity is stopped
        String email = emailEditText.getText().toString();
        if (!TextUtils.isEmpty(email)) {
            dbHelper.updateActiveStatus(email, 0);
        }
    }
    private void saveUserEmail(User user) {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_email", user.getEmail());
        editor.putString("f_name",user.getF_name());
        editor.putString("l_name",user.getL_name());
        editor.putBoolean("active_status",user.isActive_status());
        editor.apply();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
