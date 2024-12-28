package com.example.madproject.ui.discussion;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;
import com.example.madproject.databinding.FragmentDetailPostBinding;
import com.example.madproject.ui.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class DetailPostFragment extends Fragment {

    // Parameters
    private String image;
    private String content;

    // Track like status
    private boolean isLiked = false;
    private boolean isReportConfirmationVisible = false;

    private ImageView IVPostImage;
    private TextView TVPostText;
    private ImageView IBComment, IBLike, IBReport;
    private RecyclerView commentsRecyclerView;
    private Button BtnConfirmReport;
    
    private FragmentDetailPostBinding binding;
    private DiscussionViewModel discussionViewModel;
    private CommentAdapter commentAdapter;
    private List<String> commentsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailPostBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        ViewModelFactory factory = new ViewModelFactory(getContext());
        discussionViewModel = new ViewModelProvider(requireActivity(),factory).get(DiscussionViewModel.class);
        
        IVPostImage = binding.IVPostImage;
        TVPostText = binding.TVPostText;
        IBComment = binding.IBComment;
        IBLike = binding.IBLike;
        IBReport = binding.IBReport;
        commentsRecyclerView = binding.RVComments;
        BtnConfirmReport = binding.ConfirmReportButton;
        
        Post post = discussionViewModel.getPostLiveData().getValue();
        
        if(post != null) {
            String imageUrl = post.getImageUrl();
            String content = post.getContent();

            // Load image using URI or set as drawable resource
            if (imageUrl != null) {
                Uri imageUri = Uri.parse(imageUrl);
                IVPostImage.setImageURI(imageUri);
            }

            // Set content text
            if (content != null) {
                TVPostText.setText(content);
            }
        }

        // Initialize comments list and adapter
        commentsList = new ArrayList<>();
        populateDummyComments(); // Add some dummy comments
        commentAdapter = new CommentAdapter(commentsList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRecyclerView.setAdapter(commentAdapter);

        // Like button toggle logic
        IBLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    // Change to unfilled like button
                    IBLike.setImageResource(R.drawable.dislike_button);
                    // user.like(post);
                } else {
                    // Change to filled like button
                    IBLike.setImageResource(R.drawable.liked_button);
                    // user.dislike(post);
                }
                isLiked = !isLiked; // Toggle like state
            }
        });

        // Report button logic
        IBReport.setOnClickListener(v -> {
            if (!isReportConfirmationVisible) {
                BtnConfirmReport.setVisibility(View.VISIBLE);
                isReportConfirmationVisible = true;
            }
        });

        // Confirm report button logic
        BtnConfirmReport.setOnClickListener(v -> {
            BtnConfirmReport.setVisibility(View.GONE);
            isReportConfirmationVisible = false;
            Toast.makeText(getContext(), "Report Successful", Toast.LENGTH_SHORT).show();
        });

        discussionViewModel.setPostLiveData(null);

        return root;
    }

    // Add dummy comments to the list
    private void populateDummyComments() {
        commentsList.add("This is a great post!");
        commentsList.add("Thanks for sharing.");
        commentsList.add("Amazing content, keep it up!");
        commentsList.add("Really insightful post.");
        commentsList.add("Great work!");
    }
}
