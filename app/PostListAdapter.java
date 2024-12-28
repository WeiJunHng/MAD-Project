package com.example.sharingplatform;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {

    private List<Post> postList;
    private OnPostClickListener listener;

    public PostListAdapter(List<Post> postList, OnPostClickListener listener) {
        this.postList = postList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_detail_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        holder.titleTextView.setText(post.getContent());
        if (post.getImageUrl() != null) {
            // If it's a URI
            try {
                Uri imageUri = Uri.parse(post.getImageUrl());
                holder.postImageView.setImageURI(imageUri);
            } catch (Exception e) {
                // Log the error and set a placeholder image
                holder.postImageView.setImageResource(R.drawable.post_image);
            }
        } else {
            // If no image is provided, set a placeholder
            holder.postImageView.setImageResource(R.drawable.post_image);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView postImageView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.TVPosttext);
            postImageView = itemView.findViewById(R.id.IVPostImage);
        }
    }
}

