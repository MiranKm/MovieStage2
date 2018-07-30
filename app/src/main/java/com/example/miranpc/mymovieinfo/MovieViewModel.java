package com.example.miranpc.mymovieinfo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.miranpc.mymovieinfo.DataBase.MoviesDB;
import com.example.miranpc.mymovieinfo.DataBase.MoviesEntity;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {


    private LiveData<List<MoviesEntity>> moviesEntityLiveData;

    public MovieViewModel(@NonNull Application application) {
        super(application);

        MoviesDB moviesDB= MoviesDB.getInstance(getApplication().getApplicationContext());
        moviesEntityLiveData= moviesDB.movieDao().loadAllMovies();




    }

    public LiveData<List<MoviesEntity>> getMoviesEntityLiveData() {
        return moviesEntityLiveData;
    }

}
