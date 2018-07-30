package com.example.miranpc.mymovieinfo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;

import com.example.miranpc.mymovieinfo.Adapters.FavouriteMoviesAdapter;
import com.example.miranpc.mymovieinfo.DataBase.AppExecutors;
import com.example.miranpc.mymovieinfo.DataBase.MoviesDB;
import com.example.miranpc.mymovieinfo.DataBase.MoviesEntity;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMoviesActivity extends AppCompatActivity implements FavouriteMoviesAdapter.onItemClickListener {

    MoviesDB moviesDB;
    int master = 0;
    FavouriteMoviesAdapter adapter;
    RecyclerView recyclerView;
    android.support.v7.widget.Toolbar toolbar;
    private static final String TAG = "FavouriteMoviesActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movies);
        moviesDB = MoviesDB.getInstance(this);

        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);


        recyclerView = findViewById(R.id.recycler_view);

        MovieViewModel movieViewModel = new MovieViewModel(getApplication());
        movieViewModel.getMoviesEntityLiveData().observe(this, new Observer<List<MoviesEntity>>() {
            @Override
            public void onChanged(@Nullable List<MoviesEntity> moviesEntities) {
                adapter.clearList();
                adapter.setMovies(moviesEntities);

            }
        });

        setUpAdapter();


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int pos = viewHolder.getAdapterPosition();
                        final List<MoviesEntity> list = adapter.getMovie();
                        moviesDB.movieDao().deleteMovie(list.get(pos));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.clearList();
                                adapter.setMovies(list);
                            }
                        });
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clearList();
        adapter.getMovie();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new FavouriteMoviesAdapter(this, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClickListener(int position) {
        Intent intent = new Intent(this, FavouriteMovieDetails.class);
        intent.putExtra("id", position);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);

    }
}
