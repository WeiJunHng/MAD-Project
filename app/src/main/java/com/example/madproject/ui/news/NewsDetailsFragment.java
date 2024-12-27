package com.example.madproject.ui.news;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.madproject.databinding.FragmentNewsDetailsBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.example.madproject.ui.news.Models.NewsHeadlines;
import com.squareup.picasso.Picasso;

public class NewsDetailsFragment extends Fragment {

    private NewsHeadlines headline;
    private TextView ETTitle, ETAuthor, ETTime, ETDetail, ETContent;
    private ImageView IVNews;
    
    private FragmentNewsDetailsBinding binding;
    private NewsViewModel newsViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsDetailsBinding.inflate(inflater, container, false);
        
        ViewModelFactory factory = new ViewModelFactory(getContext());
        newsViewModel = new ViewModelProvider(requireActivity(),factory).get(NewsViewModel.class);

        View root = binding.getRoot();
        
        ETTitle = binding.ETTitle;
        ETAuthor = binding.ETAuthor;
        ETTime = binding.ETTime;
        ETDetail = binding.ETDetail;
        ETContent = binding.ETContent;
        
        IVNews = binding.IVNews;
        
        headline = newsViewModel.getHeadline().getValue();

        if(headline != null) {
            ETTitle.setText(headline.getTitle());
            ETAuthor.setText(headline.getAuthor());
            ETTime.setText(headline.getPublishedAt());
            ETDetail.setText(headline.getDescription());
            ETContent.setText(headline.getContent());
            Picasso.get().load(headline.getUrlToImage()).into(IVNews);
        }

        newsViewModel.setHeadline(null);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}