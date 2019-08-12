package com.example.miranpc.mymovieinfo.DataBase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.miranpc.mymovieinfo.model.MoviesEntity;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM moviesentity")
    LiveData<List<MoviesEntity>> loadAllMovies();

    @Query("SELECT * FROM Moviesentity WHERE movieId = :movieId")
    MoviesEntity loadMovieById(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(MoviesEntity movie);

    @Delete()
    void deleteMovie(MoviesEntity moviesEntity);

}


