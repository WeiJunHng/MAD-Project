package com.example.madproject.ui.admin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madproject.data.model.Discussion;
import com.example.madproject.data.model.Report;
import com.example.madproject.data.repository.DiscussionRepository;
import com.example.madproject.data.repository.ReportRepository;
import com.example.madproject.data.repository.UserRepository;

public class AdminViewModel extends ViewModel {

    private final MutableLiveData<Report> reportMutableLiveData = new MutableLiveData<>();
    private final UserRepository userRepository;
    private final DiscussionRepository discussionRepository;
    private final ReportRepository reportRepository;

    public AdminViewModel(UserRepository userRepository, DiscussionRepository discussionRepository, ReportRepository reportRepository) {
        this.userRepository = userRepository;
        this.discussionRepository = discussionRepository;
        this.reportRepository = reportRepository;
    }

    public MutableLiveData<Report> getReportMutableLiveData() {
        return reportMutableLiveData;
    }

    public void setReportMutableLiveData(Report report) {
        reportMutableLiveData.setValue(report);
    }
}
