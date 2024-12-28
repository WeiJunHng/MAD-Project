package com.example.madproject.mailsender;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Base64;

public class EmailSender {
    private static final String API_KEY = "9c2c5a2056755da2169653a73d0d77b5-2e68d0fb-7f384024"; // Replace with your Mailgun API key
    private static final String DOMAIN_NAME = "sandbox8f1af868b7d742d6b74f8c9a509d7027.mailgun.org"; // Replace with your Mailgun domain

    public void sendEmail(String from, String to, String subject, String text) {
        MailgunAPI api = MailgunClient.getMailgunService();

        // Create the Basic Auth header
        String auth = "Basic " + Base64.encodeToString(("api:" + API_KEY).getBytes(), Base64.NO_WRAP);

//        api.sendEmail(auth, from, to, subject, text).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    System.out.println("Email sent successfully!");
//                } else {
//                    System.err.println("Failed to send email: " + response.code() + " - " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                System.err.println("Error: " + t.getMessage());
//            }
//        });
    }
}

