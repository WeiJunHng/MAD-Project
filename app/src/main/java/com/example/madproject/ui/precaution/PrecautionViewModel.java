package com.example.madproject.ui.precaution;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madproject.data.repository.UserRepository;

public class PrecautionViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<String> mText;

    public PrecautionViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}