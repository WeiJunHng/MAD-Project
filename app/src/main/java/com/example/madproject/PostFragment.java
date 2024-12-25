package com.example.madproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

public class PostFragment extends Fragment {

    private static final String TAG = "PostFragment";
    private ActivityResultLauncher<Intent> imagePickerLauncher; // Launcher for image selection

    private EditText editTextPost; // EditText for post content
    private Uri selectedImageUri; // Selected image URI

    public PostFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        // Find views
        Button postButton = view.findViewById(R.id.BtnPost);
        ImageButton boldButton = view.findViewById(R.id.IBBoldText);
        ImageButton locationButton = view.findViewById(R.id.IBLocation);
        ImageButton linkButton = view.findViewById(R.id.IBLink);
        ImageButton tagUserButton = view.findViewById(R.id.IBTagUser);
        ImageButton galleryButton = view.findViewById(R.id.IBGallery);
        editTextPost = view.findViewById(R.id.ETText);

        // Set up the image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                            // Handle the selected image (e.g., display or upload)
                            Toast.makeText(getContext(), "Image selected", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Log.e(TAG, "Error loading image: " + e.getMessage());
                        }
                    }
                }
        );

        // Handle Post button click
        postButton.setOnClickListener(v -> handlePostButtonClick());

        // Handle Bold Text button click
        boldButton.setOnClickListener(v -> applyBoldText());

        // Handle Location button click
        locationButton.setOnClickListener(v -> handleLocationButtonClick());

        // Handle Link button click
        linkButton.setOnClickListener(v -> handleLinkButtonClick());

        // Handle Tag User button click
        tagUserButton.setOnClickListener(v -> handleTagUserButtonClick());

        // Handle Gallery button click
        galleryButton.setOnClickListener(v -> openImagePicker());

        return view;
    }

    private void handlePostButtonClick() {
        String postContent = editTextPost.getText().toString();
        if (postContent.isEmpty()) {
            Toast.makeText(getContext(), "Post content cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            // Save or upload post data (content and imageUri)
            Post post = new Post(postContent, selectedImageUri != null ? selectedImageUri.toString() : null);
            Toast.makeText(getContext(), "Post created successfully!", Toast.LENGTH_SHORT).show();

            // Optionally: Clear fields after posting
            editTextPost.setText("");
            selectedImageUri = null;
        }
    }

    private void applyBoldText() {
        int start = editTextPost.getSelectionStart();
        int end = editTextPost.getSelectionEnd();
        if (start < end) {
            SpannableStringBuilder spannable = new SpannableStringBuilder(editTextPost.getText());
            spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            editTextPost.setText(spannable);
            editTextPost.setSelection(end); // Keep the cursor at the end
        } else {
            Toast.makeText(getContext(), "Select text to make it bold", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLocationButtonClick() {
        Toast.makeText(getContext(), "Location feature not implemented yet", Toast.LENGTH_SHORT).show();
        // Implement location picker functionality
    }

    private void handleLinkButtonClick() {
        // Example logic: Insert a dummy link into the text
        String link = "https://example.com";
        int start = editTextPost.getSelectionStart();
        editTextPost.getText().insert(start, link);
    }

    private void handleTagUserButtonClick() {
        // Example logic: Add a dummy tagged user
        String tag = "@username";
        int start = editTextPost.getSelectionStart();
        editTextPost.getText().insert(start, tag);
    }

    private void openImagePicker() {
        // Launch the image picker intent
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }
}
