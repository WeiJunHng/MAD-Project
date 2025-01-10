package com.example.madproject.ui.news;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.madproject.databinding.FragmentNewsDetailsBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.example.madproject.ui.news.Models.NewsHeadlines;
import com.squareup.picasso.Picasso;

public class NewsDetailsFragment extends Fragment {

    private NewsHeadlines headline;
    private TextView TVTitle, TVAuthor, TVTime, TVDetail, TVContent;
    private ImageView IVNews;
    
    private FragmentNewsDetailsBinding binding;
    private NewsViewModel newsViewModel;

    private String articleUrl;
    private WebView webView;

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
        
        TVTitle = binding.TVTitle;
        TVAuthor = binding.TVAuthor;
        TVTime = binding.TVTime;
        TVDetail = binding.TVDetail;
        TVContent = binding.TVContent;
        
        IVNews = binding.IVNews;

        webView = binding.webView;


        headline = newsViewModel.getHeadline().getValue();

        if(headline != null) {
            TVTitle.setText(headline.getTitle());
            TVAuthor.setText(headline.getAuthor());
            TVTime.setText(headline.getPublishedAt());
            TVDetail.setText(headline.getDescription());
            TVContent.setText(headline.getContent());
            Picasso.get().load(headline.getUrlToImage()).into(IVNews);
        }

        newsViewModel.setHeadline(null);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(headline.getUrl());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}