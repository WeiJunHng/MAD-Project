package com.example.madproject.ui.login;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madproject.databinding.FragmentForgotPasswordEmailBinding;
import com.example.madproject.mailsender.MailSender;
import com.example.madproject.mailsender.MailgunAPI;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

import soup.neumorphism.NeumorphButton;

public class ForgotPasswordEmailFragment extends Fragment {

    private TextInputEditText ETEmail;
    private NeumorphButton BtnSendEmail, BtnCancel;

    private FragmentForgotPasswordEmailBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentForgotPasswordEmailBinding.inflate(inflater, container, false);

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

        MailgunAPI mailSender = new MailgunAPI();
        mailSender.sendOTPEmail(recipientEmail, otp);
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }
}