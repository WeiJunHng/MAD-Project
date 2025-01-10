package com.example.madproject;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ImageViewModel extends ViewModel {
    private final MutableLiveData<Uri> imageUriLiveData = new MutableLiveData<>();

    public void setImageUri(Uri uri) {
        imageUriLiveData.setValue(uri);
    }

    public MutableLiveData<Uri> getImageUriLiveData() {
        return imageUriLiveData;
    }

}
