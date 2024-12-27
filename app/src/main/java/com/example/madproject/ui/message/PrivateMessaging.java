package com.example.madproject.ui.message;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.madproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrivateMessaging#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrivateMessaging extends Fragment {

    private RecyclerView rvPrivateMessages;
    private EditText etMessage;
    private ImageButton btnSendMessage;
    private TextView tvChatUsername;
    private ImageView ivExitChatButton;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private static String currentUser;
    private static String chatWithUser;

    public PrivateMessaging() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatItem.
     */
    public static PrivateMessaging newInstance(String param1, String param2) {
        PrivateMessaging fragment = new PrivateMessaging();
        Bundle args = new Bundle();
        args.putString("currentUser", currentUser);  // The logged-in user's name
        args.putString("chatWithUser", chatWithUser);  // The other user's name in the chat
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_messaging, container, false);

        // Get arguments from previous fragment (ChatListFragment)
        if (getArguments() != null) {
            currentUser = getArguments().getString("currentUser");
            chatWithUser = getArguments().getString("chatWithUser");
        }

        // Initialize UI components
        rvPrivateMessages = view.findViewById(R.id.RVPrivateMessages);
        etMessage = view.findViewById(R.id.ETPrivateMessage);
        btnSendMessage = view.findViewById(R.id.IBPrivateMessageSendMessage);
        tvChatUsername = view.findViewById(R.id.TVPrivateMessageUsername);
        ivExitChatButton = view.findViewById(R.id.IVPrivateMessageExitChatButton);

        // Set chat username
        tvChatUsername.setText(chatWithUser);

        // Initialize the message list
        messageList = new ArrayList<>();

        // Add dummy messages for testing
        messageList.add(new Message(currentUser, chatWithUser));
        messageList.add(new Message(chatWithUser, currentUser));

        // Set up the RecyclerView
        setupRecyclerView();

        // Handle Send Button click
        btnSendMessage.setOnClickListener(v -> sendMessage());

        // Handle Exit Chat Button click (Back to Chat List)
        ivExitChatButton.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvPrivateMessages.setLayoutManager(layoutManager);

        messageAdapter = new MessageAdapter(messageList, currentUser);
        rvPrivateMessages.setAdapter(messageAdapter);
    }

    private void sendMessage() {
        String messageText = etMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // Add the new message to the message list
            messageList.add(new Message(currentUser, chatWithUser));

            // Clear the EditText
            etMessage.setText("");

            // Notify the adapter that the data has changed
            messageAdapter.notifyDataSetChanged();

            // Scroll to the bottom to show the latest message
            rvPrivateMessages.scrollToPosition(messageList.size() - 1);
        }
    }
}