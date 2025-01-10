package com.example.madproject.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;
import com.example.madproject.databinding.FragmentNewsBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.example.madproject.ui.news.Models.NewsHeadlines;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText ETSearch;
    private SearchView NewsSearchView;
    private ProgressBar progressBar;
    private NewsItemAdapter adapter;
    private FragmentNewsBinding binding;
    private NewsViewModel newsViewModel;
    private List<NewsHeadlines> allHeadlinesList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);

        ViewModelFactory factory = new ViewModelFactory(getContext());
        newsViewModel = new ViewModelProvider(requireActivity(),factory).get(NewsViewModel.class);

        View root = binding.getRoot();

        recyclerView = binding.recyclerMain;
        NewsSearchView = binding.NewsSearchView;
        progressBar = binding.progressBar;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));

        loadNewsData();

//        NewsRequestManager manager = new NewsRequestManager(requireContext());
//        manager.getNewsHeadlines(listener, "general", "");

        newsViewModel.getHeadlinesList().observe(getViewLifecycleOwner(), list -> {
            if(list != null && !list.isEmpty()){
                if(allHeadlinesList == null) allHeadlinesList = new ArrayList<>(list);
                showNews(list);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        newsViewModel.getHeadline().observe(getViewLifecycleOwner(), headline -> {
            if(headline != null){
                switchDetailsFragment();
            }
        });

        NewsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNews(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNews(newText);
                return true;
            }
        });

//        ETSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                filterNews(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showNews(List<NewsHeadlines> list) {
        if(adapter == null) {
            adapter = new NewsItemAdapter(requireActivity(), requireContext(), list);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(list);
        }
    }

    private void filterNews(String query) {
        if(allHeadlinesList == null) return;
        List<NewsHeadlines> filteredList = new ArrayList<>();
        if(query.isBlank()){
            filteredList.addAll(allHeadlinesList);
        } else {
            for(NewsHeadlines headline : allHeadlinesList){
                if (headline.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        (headline.getSource().getName() != null && headline.getSource().getName().toLowerCase().contains(query.toLowerCase()))) {
                    filteredList.add(headline);
                }
            }
        }
        adapter.updateList(filteredList);
    }

    private void loadNewsData() {
        NewsRequestManager newsRequestManager = new NewsRequestManager(requireContext());
        newsRequestManager.getNewsHeadlines(newsViewModel, "general", "");

        // Show the ProgressBar while fetching data
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);  // Hide RecyclerView until data is fetched
    }

    private void switchDetailsFragment() {
        Navigation.findNavController(requireActivity(), R.id.FCVMain).navigate(R.id.newsDetailsFragment);
    }
}