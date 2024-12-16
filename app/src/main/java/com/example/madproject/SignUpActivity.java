package com.example.madproject;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import soup.neumorphism.NeumorphButton;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText ETFirstName,ETLastName,ETUsername,ETEmail,ETPassword,ETConfirmPassword;
    CheckBox CBoxGuidelines,CBoxTerms;
    TextView TVGuidelines,TVTerms;
    NeumorphButton BtnSignUp,BtnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ETFirstName = findViewById(R.id.ETFirstName);
        ETLastName = findViewById(R.id.ETLastName);
        ETUsername = findViewById(R.id.ETUsername);
        ETEmail = findViewById(R.id.ETEmail);
        ETPassword = findViewById(R.id.ETPassword);
        ETConfirmPassword = findViewById(R.id.ETConfirmPassword);

        CBoxGuidelines = findViewById(R.id.CBoxGuidelines);
        CBoxTerms = findViewById(R.id.CBoxTerms);

        TVGuidelines = findViewById(R.id.TVGuidelines);
        TVTerms = findViewById(R.id.TVTerms);

        BtnSignUp = findViewById(R.id.BtnSignUp);
        BtnCancel = findViewById(R.id.BtnCancel);

        BtnSignUp.setOnClickListener(view -> signUp());

        BtnCancel.setOnClickListener(view -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void signUp(){
        String firstName = ETFirstName.getText().toString();
        String lastName = ETLastName.getText().toString();
        String username = ETUsername.getText().toString();
        String email = ETEmail.getText().toString();
        String password = ETPassword.getText().toString();
        String confirmPassword = ETConfirmPassword.getText().toString();

        boolean guidelinesChecked = CBoxGuidelines.isChecked();
        boolean termsChecked = CBoxTerms.isChecked();

        if(firstName.isBlank()||lastName.isBlank()||username.isBlank()||email.isBlank()||password.isBlank()||confirmPassword.isBlank()){
            Toast.makeText(this,"All the fields must be filled in!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isEmailValid(email)){
            Toast.makeText(this,"Invalid email address",Toast.LENGTH_SHORT).show();
            return;
        }

//        if(foundEmail(email)){
//            Toast.makeText(this,"This email address has been signed up",Toast.LENGTH_SHORT).show();
//            return;
//        }

//        if(foundUsername(username)){
//            Toast.makeText(this,"Username has been used.\nPlease try other",Toast.LENGTH_SHORT).show();
//            return;
//        }

        if(!password.equals(confirmPassword)){
            Toast.makeText(this,"Check your password again",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!guidelinesChecked){
            CBoxGuidelines.requestFocus();
            Toast.makeText(this,"Read and agree the Community Guidelines to continue",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!termsChecked){
            CBoxTerms.requestFocus();
            Toast.makeText(this,"Read and agree the Terms of Service to continue",Toast.LENGTH_SHORT).show();
            return;
        }

//        executeSignUp();
        Toast.makeText(this,"Successfully signed up",Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean isEmailValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}