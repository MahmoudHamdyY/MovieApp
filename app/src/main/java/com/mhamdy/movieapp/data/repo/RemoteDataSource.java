package com.mhamdy.movieapp.data.repo;

import com.mhamdy.movieapp.data.api.Api;
import com.mhamdy.movieapp.data.api.ApiService;
import com.mhamdy.movieapp.data.entities.Movie;

import java.util.List;

import io.reactivex.Observable;

public class RemoteDataSource implements RepositoryI {
    private Api api;

    public RemoteDataSource() {
        api = ApiService.getInstance();
    }

    @Override
    public Observable<List<Movie>> getMovies() {
        return api.getMovies();
    }

    @Override
    public Observable<Movie> getMovie(int movieId) {
        return api.getMovieData(movieId);
    }
}
