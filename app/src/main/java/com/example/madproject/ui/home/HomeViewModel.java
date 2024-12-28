package com.example.madproject.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madproject.data.repository.UserRepository;

public class HomeViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<String> mText;

    public HomeViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}