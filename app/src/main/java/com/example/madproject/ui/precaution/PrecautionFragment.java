package com.example.madproject.ui.precaution;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.madproject.databinding.FragmentPrecautionBinding;
import com.example.madproject.ui.ViewModelFactory;

public class PrecautionFragment extends Fragment {

    private FragmentPrecautionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPrecautionBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(getContext());
        PrecautionViewModel precautionViewModel = new ViewModelProvider(this,factory).get(PrecautionViewModel.class);

        View root = binding.getRoot();

//        final TextView textView = binding.textDashboard;
//        precautionViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        Toast.makeText(getContext(), "Precaution Fragment", Toast.LENGTH_SHORT).show();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

//package com.example.madproject.ui.precaution;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.cardview.widget.CardView;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.Navigation;
//
//import com.example.madproject.PrecautionItem;
//import com.example.madproject.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PrecautionFragment extends Fragment {
//
//    private List<PrecautionItem> precautionItems;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_precaution, container, false);
//
//        // Initialize precaution items
//        initializePrecautionItems();
//
//        // Find CardView elements from the layout
//        CardView precautionCard1 = view.findViewById(R.id.CVPrecaution1);
//        CardView precautionCard2 = view.findViewById(R.id.CVPrecaution2);
//        CardView precautionCard3 = view.findViewById(R.id.CVPrecaution3);
//
//        // Set click listeners for each CardView
//        precautionCard1.setOnClickListener(v -> openPrecautionDetail(v, 0));
//        precautionCard2.setOnClickListener(v -> openPrecautionDetail(v, 1));
//        precautionCard3.setOnClickListener(v -> openPrecautionDetail(v, 2));
//
//        return view;
//    }
//
//    private void initializePrecautionItems() {
//        // Create and populate the list of precaution items
//        precautionItems = new ArrayList<>();
//        precautionItems.add(new PrecautionItem(
//                "Protect Yourself Against HPV",
//                "1. Get vaccinated.\nHPV vaccines can prevent most cases of cervical, vaginal, vulvar, and anal cancers.\n\n"
//                        + "2. Use condoms.\nConsistent condom use can protect women from HPV infection.\n\n"
//                        + "3. Avoid direct contact.\nThe surest way to prevent genital HPV infection is to refrain from any genital contact with another person.\n\n"
//                        + "4. Get tested.\nHPV infections can be diagnosed with a Pap test, which checks for cancer or precancerous changes of the cervix, or a molecular test that looks for HPV DNA.",
//                R.id.IVPrecautionImage1
//        ));
//        precautionItems.add(new PrecautionItem(
//                "Top 5 Safety Tips for Women",
//                "1. Buddy up\nDo your best to not walk alone. The more the merrier in terms of safety!\n\n"
//                        + "2. Update your friends or family.\nLet your friends or family know where you’re going and that you’ll be alone. Giving them updates along the way will give the both of you peace of mind.\n\n"
//                        + "3. Take a self-defense class\nTurn safety into fun and try a class in self-defense! We never want to think the worst could happen. However, in terms of learning to protect oneself in a potentially dangerous situation, being equipped physically and mentally to handle those scenarios is key.\n\n"
//                        + "4. Ping Your Location\nSmartphones are not only a great tool for taking photos, sending messages, and sharing news, but they can also be lifesavers. Most smartphones have a great emergency feature that allows users to ping their location to anyone in their contact list. This feature is easy to setup and will draw attention to the user should they find themselves in an unsafe situation.",
//                R.id.IVPrecautionImage2
//        ));
//        precautionItems.add(new PrecautionItem(
//                "What Are HIV and AIDS?",
//                "HIV (human immunodeficiency virus) is spread by contact with certain bodily fluids of a person with HIV, including blood, semen, pre-seminal fluid, vaginal fluids, and breast milk. HIV is most commonly spread through unprotected sex, sharing injection drug equipment, or getting stuck with a needle that has the blood of someone with HIV on it. HIV can also be spread from a mother to her child during pregnancy, childbirth, or breastfeeding.",
//                R.id.IVPrecautionImage3
//        ));
//    }
//
//    private void openPrecautionDetail(View view, int position) {
//        // Get the selected precaution item based on position
//        PrecautionItem selectedItem = precautionItems.get(position);
//
//        // Create a bundle to pass data to the detail fragment
//        Bundle bundle = new Bundle();
//        bundle.putString("PRECAUTION_TITLE", selectedItem.getTitle());
//        bundle.putString("PRECAUTION_DESCRIPTION", selectedItem.getDescription());
//        bundle.putInt("PRECAUTION_IMAGE_RES_ID", selectedItem.getImageResId());
//
//        // Navigate to the PrecautionDetailFragment with the bundle
//        Navigation.findNavController(view).navigate(R.id.action_precautionFragment_to_precautionDetailFragment, bundle);
//    }
//}
