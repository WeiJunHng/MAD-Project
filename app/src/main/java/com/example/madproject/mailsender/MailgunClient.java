package com.example.madproject.mailsender;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MailgunClient {
    private static final String BASE_URL = "https://api.mailgun.net";
    private static Retrofit retrofit;

    public static MailgunAPI getMailgunService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(MailgunAPI.class);
    }
}

