package com.example.madproject.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.madproject.R;
import com.example.madproject.data.model.User;
import com.example.madproject.data.repository.UserRepository;
import com.google.android.material.textfield.TextInputEditText;

public class ChangePasswordDialog extends DialogFragment {

    private UserRepository userRepository;
    private User currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);

        userRepository = new UserRepository(requireContext());
        currentUser = userRepository.getCurrentUser();

        ImageButton btnClose = rootView.findViewById(R.id.BtnCloseIcon);

        btnClose.setOnClickListener(v -> {
            v.setScaleX(0.9f);
            v.setScaleY(0.9f);
            v.postDelayed(() -> {
                v.setScaleX(1f);
                v.setScaleY(1f);
                dismiss();
            }, 100);
        });

        TextInputEditText ETCurrentPassword = rootView.findViewById(R.id.ETCurrentPassword);
        TextInputEditText ETNewPassword = rootView.findViewById(R.id.ETNewPassword);
        TextInputEditText ETConfirmPassword = rootView.findViewById(R.id.ETConfirmPassword);
        Button btnSave = rootView.findViewById(R.id.BtnSave);

        btnSave.setOnClickListener(v -> {
            // Get the values entered by the user
            String currentPassword = ETCurrentPassword.getText().toString().trim();
            String newPassword = ETNewPassword.getText().toString().trim();
            String confirmPassword = ETConfirmPassword.getText().toString().trim();

            // Check if the current password is correct
            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                // Show an error message if any field is empty
                Toast.makeText(requireContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!currentPassword.equals(currentUser.getPassword())) {
                // Show an error if the current password doesn't match the stored password
                Toast.makeText(requireContext(), "Current password is incorrect.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the new password and confirm password match
            if (!newPassword.equals(confirmPassword)) {
                // Show an error message if the new password doesn't match the confirmation
                Toast.makeText(requireContext(), "New password and confirm password do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the new password is different from the current password
            if (newPassword.equals(currentPassword)) {
                // Show an error message if the new password is the same as the current password
                Toast.makeText(requireContext(), "New password cannot be the same as the current password.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update the password in the currentUser object
            currentUser.setPassword(newPassword);

            // Save the updated password to the database
            userRepository.updateUserInFirestore(currentUser);

            // Show a success message
            Toast.makeText(requireContext(), "Password updated successfully.", Toast.LENGTH_SHORT).show();

            // Optionally, clear the fields after saving
            ETCurrentPassword.setText("");
            ETNewPassword.setText("");
            ETConfirmPassword.setText("");

            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.FCVMain, new ProfilePageFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return rootView;
    }
}