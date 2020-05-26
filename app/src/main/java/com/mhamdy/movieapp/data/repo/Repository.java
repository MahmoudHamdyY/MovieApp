package com.mhamdy.movieapp.data.repo;

import com.mhamdy.movieapp.data.entities.Movie;

import java.util.List;

import io.reactivex.Observable;

public class Repository implements RepositoryI {

    private CacheDataSource cacheDataSource;
    private RemoteDataSource remoteDataSource;

    public Repository(RemoteDataSource remoteDataSource, CacheDataSource cacheDataSource) {
        this.cacheDataSource = cacheDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public Observable<List<Movie>> getMovies() {
        return Observable.concatArray(cacheDataSource.getMovies(),
                remoteDataSource.getMovies()
                        .doOnNext(movies -> {
                            cacheDataSource.saveMovies(movies);
                        })).onErrorResumeNext(cacheDataSource.getMovies());
    }

    @Override
    public Observable<Movie> getMovie(int movieId) {
        return Observable.concatArray(cacheDataSource.getMovie(movieId),
                remoteDataSource.getMovie(movieId)
                        .doOnNext(movie -> {
                            cacheDataSource.saveMovie(movie);
                        })).onErrorResumeNext(cacheDataSource.getMovie(movieId));
    }
}
