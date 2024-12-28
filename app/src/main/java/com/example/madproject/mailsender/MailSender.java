package com.example.madproject.mailsender;

import android.util.Log;

import java.util.Properties;

import javax.mail.*;
import javax.mail.Authenticator;
import javax.mail.internet.*;

public class MailSender {

    private static final String FROM_EMAIL = "madguardian04@gmail.com";
    private static final String PASSWORD = "guardianofsdg";
    private final Properties properties = new Properties();
    private final Session session;

    public MailSender() {
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });
    }

    public void sendOTPEmail(String recipientEmail, String otp) {
        new Thread(()->{
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(FROM_EMAIL));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject("Your OTP Code");

                String emailMessage = "Your OTP verification code is: " + otp;

                message.setText(emailMessage);

                Transport.send(message);

                Log.d("OTP", "OTP sent to: " + recipientEmail);
            } catch (MessagingException e) {
                e.printStackTrace();
                Log.e("OTP", "Error sending OTP email: " + e.getMessage());
            }
        }).start();
    }

}

