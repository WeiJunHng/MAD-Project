package com.example.madproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscussionForum#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscussionForum extends Fragment implements OnPostClickListener{
    private RecyclerView recyclerView;
    private PostListAdapter postListAdapter;
    private List<Post> postList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiscussionForum() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscussionForum.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscussionForum newInstance(String param1, String param2) {
        DiscussionForum fragment = new DiscussionForum();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discussion_forum, container, false);

        recyclerView = view.findViewById(R.id.RVPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Example data
        postList = new ArrayList<>();
        postList.add(new Post("Content 1", "https://example.com/image1.jpg"));
        postList.add(new Post( "Content 2", "https://example.com/image2.jpg"));

        postListAdapter = new PostListAdapter(postList, this);
        recyclerView.setAdapter(postListAdapter);

        return view;
    }

    @Override
    public void onPostClick(Post post) {
        // Navigate to DetailPost fragment
        DetailPost detailPostFragment = DetailPost.newInstance(post.getImageUrl(), post.getContent());
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FLFragmentContainer, detailPostFragment)
                .addToBackStack(null)
                .commit();
    }
}