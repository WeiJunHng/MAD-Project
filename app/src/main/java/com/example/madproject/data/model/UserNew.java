package com.example.madproject.data.model;

import java.util.Objects;

public class UserNew extends Identifiable {

    private String id, firstName, lastName, username, email, password, 
            profilePicURL, gender, birthday, contactInfo;

    private int age, period;
    
    public UserNew(String id, String firstName, String lastName, String username, String email, String password,
                   String profilePicURL, String gender, int age, String birthday, String contactInfo, Integer period) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.birthday = birthday;
        this.contactInfo = contactInfo;

        this.profilePicURL = Objects.requireNonNullElse(profilePicURL, "default_profile_pic_url");
        this.period = Objects.requireNonNullElse(period, 28);

//        this.profilePicURL = "default_profile_pic_url";
//        if (profilePicURL != null) {
//            this.profilePicURL = profilePicURL;
//        }
    }

    public String getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getProfilePicURL() {
        return profilePicURL;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }
    
    public String getBirthday() {
        return birthday;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
