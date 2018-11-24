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
import android.widget.Toast;

import com.example.miranpc.mymovieinfo.Adapters.MoviesRecyclerAdapter;
import com.example.miranpc.mymovieinfo.AsyncTasks.MovieAsyncTaskLoader;
import com.example.miranpc.mymovieinfo.DataBase.MoviesEntity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MoviesEntity>>, MoviesRecyclerAdapter.onClickListener {

    private static final String TAG = "MainActivity";
    private static String movie = "popular";
    public static final String MOVIE="movie";
    public static final String PAGE="page";


    android.support.v7.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    MoviesRecyclerAdapter moviesRecyclerAdapter;
    TextView internetTV;
    ProgressBar loadingPb;
    static int page = 1;
    GridLayoutManager gridLayoutManager;
    LoaderManager loaderManager;
    List<MoviesEntity> moviesEntityList;
    Bundle bundleStartLoaderData, bundleRestartLoaderData;

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
        bundleStartLoaderData = new Bundle();
        bundleRestartLoaderData = new Bundle();

        bundleStartLoaderData.putString(MOVIE, movie);
        bundleStartLoaderData.putInt(PAGE, page);
        moviesEntityList = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(this, 2);

        recyclerView = findViewById(R.id.recycler_view);
        internetTV = findViewById(R.id.internet_text);
        loadingPb = findViewById(R.id.pb);

        loadingPb.setVisibility(View.VISIBLE);
        setUpRecyclerAdapter();


        if (checkInternetConn()) {
            internetTV.setVisibility(View.GONE);
            loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(0, bundleStartLoaderData, this).forceLoad();
        } else {
            internetTV.setVisibility(View.VISIBLE);
            loadingPb.setVisibility(View.GONE);
        }

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                page++;
                Log.d(TAG, "onLoadMore: " + page);
                bundleRestartLoaderData.putString(MOVIE, movie);
                bundleRestartLoaderData.putInt(PAGE, page);
                Toast.makeText(MainActivity.this, "page = " + page, Toast.LENGTH_SHORT).show();
                loaderManager.restartLoader(0, bundleRestartLoaderData, MainActivity.this).forceLoad();

            }
        });


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
                LoadData(loaderManager, "popular", "Popular", 1);
                break;
            case R.id.top_rated:
                LoadData(loaderManager, "top_rated", "Top Rated", 1);
                break;
            case R.id.now_playing:
                LoadData(loaderManager, "now_playing", "Now Playing", 1);
                break;
            case R.id.upcoming:
                LoadData(loaderManager, "upcoming", "Upcoming", 1);
                break;
            case R.id.fav:
                startActivity(new Intent(this, FavouriteMoviesActivity.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void LoadData(LoaderManager loaderManager, String movieCAt, String title, int pageCount) {
        if (checkInternetConn()) {
            internetTV.setVisibility(View.GONE);
            moviesRecyclerAdapter.clearListOfMovies();
            movie = movieCAt;
            setTitle(title);
            bundleRestartLoaderData.putString(MOVIE, movieCAt);
            bundleRestartLoaderData.putInt(PAGE, page);
            page = pageCount;
            loaderManager.restartLoader(0, bundleRestartLoaderData, this).forceLoad();
        } else internetTV.setVisibility(View.VISIBLE);
    }

    private void setUpRecyclerAdapter() {
        moviesRecyclerAdapter = new MoviesRecyclerAdapter(this, moviesEntityList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(moviesRecyclerAdapter);
    }


    @NonNull
    @Override
    public Loader<List<MoviesEntity>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(TAG, "onCreateLoader: started ");
        loadingPb.setVisibility(View.VISIBLE);
        return new MovieAsyncTaskLoader(this, args.getString("movie"), args.getInt("page"));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<MoviesEntity>> loader, List<MoviesEntity> data) {
        moviesEntityList.addAll(data);
        moviesRecyclerAdapter.notifyItemRangeInserted(moviesRecyclerAdapter.getItemCount(), data.size() - 1);
        loadingPb.setVisibility(View.GONE);
        Log.d(TAG, "onLoadFinished: load finished");

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MoviesEntity>> loader) {

    }

    @Override
    public void onItemClickListener(MoviesEntity movieModel) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(MOVIE, movieModel);
        /*intent.putExtra("name", movieModel.getMovieTitle());
        intent.putExtra("image", movieModel.getMovieBackDrop());
        intent.putExtra("overview", movieModel.getMovieOverView());
        intent.putExtra("rating", movieModel.getMovieRating());
        intent.putExtra("date", movieModel.getMovieYear());
        intent.putExtra("id", movieModel.getMovieId());*/
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
    }
}
