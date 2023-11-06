package com.example.socialmediaapp.dto;

import android.net.Uri;

public class ImgDTO {
    private PostImage imageUrl;
    private Uri imageUri;

    public ImgDTO(PostImage imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImgDTO(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public PostImage getImageUrl() {
        return imageUrl;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}
