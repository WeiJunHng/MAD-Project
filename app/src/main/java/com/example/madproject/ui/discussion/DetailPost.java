package com.example.madproject.ui.discussion;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailPost#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailPost extends Fragment {

    // Parameter arguments
    private static final String ARG_IMAGE = "image";
    private static final String ARG_CONTENT = "content";

    // Parameters
    private String image;
    private String content;

    // Track like status
    private boolean isLiked = false;
    private boolean isReportConfirmationVisible = false;

    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private List<String> commentsList;

    public DetailPost() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param image Image URL or resource.
     * @param content Content text for the post.
     * @return A new instance of fragment DetailPost.
     */
    public static DetailPost newInstance(String image, String content) {
        DetailPost fragment = new DetailPost();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE, image);
        args.putString(ARG_CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            image = getArguments().getString(ARG_IMAGE);
            content = getArguments().getString(ARG_CONTENT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_post, container, false);

        // Find views
        ImageView imageView = view.findViewById(R.id.IVPostImage);
        TextView contentTextView = view.findViewById(R.id.TVPosttext);
        ImageButton likeButton = view.findViewById(R.id.IBLike);
        ImageButton reportButton = view.findViewById(R.id.IBReport);
        View confirmReportButton = view.findViewById(R.id.ConfirmReportButton);
        commentsRecyclerView = view.findViewById(R.id.RVComments);

        // Load image using URI or set as drawable resource
        if (image != null) {
            Uri imageUri = Uri.parse(image);
            imageView.setImageURI(imageUri);
        }

        // Set content text
        if (content != null) {
            contentTextView.setText(content);
        }

        // Initialize comments list and adapter
        commentsList = new ArrayList<>();
        populateDummyComments(); // Add some dummy comments
        commentAdapter = new CommentAdapter(commentsList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRecyclerView.setAdapter(commentAdapter);

        // Like button toggle logic
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    // Change to unfilled like button
                    likeButton.setImageResource(R.drawable.dislike_button);
                } else {
                    // Change to filled like button
                    likeButton.setImageResource(R.drawable.liked_button);
                }
                isLiked = !isLiked; // Toggle like state
            }
        });

        // Report button logic
        reportButton.setOnClickListener(v -> {
            if (!isReportConfirmationVisible) {
                confirmReportButton.setVisibility(View.VISIBLE);
                isReportConfirmationVisible = true;
            }
        });

        // Confirm report button logic
        confirmReportButton.setOnClickListener(v -> {
            confirmReportButton.setVisibility(View.GONE);
            isReportConfirmationVisible = false;
            Toast.makeText(getContext(), "Report Successful", Toast.LENGTH_SHORT).show();
        });

        return view;
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
