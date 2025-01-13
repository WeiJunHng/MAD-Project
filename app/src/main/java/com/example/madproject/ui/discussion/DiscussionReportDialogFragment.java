package com.example.madproject.ui.discussion;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madproject.data.model.Discussion;
import com.example.madproject.data.model.Report;
import com.example.madproject.data.model.User;
import com.example.madproject.data.repository.DiscussionRepository;
import com.example.madproject.data.repository.ReportRepository;
import com.example.madproject.data.repository.UserRepository;
import com.example.madproject.databinding.FragmentDiscussionReportDialogBinding;
import com.example.madproject.ui.ViewModelFactory;

import java.util.Date;

public class DiscussionReportDialogFragment extends DialogFragment {

    private FragmentDiscussionReportDialogBinding binding;
    private ImageButton BtnClose;
    private LinearLayout LayoutReasonMain, LayoutReasonDetails;
    private ListView LVMainReason, LVDetailsReason;
    private TextView TVReasonTitle;
    private ProgressDialog progressDialog;
    private UserRepository userRepository;
    private DiscussionRepository discussionRepository;
    private ReportRepository reportRepository;
    private DiscussionViewModel discussionViewModel;
    private User currentUser;
    private Discussion discussion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository(requireContext());
        discussionRepository = new DiscussionRepository(requireContext());
        reportRepository = new ReportRepository(requireContext());

        currentUser = userRepository.getCurrentUser();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDiscussionReportDialogBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        ViewModelFactory factory = new ViewModelFactory(getContext());
        discussionViewModel = new ViewModelProvider(requireActivity(), factory).get(DiscussionViewModel.class);

        BtnClose = binding.BtnClose;
        LayoutReasonMain = binding.LayoutReasonMain;
        LayoutReasonDetails = binding.LayoutReasonDetails;
        TVReasonTitle = binding.TVReasonTitle;
        LVMainReason = binding.MainReasonListView;
        LVDetailsReason = binding.DetailsReasonListView;

        BtnClose.setOnClickListener(v -> dismiss());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Submit Report");
        progressDialog.setMessage("Submitting...");

        discussionViewModel.getDiscussionReportSelectedLiveData().observe(getViewLifecycleOwner(), discussion -> {
            if(discussion != null) {
                this.discussion = discussion;
                discussionViewModel.setDiscussionReportSelectedLiveData(null);
            }
        });
        discussionViewModel.getDiscussionReportSubmittedLiveData().observe(getViewLifecycleOwner(), submitted -> {
            if(submitted != null && submitted) {
                discussionViewModel.setDiscussionReportSubmittedLiveData(null);
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Report submitted successfully!", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        // List items
        String[] mainReasons = {
                "I just don't like it",
                "Bullying or unwanted contact",
                "Suicide, self-injury or eating disorders",
                "Violence, hate or exploitation",
                "Selling or promoting restricted items",
                "Nudity or sexual activity",
                "Scam, fraud or spam",
                "False information"
        };

        String[] reasonTitles = {
                "How is it bullying or unwanted contact?",
                "What kind of self-harm?",
                "How is it violence, hate or exploitation?",
                "What is being sold or promoted?",
                "How is this nudity or sexual activity?",
                "Which best describes the problem?",
                "What kind of false information?"
        };

        String[][] detailsReasonsList = {
                {"Threatening to share or sharing nude images", "Bullying or harassment", "Spam"},
                {"Suicide or self-injury", "Eating disorder"},
                {"Credible threat to safety", "Seems like terrorism or organized crime", "Seems like exploitation",
                        "Hate speech or symbols", "Calling for violence", "Showing violence, death or severe injury", "Animal abuse"},
                {"Drugs", "Weapons", "Animals"},
                {"Threatening to share or sharing nude images", "Seems like prostitution", "Seems like sexual exploitation",
                        "Nudity or sexual activity"},
                {"Fraud or scam", "Spam"},
                {"Health", "Politics", "Social issues", "Digitally created or altered"}
        };

        ReportItemAdapter adapterMainReason = new ReportItemAdapter(mainReasons);
        LVMainReason.setAdapter(adapterMainReason);

        LVMainReason.setOnItemClickListener((parent, view, position, id) -> {
            if(position == 0) {
                submitReport(mainReasons[position]);
                return;
            }

            LayoutReasonMain.setVisibility(View.GONE);
            LayoutReasonDetails.setVisibility(View.VISIBLE);
            TVReasonTitle.setText(reasonTitles[position-1]);

            String[] detailsReasons = detailsReasonsList[position-1];
            ReportItemAdapter adapterDetailsReason = new ReportItemAdapter(detailsReasons);
            LVDetailsReason.setAdapter(adapterDetailsReason);
            LVDetailsReason.setOnItemClickListener((parent1, view1, position1, id1) -> {
                String reason = detailsReasons[position1];
                submitReport(reason);
            });
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Optionally set dialog width/height or style
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void submitReport(String reason) {
        progressDialog.show();
        discussionViewModel.submitReport(discussion.getId(), currentUser.getId(), reason);
    }
}