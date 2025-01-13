package com.example.madproject.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.madproject.ImageHandler;
import com.example.madproject.data.model.Discussion;
import com.example.madproject.data.model.Report;
import com.example.madproject.data.repository.DiscussionRepository;
import com.example.madproject.data.repository.ReportRepository;
import com.example.madproject.databinding.FragmentManageContentDetailsBinding;
import com.example.madproject.ui.ViewModelFactory;

import java.text.SimpleDateFormat;

import soup.neumorphism.NeumorphCardView;

public class ManageContentDetailsFragment extends Fragment {

    private FragmentManageContentDetailsBinding binding;
    private NeumorphCardView CVImage;
    private ImageView IVPostImage;
    private TextView TVUsername, TVPostText, TVStatus, TVDate, TVLikeCount, TVCommentCount, TVReason;
    private AdminViewModel adminViewModel;
    private DiscussionRepository discussionRepository;
    private ReportRepository reportRepository;
    private Report report;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        discussionRepository = new DiscussionRepository(requireContext());
        reportRepository = new ReportRepository(requireContext());

        ViewModelFactory factory = new ViewModelFactory(requireContext());
        adminViewModel = new ViewModelProvider(requireActivity(), factory).get(AdminViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManageContentDetailsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        CVImage = binding.CVImage;
        IVPostImage = binding.IVPostImage;
        TVUsername = binding.TVUsername;
        TVPostText = binding.TVPostText;
        TVStatus = binding.TVStatus;
        TVDate = binding.TVDate;
        TVLikeCount = binding.TVLikeCount;
        TVCommentCount = binding.TVCommentCount;
        TVReason = binding.TVReason;

        report = adminViewModel.getReportMutableLiveData().getValue();
        adminViewModel.setReportMutableLiveData(null);

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        Discussion discussion = discussionRepository.getDiscussionById(report.getDiscussionId());
        TVUsername.setText(discussionRepository.getAuthorUsername(discussion));
        TVPostText.setText(discussion.getContent());
        TVDate.setText(formatter.format(discussion.getTimestamp()));
        TVLikeCount.setText(String.valueOf(discussionRepository.getLikeCount(discussion)));
        TVCommentCount.setText(String.valueOf(discussionRepository.getCommentCount(discussion)));
        TVReason.setText(report.getContent());
//        TVStatus.setText(discussion.getStatus());

        String encodedImage = discussion.getPictureUrl();
        CVImage.setVisibility(View.GONE);
        if(encodedImage != null && !encodedImage.equals("defaultPicUrl")) {
            CVImage.setVisibility(View.VISIBLE);
            ImageHandler.loadImage(encodedImage, IVPostImage);
        }

        return root;
    }
}