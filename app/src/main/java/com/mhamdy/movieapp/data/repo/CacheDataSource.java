package com.mhamdy.movieapp.data.repo;

import com.mhamdy.movieapp.data.db.AppDatabase;
import com.mhamdy.movieapp.data.entities.Movie;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class CacheDataSource implements RepositoryI {
    private AppDatabase database;

    public CacheDataSource(AppDatabase database) {
        this.database = database;
    }

    @Override
    public Observable<List<Movie>> getMovies() {
        return database.getMovieDao().getMovies().filter(movies -> !movies.isEmpty()).toObservable();
    }

    @Override
    public Observable<Movie> getMovie(int movieId) {
        return database.getMovieDao().getMovie(movieId).toObservable();
    }

    void saveMovies(List<Movie> movies) {
        Observable.fromCallable(() -> {
            database.getMovieDao().insertAll(movies);
            return true;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }

    void saveMovie(Movie movie) {
        Observable.fromCallable(() -> {
            database.getMovieDao().insert(movie);
            return true;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }
}
