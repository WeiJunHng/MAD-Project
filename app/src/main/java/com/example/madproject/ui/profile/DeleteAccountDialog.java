package com.example.madproject.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.madproject.R;
import com.example.madproject.data.model.User;
import com.example.madproject.data.repository.UserRepository;
import com.example.madproject.ui.login.LoginActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteAccountDialog extends DialogFragment {

    private TextInputEditText etPassword;
    private UserRepository userRepository;
    private User currentUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_account, container, false);

        Button btnConfirm = view.findViewById(R.id.BtnConfirmDeleteAccount);
        Button btnCancel = view.findViewById(R.id.BtnCancelDeleteAccount);
        ImageButton btnClose = view.findViewById(R.id.BtnCloseIcon);
        etPassword = view.findViewById(R.id.ETPassword);

        userRepository = new UserRepository(requireContext());
        currentUser = userRepository.getCurrentUser();

        TextView ETEmail = view.findViewById(R.id.TVDeleteAccountDescriptionEmail);
        ETEmail.setText(currentUser.getEmail() + "?");

        btnConfirm.setOnClickListener(v -> {
            v.setScaleX(0.9f);
            v.setScaleY(0.9f);
            v.postDelayed(() -> {
                v.setScaleX(1f);
                v.setScaleY(1f);
                handleAccountDeletion();
            }, 100);
        });

        btnCancel.setOnClickListener(v -> {
            v.setScaleX(0.9f);
            v.setScaleY(0.9f);
            v.postDelayed(() -> {
                v.setScaleX(1f);
                v.setScaleY(1f);
                dismiss();
            }, 100);
        });

        btnClose.setOnClickListener(v -> {
            v.setScaleX(0.9f);
            v.setScaleY(0.9f);
            v.postDelayed(() -> {
                v.setScaleX(1f);
                v.setScaleY(1f);
                dismiss();
            }, 100);
        });

        return view;
    }

    private void handleAccountDeletion() {
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current user from the repository
        User currentUser = userRepository.getCurrentUser();
        if (currentUser != null) {
            String storedPassword = currentUser.getPassword(); // Retrieve password from the database

            // Check if the entered password matches the stored password
            if (password.equals(storedPassword)) {
                // Delete the user account
                userRepository.deleteUserInFirestore(currentUser);

                Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

                // Navigate to the login page
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                dismiss();
            } else {
                // Show error if passwords do not match
                Toast.makeText(getContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "No user is signed in", Toast.LENGTH_SHORT).show();
        }
    }
}
