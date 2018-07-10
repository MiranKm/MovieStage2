package com.example.miranpc.mymovieinfo;

import android.app.Application;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.miranpc.mymovieinfo.model.MovieModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MovieModel>>, MoviesRecyclerAdapter.onClickListener {

    private static final String TAG = "MainActivity";


    private static String movie = "popular";


    private RecyclerView recyclerView;
    MoviesRecyclerAdapter moviesRecyclerAdapter;
    TextView internetTV;


    public boolean fun() {

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

        recyclerView = findViewById(R.id.recycler_view);
        internetTV = findViewById(R.id.internet_text);

        setUpRecyclerAdapter();

        recyclerView.setAdapter(moviesRecyclerAdapter);


        if (fun()) {
            internetTV.setVisibility(View.GONE);
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(0, null, this).forceLoad();
        } else
            internetTV.setVisibility(View.VISIBLE);


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

                if (fun()) {
                    internetTV.setVisibility(View.GONE);
                    moviesRecyclerAdapter.clearListOfMovies();
                    movie = "popular";
                    loaderManager.restartLoader(0, null, this).forceLoad();
                } else internetTV.setVisibility(View.VISIBLE);

                break;
            case R.id.top_rated:
                if (fun()) {
                    internetTV.setVisibility(View.GONE);
                    moviesRecyclerAdapter.clearListOfMovies();
                    movie = "top_rated";
                    loaderManager.restartLoader(0, null, this).forceLoad();

                } else internetTV.setVisibility(View.VISIBLE);

                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setUpRecyclerAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        moviesRecyclerAdapter = new MoviesRecyclerAdapter(this, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

    }


    @NonNull
    @Override
    public Loader<List<MovieModel>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(TAG, "onCreateLoader: started ");
        return new MovieAsyncTaskLoader(this, movie);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<MovieModel>> loader, List<MovieModel> data) {
        moviesRecyclerAdapter.addMovies(data);
        recyclerView.setAdapter(moviesRecyclerAdapter);
        Log.d(TAG, "onLoadFinished: load finished");
        Toast.makeText(this, "load finished", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MovieModel>> loader) {

    }

    @Override
    public void onItemClickListener(MovieModel movieModel) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("name", movieModel.getMovieTitle());
        intent.putExtra("image", movieModel.getMovieBackDrop());
        intent.putExtra("overview", movieModel.getMovieOverView());
        intent.putExtra("rating", movieModel.getMovieRating());
        intent.putExtra("date", movieModel.getMovieYear());
        startActivity(intent);
    }
}
