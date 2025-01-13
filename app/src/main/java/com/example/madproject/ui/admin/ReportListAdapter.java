package com.example.madproject.ui.admin;

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

import com.example.madproject.ImageHandler;
import com.example.madproject.R;
import com.example.madproject.data.model.Discussion;
import com.example.madproject.data.model.Report;
import com.example.madproject.data.model.User;
import com.example.madproject.data.repository.DiscussionRepository;
import com.example.madproject.data.repository.UserRepository;
import com.example.madproject.databinding.ReportedPostItemBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.example.madproject.ui.discussion.DiscussionViewModel;

import java.util.List;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.PostViewHolder> {

    private List<Report> reportList;

    private final FragmentActivity activity;
    private final Context context;
    private DiscussionViewModel discussionViewModel;
    private AdminViewModel adminViewModel;
    private DiscussionRepository discussionRepository;
    private UserRepository userRepository;

    public ReportListAdapter(FragmentActivity activity, Context context, List<Report> reportItems) {
        this.activity = activity;
        this.context = context;
        this.reportList = reportItems;

        discussionRepository = new DiscussionRepository(context);
        userRepository = new UserRepository(context);
        discussionRepository.fetchDiscussions();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reported_post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        ViewModelFactory factory = new ViewModelFactory(context);
        adminViewModel = new ViewModelProvider(activity,factory).get(AdminViewModel.class);
        discussionViewModel = new ViewModelProvider(activity,factory).get(DiscussionViewModel.class);

        holder.IVPostImage.setVisibility(View.GONE);
        holder.IVPostImage.setImageURI(null);

        Report report = reportList.get(position);
        Discussion discussion = discussionRepository.getDiscussionById(report.getDiscussionId());

        holder.TVAuthorName.setText(discussionRepository.getAuthorUsername(discussion));
        holder.TVPostText.setText(discussion.getContent());

        String encodedImage = discussion.getPictureUrl();
        if(encodedImage != null && !encodedImage.equals("defaultPicUrl")) {
            holder.IVPostImage.setVisibility(View.VISIBLE);
            ImageHandler.loadImage(encodedImage,holder.IVPostImage);
        }

        holder.root.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if(adapterPosition != RecyclerView.NO_POSITION) {
                adminViewModel.setReportMutableLiveData(report);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        private View root;
        private TextView TVAuthorName, TVPostText, TVStatus;
        private ImageView IVPostImage;
        private ImageButton BtnRestore, BtnRemove;
        private final ReportedPostItemBinding binding;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ReportedPostItemBinding.bind(itemView);
            root = binding.getRoot();

            IVPostImage = binding.IVPostImage;

            TVAuthorName = binding.TVAuthorName;
            TVPostText = binding.TVContent;
            TVStatus = binding.TVStatus;

            BtnRemove = binding.BtnRemove;
            BtnRestore = binding.BtnRestore;
        }
    }
}

