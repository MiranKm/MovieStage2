package com.example.miranpc.mymovieinfo;

import android.arch.lifecycle.Observer;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.miranpc.mymovieinfo.DataBase.MoviesDB;
import com.example.miranpc.mymovieinfo.model.MoviesEntity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class FavouriteMovieDetails extends AppCompatActivity {

    private static final String TAG = "FavouriteMovieDetails";

    TextView titleTv, date, overView, rating;
    ImageView imageView;
    Toolbar toolbar;
    ProgressBar loadingPB;
    FloatingActionButton fab;
    RecyclerView reviewsRv;

    MoviesDB moviesDB;
    int movieId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movie_details);


        moviesDB = MoviesDB.getInstance(this);

        titleTv = findViewById(R.id.title);
        fab = findViewById(R.id.fab);
        loadingPB = findViewById(R.id.loading);
        overView = findViewById(R.id.overview);
        date = findViewById(R.id.date);
        rating = findViewById(R.id.rating);
        imageView = findViewById(R.id.image);
        reviewsRv = findViewById(R.id.reviews_rv);

        final android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getIntent().getStringExtra("name"));

        movieId = getIntent().getIntExtra("id", 0);


        MovieViewModel movieViewModel = new MovieViewModel(getApplication());
        movieViewModel.getMoviesEntityLiveData().observe(this, new Observer<List<MoviesEntity>>() {
            @Override
            public void onChanged(@Nullable List<MoviesEntity> moviesEntities) {

                String movieTitle = moviesEntities.get(movieId).getMovieTitle();
                String moviePoster = moviesEntities.get(movieId).getMoviePoster();
                String movieRating = moviesEntities.get(movieId).getMovieRating();
                String movieDate = moviesEntities.get(movieId).getMovieYear();
                String movieOverView = moviesEntities.get(movieId).getMovieOverView();


                settingDatas(movieTitle, moviePoster, movieRating, movieDate, movieOverView);
            }
        });


    }


    private void settingDatas(String movieTitle, String moviePoster, String movieRating, String movieDate, String movieOverView) {
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
            Picasso.with(this).load(moviePoster).into(imageView);

        } else
            Toast.makeText(this, " movie error", Toast.LENGTH_SHORT).show();
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
}
