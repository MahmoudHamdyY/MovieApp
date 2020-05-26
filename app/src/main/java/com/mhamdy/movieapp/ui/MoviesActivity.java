package com.mhamdy.movieapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mhamdy.movieapp.R;
import com.mhamdy.movieapp.data.db.AppDatabase;
import com.mhamdy.movieapp.data.repo.CacheDataSource;
import com.mhamdy.movieapp.data.repo.RemoteDataSource;
import com.mhamdy.movieapp.data.repo.Repository;

public class MoviesActivity extends AppCompatActivity implements MoviesAdapter.OnItemClickListener {

    private RecyclerView moviesRv;
    private ImageView progressIv;
    private MoviesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        bindViews();
        initData();
        initObservers();
    }

    private void bindViews() {
        progressIv = findViewById(R.id.progress_iv);
        moviesRv = findViewById(R.id.movies_rv);
        moviesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void initData() {
        viewModel = new ViewModelProvider(this,
                new MoviesViewModel.MoviesViewModelFactory(new Repository(new RemoteDataSource(),
                        new CacheDataSource(AppDatabase.getInstance(this))))).get(MoviesViewModel.class);
        viewModel.getMovies();
    }

    private void initObservers() {
        viewModel.loadingData.observe(this,
                loading -> progressIv.setVisibility(loading ? View.VISIBLE : View.GONE));
        viewModel.moviesLiveData.observe(this,
                movies -> moviesRv.setAdapter(new MoviesAdapter(movies, MoviesActivity.this)));
        viewModel.errorMessageData.observe(this,
                message -> Toast.makeText(MoviesActivity.this, message, Toast.LENGTH_LONG).show());
    }

    @Override
    public void onItemClicked(int itemId) {
        Intent movieOverViewIntent = new Intent(this, MovieOverviewActivity.class);
        movieOverViewIntent.putExtra(MovieOverviewActivity.EXTRA_MOVIE_ID, itemId);
        startActivity(movieOverViewIntent);
    }
}
