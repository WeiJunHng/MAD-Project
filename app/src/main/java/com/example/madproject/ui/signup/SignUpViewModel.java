package com.example.madproject.ui.signup;

import android.content.Context;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.madproject.data.model.User;
import com.example.madproject.data.repository.UserRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class SignUpViewModel extends ViewModel {

    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMsgLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> guidelinesAgreeLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> termsAgreeLiveData = new MutableLiveData<>();
    private final UserRepository userRepository;

    public SignUpViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }
    
    public LiveData<String> getErrorMsgLiveData() {
        return errorMsgLiveData;
    }

    public LiveData<Boolean> getGuidelinesAgreeLiveData() {
        return guidelinesAgreeLiveData;
    }

    public LiveData<Boolean> getTermsAgreeLiveData() {
        return termsAgreeLiveData;
    }

    public void resetUserLiveData() {
        userLiveData.setValue(null);
    }

    public void resetErrorMsgLiveData() {
        errorMsgLiveData.setValue(null);
    }

    public void setGuidelinesAgreeLiveData(Boolean guidelinesAgree) {
        guidelinesAgreeLiveData.setValue(guidelinesAgree);
    }

    public void setTermsAgreeLiveData(Boolean termsAgree) {
        termsAgreeLiveData.setValue(termsAgree);
    }

    public void signUpUser_1 (String firstName, String lastName, String username, String email, String password,
                             String confirmPassword, boolean guidelinesChecked, boolean termsChecked) {
        new Thread(() -> {
            boolean emailExist = userRepository.emailExist(email);
            boolean usernameExist = userRepository.usernameExist(username);
            
            if(firstName.isBlank()||lastName.isBlank()||username.isBlank()||email.isBlank()||password.isBlank()||confirmPassword.isBlank()) {
                errorMsgLiveData.postValue("All the fields must be filled in!");
                return;
            }

            if(!isEmailValid(email)){
                errorMsgLiveData.postValue("Invalid email address");
                return;
            }

            if(!password.equals(confirmPassword)){
                errorMsgLiveData.postValue("Check your password again");
                return;
            }

            if(!guidelinesChecked){
                errorMsgLiveData.postValue("Read and agree the Community Guidelines to continue");
                return;
            }

            if(!termsChecked){
                errorMsgLiveData.postValue("Read and agree the Terms of Service to continue");
                return;
            }

            if(emailExist){
                errorMsgLiveData.postValue("This email address has been signed up");
                return;
            }

            if(usernameExist){
                errorMsgLiveData.postValue("Username has been used.\nPlease try other");
                return;
            }
            
            String userId = userRepository.createUserId();
            User user = new User(userId, firstName, lastName, username, email, password);
            userLiveData.postValue(user);
        }).start();
    }

    public void signUpUser_2(Context context, User user, String gender, String birthday, String contact, String encodedImage) {
        new Thread(() -> {

            if(gender.isBlank()||birthday.isBlank()||contact.isBlank()) {
                errorMsgLiveData.postValue("All the fields must be filled in!");
                return;
            }

            String userId = userRepository.createUserId();
            String firstName = user.getFirstName();
            String lastName = user.getLastName();
            String username = user.getUsername();
            String email = user.getEmail();
            String password = user.getPassword();

            // Calculate age based on birthday
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate birthLocalDate = LocalDate.parse(birthday, formatter);
            LocalDate currentDate = LocalDate.now();

            Date birthDate = Date.from(birthLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            int age = Period.between(birthLocalDate, currentDate).getYears();

//            String imageUrl = encodedImage == null ? null : ImageHandlerNew.uploadImageToCloudinary(context, encodedImage);

            User userNew = new User(userId, firstName, lastName, username, email, password, encodedImage, gender, age, birthDate, contact, null);

            userRepository.insertUserToFirestore(userNew);
            userLiveData.postValue(userNew);
        }).start();
    }

    private boolean isEmailValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
