package com.example.madproject.ui.discussion;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.ImageHandler;
import com.example.madproject.R;
import com.example.madproject.data.model.Discussion;
import com.example.madproject.data.model.DiscussionComment;
import com.example.madproject.data.model.DiscussionLike;
import com.example.madproject.data.model.User;
import com.example.madproject.data.repository.DiscussionRepository;
import com.example.madproject.data.repository.UserRepository;
import com.example.madproject.databinding.FragmentDetailPostBinding;
import com.example.madproject.ui.ViewModelFactory;

import java.util.Date;
import java.util.List;

public class DetailPostFragment extends Fragment {

    // Parameters
    private String image;
    private String content;

    // Track like status
    private boolean isLikedInitial = false;
    private boolean isLiked = false;
    private boolean isReportConfirmationVisible = false;

    private ImageView IVPostImage;
    private TextView TVPostText;
    private ImageButton IBComment, IBLike, BtnSendComment, IBReport;
    private RecyclerView commentsRecyclerView;
    private EditText ETComment;
    private Button BtnConfirmReport;
    
    private FragmentDetailPostBinding binding;
    private DiscussionViewModel discussionViewModel;
    private CommentAdapter commentAdapter;
    private List<DiscussionComment> commentsList;
    private DiscussionRepository discussionRepository;
    private User currentUser;
    private Discussion discussion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailPostBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        discussionRepository = new DiscussionRepository(requireContext());
        UserRepository userRepository = new UserRepository(requireContext());
        userRepository.fetchUsers();

        ViewModelFactory factory = new ViewModelFactory(getContext());
        discussionViewModel = new ViewModelProvider(requireActivity(),factory).get(DiscussionViewModel.class);
        
        IVPostImage = binding.IVPostImage;
        TVPostText = binding.TVPostText;
        IBComment = binding.IBComment;
        IBLike = binding.IBLike;
        IBReport = binding.IBReport;
        commentsRecyclerView = binding.RVComments;
        ETComment = binding.ETComment;
        BtnSendComment = binding.BtnSendComment;
        BtnConfirmReport = binding.ConfirmReportButton;

        currentUser = userRepository.getCurrentUser();
        discussion = discussionViewModel.getDiscussionLiveData().getValue();

        if(discussion != null) {
            String imageUrl = discussion.getPictureUrl();
            String content = discussion.getContent();

            // Load image using URI or set as drawable resource
//            if (imageUrl == null) {
//                IVPostImage.setVisibility(View.GONE);
//            } else {
//                CloudinaryUploader.loadImage(userRepository.getCurrentUser().getProfilePicURL(), IVPostImage);
//                Uri imageUri = Uri.parse(imageUrl);
//                IVPostImage.setImageURI(imageUri);
//            }
            ImageHandler.loadImage(discussion.getPictureUrl(), IVPostImage);
            // Set content text
            if (content != null) {
                TVPostText.setText(content);
            }
        }

        // Initialize Like Button
        new Thread(() -> {
            if(discussion._getInitialLiked() == null) {
                boolean isLiked = discussionRepository.isLiked(discussion, currentUser);
                discussion._setInitialLiked(isLiked);
                discussion._setLiked(isLiked);
            }
            requireActivity().runOnUiThread(() -> {
                if(discussion.isLiked()) {
                    IBLike.setImageResource(R.drawable.liked_button);
                } else {
                    IBLike.setImageResource(R.drawable.dislike_button);
                }
            });
        }).start();
//        new Thread(() -> {
//            isLikedInitial = discussionRepository.isLiked(post, currentUser);
//            isLiked = isLikedInitial;
//            requireActivity().runOnUiThread(() -> {
//                if(isLikedInitial) {
//                    IBLike.setImageResource(R.drawable.liked_button);
//                } else {
//                    IBLike.setImageResource(R.drawable.dislike_button);
//                }
//            });
//        }).start();

        // Initialize comments list and adapter
        commentsList = discussionRepository.getAllCommentByDiscussion(discussion.getId());
//        populateDummyComments(); // Add some dummy comments
        commentAdapter = new CommentAdapter(commentsList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsRecyclerView.setAdapter(commentAdapter);

        IBComment.setOnClickListener(v -> ETComment.requestFocus());

        // Like button toggle logic
        IBLike.setOnClickListener(v -> {
            if (discussion.isLiked()) {
                // Change to unfilled like button
                discussion.disliked();
                IBLike.setImageResource(R.drawable.dislike_button);
            } else {
                // Change to filled like button
                discussion.liked();
                IBLike.setImageResource(R.drawable.liked_button);
            }
//            isLiked = !isLiked; // Toggle like state
        });

        // Report button logic
        IBReport.setOnClickListener(v -> {
            discussionViewModel.setDiscussionReportSelectedLiveData(discussion);
            DiscussionReportDialogFragment reportDialog = new DiscussionReportDialogFragment();
            reportDialog.show(getParentFragmentManager(), "reportDialog");
//            if (!isReportConfirmationVisible) {
//                BtnConfirmReport.setVisibility(View.VISIBLE);
//                isReportConfirmationVisible = true;
//            }
        });

        // Confirm report button logic
        BtnConfirmReport.setOnClickListener(v -> {
            BtnConfirmReport.setVisibility(View.GONE);
            isReportConfirmationVisible = false;
            Toast.makeText(getContext(), "Report Successful", Toast.LENGTH_SHORT).show();
        });

        BtnSendComment.setOnClickListener(v -> {
            String commentContent = ETComment.getText().toString();

            if(commentContent.isBlank()){
                Toast.makeText(getContext(), "Comment cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            String commentId = discussionRepository.createDiscussionCommentId();
            User commenter = userRepository.getCurrentUser();
            DiscussionComment comment = new DiscussionComment(commentId, discussion.getId(), commenter.getId(), new Date(), commentContent);

            discussionRepository.insertDiscussionCommentInFirestore(comment);

            commentsList.add(comment);
            commentAdapter.notifyDataSetChanged();

            // Hide keyboard
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = getView();
            if (view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            ETComment.setText(null);
            ETComment.clearFocus();
        });

        discussionViewModel.setDiscussionLiveData(null);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        new Thread(() -> {
            if(discussion.isLiked() != discussion._getInitialLiked()) {
                discussion._setInitialLiked(discussion.isLiked());
                if(discussion.isLiked()) {
                    discussionRepository.insertDiscussionLikeInFirestore(new DiscussionLike(discussion.getId(), currentUser.getId(), new Date()));
                } else {
                    DiscussionLike discussionLike = discussionRepository.getDiscussionLike(discussion.getId(), currentUser.getId());
                    discussionRepository.deleteDiscussionLikeInFirestore(discussionLike);
                }
                discussionViewModel.setBackFromDetailLiveData(true);
            }
        }).start();
    }

    // Add dummy comments to the list
//    private void populateDummyComments() {
//        commentsList.add("This is a great post!");
//        commentsList.add("Thanks for sharing.");
//        commentsList.add("Amazing content, keep it up!");
//        commentsList.add("Really insightful post.");
//        commentsList.add("Great work!");
//    }
}
