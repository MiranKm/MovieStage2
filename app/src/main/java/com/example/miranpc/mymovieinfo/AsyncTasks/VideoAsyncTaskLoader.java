package com.example.miranpc.mymovieinfo.AsyncTasks;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.miranpc.mymovieinfo.ApiUtil;
import com.example.miranpc.mymovieinfo.model.VideoModel;

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

public class VideoAsyncTaskLoader extends AsyncTaskLoader<List<VideoModel>> {

    private static final String TAG = "VideoAsyncTaskLoader";


    private int videoId;

    public VideoAsyncTaskLoader(@NonNull Context context, int videoId) {
        super(context);
        this.videoId = videoId;
    }

    @Nullable
    @Override
    public List<VideoModel> loadInBackground() {
        List<VideoModel> parsed = null;
        String videoJson;

        try {

            URL videoUrl = urlMakerforVideo(String.valueOf(videoId));
            videoJson = fetchData(videoUrl);
            parsed = videoParsing(videoJson);
            return parsed;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parsed;
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

    private URL urlMakerforVideo(String... args) throws MalformedURLException {
        Uri uri = Uri.parse(ApiUtil.MAIN_LINK).buildUpon()
                .appendPath(args[0])
                .appendPath(ApiUtil.VIDEOS).appendQueryParameter("api_key", ApiUtil.API_KEY).appendQueryParameter("language", "en-US")
                .build();

        URL url = new URL(uri.toString());

        return url;
    }


    private List<VideoModel> videoParsing(String json) throws JSONException {

        List<VideoModel> videoDetail = new ArrayList<>();

        JSONObject root = new JSONObject(json);

        JSONArray videoJA = root.getJSONArray("results");

        for (int i = 0; i < videoJA.length(); i++) {
            JSONObject videoDetailsObj = videoJA.getJSONObject(i);

            String videosKey = videoDetailsObj.getString("key");
            String videoName = videoDetailsObj.getString("name");
            videoDetail.add(new VideoModel(videoName, videosKey));

        }
        return videoDetail;
    }
}
