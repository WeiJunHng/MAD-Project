package com.example.madproject.ui.discussion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.madproject.R;
import com.example.madproject.databinding.FragmentDiscussionForumBinding;
import com.example.madproject.ui.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class DiscussionForumFragment extends Fragment {

    private ImageButton IBForumChatButton, IBForumPostButton;
    private RecyclerView recyclerView;
    private PostListAdapter postListAdapter;
    private final List<Post> postList = new ArrayList<>();
    private FragmentDiscussionForumBinding binding;
    private DiscussionViewModel discussionViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Example data
        postList.add(new Post("Content 1", "https://example.com/image1.jpg"));
        postList.add(new Post( "Content 2", "https://example.com/image2.jpg"));

        postListAdapter = new PostListAdapter(getActivity(), getContext(), postList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(postListAdapter);

        discussionViewModel.getPostLiveData().observe(getViewLifecycleOwner(),post -> {
            if(post != null) {
                switchPostDetailsFragment();
            }
        });

        // Navigate to Create Post page when the post button is clicked
        IBForumPostButton.setOnClickListener(v -> navigateToCreatePostFragment());
        
        return root;
    }

    private void navigateToCreatePostFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FCVMain, new CreatePostFragment())
                .addToBackStack(null)
                .commit();
    }

    private void switchPostDetailsFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FCVMain, new DetailPostFragment())
                .addToBackStack(null)
                .commit();
    }
}