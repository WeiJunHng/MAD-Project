package com.example.madproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class DetailPostAdapter extends RecyclerView.Adapter<DetailPostAdapter.PostViewHolder> {

    private List<Post> postList;
    private OnPostClickListener onPostClickListener;

    public DetailPostAdapter(List<Post> postList, OnPostClickListener onPostClickListener) {
        this.postList = postList;
        this.onPostClickListener = onPostClickListener;
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

        // Set content text
        holder.content.setText(post.getContent());

        // Set image from URI (uploaded by the user)
        if (post.getImageUrl() != null) {
            try {
                Uri imageUri = Uri.parse(post.getImageUrl());
                InputStream inputStream = holder.itemView.getContext().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                holder.imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                // You can set a placeholder or default image if needed
                holder.imageView.setImageResource(R.mipmap.empty_white_image);
            }
        } else {
            // Set a placeholder if no image is provided
            holder.imageView.setImageResource(R.mipmap.empty_white_image);
        }

        // Handle click events
        holder.itemView.setOnClickListener(v -> {
            if (onPostClickListener != null) {
                onPostClickListener.onPostClick(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        ImageView imageView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.TVPosttext);
            imageView = itemView.findViewById(R.id.IVPostImage);
        }
    }
}
