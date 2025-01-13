package com.example.madproject.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.madproject.data.repository.DiscussionRepository;
import com.example.madproject.data.repository.ReportRepository;
import com.example.madproject.data.repository.UserRepository;
import com.example.madproject.ui.admin.AdminViewModel;
import com.example.madproject.ui.discussion.DiscussionViewModel;
import com.example.madproject.ui.login.LoginViewModel;
import com.example.madproject.ui.news.NewsViewModel;
import com.example.madproject.ui.precaution.PrecautionViewModel;
import com.example.madproject.ui.home.HomeViewModel;
import com.example.madproject.ui.signup.SignUpViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final UserRepository userRepository;
    private final DiscussionRepository discussionRepository;
    private final ReportRepository reportRepository;

    public ViewModelFactory(Context context) {
        userRepository = new UserRepository(context);
        discussionRepository = new DiscussionRepository(context);
        reportRepository = new ReportRepository(context);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(userRepository);
        }
        if (modelClass.isAssignableFrom(SignUpViewModel.class)) {
            return (T) new SignUpViewModel(userRepository);
        }
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(userRepository);
        }
        if (modelClass.isAssignableFrom(NewsViewModel.class)) {
            return (T) new NewsViewModel(userRepository);
        }
        if (modelClass.isAssignableFrom(PrecautionViewModel.class)) {
            return (T) new PrecautionViewModel(userRepository);
        }
        if (modelClass.isAssignableFrom(DiscussionViewModel.class)) {
            return (T) new DiscussionViewModel(userRepository, discussionRepository, reportRepository);
        }
        if (modelClass.isAssignableFrom(AdminViewModel.class)) {
            return (T) new AdminViewModel(userRepository, discussionRepository, reportRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

