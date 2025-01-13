package com.example.madproject.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.madproject.R;
import com.example.madproject.data.model.User;
import com.example.madproject.data.repository.UserRepository;
import com.example.madproject.ui.login.LoginActivity;

public class SignOutDialog extends DialogFragment {

    private UserRepository userRepository;
    private User currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_out, container, false);

        userRepository = new UserRepository(requireContext());
        currentUser = userRepository.getCurrentUser();

        Button btnConfirm = view.findViewById(R.id.BtnConfirmSignOut);
        Button btnCancel = view.findViewById(R.id.BtnCancelSignOut);
        ImageButton btnClose = view.findViewById(R.id.BtnCloseIcon);

        btnConfirm.setOnClickListener(v -> {
            v.setScaleX(0.9f);
            v.setScaleY(0.9f);
            v.postDelayed(() -> {
                v.setScaleX(1f);
                v.setScaleY(1f);
                logOutUser();
                navigateToLoginPage();
                dismiss();
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
    private void logOutUser() {
        currentUser = null;
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", null);
        editor.apply();

        String userId = sharedPreferences.getString("userId", null);

        navigateToLoginPage();
        if (userId == null) {
            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to log out", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToLoginPage() {
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        requireActivity().finish();
        startActivity(intent);

    }

}