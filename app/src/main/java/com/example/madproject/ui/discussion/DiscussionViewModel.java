package com.example.madproject.ui.discussion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madproject.data.repository.UserRepository;

public class DiscussionViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<Post> postLiveData;

    public DiscussionViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        postLiveData = new MutableLiveData<>();
    }

    public void setPostLiveData(Post post) {
        this.postLiveData.setValue(post);
    }

    public LiveData<Post> getPostLiveData() {
        return postLiveData;
    }
}