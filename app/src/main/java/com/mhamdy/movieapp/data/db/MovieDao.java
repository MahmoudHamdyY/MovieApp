package com.mhamdy.movieapp.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mhamdy.movieapp.data.entities.Movie;

import java.util.List;


import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public abstract class MovieDao {

    @Query("SELECT * FROM movies")
    public abstract Maybe<List<Movie>> getMovies();

    @Query("SELECT * FROM movies where id = :movieId")
    public abstract Single<Movie> getMovie(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<Movie> movies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Movie movie);
}
