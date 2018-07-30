package com.example.miranpc.mymovieinfo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.miranpc.mymovieinfo.Adapters.MoviesRecyclerAdapter;
import com.example.miranpc.mymovieinfo.AsyncTasks.MovieAsyncTaskLoader;
import com.example.miranpc.mymovieinfo.DataBase.MoviesEntity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MoviesEntity>>, MoviesRecyclerAdapter.onClickListener {

    private static final String TAG = "MainActivity";
    private static String movie = "popular";


    android.support.v7.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    MoviesRecyclerAdapter moviesRecyclerAdapter;
    TextView internetTV;
    ProgressBar loadingPb;
    MoviesEntity moviesEntity;


    public boolean checkInternetConn() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        recyclerView = findViewById(R.id.recycler_view);
        internetTV = findViewById(R.id.internet_text);
        loadingPb = findViewById(R.id.pb);

        loadingPb.setVisibility(View.VISIBLE);
        setUpRecyclerAdapter();


        if (checkInternetConn()) {
            internetTV.setVisibility(View.GONE);
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(0, null, this).forceLoad();
        } else {
            internetTV.setVisibility(View.VISIBLE);
            loadingPb.setVisibility(View.GONE);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LoaderManager loaderManager = getSupportLoaderManager();

        switch (item.getItemId()) {
            case R.id.popular:
                if (checkInternetConn()) {
                    internetTV.setVisibility(View.GONE);
                    moviesRecyclerAdapter.clearListOfMovies();
                    movie = "popular";
                    setTitle("Popular");
                    loaderManager.restartLoader(0, null, this).forceLoad();
                } else internetTV.setVisibility(View.VISIBLE);
                break;

            case R.id.top_rated:
                if (checkInternetConn()) {
                    internetTV.setVisibility(View.GONE);
                    moviesRecyclerAdapter.clearListOfMovies();
                    movie = "top_rated";
                    setTitle("Top Rated");
                    loaderManager.restartLoader(0, null, this).forceLoad();

                } else internetTV.setVisibility(View.VISIBLE);

                break;
            case R.id.now_playing:
                if (checkInternetConn()) {
                    internetTV.setVisibility(View.GONE);
                    moviesRecyclerAdapter.clearListOfMovies();
                    movie = "now_playing";
                    setTitle("Now Playing");
                    loaderManager.restartLoader(0, null, this).forceLoad();

                } else internetTV.setVisibility(View.VISIBLE);

                break;
            case R.id.upcoming:
                if (checkInternetConn()) {
                    internetTV.setVisibility(View.GONE);
                    moviesRecyclerAdapter.clearListOfMovies();
                    movie = "upcoming";
                    setTitle("Upcoming");
                    loaderManager.restartLoader(0, null, this).forceLoad();
                } else internetTV.setVisibility(View.VISIBLE);

                break;
            case R.id.fav:
                startActivity(new Intent(this, FavouriteMoviesActivity.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);

                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setUpRecyclerAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        moviesRecyclerAdapter = new MoviesRecyclerAdapter(this, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(moviesRecyclerAdapter);


    }


    @NonNull
    @Override
    public Loader<List<MoviesEntity>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(TAG, "onCreateLoader: started ");
        loadingPb.setVisibility(View.VISIBLE);
        return new MovieAsyncTaskLoader(this, movie);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<MoviesEntity>> loader, List<MoviesEntity> data) {
        moviesRecyclerAdapter.addMovies(data);
        recyclerView.setAdapter(moviesRecyclerAdapter);
        loadingPb.setVisibility(View.GONE);
        Log.d(TAG, "onLoadFinished: load finished");

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MoviesEntity>> loader) {

    }

    @Override
    public void onItemClickListener(MoviesEntity movieModel) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("name", movieModel.getMovieTitle());
        intent.putExtra("image", movieModel.getMovieBackDrop());
        intent.putExtra("overview", movieModel.getMovieOverView());
        intent.putExtra("rating", movieModel.getMovieRating());
        intent.putExtra("date", movieModel.getMovieYear());
        intent.putExtra("id", movieModel.getMovieId());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
    }
}
