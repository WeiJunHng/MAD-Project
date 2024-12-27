package com.example.madproject.ui.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madproject.data.repository.UserRepository;
import com.example.madproject.ui.news.Models.NewsHeadlines;

import java.util.List;

public class NewsViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<List<NewsHeadlines>> headlinesList;
    private final MutableLiveData<NewsHeadlines> headline;

    public NewsViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        headlinesList = new MutableLiveData<>();
        headline = new MutableLiveData<>();
    }

    public void setHeadlinesList(List<NewsHeadlines> headlinesList) {
        this.headlinesList.setValue(headlinesList);
    }

    public void setHeadline(NewsHeadlines headline) {
        this.headline.setValue(headline);
    }

    public LiveData<List<NewsHeadlines>> getHeadlinesList() {
        return headlinesList;
    }

    public LiveData<NewsHeadlines> getHeadline() {
        return headline;
    }
}