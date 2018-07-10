package com.example.miranpc.mymovieinfo;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.miranpc.mymovieinfo.model.MovieModel;

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

public class MovieAsyncTaskLoader extends AsyncTaskLoader<List<MovieModel>> {

    private static final String TAG = "MovieAsyncTaskLoader";


    public final static String MAIN_LINK = "http://api.themoviedb.org/3/movie/";
    public final static String API_KEY = "";   // Todo add your api key here
    public final static String LANG = "en-US";

    String movieCat;

    public MovieAsyncTaskLoader(Context context, String movieCat) {
        super(context);
        this.movieCat = movieCat;

    }


    @Override
    public List<MovieModel> loadInBackground() {

        List<MovieModel> moviesList = null;

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
        Uri uri = Uri.parse(MAIN_LINK).buildUpon()
                .appendPath(args[0])
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", LANG)
                .build();

        URL url = new URL(uri.toString());

        return url;
    }


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


    private List<MovieModel> parsMovieJson(String json) throws JSONException {


        List<MovieModel> listOfMovies = new ArrayList<>();

        JSONObject root = new JSONObject(json);

        int pageNumber = root.getInt("page");

        JSONArray detailsJsonArr = root.getJSONArray("results");

        for (int i = 0; i < detailsJsonArr.length(); i++) {

            Log.d(TAG, "parsMovieJson: i s loop " + i);

            JSONObject detailsJsonObj = detailsJsonArr.getJSONObject(i);

            String movieTitle = detailsJsonObj.getString("title");
            String movieBackDrop = "http://image.tmdb.org/t/p/w500/" + detailsJsonObj.getString("poster_path");
            String movieOverView = detailsJsonObj.getString("overview");
            String movieBackdrop = "http://image.tmdb.org/t/p/w780/" + detailsJsonObj.getString("backdrop_path");
            String movieRating = detailsJsonObj.getString("vote_average");
            String movieYear = detailsJsonObj.getString("release_date");


            listOfMovies.add(new MovieModel(movieTitle, movieRating, movieYear, movieOverView, movieBackdrop, movieBackDrop));
        }

        return listOfMovies;
    }

}
