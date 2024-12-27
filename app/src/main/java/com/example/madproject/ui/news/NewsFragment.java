package com.example.madproject.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.madproject.databinding.FragmentNewsBinding;
import com.example.madproject.ui.ViewModelFactory;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(getContext());
        NewsViewModel newsViewModel = new ViewModelProvider(this,factory).get(NewsViewModel.class);

        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        newsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}