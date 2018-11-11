package com.example.miranpc.mymovieinfo.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miranpc.mymovieinfo.DataBase.MoviesEntity;
import com.example.miranpc.mymovieinfo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder> {

    private List<MoviesEntity> listOfMovies;
    private Context context;
    private onClickListener onClickListener;

    public MoviesRecyclerAdapter(Context context, List<MoviesEntity> listOfMovies, MoviesRecyclerAdapter.onClickListener onClickListener) {
        this.listOfMovies = listOfMovies;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    public MoviesRecyclerAdapter(Context context, onClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        listOfMovies = new ArrayList<>();
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.item_grid_layout, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MoviesEntity movieModel = listOfMovies.get(position);
        holder.textView.setText(movieModel.getMovieTitle());
        Picasso.with(context).load(movieModel.getMoviePoster()).placeholder(R.drawable.placer_holder).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (listOfMovies.size() == 0) {
            return 0;
        } else
            return listOfMovies.size();
    }

    public interface onClickListener {
        void onItemClickListener(MoviesEntity movieModel);
    }


    public void clearListOfMovies() {
        if (listOfMovies == null) {
            listOfMovies = new ArrayList<>();
        } else {
            int itemCount = listOfMovies.size();
            listOfMovies.clear();
            notifyItemRangeRemoved(0, itemCount);
        }

    }

    public void addMovies(List<MoviesEntity> movies) {
        int posStart = listOfMovies.size();
        if (movies != null)
            listOfMovies.addAll(movies);
        notifyItemRangeInserted(posStart, listOfMovies.size());
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movie_image);
            textView = itemView.findViewById(R.id.movie_name);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            MoviesEntity movies = listOfMovies.get(getAdapterPosition());
            onClickListener.onItemClickListener(movies);
        }
    }

}
