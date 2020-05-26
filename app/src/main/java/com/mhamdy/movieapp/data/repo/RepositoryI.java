package com.mhamdy.movieapp.data.repo;

import com.mhamdy.movieapp.data.entities.Movie;

import java.util.List;

import io.reactivex.Observable;

public interface RepositoryI {
    Observable<List<Movie>> getMovies();
    Observable<Movie> getMovie(int movieId);
}
