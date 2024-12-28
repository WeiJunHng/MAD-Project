package com.example.madproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;  // List of User objects
    private OnUserClickListener onUserClickListener;

    // Constructor for UserAdapter
    public UserAdapter(List<User> userList, OnUserClickListener onUserClickListener) {
        this.userList = userList;
        this.onUserClickListener = onUserClickListener;
    }

    // ViewHolder for each user item in the list
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profileImage;

        public UserViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.TVUserChatListName);
            profileImage = itemView.findViewById(R.id.IVChatItemProfileImage);
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.username.setText(user.getUsername());
        holder.profileImage.setImageResource(user.getProfileImageResId());

        // Set click listener to start chat
        holder.itemView.setOnClickListener(v -> onUserClickListener.onUserClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Interface for click listener
    public interface OnUserClickListener {
        void onUserClick(User user);
    }
}
