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

import com.example.madproject.databinding.FragmentGuidelinesBinding;
import com.example.madproject.ui.ViewModelFactory;

import java.util.List;

public class GuidelinesFragment extends DialogFragment {

    private FragmentGuidelinesBinding binding;
    private ImageButton BtnClose;
    private RecyclerView RVRespectUsers, RVSharingContent, RVInteractions, RVIntellectual, RVCompliance, RVViolence, RVNewsworthy, RVSupport;
    private CheckBox CBoxAgree;

    private SignUpViewModel signUpViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGuidelinesBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(requireActivity());
        signUpViewModel = new ViewModelProvider(requireActivity(),factory).get(SignUpViewModel.class);

        View root = binding.getRoot();

        BtnClose = binding.BtnClose;

        RVRespectUsers = binding.RVRespectUsers;
        RVSharingContent = binding.RVSharingContent;
        RVInteractions = binding.RVInteractions;
        RVIntellectual = binding.RVIntellectual;
        RVCompliance = binding.RVCompliance;
        RVViolence = binding.RVViolence;
        RVNewsworthy = binding.RVNewsworthy;
        RVSupport = binding.RVSupport;

        CBoxAgree = binding.CBoxAgree;

        List<RecyclerView> RVList = List.of(RVRespectUsers, RVSharingContent, RVInteractions, RVIntellectual, RVCompliance, RVViolence, RVNewsworthy, RVSupport);
        List<List<String>> bulletPointsList = List.of(
                List.of("Gender Equality: We encourage open dialogue about gender equality issues, but any content that promotes hate speech, discrimination, or violence based on gender or gender identity is strictly prohibited.",
                        "Safety: We prioritize the safety of all users, especially when sharing personal information. Please avoid sharing sensitive data (like personal addresses or phone numbers) unless absolutely necessary and safe to do so."),
                List.of("Health-Related Content: Content related to menstrual health, reproductive health, and other aspects of female health is encouraged. We welcome discussions about period tracking, pregnancy, gender-affirming health, and other health-related topics, but content must be respectful and accurate.",
                        "Respect for Privacy: You can share your experiences, but respect others' privacy. Do not share personal or intimate content about others without their consent. Avoid sharing explicit images unless they are in the context of health advocacy (e.g. breastfeeding, health recovery).",
                        "Gender-Inclusive Language: Please use inclusive and respectful language when discussing gender and gender identity. Soluna is a space for all genders, and we ask that you respect and celebrate diverse identities.",
                        "Appropriate Images and Videos: Content should be appropriate for users of all ages. We do not allow nudity unless it is in the context of breastfeeding, medical conditions (e.g. post-surgery, mastectomy), or protests that align with our values of gender equality and women’s rights."),
                List.of("No Spam or Manipulation: Do not engage in activities that artificially inflate engagement (e.g. buying likes, followers, or fake reviews). Spammy or repetitive comments, as well as unsolicited commercial messages, are not allowed.",
                        "Authenticity: Share authentic content that contributes positively to discussions on women’s rights, gender equality, and health. We encourage users to post personal stories, advice, and experiences that align with our mission.",
                        "No Impersonation: Do not impersonate others, and do not create accounts with the intent to harm, deceive, or mislead the community."),
                List.of("Credit Content Properly: If you are sharing content that belongs to others (e.g. articles, artwork), be sure to credit the original creators and respect copyright laws."),
                List.of("No Illegal Activity: Any content related to illegal activities (e.g. the sale of drugs, firearms, or non-medical products) is strictly prohibited. This includes content promoting the illegal sale of substances or dangerous practices.",
                        "No Harmful Content: We have zero tolerance for content that promotes or encourages self-harm, violence, or any form of abuse. This includes content encouraging eating disorders, cutting, or any harmful actions toward oneself or others."),
                List.of("Zero Tolerance for Abuse: Any content that targets individuals or groups with the intent to shame, degrade, or harm them will be removed immediately. This includes threats, harassment, or any form of personal attacks.",
                        "Violence and Harmful Behavior: We do not allow content that encourages violence against individuals or groups, including threats of physical harm, verbal abuse, or targeted harassment."),
                List.of("Graphic Content: Be mindful when sharing graphic images or videos related to sensitive topics (e.g. health conditions, violence against women). Always provide context, and if the content could be disturbing, include a warning in the caption.",
                        "Sensitive Topics: When discussing issues related to women’s rights, menstruation, or reproductive health, remember that these topics can be deeply personal and sometimes sensitive. Always approach them with respect and care."),
                List.of("Reporting Violations: If you believe someone has violated these guidelines, report it immediately so we can address it swiftly.",
                        "Disputes and Resolutions: If someone posts content that you believe violates your rights or privacy, we encourage you to reach out directly. If that doesn’t work, you can file a copyright or trademark report.")
        );

        for(int i=0;i<RVList.size();i++) {
            RecyclerView recyclerView = RVList.get(i);
            BulletAdapter adapter = new BulletAdapter(bulletPointsList.get(i));

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }

        signUpViewModel.getGuidelinesAgreeLiveData().observe(getViewLifecycleOwner(), guidelinesAgree -> {
            if(guidelinesAgree != null) {
                CBoxAgree.setChecked(guidelinesAgree);
            }
        });

        CBoxAgree.setOnClickListener(v -> signUpViewModel.setGuidelinesAgreeLiveData(CBoxAgree.isChecked()));

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