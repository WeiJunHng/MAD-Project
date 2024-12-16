package com.example.madproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import soup.neumorphism.NeumorphButton;

import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText ETEmail,ETPassword;
    TextView TVForgotPasswrod,TVSignUp;
    NeumorphButton BtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ETEmail = findViewById(R.id.ETEmail);
        ETPassword = findViewById(R.id.ETPassword);
        TVForgotPasswrod = findViewById(R.id.TVForgotPassword);
        TVSignUp = findViewById(R.id.TVSignUp);
        BtnLogin = findViewById(R.id.BtnLogin);

        BtnLogin.setOnClickListener(view -> login());

        TVSignUp.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this,SignUpActivity.class)));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void login(){
        String email = ETEmail.getText().toString();
        String password = ETPassword.getText().toString();

        if(email.isBlank() || password.isBlank()){
            Toast.makeText(this,"All the fields must be filled in!",Toast.LENGTH_SHORT).show();
            return;
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                LocalDate today= LocalDate.now();
//                User user = userDao.getByEmail(email);
//                runOnUiThread(() -> {
//                    if (user != null && user.getPassword().equals(password)) {
//                        // Login successful
//                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//
//                        // Get userId
//                        String userId = user.getUserId();
//                        int strikeLoginDay = user.getStrikeLoginDays();
//                        user.setStrikeLoginDays(++strikeLoginDay);
//                        user.setLastLogin(today.toString());
//
//                        Executor.executeTask(() -> {
//                            // If username does not exist, proceed to insert the user
//                            FirestoreManager firestoreManager = new FirestoreManager(AppDatabase.getDatabase(getApplicationContext()));
//                            firestoreManager.onInsertUpdate("update","user", user, getApplicationContext());});
//
//                        // Get SharedPreferences
//                        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
//
//                        // Save userId in SharedPreferences
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("user_id", userId);
//                        editor.apply();
//
//                        // Navigate to another activity
//                        Intent intent = new Intent(loginpage_activity.this, NavVewBnv.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        // Login failed
//                        Toast.makeText(loginpage_activity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }).start();
    }
}