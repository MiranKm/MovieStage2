package com.example.miranpc.mymovieinfo.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.miranpc.mymovieinfo.model.MoviesEntity;

@Database(entities = {MoviesEntity.class}, version = 1, exportSchema = false)
public abstract class MoviesDB extends RoomDatabase {

    private static final String LOG_TAG = MoviesDB.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movies";
    private static MoviesDB sInstance;

    public static MoviesDB getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MoviesDB.class, MoviesDB.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract MovieDao movieDao();

}
