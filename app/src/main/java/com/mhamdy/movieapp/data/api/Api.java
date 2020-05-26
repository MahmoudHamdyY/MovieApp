package com.mhamdy.movieapp.data.api;

import com.mhamdy.movieapp.data.entities.Movie;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {
    @GET("movies")
    Observable<List<Movie>> getMovies();

    @GET("movies/{movie_id}")
    Observable<Movie> getMovieData(
            @Path("movie_id") int movieId
    );
}
