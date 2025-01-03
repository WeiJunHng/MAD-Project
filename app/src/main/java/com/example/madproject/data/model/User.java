package com.example.madproject.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "user",
        indices = {
                @Index(value = {"email"}, unique = true),
                @Index(value = {"username"}, unique = true),
                @Index(value = {"contactInfo"}, unique = true)
        })
public class User extends Identifiable {
    @PrimaryKey
    @NonNull
    private String id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String username;

    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    @ColumnInfo(defaultValue = "default_profile_pic_url")
    private String profilePicURL;

    @NonNull
    private String gender;

    private int age;

    @NonNull
    private String birthday;

    @Nullable
    private String contactInfo;

    @Nullable
    @ColumnInfo(defaultValue = "28")
    private Integer period;

    public User(){

    }

    public User(@NonNull String id, @NonNull String firstName, @NonNull String lastName, @NonNull String username,
                @NonNull String email, @NonNull String password, @Nullable String profilePicURL, @NonNull String gender,
                int age, @NonNull String birthday, @Nullable String contactInfo, @Nullable Integer period) {
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

        this.profilePicURL = "default_profile_pic_url";
        if (profilePicURL != null) {
            this.profilePicURL = profilePicURL;
        }

        if (!gender.equals("Female")) {
            this.period = null;
        } else if (period == null) {
            this.period = 28;
        } else {
            this.period = period;
        }
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public String getProfilePicURL() {
        return profilePicURL;
    }

    @NonNull
    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    @NonNull
    public String getBirthday() {
        return birthday;
    }

    @Nullable
    public String getContactInfo() {
        return contactInfo;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@NonNull String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public void setProfilePicURL(@NonNull String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public void setGender(@NonNull String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBirthday(@NonNull String birthday) {
        this.birthday = birthday;
    }

    public void setContactInfo(@Nullable String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setPeriod(@NonNull Integer period) {
        this.period = period;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }
}
