package com.example.madproject.ui.precaution;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        final TextView textView = binding.textDashboard;
        precautionViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}