package com.example.madproject;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageHandler extends Fragment {

    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int IMAGE_PICKER_REQUEST_CODE = 100;

    private ImageView profileImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void requestImage(View view) {
        profileImageView = (ImageView) view;

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            openImagePicker();
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            profileImageView.setImageURI(imageUri);

            // Optionally upload the image to Cloudinary
            uploadImageToCloudinary(imageUri);
        }
    }

    private void uploadImageToCloudinary(Uri imageUri) {
        if (imageUri == null) {
            Toast.makeText(requireContext(), "Invalid image URI", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File file = new File(getContext().getCacheDir(), "temp_profile_pic.jpg");
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);

            try (FileOutputStream out = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }

            new Thread(() -> {
                try {
                    String imageUrl = CloudinaryUploader.uploadImage(file.getAbsolutePath());
                    requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Uploaded to: " + imageUrl, Toast.LENGTH_SHORT).show());
                } catch (Exception e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show());
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(requireContext(), "Storage permission is required to upload images", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
