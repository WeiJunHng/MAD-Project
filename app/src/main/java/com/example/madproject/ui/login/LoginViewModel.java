package com.example.madproject.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madproject.data.model.User;
import com.example.madproject.data.repository.UserRepository;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> verificationCode = new MutableLiveData<>();
    private final MutableLiveData<String> forgotPwEmail = new MutableLiveData<>();
    private final MutableLiveData<Boolean> statusResetPw = new MutableLiveData<>();
    private final UserRepository userRepository;

    public LoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository; // Assume UserRepository handles data operations
    }

    public void resetUserLiveData() {
        userLiveData.postValue(null);
    }

    public void setVerificationCode(String code) {
        verificationCode.postValue(code);
    }

    public void setForgotPwEmail(String email) {
        forgotPwEmail.postValue(email);
    }

    public LiveData<String> getForgotPwEmail() {
        return forgotPwEmail;
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getVerificationCode() {
        return verificationCode;
    }

    public LiveData<Boolean> getStatusResetPw() {
        return statusResetPw;
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

    public void resetPassword(String email, String newPassword) {
        new Thread(() -> {
            User user = userRepository.getUserByEmail(email);

            if (user == null) {
                statusResetPw.postValue(false);
                return;
            }

            user.setPassword(newPassword);
            userRepository.updateUserInFirestore(user);
            statusResetPw.postValue(true);
        }).start();
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
}
