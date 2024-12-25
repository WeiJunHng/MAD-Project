package com.example.madproject.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.madproject.MainActivity;
import com.example.madproject.R;
import com.example.madproject.data.DAO.UserDAO;
import com.example.madproject.data.db.AppDatabase;
import com.example.madproject.data.db.FirestoreManager;
import com.example.madproject.data.model.User;
import com.example.madproject.ui.signup.SignUpActivity;
import com.example.madproject.ui.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import soup.neumorphism.NeumorphButton;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText ETEmail,ETPassword;
    TextView TVForgotPassword,TVSignUp;
    NeumorphButton BtnLogin;

    private AppDatabase database;
    private FirestoreManager firestoreManager;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        database = AppDatabase.getDatabase(getApplicationContext());
        firestoreManager = new FirestoreManager(database);

        firestoreManager.clearTables();
        firestoreManager.syncUserTable();

        ETEmail = findViewById(R.id.ETEmail);
        ETPassword = findViewById(R.id.ETPassword);
        TVForgotPassword = findViewById(R.id.TVForgotPassword);
        TVSignUp = findViewById(R.id.TVSignUp);
        BtnLogin = findViewById(R.id.BtnLogin);

        ViewModelFactory factory = new ViewModelFactory(getApplicationContext());
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        loginViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                // Login successful
                saveUserSession(user);
                Toast.makeText(this,"Login successfully!",Toast.LENGTH_SHORT).show();
                navigateToHome();
            } else {
                // Login failed
                Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        BtnLogin.setOnClickListener(view -> login());

        TVSignUp.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        firestoreManager.clearTables();
        firestoreManager.syncUserTable();
    }

    private void login() {
        String email = ETEmail.getText().toString();
        String password = ETPassword.getText().toString();

        if(email.isBlank() || password.isBlank()){
            Toast.makeText(this,"All the fields must be filled in!",Toast.LENGTH_SHORT).show();
            return;
        }

        loginViewModel.loginUser(email, password);
    }

    private void saveUserSession(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", user.getUserId());
        editor.apply();
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}