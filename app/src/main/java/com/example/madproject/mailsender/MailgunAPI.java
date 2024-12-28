package com.example.madproject.mailsender;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.*;
import java.io.IOException;

public class MailgunAPI {
    private static final String MAILGUN_API_KEY = "9c2c5a2056755da2169653a73d0d77b5-2e68d0fb-7f384024";
    private static final String DOMAIN = "sandbox8f1af868b7d742d6b74f8c9a509d7027.mailgun.org";
    private static final String FROM_EMAIL = "no-reply@soluna.com";

    public void sendOTPEmail(String recipientEmail, String otp) {
        OkHttpClient client = new OkHttpClient();

        // Prepare the request data (email content)
        RequestBody requestBody = new FormBody.Builder()
                .add("from", FROM_EMAIL)
                .add("to", recipientEmail)
                .add("subject", "Your OTP Code")
                .add("text", "Your OTP code is: " + otp)
                .build();

        // Create the request
        Request request = new Request.Builder()
                .url("https://api.mailgun.net/v3/" + DOMAIN + "/messages")
                .addHeader("Authorization", "Basic " + getBasicAuth())
                .post(requestBody)
                .build();

        // Make the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // OTP email sent successfully
                    Log.d("Mailgun", "OTP email sent successfully to: " + recipientEmail);
                } else {
                    // Error occurred
                    Log.e("Mailgun", "Error sending OTP email: " + response.body().string());
                }
            }
        });
    }

    // Helper method to get Basic Auth header value
    private String getBasicAuth() {
        String credentials = "api:" + MAILGUN_API_KEY;
        return Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }
}

