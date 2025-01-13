package com.example.madproject.ui.discussion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.madproject.R;
import com.example.madproject.data.db.AppDatabase;
import com.example.madproject.data.db.FirestoreManager;
import com.example.madproject.data.model.Discussion;
import com.example.madproject.data.model.DiscussionLike;
import com.example.madproject.data.model.User;
import com.example.madproject.data.repository.DiscussionRepository;
import com.example.madproject.databinding.FragmentDiscussionForumBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;
import java.util.List;

public class DiscussionForumFragment extends Fragment {

    private ImageButton IBForumChatButton, IBForumPostButton;
    private RecyclerView recyclerView;
    private PostListAdapter postListAdapter;
    private List<Discussion> postList;
    private FragmentDiscussionForumBinding binding;
    private DiscussionRepository discussionRepository;
    private DiscussionViewModel discussionViewModel;
    private AppDatabase appDatabase;
    private FirestoreManager firestoreManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        discussionRepository = new DiscussionRepository(requireContext());
        postList = discussionRepository.getAllDiscussion();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDiscussionForumBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        ViewModelFactory factory = new ViewModelFactory(getContext());
        discussionViewModel = new ViewModelProvider(requireActivity(),factory).get(DiscussionViewModel.class);

        recyclerView = binding.RVPosts;
        IBForumChatButton = binding.IBForumChatButton;
        IBForumPostButton = binding.IBForumPostButton;

        discussionViewModel.getBackFromDetailLiveData().observe(getViewLifecycleOwner(), back -> {
            if(back == null) {
                postList = discussionRepository.getAllDiscussion();
            } else if(!back) {
                postList = discussionRepository.getAllDiscussion();
                discussionViewModel.setBackFromDetailLiveData(false);
            }
        });

        postListAdapter = new PostListAdapter(getActivity(), getContext(), postList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(postListAdapter);

        discussionViewModel.getDiscussionLiveData().observe(getViewLifecycleOwner(), post -> {
            if(post != null) {
                switchPostDetailsFragment();
            }
        });

        // Navigate to Create Post page when the post button is clicked
        IBForumPostButton.setOnClickListener(v -> navigateToCreatePostFragment());
        
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        new Thread(() -> {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
            String currentUserId = sharedPreferences.getString("userId", null);

            for(Discussion post:postList) {
                if(post.isLiked() != post._getInitialLiked()) {
                    post._setInitialLiked(post.isLiked());
                    if(post.isLiked()) {
                        discussionRepository.insertDiscussionLikeInFirestore(new DiscussionLike(post.getId(), currentUserId, new Date()));
                    } else {
                        DiscussionLike discussionLike = discussionRepository.getDiscussionLike(post.getId(), currentUserId);
                        discussionRepository.deleteDiscussionLikeInFirestore(discussionLike);
                    }
                }
            }
        }).start();
    }

    private void navigateToCreatePostFragment() {
        Navigation.findNavController(requireActivity(), R.id.FCVMain).navigate(R.id.createPostFragment);
    }

    private void switchPostDetailsFragment() {
        Navigation.findNavController(requireActivity(), R.id.FCVMain).navigate(R.id.detailPostFragment);
    }
}