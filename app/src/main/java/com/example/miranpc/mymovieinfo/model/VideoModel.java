package com.example.miranpc.mymovieinfo.model;

public class VideoModel {

    private String videoName;
    private String videoKey;

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public VideoModel(String videoName, String videoKey) {

        this.videoName = videoName;
        this.videoKey = videoKey;
    }
}
