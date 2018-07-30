package com.example.miranpc.mymovieinfo.AsyncTasks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.miranpc.mymovieinfo.ApiUtil;
import com.example.miranpc.mymovieinfo.model.MovieReviewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReviewAsyncTaskLoader extends android.support.v4.content.AsyncTaskLoader<List<MovieReviewModel>>/*AsyncTaskLoader<List<MovieReviewModel>>*/ {

    private int movieId;
    private static final String TAG = "ReviewAsyncTaskLoader";


    public ReviewAsyncTaskLoader(Context context, int movieId) {
        super(context);
        this.movieId = movieId;
    }


    @Override
    public List<MovieReviewModel> loadInBackground() {

        List<MovieReviewModel> list = null;
        try {
            URL url = urlMaker(String.valueOf(movieId));

            String json = fetchData(url);


            list = reviewParsing(json);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
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


    private List<MovieReviewModel> reviewParsing(String json) throws JSONException {

        List<MovieReviewModel> list = new ArrayList<>();
        JSONObject root = new JSONObject(json);

        JSONArray resultArray = root.getJSONArray("results");

        for (int i = 0; i < resultArray.length(); i++) {

            JSONObject reviews = resultArray.getJSONObject(i);

            String author = reviews.getString("author");
            String content = reviews.getString("content");
            String url = reviews.getString("url");

            list.add(new MovieReviewModel(author, content, url));

        }

        return list;
    }


    private URL urlMaker(String... args) throws MalformedURLException {
        Uri uri = Uri.parse(ApiUtil.MAIN_LINK).buildUpon()
                .appendPath(args[0]+"\\")
                .appendPath(ApiUtil.REVIEWS)
                .appendQueryParameter("api_key", ApiUtil.API_KEY)
                .appendQueryParameter("language", ApiUtil.LANG).build();

        URL url = new URL(uri.toString());

        return url;

    }
}


