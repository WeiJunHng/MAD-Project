package com.example.madproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if savedInstanceState is null to avoid reloading the fragment on orientation change
        if (savedInstanceState == null) {
            // Load the LocationFragment into the fragment_container
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new location()); // 'location' is the fragment class
            transaction.commit();
        }
    }
}

/*
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String imagePath = "/storage/emulated/0/Download/516A4232.JPG";

        // Check if the file exists
        File file = new File(imagePath);
        if (file.exists()) {
            Log.d("File Path", "File exists at: " + imagePath);
        } else {
            Log.e("File Path", "File does not exist at: " + imagePath);
        }

        // Proceed with Cloudinary upload if the file exists
        new Thread(() -> {
            try {
                if (file.exists()) {
                    String imageUrl = CloudinaryUploader.uploadImage(imagePath);
                    Log.d("Cloudinary", "Uploaded image URL: " + imageUrl);
                } else {
                    Log.e("Cloudinary", "File does not exist. Cannot upload.");
                }
            } catch (Exception e) {
                Log.e("Cloudinary", "Upload failed", e);
            }
        }).start();
    }
}
*/
