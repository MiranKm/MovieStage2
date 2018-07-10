package com.example.miranpc.mymovieinfo;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class DetailsActivity extends AppCompatActivity {


    TextView textView, date, overView, rating;
    ImageView imageView;
    Toolbar toolbar;
    ProgressBar loadingPB;

    private static final String TAG = "DetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        textView = findViewById(R.id.title);
        loadingPB = findViewById(R.id.loading);
        overView = findViewById(R.id.overview);
        date = findViewById(R.id.date);
        rating = findViewById(R.id.rating);
        imageView = findViewById(R.id.image);


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


        if (movieTitle != null && moviePoster != null && movieDate != null && movieOverView != null && movieRating != null) {
            textView.setText(movieTitle);
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
            Picasso.with(this).load(getIntent().getStringExtra("image")).into(imageView);

        } else
            Toast.makeText(this, " movie error", Toast.LENGTH_SHORT).show();


    }


}
