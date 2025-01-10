package com.example.madproject.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.madproject.R;
import com.example.madproject.data.model.User;
import com.example.madproject.databinding.FragmentForgotPasswordResetBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import soup.neumorphism.NeumorphButton;

public class ForgotPasswordResetFragment extends Fragment {

    private TextInputEditText ETPassword, ETConfirmPassword;
    private NeumorphButton BtnReset, BtnCancel;

    private FragmentForgotPasswordResetBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentForgotPasswordResetBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(requireActivity());
        loginViewModel = new ViewModelProvider(requireActivity(), factory).get(LoginViewModel.class);

        View root = binding.getRoot();

        ETPassword = binding.ETPassword;
        ETConfirmPassword = binding.ETConfirmPassword;
        BtnReset = binding.BtnReset;
        BtnCancel = binding.BtnCancel;

        loginViewModel.getStatusResetPw().observe(getViewLifecycleOwner(), status -> {
            if (status) {
                Toast.makeText(getContext(), "Password reset successfully", Toast.LENGTH_SHORT).show();
                loginViewModel.setForgotPwEmail(null);
                loginViewModel.setVerificationCode(null);
                requireActivity().finish();
            } else {
                Toast.makeText(getContext(), "Password reset failed", Toast.LENGTH_SHORT).show();
            }
        });

        BtnReset.setOnClickListener(v -> reset());
        BtnCancel.setOnClickListener(view -> requireActivity().finish());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void reset() {
        String email = loginViewModel.getForgotPwEmail().getValue();
        String newPw = ETPassword.getText().toString();
        String confirmNewPw = ETConfirmPassword.getText().toString();

        if(!newPw.equals(confirmNewPw)) {
            Toast.makeText(getContext(), "Check your password again", Toast.LENGTH_SHORT).show();
            return;
        }

        loginViewModel.resetPassword(email, newPw);
    }
}