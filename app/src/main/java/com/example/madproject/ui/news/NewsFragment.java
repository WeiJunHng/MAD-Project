package com.example.madproject.ui.news;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;
import com.example.madproject.databinding.FragmentNewsBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.example.madproject.ui.news.Models.NewsApiResponse;
import com.example.madproject.ui.news.Models.NewsHeadlines;

import java.util.List;

public class NewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsItemAdapter adapter;
    private ProgressDialog dialog;
    private FragmentNewsBinding binding;
    private NewsViewModel newsViewModel;

    private final OnFetchDataListener<NewsApiResponse> newsAPIResponseListener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            showNews(list);
            dialog.dismiss();
        }

        @Override
        public void onError(String message) {

        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(getContext());
        newsViewModel = new ViewModelProvider(requireActivity(),factory).get(NewsViewModel.class);

        View root = binding.getRoot();

        recyclerView = binding.recyclerMain;

        newsViewModel.getHeadlinesList().observe(getViewLifecycleOwner(), list -> {
            if(list != null && !list.isEmpty()){
                showNews(list);
                dialog.dismiss();
            }
        });
        newsViewModel.getHeadline().observe(getViewLifecycleOwner(), headline -> {
            if(headline != null){
                switchDetailsFragment();
            }
        });

        dialog = new ProgressDialog(requireContext());
        dialog.setTitle("Fetching news articles...");
        dialog.show();

        NewsRequestManager newsRequestManager = new NewsRequestManager(requireContext());
        newsRequestManager.getNewsHeadlines(newsViewModel, "general", "");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showNews(List<NewsHeadlines> list) {
        recyclerView = binding.recyclerMain;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        adapter =  new NewsItemAdapter(requireActivity(), requireContext(), list);
        recyclerView.setAdapter(adapter);
    }

    private void switchDetailsFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.FCVMain, new NewsDetailsFragment())
                .addToBackStack(null)
                .commit();
    }
}