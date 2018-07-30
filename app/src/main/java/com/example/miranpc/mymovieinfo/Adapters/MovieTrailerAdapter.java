package com.example.miranpc.mymovieinfo.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miranpc.mymovieinfo.R;
import com.example.miranpc.mymovieinfo.model.VideoModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.VideoViewHolder> {

    private Context context;
    private List<VideoModel> videoModelList;
    private onItemClickListener onClickListener;


    public MovieTrailerAdapter(Context context, onItemClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        videoModelList = new ArrayList<>();

    }

    public interface onItemClickListener {
        void itemClickListener(VideoModel videoModel);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(context).inflate(R.layout.movie_trailers_item, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.videoName.setText(videoModelList.get(position).getVideoName());
    }



    public void addMovies(List<VideoModel> videos) {
        Log.d(TAG, "addMovies: videos size"+ videoModelList.size());
        int posStart = videoModelList.size();
        if (videos != null) {
            videoModelList.addAll(videos);
            notifyDataSetChanged();
        }
        notifyItemRangeInserted(posStart, videoModelList.size());
    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: sizee"+ videoModelList.size());
        return videoModelList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView videoName;

        public VideoViewHolder(View itemView) {
            super(itemView);

            videoName = itemView.findViewById(R.id.video_name);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            VideoModel movies = videoModelList.get(getAdapterPosition());
            onClickListener.itemClickListener(movies);

        }
    }
}
