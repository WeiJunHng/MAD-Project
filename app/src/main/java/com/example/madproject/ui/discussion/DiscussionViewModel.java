package com.example.madproject.ui.discussion;

import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madproject.ImageHandler;
import com.example.madproject.data.model.Discussion;
import com.example.madproject.data.model.Report;
import com.example.madproject.data.model.User;
import com.example.madproject.data.repository.DiscussionRepository;
import com.example.madproject.data.repository.ReportRepository;
import com.example.madproject.data.repository.UserRepository;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DiscussionViewModel extends ViewModel {

    private final UserRepository userRepository;
    private final DiscussionRepository discussionRepository;
    private final ReportRepository reportRepository;
    private final MutableLiveData<Discussion> discussionLiveData;
    private final MutableLiveData<Discussion> discussionCreatedLiveData = new MutableLiveData<>();
    private final MutableLiveData<Discussion> discussionReportSelectedLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> discussionReportSubmittedLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> backFromDetailLiveData = new MutableLiveData<>();

    public DiscussionViewModel(UserRepository userRepository, DiscussionRepository discussionRepository, ReportRepository reportRepository) {
        this.userRepository = userRepository;
        this.discussionRepository = discussionRepository;
        this.reportRepository = reportRepository;
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

    public void setDiscussionReportSelectedLiveData(Discussion discussion) {
        discussionReportSelectedLiveData.postValue(discussion);
    }

    public LiveData<Discussion> getDiscussionReportSelectedLiveData() {
        return discussionReportSelectedLiveData;
    }

    public void setDiscussionReportSubmittedLiveData(Boolean submitted) {
        discussionReportSubmittedLiveData.postValue(submitted);
    }

    public LiveData<Boolean> getDiscussionReportSubmittedLiveData() {
        return discussionReportSubmittedLiveData;
    }

    public void setBackFromDetailLiveData(boolean back) {
        backFromDetailLiveData.postValue(back);
    }

    public LiveData<Boolean> getBackFromDetailLiveData() {
        return backFromDetailLiveData;
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
        Future<String> future = executorService.submit(() -> {
            User author = userRepository.getUserById(discussion.getAuthorId());
            if(author == null) return null;
            return author.getFormattedUsername();
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
//            return null;
            throw new RuntimeException(e);
        }
    }

    public void submitReport(String discussionId, String reporterId, String reason) {
        new Thread(() -> {
            String reportId = reportRepository.createReportId();
            Date timestamp = new Date();
            Report report = new Report(reportId, discussionId, reporterId, timestamp, reason, Report.STATUS.POSTED);
            reportRepository.insertReportInFirestore(report);
            discussionReportSubmittedLiveData.postValue(true);
        }).start();
    }
}