package com.mhamdy.movieapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mhamdy.movieapp.R;
import com.mhamdy.movieapp.data.entities.Movie;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder> {

    private List<Movie> movies;
    private OnItemClickListener listener;

    MoviesAdapter(List<Movie> movies, OnItemClickListener onItemClickListener) {
        this.movies = movies;
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public MoviesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MoviesHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bindItem(movie.getImageUrl());
        holder.itemView.setOnClickListener(view -> listener.onItemClicked(movie.getId()));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface OnItemClickListener {
        void onItemClicked(int itemId);
    }

    static class MoviesHolder extends RecyclerView.ViewHolder {

        ImageView moviePoster;

        MoviesHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movie_image);
        }

        void bindItem(String imageUrl) {
            Glide.with(itemView.getContext()).load(imageUrl)
                    .placeholder(R.drawable.progress_animation)
                    .skipMemoryCache(false)
                    .dontAnimate().into(moviePoster);
        }
    }
}
