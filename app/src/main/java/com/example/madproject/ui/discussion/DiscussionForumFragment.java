package com.example.madproject.ui.discussion;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.madproject.R;
import com.example.madproject.data.model.Discussion;
import com.example.madproject.data.repository.DiscussionRepository;
import com.example.madproject.databinding.FragmentDiscussionForumBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class DiscussionForumFragment extends Fragment {

    private ImageButton IBForumChatButton, IBForumPostButton;
    private RecyclerView recyclerView;
    private PostListAdapter postListAdapter;
    private List<Discussion> postList;
    private FragmentDiscussionForumBinding binding;
    private DiscussionRepository discussionRepository;;
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

        discussionRepository = new DiscussionRepository(requireContext());

        ViewModelFactory factory = new ViewModelFactory(getContext());
        discussionViewModel = new ViewModelProvider(requireActivity(),factory).get(DiscussionViewModel.class);

        recyclerView = binding.RVPosts;
        IBForumChatButton = binding.IBForumChatButton;
        IBForumPostButton = binding.IBForumPostButton;

        // Example data
        postList = discussionRepository.getAllDiscussion();
//        postList.add(new Post("Content 1", "https://example.com/image1.jpg"));
//        postList.add(new Post( "Content 2", "https://example.com/image2.jpg"));

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

//    private void navigateToCreatePostFragment() {
//        requireActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.FCVMain, new CreatePostFragment())
//                .addToBackStack(null)
//                .commit();
//    }

    private void navigateToCreatePostFragment() {
        Navigation.findNavController(requireActivity(), R.id.FCVMain).navigate(R.id.createPostFragment);
    }

    private void switchPostDetailsFragment() {
        Navigation.findNavController(requireActivity(), R.id.FCVMain).navigate(R.id.detailPostFragment);
    }

//    private void switchPostDetailsFragment() {
//        requireActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.FCVMain, new DetailPostFragment())
//                .addToBackStack(null)
//                .commit();
//    }
}