package com.example.madproject.ui.discussion;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.madproject.ImageHandler;
import com.example.madproject.R;
import com.example.madproject.databinding.FragmentCreatePostBinding;
import com.example.madproject.ui.ViewModelFactory;

public class CreatePostFragment extends Fragment {

    private static final String TAG = "PostFragment";
    private ActivityResultLauncher<Intent> imagePickerLauncher; // Launcher for image selection

    private CardView CVSelectedImage;
    private ImageView IVSelectedImage;
    private EditText editTextPost; // EditText for post content
    private Uri selectedImageUri; // Selected image URI
    private ProgressDialog progressDialog;
    private FragmentCreatePostBinding binding;
    private DiscussionViewModel discussionViewModel;
    private ImageHandler imageHandler;

    public CreatePostFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        binding = FragmentCreatePostBinding.inflate(inflater, container, false);
        
        View root = binding.getRoot();
        
        ViewModelFactory factory = new ViewModelFactory(requireActivity());
        discussionViewModel = new ViewModelProvider(requireActivity(),factory).get(DiscussionViewModel.class);

        // Find views
        Button postButton = binding.BtnPost;
        ImageButton boldButton = binding.IBBoldText;
        ImageButton locationButton = binding.IBLocation;
        ImageButton linkButton = binding.IBLink;
        ImageButton tagUserButton = binding.IBTagUser;
        ImageButton galleryButton = binding.IBGallery;
        CVSelectedImage = binding.CVSelectedImage;
        IVSelectedImage = binding.IVSelectedImage;
        editTextPost = binding.ETText;

        imageHandler = new ImageHandler(this, IVSelectedImage, CVSelectedImage);

        // Set up the image picker launcher
//        imagePickerLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
//                        selectedImageUri = result.getData().getData();
//                        try {
//                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
//                            // Handle the selected image (e.g., display or upload)
//                            Toast.makeText(getContext(), "Image selected "+selectedImageUri, Toast.LENGTH_SHORT).show();
//                        } catch (IOException e) {
//                            Log.e(TAG, "Error loading image: " + e.getMessage());
//                        }
//                    }
//                }
//        );

//        IVSelectedImage.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
//            if (IVSelectedImage.getDrawable() == null) {
//                CVSelectedImage.setVisibility(View.GONE);
//            } else {
//                CVSelectedImage.setVisibility(View.VISIBLE);
//            }
//        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Create Post");
        progressDialog.setMessage("Uploading...");

        discussionViewModel.getDiscussionCreatedLiveData().observe(getViewLifecycleOwner(), discussion -> {
            if (discussion != null) {
                Toast.makeText(getContext(), "Discussion created successfully!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                discussionViewModel.setDiscussionCreatedLiveData(null);
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

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

        return root;
    }

    private void handlePostButtonClick() {
        String postContent = editTextPost.getText().toString();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("userPreferences", MODE_PRIVATE);
        String authorId = sharedPreferences.getString("userId", null);

        String imageUrl = null;
        if(selectedImageUri != null) {
            imageUrl = selectedImageUri.toString();
        }

        if (postContent.isEmpty()) {
            Toast.makeText(getContext(), "Post content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (authorId == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save or upload post data (content and imageUri)
//        Post post = new Post(postContent, selectedImageUri != null ? selectedImageUri.toString() : null);
//        Toast.makeText(getContext(), "Post created successfully!", Toast.LENGTH_SHORT).show();

        progressDialog.show();

//        String encodedImage = ImageHandlerNew.encodeImage(IVSelectedImage);
        discussionViewModel.createDiscussion(authorId, IVSelectedImage, postContent);

        // Optionally: Clear fields after posting
        editTextPost.setText("");
        selectedImageUri = null;
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        NavController navController = Navigation.findNavController(requireActivity(), R.id.FCVMain);
//        navController.popBackStack();
//        Navigation.findNavController(requireActivity(), R.id.FCVMain).navigate(R.id.DestPost);
//    }

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
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        imagePickerLauncher.launch(intent);
        imageHandler.launchImagePicker();
    }
}
