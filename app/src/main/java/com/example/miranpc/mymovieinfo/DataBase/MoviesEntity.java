package com.example.miranpc.mymovieinfo.DataBase;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class MoviesEntity implements Parcelable {

    @PrimaryKey
    private int movieId;
    private String movieTitle;
    private String movieRating;
    private String movieYear;
    private String movieOverView;
    private String movieBackDrop;
    private String moviePoster;
    public int ID;

    protected MoviesEntity(Parcel in) {
        movieId = in.readInt();
        movieTitle = in.readString();
        movieRating = in.readString();
        movieYear = in.readString();
        movieOverView = in.readString();
        movieBackDrop = in.readString();
        moviePoster = in.readString();
        ID = in.readInt();
    }

    public static final Creator<MoviesEntity> CREATOR = new Creator<MoviesEntity>() {
        @Override
        public MoviesEntity createFromParcel(Parcel in) {
            return new MoviesEntity(in);
        }

        @Override
        public MoviesEntity[] newArray(int size) {
            return new MoviesEntity[size];
        }
    };

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

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


    public MoviesEntity(int movieId, String movieTitle, String movieRating, String movieYear, String movieOverView, String movieBackDrop, String moviePoster) {

        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieRating = movieRating;
        this.movieYear = movieYear;
        this.movieOverView = movieOverView;
        this.movieBackDrop = movieBackDrop;
        this.moviePoster = moviePoster;
    }


    @Override
    public int describeContents() {
        return 0;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(movieTitle);
        dest.writeString(movieRating);
        dest.writeString(movieYear);
        dest.writeString(movieOverView);
        dest.writeString(movieBackDrop);
        dest.writeString(moviePoster);
        dest.writeInt(ID);
    }
}
