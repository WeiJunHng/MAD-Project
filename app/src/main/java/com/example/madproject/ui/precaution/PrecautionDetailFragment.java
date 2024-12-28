package com.example.madproject.ui.precaution;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.madproject.R;
import com.example.madproject.databinding.FragmentPrecautionBinding;
import com.example.madproject.databinding.FragmentPrecautionDetailBinding;
import com.example.madproject.ui.ViewModelFactory;

public class PrecautionDetailFragment extends Fragment {

    private TextView TVTitle, TVDescription;
    private ImageView IVPrecaution;
    private FragmentPrecautionDetailBinding binding;
    private PrecautionViewModel precautionViewModel;
    private PrecautionItem precautionItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPrecautionDetailBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(getContext());
        precautionViewModel = new ViewModelProvider(requireActivity(),factory).get(PrecautionViewModel.class);

        View root = binding.getRoot();

        TVTitle = binding.tvTitle;
        TVDescription = binding.tvDescription;
        IVPrecaution = binding.ivPrecautionImage;

        precautionItem = precautionViewModel.getPrecaution().getValue();

        if(precautionItem != null) {
            TVTitle.setText(precautionItem.getTitle());
            TVDescription.setText(precautionItem.getDescription());
            IVPrecaution.setImageResource(precautionItem.getImageResId());
        }

        precautionViewModel.setPrecaution(null);

        return root;
    }
}