package com.example.madproject.ui.precaution;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madproject.data.repository.UserRepository;
import com.example.madproject.ui.news.Models.NewsHeadlines;
import com.example.madproject.ui.precaution.PrecautionItem;

import java.util.List;

public class PrecautionViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<List<NewsHeadlines>> headlinesList;
    private final MutableLiveData<PrecautionItem> precaution;

    public PrecautionViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        headlinesList = new MutableLiveData<>();
        precaution = new MutableLiveData<>();
    }

    public void setHeadlinesList(List<NewsHeadlines> headlinesList) {
        this.headlinesList.setValue(headlinesList);
    }

    public void setPrecaution(PrecautionItem precaution) {
        this.precaution.setValue(precaution);
    }

    public LiveData<List<NewsHeadlines>> getHeadlinesList() {
        return headlinesList;
    }

    public LiveData<PrecautionItem> getPrecaution() {
        return precaution;
    }
}