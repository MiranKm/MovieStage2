package com.example.miranpc.mymovieinfo.model;

public class MovieModel {
    private String movieTitle;
    private String movieRating;
    private String movieYear;
    private String movieOverView;
    private String movieBackDrop;
    private String moviePoster;

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }

    public String getMovieOverView() {
        return movieOverView;
    }

    public void setMovieOverView(String movieOverView) {
        this.movieOverView = movieOverView;
    }

    public String getMovieBackDrop() {
        return movieBackDrop;
    }

    public void setMovieBackDrop(String movieBackDrop) {
        this.movieBackDrop = movieBackDrop;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public MovieModel(String movieTitle, String movieRating, String movieYear, String movieOverView, String movieBackDrop, String moviePoster) {

        this.movieTitle = movieTitle;
        this.movieRating = movieRating;
        this.movieYear = movieYear;
        this.movieOverView = movieOverView;
        this.movieBackDrop = movieBackDrop;
        this.moviePoster = moviePoster;
    }
}
