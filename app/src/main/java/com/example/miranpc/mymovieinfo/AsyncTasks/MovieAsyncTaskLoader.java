package com.example.miranpc.mymovieinfo.AsyncTasks;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.miranpc.mymovieinfo.ApiUtil;
import com.example.miranpc.mymovieinfo.DataBase.MoviesEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovieAsyncTaskLoader extends AsyncTaskLoader<List<MoviesEntity>> {

    private static final String TAG = "MovieAsyncTaskLoader";

    String movieCat;

    public MovieAsyncTaskLoader(Context context, String movieCat) {
        super(context);
        this.movieCat = movieCat;

    }


    @Override
    public List<MoviesEntity> loadInBackground() {
        List<MoviesEntity> moviesList = null;
        try {
            URL urlR = urlMaker(movieCat);

            String json = fetchData(urlR);


            moviesList = parsMovieJson(json);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return moviesList;
    }

    private URL urlMaker(String... args) throws MalformedURLException {
        Uri uri = Uri.parse(ApiUtil.MAIN_LINK).buildUpon()
                .appendPath(args[0])
                .appendQueryParameter("api_key", ApiUtil.API_KEY)
                .appendQueryParameter("language", ApiUtil.LANG)
                .build();

        URL url = new URL(uri.toString());

        return url;
    }
    //351286


    private String fetchData(URL url) throws IOException {
        String jsonString = null;
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A");

        if (scanner.hasNext()) {
            jsonString = scanner.next();

        }

        return jsonString;
    }


    private List<MoviesEntity> parsMovieJson(String json) throws JSONException {


        List<MoviesEntity> listOfMovies = new ArrayList<>();

        JSONObject root = new JSONObject(json);

        int pageNumber = root.getInt("page");

        JSONArray detailsJsonArr = root.getJSONArray("results");

        for (int i = 0; i < detailsJsonArr.length(); i++) {

            Log.d(TAG, "parsMovieJson: i s loop " + i);

            JSONObject detailsJsonObj = detailsJsonArr.getJSONObject(i);

            String movieTitle = detailsJsonObj.getString("title");
            int movieId = detailsJsonObj.getInt("id");
            String movieBackDrop = "http://image.tmdb.org/t/p/w500/" + detailsJsonObj.getString("poster_path");
            Log.d(TAG, "parsMovieJson: poster path " + movieBackDrop);
            String movieOverView = detailsJsonObj.getString("overview");
            String movieBackdrop = "http://image.tmdb.org/t/p/w780/" + detailsJsonObj.getString("backdrop_path");
            Log.d(TAG, "parsMovieJson: poster path " + movieBackDrop);
            String movieRating = detailsJsonObj.getString("vote_average");
            String movieYear = detailsJsonObj.getString("release_date");


            listOfMovies.add(new MoviesEntity(movieId, movieTitle, movieRating, movieYear, movieOverView, movieBackdrop, movieBackDrop));
        }

        return listOfMovies;
    }


}
