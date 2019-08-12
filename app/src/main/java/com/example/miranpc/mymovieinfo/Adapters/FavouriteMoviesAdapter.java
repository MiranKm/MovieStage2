package com.example.miranpc.mymovieinfo.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miranpc.mymovieinfo.model.MoviesEntity;
import com.example.miranpc.mymovieinfo.R;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMoviesAdapter extends RecyclerView.Adapter<FavouriteMoviesAdapter.MovieViewHolder> {

    private List<MoviesEntity> listOfMovies;
    private Context context;
    private onItemClickListener onClickListener;
    int counter;

    public FavouriteMoviesAdapter(Context context, onItemClickListener onClicking) {
        this.context = context;
        this.onClickListener = onClicking;
        listOfMovies = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.fav_rv_layout, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        MoviesEntity moviesEntity = listOfMovies.get(position);
        holder.count.setText(String.valueOf(position + 1));
        holder.title.setText(listOfMovies.get(position).getMovieTitle());
    }

    public void setMovies(List<MoviesEntity> movies) {
        listOfMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listOfMovies.size();
    }


    public List<MoviesEntity> getMovie() {
        return listOfMovies;
    }


    public void addMovies(List<MoviesEntity> movies) {
        int posStart = listOfMovies.size();
        if (movies != null) {
            listOfMovies.addAll(movies);
            notifyDataSetChanged();
        }

        notifyItemRangeInserted(posStart, listOfMovies.size());
    }

    public void clearList(){
        if (listOfMovies == null) {
            listOfMovies = new ArrayList<>();
        } else {
            int itemCount = listOfMovies.size();
            listOfMovies.clear();
            notifyItemRangeRemoved(0, itemCount);
        }

    }



    public interface onItemClickListener {
        void onItemClickListener(int position);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView count;

        public MovieViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movie_title);
            count = itemView.findViewById(R.id.count);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onClickListener.onItemClickListener(getAdapterPosition());
        }
    }
}
