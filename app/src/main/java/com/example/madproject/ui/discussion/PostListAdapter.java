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

import com.example.madproject.ImageHandler;
import com.example.madproject.R;
import com.example.madproject.data.model.Discussion;
import com.example.madproject.databinding.PostListItemBinding;
import com.example.madproject.ui.ViewModelFactory;

import soup.neumorphism.NeumorphCardView;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {

    private List<Discussion> postList;

    private final FragmentActivity activity;
    private final Context context;
    private DiscussionViewModel discussionViewModel;

    public PostListAdapter(FragmentActivity activity, Context context, List<Discussion> postItems) {
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

        holder.CVImage.setVisibility(View.GONE);
        holder.IVPostImage.setImageURI(null);

        Discussion discussion = postList.get(position);

        holder.TVAuthorName.setText(discussionViewModel.getAuthorUsername(discussion));
        holder.TVPostText.setText(discussion.getContent());

        String encodedImage = discussion.getPictureUrl();
        if(encodedImage != null && !encodedImage.equals("defaultPicUrl")) {
            holder.CVImage.setVisibility(View.VISIBLE);
            ImageHandler.loadImage(discussion.getPictureUrl(),holder.IVPostImage);
        }

//        if (discussion.getPictureUrl() != null) {
//            // If it's a URI
//            try {
//                Uri imageUri = Uri.parse(discussion.getPictureUrl());
//                holder.IVPostImage.setImageURI(imageUri);
//            } catch (Exception e) {
//                // Log the error and set a placeholder image
//                holder.IVPostImage.setImageResource(R.drawable.post_image);
//            }
//        } else {
//            // If no image is provided, set a placeholder
//            holder.IVPostImage.setImageResource(R.drawable.post_image);
//        }

        holder.root.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if(adapterPosition != RecyclerView.NO_POSITION) {
                discussionViewModel.setDiscussionLiveData(discussion);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        private NeumorphCardView root;
        private TextView TVAuthorName, TVPostText;
        private NeumorphCardView CVImage;
        private ImageView IVPostImage;
        private ImageButton IBComment, IBLike, IBReport;
        private final PostListItemBinding binding;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = PostListItemBinding.bind(itemView);
            root = binding.getRoot();

            TVAuthorName = binding.TVAuthorName;
            TVPostText = binding.TVPostText;
            CVImage = binding.CVImage;
            IVPostImage = binding.IVPostImage;
            IBComment = binding.IBComment;
            IBLike = binding.IBLike;
            IBReport = binding.IBReport;
        }
    }
}

