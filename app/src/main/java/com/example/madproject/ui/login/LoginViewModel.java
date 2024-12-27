package com.example.madproject.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madproject.data.model.User;
import com.example.madproject.data.repository.UserRepository;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final UserRepository userRepository;

    public LoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository; // Assume UserRepository handles data operations
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public void loginUser(String email, String password) {
        // Perform login logic in a background thread
        new Thread(() -> {
            User user = userRepository.getUserByEmail(email); // Fetch user from database
            if (user != null && password.equals(user.getPassword())) {
                userRepository.updateUserInFirestore(user);
                userLiveData.postValue(user); // Update LiveData
            } else {
                userLiveData.postValue(null); // Null indicates login failure
            }
        }).start();
    }
}
