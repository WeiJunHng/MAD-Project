package com.example.madproject.ui.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madproject.data.repository.UserRepository;

public class NewsViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<String> mText;

    public NewsViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}