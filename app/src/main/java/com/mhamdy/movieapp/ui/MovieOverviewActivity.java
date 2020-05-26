package com.mhamdy.movieapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mhamdy.movieapp.R;
import com.mhamdy.movieapp.data.db.AppDatabase;
import com.mhamdy.movieapp.data.repo.CacheDataSource;
import com.mhamdy.movieapp.data.repo.RemoteDataSource;
import com.mhamdy.movieapp.data.repo.Repository;

public class MovieOverviewActivity extends AppCompatActivity {

    public static String EXTRA_MOVIE_ID = "movie_id_extra";
    private MovieOverviewViewModel viewModel;
    private ImageView movieImageTv, progressIv;
    private TextView movieNameTv, sheetMovieNameTv, movieDescriptionTv;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_overview);

        bindViews();
        initData();
        initObservers();
        initListeners();
    }

    private void bindViews() {
        movieImageTv = findViewById(R.id.movie_image);
        progressIv = findViewById(R.id.progress_iv);
        movieNameTv = findViewById(R.id.movie_name);
        sheetMovieNameTv = findViewById(R.id.name_tv);
        movieDescriptionTv = findViewById(R.id.description_tv);
        FrameLayout bottomSheetFrame = findViewById(R.id.movie_description_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetFrame);
    }

    private void initData() {
        viewModel = new ViewModelProvider(this,
                new MovieOverviewViewModel.MovieOverviewViewModelFactory(new Repository(new RemoteDataSource(),
                        new CacheDataSource(AppDatabase.getInstance(this))))).get(MovieOverviewViewModel.class);
        if (!getIntent().hasExtra(EXTRA_MOVIE_ID))
            throw new IllegalStateException("movie id is not passed");
        viewModel.getMovieData(getIntent().getIntExtra(EXTRA_MOVIE_ID, 0));
    }

    private void initObservers() {
        viewModel.movieLiveData.observe(this, movie -> {
            Glide.with(MovieOverviewActivity.this)
                    .load(movie.getImageUrl())
                    .placeholder(R.drawable.progress_animation)
                    .dontAnimate()
                    .into(movieImageTv);
            movieDescriptionTv.setText(movie.getDescription());
            movieNameTv.setText(movie.getName());
            sheetMovieNameTv.setText(movie.getName());
        });
        viewModel.loadingData.observe(this,
                loading -> progressIv.setVisibility(loading ? View.VISIBLE : View.GONE));
        viewModel.errorMessageData.observe(this,
                message -> Toast.makeText(MovieOverviewActivity.this, message, Toast.LENGTH_LONG).show());
    }

    private void initListeners() {
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    movieNameTv.setVisibility(View.VISIBLE);
                    sheetMovieNameTv.setVisibility(View.GONE);
                    movieImageTv.setAlpha(1f);
                } else {
                    movieNameTv.setVisibility(View.GONE);
                    sheetMovieNameTv.setVisibility(View.VISIBLE);
                    movieImageTv.setAlpha(0.5f);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                movieNameTv.setVisibility(View.GONE);
                sheetMovieNameTv.setVisibility(View.VISIBLE);
                movieImageTv.setAlpha(0.5f);
            }
        });
    }
}
