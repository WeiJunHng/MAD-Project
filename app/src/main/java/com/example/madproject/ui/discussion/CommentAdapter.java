package com.example.madproject.ui.discussion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;
import com.example.madproject.data.model.DiscussionComment;
import com.example.madproject.data.model.User;
import com.example.madproject.data.repository.UserRepository;
import com.example.madproject.databinding.ItemCommentBinding;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<DiscussionComment> commentsList;

    public CommentAdapter(List<DiscussionComment> commentsList) {
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        DiscussionComment comment = commentsList.get(position);
        UserRepository userRepository = new UserRepository(holder.itemView.getContext());
        holder.TVCommenterUsername.setText(userRepository.getUserById(comment.getCommenterId()).getFormattedUsername());
        holder.TVCommentText.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView TVCommenterUsername, TVCommentText;
        private ItemCommentBinding binding;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCommentBinding.bind(itemView);
            TVCommentText = binding.TVCommentText;
            TVCommenterUsername = binding.TVCommenterUsername;
        }
    }
}

