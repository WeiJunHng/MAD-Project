package com.example.madproject.ui.discussion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madproject.R;

import java.util.ArrayList;
import java.util.List;

public class DiscussionForum extends Fragment implements OnPostClickListener{
    private RecyclerView recyclerView;
    private PostListAdapter postListAdapter;
    private List<Post> postList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discussion_forum, container, false);

        recyclerView = view.findViewById(R.id.RVPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Example data
        postList = new ArrayList<>();
        postList.add(new Post("Content 1", "https://example.com/image1.jpg"));
        postList.add(new Post( "Content 2", "https://example.com/image2.jpg"));

        postListAdapter = new PostListAdapter(postList, this);
        recyclerView.setAdapter(postListAdapter);

        // Navigate to Chat List when the chat button is clicked
        view.findViewById(R.id.IBForumChatButton).setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_discussionForum_to_chatListFragment);
        });

        return view;
    }

    @Override
    public void onPostClick(Post post) {
        // Navigate to DetailPost fragment
        DetailPost detailPostFragment = DetailPost.newInstance(post.getImageUrl(), post.getContent());
        replaceFragment(detailPostFragment);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FCVMain, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}