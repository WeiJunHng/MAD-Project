package com.example.madproject.ui.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.madproject.R;
import com.example.madproject.databinding.FragmentForgotPasswordVerifyBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import soup.neumorphism.NeumorphButton;

public class ForgotPasswordVerifyFragment extends Fragment {

    private TextInputEditText ETCode;
    private NeumorphButton BtnVerify, BtnCancel;
    private FragmentForgotPasswordVerifyBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentForgotPasswordVerifyBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(requireActivity());
        loginViewModel = new ViewModelProvider(requireActivity(), factory).get(LoginViewModel.class);

        View root = binding.getRoot();

        ETCode = binding.ETCode;
        BtnVerify = binding.BtnVerify;
        BtnCancel = binding.BtnCancel;

        BtnVerify.setOnClickListener(v -> verifyCode());
        BtnCancel.setOnClickListener(view -> requireActivity().finish());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void verifyCode() {
        String code = loginViewModel.getVerificationCode().getValue();
        String codeInput = ETCode.getText().toString().trim();

        if(codeInput.equals(code)) {
            switchResetPasswordFragment();
        } else {
            Toast.makeText(requireContext(), "Invalid verification code", Toast.LENGTH_SHORT).show();
        }
    }

    private void switchResetPasswordFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.ForgotPasswordFCV, new ForgotPasswordResetFragment())
                .addToBackStack(null)
                .commit();
    }
}