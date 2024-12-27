package com.example.madproject.ui.message;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madproject.R;
import com.example.madproject.User;
import com.example.madproject.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatList extends Fragment {

    private RecyclerView rvChatList;
    private List<User> userList;  // List of User objects
    private String currentUser;

    public ChatList() {
        // Required empty public constructor
    }

    public static ChatList newInstance(String currentUser) {
        ChatList fragment = new ChatList();
        Bundle args = new Bundle();
        args.putString("currentUser", currentUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        if (getArguments() != null) {
            currentUser = getArguments().getString("currentUser");
        }

        rvChatList = view.findViewById(R.id.RVChatList);

        // Create dummy user data
        userList = new ArrayList<>();
        userList.add(new User("Test User 1", R.mipmap.sample_profile_pic));
        userList.add(new User("Test User 2", R.mipmap.sample_profile_pic));
        userList.add(new User("Test User 3", R.mipmap.sample_profile_pic));

        setupRecyclerView();

        view.findViewById(R.id.IBBackToDiscussion).setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvChatList.setLayoutManager(layoutManager);

        UserAdapter userAdapter = new UserAdapter(userList, this::startChat);
        rvChatList.setAdapter(userAdapter);
    }

    private void startChat(User chatWithUser) {
        PrivateMessaging fragment = PrivateMessaging.newInstance(currentUser, chatWithUser.getUsername());
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.CLmain, fragment)
                .addToBackStack(null)
                .commit();
    }
}
