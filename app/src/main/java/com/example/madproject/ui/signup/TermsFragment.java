package com.example.madproject.ui.signup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.example.madproject.databinding.FragmentTermsBinding;
import com.example.madproject.ui.ViewModelFactory;

import java.util.List;

public class TermsFragment extends DialogFragment {

    private FragmentTermsBinding binding;
    private ImageButton BtnClose;
    private RecyclerView RVRestrictions, RVProhibitedContent, RVProhibitedActivities, RVLimitation;
    private CheckBox CBoxAgree;

    private SignUpViewModel signUpViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTermsBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(requireActivity());
        signUpViewModel = new ViewModelProvider(requireActivity(),factory).get(SignUpViewModel.class);

        View root = binding.getRoot();

        BtnClose = binding.BtnClose;

        RVRestrictions = binding.RVRestrictions;
        RVProhibitedContent = binding.RVProhibitedContent;
        RVProhibitedActivities = binding.RVProhibitedActivities;
        RVLimitation = binding.RVLimitation;

        CBoxAgree = binding.CBoxAgree;

        List<RecyclerView> RVList = List.of(RVRestrictions, RVProhibitedContent, RVProhibitedActivities, RVLimitation);
        List<List<String>> bulletPointsList = List.of(
                List.of("Create an account using false information.",
                        "Use the Service for any illegal or unauthorized purpose.",
                        "Impersonate another person or entity."),
                List.of("Violates intellectual property rights.",
                        "Promotes hate speech, discrimination, or violence.",
                        "Contains false or misleading information."),
                List.of("Engage in spamming, phishing, or fraudulent activities.",
                        "Attempt to interfere with the Service’s functionality.",
                        "Use automated tools to access or collect data from the Service."),
                List.of("Indirect, incidental, or consequential damages.",
                        "Loss of data or unauthorized access to your account.",
                        "Content posted by users on the platform.")
        );

        for(int i=0;i<RVList.size();i++) {
            RecyclerView recyclerView = RVList.get(i);
            BulletAdapter adapter = new BulletAdapter(bulletPointsList.get(i));

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }

        RVRestrictions.post(() -> {
            ViewGroup.LayoutParams params = RVRestrictions.getLayoutParams();
            params.height = RVRestrictions.computeVerticalScrollRange() + 30; // Dynamically set height
            RVRestrictions.setLayoutParams(params);
        });

        signUpViewModel.getTermsAgreeLiveData().observe(getViewLifecycleOwner(), termsAgree -> {
            if(termsAgree != null) {
                CBoxAgree.setChecked(termsAgree);
            }
        });

        CBoxAgree.setOnClickListener(v -> signUpViewModel.setTermsAgreeLiveData(CBoxAgree.isChecked()));

        BtnClose.setOnClickListener(v -> dismiss());

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}