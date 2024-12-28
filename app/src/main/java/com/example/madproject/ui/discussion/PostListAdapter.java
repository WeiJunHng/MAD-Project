package com.example.madproject.ui.discussion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import android.net.Uri;

import com.example.madproject.R;
import com.example.madproject.databinding.PostListItemBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.example.madproject.ui.precaution.PrecautionViewModel;

import soup.neumorphism.NeumorphCardView;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {

    private List<Post> postList;

    private final FragmentActivity activity;
    private final Context context;
    private DiscussionViewModel discussionViewModel;

    public PostListAdapter(FragmentActivity activity, Context context, List<Post> postItems) {
        this.activity = activity;
        this.context = context;
        this.postList = postItems;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        ViewModelFactory factory = new ViewModelFactory(context);
        discussionViewModel = new ViewModelProvider(activity,factory).get(DiscussionViewModel.class);

        Post post = postList.get(position);

        holder.TVPostText.setText(post.getContent());
        if (post.getImageUrl() != null) {
            // If it's a URI
            try {
                Uri imageUri = Uri.parse(post.getImageUrl());
                holder.IVPostImage.setImageURI(imageUri);
            } catch (Exception e) {
                // Log the error and set a placeholder image
                holder.IVPostImage.setImageResource(R.drawable.post_image);
            }
        } else {
            // If no image is provided, set a placeholder
            holder.IVPostImage.setImageResource(R.drawable.post_image);
        }

        holder.root.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if(adapterPosition != RecyclerView.NO_POSITION) {
                discussionViewModel.setPostLiveData(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        private NeumorphCardView root;
        private TextView TVPostText;
        private ImageView IVPostImage;
        private ImageButton IBComment, IBLike, IBReport;
        private final PostListItemBinding binding;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = PostListItemBinding.bind(itemView);
            root = binding.getRoot();

            TVPostText = binding.TVPostText;
            IVPostImage = binding.IVPostImage;
            IBComment = binding.IBComment;
            IBLike = binding.IBLike;
            IBReport = binding.IBReport;
        }
    }
}

