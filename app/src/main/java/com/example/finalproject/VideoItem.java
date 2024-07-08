package com.example.finalproject;

public class VideoItem {
    private String title;
    private String videoUri;
    private String thumbnailUrl;

    public VideoItem(String title, String videoUri, String thumbnailUrl) {
        this.title = title;
        this.videoUri = videoUri;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
