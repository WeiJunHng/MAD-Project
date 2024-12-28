package com.example.madproject;

import okhttp3.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class CloudinaryUploader {

    // Cloudinary credentials
    private static final String CLOUD_NAME = "dq1kmpbhe";  // Correct cloud name
    private static final String API_KEY = "193173525922352"; // API Key
    private static final String UPLOAD_PRESET = "profile_picture_upload"; // Correct unsigned preset

    public static String uploadImage(String imagePath) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        File file = new File(imagePath);
        if (!file.exists()) {
            throw new IOException("File not found at " + imagePath);
        }

        MediaType mediaType = MediaType.parse("image/*");
        RequestBody fileBody = RequestBody.create(file, mediaType);

        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("upload_preset", UPLOAD_PRESET) // Correct unsigned preset name
                .build();

        Request request = new Request.Builder()
                .url("https://api.cloudinary.com/v1_1/" + CLOUD_NAME + "/image/upload")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Upload failed: " + response.body().string());
        }

        String responseBody = response.body().string();
        JSONObject jsonResponse = new JSONObject(responseBody);

        // Extract and return the secure URL of the uploaded image
        return jsonResponse.getString("secure_url");
    }
}
