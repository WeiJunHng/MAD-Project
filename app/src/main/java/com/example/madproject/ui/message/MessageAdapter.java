package com.example.madproject.ui.message;
//List of all chats
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private String currentUser;

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profileImage;

        public MessageViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.TVChatItemUsername);
            profileImage = itemView.findViewById(R.id.IVChatItemProfileImage);
        }
    }

    public MessageAdapter(List<Message> messages, String currentUser) {
        this.messageList = messages;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message currentMessage = messageList.get(position);

        // Set username
        holder.username.setText(currentMessage.getSender());

        // Set profile image
        if (currentMessage.getSender().equals(currentUser)) {
            holder.profileImage.setImageResource(R.mipmap.sample_profile_pic); // Current user's profile picture
        } else {
            holder.profileImage.setImageResource(R.mipmap.sample_profile_pic); // Other user's profile picture
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
