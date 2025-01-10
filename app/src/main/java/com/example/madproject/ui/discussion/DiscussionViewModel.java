package com.example.madproject.ui.discussion;

import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madproject.ImageHandler;
import com.example.madproject.data.model.Discussion;
import com.example.madproject.data.repository.DiscussionRepository;
import com.example.madproject.data.repository.UserRepository;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DiscussionViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final DiscussionRepository discussionRepository;
    private final MutableLiveData<Discussion> discussionLiveData;
    private final MutableLiveData<Discussion> discussionCreatedLiveData = new MutableLiveData<>();

    public DiscussionViewModel(UserRepository userRepository, DiscussionRepository discussionRepository) {
        this.userRepository = userRepository;
        this.discussionRepository = discussionRepository;
        discussionLiveData = new MutableLiveData<>();
    }

    public void setDiscussionLiveData(Discussion discussion) {
        this.discussionLiveData.setValue(discussion);
    }

    public LiveData<Discussion> getDiscussionLiveData() {
        return discussionLiveData;
    }

    public void setDiscussionCreatedLiveData(Discussion discussion) {
        discussionCreatedLiveData.postValue(discussion);
    }

    public LiveData<Discussion> getDiscussionCreatedLiveData() {
        return discussionCreatedLiveData;
    }

    public void createDiscussion(String authorId, ImageView imageView, String content) {
        new Thread(() -> {
            String encodedImage = ImageHandler.encodeImage(imageView);
            String discussionId = discussionRepository.createDiscussionId();
            Date timestamp = new Date();
            Discussion discussion = new Discussion(discussionId, authorId, timestamp, encodedImage, content);
            discussionRepository.insertDiscussionToFirestore(discussion);
            discussionCreatedLiveData.postValue(discussion);
        }).start();
    }

    public String getAuthorUsername(Discussion discussion) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(() -> userRepository.getUserById(discussion.getAuthorId()).getFormattedUsername());

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}