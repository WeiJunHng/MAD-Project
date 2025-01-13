package com.example.madproject;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ImageHandler {

//    private static final int STORAGE_PERMISSION_CODE = 101;

    private final ActivityResultLauncher<Intent> imagePickerLauncher;
    private final ActivityResultLauncher<String>  requestPermissionLauncher;

    public ImageHandler(@NonNull Fragment fragment, @NonNull ImageView imageView) {
        imagePickerLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        imageView.setImageURI(selectedImageUri);
                    }
                }
        );

        requestPermissionLauncher = fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Launch image picker if permission granted
                        imagePickerLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                    } else {
                        Toast.makeText(fragment.getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    }
        });
    }

    public ImageHandler(@NonNull Fragment fragment, @NonNull ImageView imageView, @NonNull CardView cardView) {
        imagePickerLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        cardView.setVisibility(View.VISIBLE);
                        imageView.setImageURI(selectedImageUri);
                    }
                }
        );

        requestPermissionLauncher = fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Launch image picker if permission granted
                        imagePickerLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                    } else {
                        Toast.makeText(fragment.getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void launchImagePicker() {
        requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
    }

//    public static String uploadImageToCloudinary(Context context, Bitmap bitmap) {
//        try {
//            File file = new File(context.getCacheDir(), "temp_profile_pic.jpg");
////            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
//
//            FileOutputStream out = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//
//            return CloudinaryUploader.uploadImage(file.getAbsolutePath());
//        } catch (Exception e) {
//            Log.e("Cloudinary", "Error uploading image: " + e.getMessage());
//            return null;
//        }
//    }

    public static Drawable getNotAvailableDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.not_available);
    }

    public static void loadImage(String imageBase64, ImageView imageView) {
        Activity activity = (Activity) imageView.getContext();
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            try {
                byte[] decodedBytes = android.util.Base64.decode(imageBase64, Base64.NO_WRAP);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                activity.runOnUiThread(() -> imageView.setImageBitmap(bitmap));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                activity.runOnUiThread(() -> imageView.setImageResource(R.drawable.not_available));
            }
        } else {
            activity.runOnUiThread(() -> imageView.setImageResource(R.drawable.not_available));
        }
    }

    public static String encodeImage(ImageView imageView) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(() -> {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
            if (bitmapDrawable == null) return null;

            Bitmap bitmap = bitmapDrawable.getBitmap();

            try {
                // Compress and convert the bitmap to Base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.WEBP, 60, byteArrayOutputStream);
                return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);
            } catch (Exception e) {
                Log.e("SettingsFragment", "Error processing avatar image", e);
                Toast.makeText(imageView.getContext(), "Failed to process avatar image", Toast.LENGTH_SHORT).show();
                return null; // Stop further processing
            }
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void launch(@NonNull Fragment fragment, @NonNull ImageView imageView) {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        ActivityResultLauncher<Intent> imagePickerLauncher = fragment.registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(), result -> {
//                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
//                        Uri selectedImageUri = result.getData().getData();
//                        imageView.setImageURI(selectedImageUri);
//                        try {
//                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
//                            // Handle the selected image (e.g., display or upload)
//                            Toast.makeText(fragment, "Image selected "+selectedImageUri, Toast.LENGTH_SHORT).show();
//                        } catch (IOException e) {
//                            Log.e(TAG, "Error loading image: " + e.getMessage());
//                        }
//                    }
//                }
//        );
//
//        ActivityResultLauncher<String>  requestPermissionLauncher = fragment.registerForActivityResult(
//                new ActivityResultContracts.RequestPermission(),
//                isGranted -> {
//                    if (isGranted) {
//                        // Launch image picker if permission granted
//                        imagePickerLauncher.launch(intent);
//                    } else {
//                        Toast.makeText(fragment.getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        if (ActivityCompat.checkSelfPermission(fragment.requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(fragment.requireActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//        } else {
//            imagePickerLauncher.launch(intent);
//        }
//
//        requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
//    }

}
