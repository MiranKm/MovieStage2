package com.example.miranpc.mymovieinfo.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miranpc.mymovieinfo.R;
import com.example.miranpc.mymovieinfo.model.MovieReviewModel;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ReviewViewHolder> {

    List<MovieReviewModel> listOfReviews;
    private Context context;
    private onItemClick onClickListener;

    public MovieReviewAdapter(Context context, onItemClick onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        listOfReviews = new ArrayList<>();
    }


    public interface onItemClick {
        void onItemClickListener(MovieReviewModel movieReview);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.reviews_layout, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        MovieReviewModel movieReviewModelList = listOfReviews.get(position);

        holder.urlTv.setText(movieReviewModelList.getUrl());
        holder.authorTv.setText(movieReviewModelList.getAuthor());
        holder.contentTv.setText(movieReviewModelList.getContent());

    }

    @Override
    public int getItemCount() {
        return listOfReviews.size();
    }

    public void addReviews(List<MovieReviewModel> movies) {
        int posStart = listOfReviews.size();
        if (movies != null) {
            listOfReviews.addAll(movies);
            notifyDataSetChanged();
        }
        notifyItemRangeInserted(posStart, listOfReviews.size());
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView authorTv;
        TextView contentTv;
        TextView urlTv;
        TextView count;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            authorTv = itemView.findViewById(R.id.author);
            count = itemView.findViewById(R.id.count);
            contentTv = itemView.findViewById(R.id.content);
            urlTv = itemView.findViewById(R.id.url);

        }

        @Override
        public void onClick(View v) {
            MovieReviewModel movies = listOfReviews.get(getAdapterPosition());
            onClickListener.onItemClickListener(movies);
        }
    }
}
