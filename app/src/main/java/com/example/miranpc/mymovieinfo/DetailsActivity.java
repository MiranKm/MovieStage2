package com.example.miranpc.mymovieinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.miranpc.mymovieinfo.Adapters.MovieReviewAdapter;
import com.example.miranpc.mymovieinfo.Adapters.MovieTrailerAdapter;
import com.example.miranpc.mymovieinfo.AsyncTasks.ReviewAsyncTaskLoader;
import com.example.miranpc.mymovieinfo.AsyncTasks.VideoAsyncTaskLoader;
import com.example.miranpc.mymovieinfo.DataBase.AppExecutors;
import com.example.miranpc.mymovieinfo.DataBase.MoviesDB;
import com.example.miranpc.mymovieinfo.DataBase.MoviesEntity;
import com.example.miranpc.mymovieinfo.model.MovieReviewModel;
import com.example.miranpc.mymovieinfo.model.VideoModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class DetailsActivity extends AppCompatActivity implements MovieReviewAdapter.onItemClick, MovieTrailerAdapter.onItemClickListener {

    private static final String TAG = "DetailsActivity";

    TextView titleTv, date, overView, rating, naTrailers, naReviews;
    ImageView imageView;
    Toolbar toolbar;
    ProgressBar loadingPB;
    FloatingActionButton fab;
    RecyclerView reviewsRv;
    RecyclerView trailersRecyclerView;

    MoviesDB moviesDB;

    MoviesEntity moviesEntity;
    MovieReviewAdapter reviewAdapter;
    MovieTrailerAdapter trailersAdapter;

    int movieId;
    private boolean isFavourite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        moviesDB = MoviesDB.getInstance(this);

        titleTv = findViewById(R.id.title);
        trailersRecyclerView = findViewById(R.id.trailers);
        fab = findViewById(R.id.fab);
        loadingPB = findViewById(R.id.loading);
        overView = findViewById(R.id.overview);
        date = findViewById(R.id.date);
        rating = findViewById(R.id.rating);
        imageView = findViewById(R.id.image);
        reviewsRv = findViewById(R.id.reviews_rv);
        naTrailers = findViewById(R.id.na_trailer);
        naReviews = findViewById(R.id.na_reviews);

        final android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getIntent().getStringExtra("name"));


        String movieTitle = getIntent().getStringExtra("name");
        String moviePoster = getIntent().getStringExtra("image");
        String movieRating = getIntent().getStringExtra("rating");
        String movieDate = getIntent().getStringExtra("date");
        String movieOverView = getIntent().getStringExtra("overview");
        movieId = getIntent().getIntExtra("id", 0);

        settingData(movieTitle, moviePoster, movieRating, movieDate, movieOverView);

        moviesEntity = new MoviesEntity(movieId, movieTitle, movieRating, movieDate, movieOverView, null, moviePoster);


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                MoviesEntity movie = moviesDB.movieDao().loadMovieById(movieId);
                if (movie != null) {
                    isFavourite = true;
                    fab.setImageResource(R.drawable.ic_delete_black_24dp);
                } else {
                    isFavourite = false;
                    fab.setImageResource(R.drawable.ic_star_white_24dp);
                }
            }
        });

        LoaderManager videoManager = getSupportLoaderManager();
        videoManager.initLoader(1, null, reviewLoader).forceLoad();


        LoaderManager reviewManager = getSupportLoaderManager();
        reviewManager.initLoader(2, null, videoLoader).forceLoad();


        reviewsRecyclerAdapter();
        trailersRecyclerAdapter();


    }

    private void settingData(String movieTitle, String moviePoster, String movieRating, String movieDate, String movieOverView) {
        if (movieTitle != null && moviePoster != null && movieDate != null && movieOverView != null && movieRating != null) {
            titleTv.setText(movieTitle);
            date.setText(movieDate);
            rating.setText(movieRating + " / 10");
            overView.setText(movieOverView);

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    imageView.setImageBitmap(bitmap);
                    loadingPB.setVisibility(View.GONE);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    loadingPB.setVisibility(View.GONE);

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    loadingPB.setVisibility(View.VISIBLE);
                }
            };
            Picasso.with(this).load(getIntent().getStringExtra("image")).into(target);

        } else
            Toast.makeText(this, " movie error", Toast.LENGTH_SHORT).show();
    }


    public void addToFav(View view) {
        if (isFavourite) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    moviesDB.movieDao().deleteMovie(moviesEntity);
                }
            });
            isFavourite = false;
            Toast.makeText(this, "Movie Deleted From your Favourites", Toast.LENGTH_SHORT).show();
            fab.setImageResource(R.drawable.ic_star_white_24dp);
        } else {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    moviesDB.movieDao().insertMovie(moviesEntity);
                }
            });

            isFavourite = true;
            Toast.makeText(this, "Movie Added to your Favourites", Toast.LENGTH_SHORT).show();
            fab.setImageResource(R.drawable.ic_delete_black_24dp);
        }


    }


    private void reviewsRecyclerAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        reviewAdapter = new MovieReviewAdapter(DetailsActivity.this, this);
        reviewsRv.setHasFixedSize(true);
        reviewsRv.setLayoutManager(manager);
        reviewsRv.setAdapter(reviewAdapter);


    }

    private void trailersRecyclerAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        trailersAdapter = new MovieTrailerAdapter(DetailsActivity.this, this);
        trailersRecyclerView.setHasFixedSize(true);
        trailersRecyclerView.setLayoutManager(manager);
        trailersRecyclerView.setAdapter(trailersAdapter);

    }

    private LoaderManager.LoaderCallbacks<List<VideoModel>> videoLoader = new LoaderManager.LoaderCallbacks<List<VideoModel>>() {
        @NonNull
        @Override
        public Loader onCreateLoader(int id, @Nullable Bundle args) {
            return new VideoAsyncTaskLoader(DetailsActivity.this, movieId);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<VideoModel>> loader, List<VideoModel> data) {
            if (!data.isEmpty()) {
                trailersAdapter.addMovies(data);
                trailersRecyclerView.setAdapter(trailersAdapter);
                naTrailers.setVisibility(View.GONE);
            }

        }


        @Override
        public void onLoaderReset(@NonNull Loader loader) {

        }
    };


    private LoaderManager.LoaderCallbacks<List<MovieReviewModel>> reviewLoader = new LoaderManager.LoaderCallbacks<List<MovieReviewModel>>() {

        @NonNull
        @Override
        public Loader onCreateLoader(int id, @Nullable Bundle args) {
            return new ReviewAsyncTaskLoader(DetailsActivity.this, movieId);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<MovieReviewModel>> loader, List<MovieReviewModel> data) {
            if (!data.isEmpty()) {

                reviewAdapter.addReviews(data);
                reviewsRv.setAdapter(reviewAdapter);
                naReviews.setVisibility(View.GONE);
            }

        }

        @Override
        public void onLoaderReset(@NonNull Loader loader) {

        }
    };

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

    @Override
    public void onItemClickListener(MovieReviewModel movieReview) {

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

    @Override
    public void itemClickListener(VideoModel videoModel) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoModel.getVideoKey())));
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
    }
}
