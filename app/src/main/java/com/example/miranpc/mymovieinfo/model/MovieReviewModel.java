package com.example.miranpc.mymovieinfo.model;

public class MovieReviewModel extends Throwable {
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String author;
    private String content;
    private String url;

    public MovieReviewModel(String author, String content, String url) {
        this.author = author;
        this.content = content;
        this.url = url;
    }
}
