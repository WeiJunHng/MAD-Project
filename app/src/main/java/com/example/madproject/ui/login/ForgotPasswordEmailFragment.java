package com.example.madproject.ui.login;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madproject.R;
import com.example.madproject.databinding.FragmentForgotPasswordEmailBinding;
import com.example.madproject.mailsender.MailSender;
import com.example.madproject.mailsender.MailgunAPI;
import com.example.madproject.ui.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

import soup.neumorphism.NeumorphButton;

public class ForgotPasswordEmailFragment extends Fragment {

    private TextInputEditText ETEmail;
    private NeumorphButton BtnSendEmail, BtnCancel;

    private FragmentForgotPasswordEmailBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentForgotPasswordEmailBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(requireActivity());
        loginViewModel = new ViewModelProvider(requireActivity(), factory).get(LoginViewModel.class);

        View root = binding.getRoot();

        ETEmail = binding.ETEmail;
        BtnSendEmail = binding.BtnSendEmail;
        BtnCancel = binding.BtnCancel;

        BtnSendEmail.setOnClickListener(view -> sendVerificationEmail());

        BtnCancel.setOnClickListener(view -> requireActivity().finish());

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });

        return root;
    }

    private void sendVerificationEmail() {
        String recipientEmail = ETEmail.getText().toString();
        String otp = generateOTP();

        loginViewModel.setForgotPwEmail(recipientEmail);
        loginViewModel.setVerificationCode(otp);

        MailgunAPI mailSender = new MailgunAPI();
        mailSender.sendOTPEmail(recipientEmail, otp);

        switchVerifyFragment();
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }

    private void switchVerifyFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.ForgotPasswordFCV, new ForgotPasswordVerifyFragment())
                .addToBackStack(null)
                .commit();
    }
}