package com.example.madproject.ui.precaution;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;
import com.example.madproject.databinding.FragmentPrecautionBinding;
import com.example.madproject.ui.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class PrecautionFragment extends Fragment {

    private TextView TVPrecautionTitle;
    private RecyclerView recyclerView;

    private FragmentPrecautionBinding binding;
    private PrecautionViewModel precautionViewModel;
    private List<PrecautionItem> precautionItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {View view = inflater.inflate(R.layout.fragment_precaution, container, false);
        binding = FragmentPrecautionBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(getContext());
        precautionViewModel = new ViewModelProvider(requireActivity(),factory).get(PrecautionViewModel.class);

        View root = binding.getRoot();

        TVPrecautionTitle = binding.TVPrecautionTitle;
        recyclerView = binding.RVPrecautions;

        precautionViewModel.getPrecaution().observe(getViewLifecycleOwner(), precautionItem -> {
            if(precautionItem != null) {
                switchDetailsFragment();
            }
        });

        precautionItems = new ArrayList<>();
        precautionItems.add(new PrecautionItem(
                "Protect Yourself Against HPV",
                "1. Get vaccinated.\nHPV vaccines can prevent most cases of cervical, vaginal, vulvar, and anal cancers.\n\n"
                        + "2. Use condoms.\nConsistent condom use can protect women from HPV infection.\n\n"
                        + "3. Avoid direct contact.\nThe surest way to prevent genital HPV infection is to refrain from any genital contact with another person.\n\n"
                        + "4. Get tested.\nHPV infections can be diagnosed with a Pap test, which checks for cancer or precancerous changes of the cervix, or a molecular test that looks for HPV DNA.",
                R.drawable.precaution_image1
        ));
        precautionItems.add(new PrecautionItem(
                "Top 5 Safety Tips for Women",
                "1. Buddy up\nDo your best to not walk alone. The more the merrier in terms of safety!\n\n"
                        + "2. Update your friends or family.\nLet your friends or family know where you’re going and that you’ll be alone. Giving them updates along the way will give the both of you peace of mind.\n\n"
                        + "3. Take a self-defense class\nTurn safety into fun and try a class in self-defense! We never want to think the worst could happen. However, in terms of learning to protect oneself in a potentially dangerous situation, being equipped physically and mentally to handle those scenarios is key.\n\n"
                        + "4. Ping Your Location\nSmartphones are not only a great tool for taking photos, sending messages, and sharing news, but they can also be lifesavers.",
                R.drawable.precaution_image2
        ));
        precautionItems.add(new PrecautionItem(
                "What Are HIV and AIDS?",
                "HIV (human immunodeficiency virus) is spread by contact with certain bodily fluids of a person with HIV, including blood, semen, pre-seminal fluid, vaginal fluids, and breast milk. HIV is most commonly spread through unprotected sex, sharing injection drug equipment, or getting stuck with a needle that has the blood of someone with HIV on it. HIV can also be spread from a mother to her child during pregnancy, childbirth, or breastfeeding.",
                R.drawable.precaution_image3
        ));

        PrecautionAdapter adapter = new PrecautionAdapter(getActivity(), getContext(), precautionItems);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return root;
    }

//    private void initializePrecautionItems() {
//        precautionItems = new ArrayList<>();
//        precautionItems.add(new PrecautionItem(
//                "Protect Yourself Against HPV",
//                "1. Get vaccinated.\nHPV vaccines can prevent most cases of cervical, vaginal, vulvar, and anal cancers.\n\n"
//                        + "2. Use condoms.\nConsistent condom use can protect women from HPV infection.\n\n"
//                        + "3. Avoid direct contact.\nThe surest way to prevent genital HPV infection is to refrain from any genital contact with another person.\n\n"
//                        + "4. Get tested.\nHPV infections can be diagnosed with a Pap test, which checks for cancer or precancerous changes of the cervix, or a molecular test that looks for HPV DNA.",
//                R.drawable.precaution_image1
//        ));
//        precautionItems.add(new PrecautionItem(
//                "Top 5 Safety Tips for Women",
//                "1. Buddy up\nDo your best to not walk alone. The more the merrier in terms of safety!\n\n"
//                        + "2. Update your friends or family.\nLet your friends or family know where you’re going and that you’ll be alone. Giving them updates along the way will give the both of you peace of mind.\n\n"
//                        + "3. Take a self-defense class\nTurn safety into fun and try a class in self-defense! We never want to think the worst could happen. However, in terms of learning to protect oneself in a potentially dangerous situation, being equipped physically and mentally to handle those scenarios is key.\n\n"
//                        + "4. Ping Your Location\nSmartphones are not only a great tool for taking photos, sending messages, and sharing news, but they can also be lifesavers.",
//                R.drawable.precaution_image2 // Replace with actual drawable resource
//        ));
//        precautionItems.add(new PrecautionItem(
//                "What Are HIV and AIDS?",
//                "HIV (human immunodeficiency virus) is spread by contact with certain bodily fluids of a person with HIV, including blood, semen, pre-seminal fluid, vaginal fluids, and breast milk. HIV is most commonly spread through unprotected sex, sharing injection drug equipment, or getting stuck with a needle that has the blood of someone with HIV on it. HIV can also be spread from a mother to her child during pregnancy, childbirth, or breastfeeding.",
//                R.drawable.
//        ));
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void switchDetailsFragment() {
        Navigation.findNavController(requireActivity(), R.id.FCVMain).navigate(R.id.DestPrecautionDetail);
    }

    private void openPrecautionDetail(View view, int position) {
        PrecautionItem selectedItem = precautionItems.get(position);

        // Pass data to the detail fragment
        Bundle bundle = new Bundle();
        bundle.putString("PRECAUTION_TITLE", selectedItem.getTitle());
        bundle.putString("PRECAUTION_DESCRIPTION", selectedItem.getDescription());
        bundle.putInt("PRECAUTION_IMAGE_RES_ID", selectedItem.getImageResId());

        Navigation.findNavController(view).navigate(R.id.action_precautionFragment_to_precautionDetailFragment, bundle);
    }
}
