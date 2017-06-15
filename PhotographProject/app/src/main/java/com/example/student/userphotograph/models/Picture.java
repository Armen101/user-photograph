package com.example.student.userphotograph.models;

import android.net.Uri;

public class Picture {

    private String title;
    private String imageUri;

    public Picture() {
    }

    public Picture(String title, String imageUri) {
        this.title = title;
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
