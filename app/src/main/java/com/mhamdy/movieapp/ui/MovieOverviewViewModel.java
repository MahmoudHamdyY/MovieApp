package com.mhamdy.movieapp.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mhamdy.movieapp.data.SingleLiveEvent;
import com.mhamdy.movieapp.data.entities.Movie;
import com.mhamdy.movieapp.data.repo.Repository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

class MovieOverviewViewModel extends ViewModel {

    SingleLiveEvent<Movie> movieLiveData = new SingleLiveEvent<>();
    SingleLiveEvent<Boolean> loadingData = new SingleLiveEvent<>();
    SingleLiveEvent<String> errorMessageData = new SingleLiveEvent<>();

    private CompositeDisposable disposable = new CompositeDisposable();
    private Repository repository;

    private MovieOverviewViewModel(Repository repository) {
        this.repository = repository;
    }

    void getMovieData(int movieId) {
        disposable.add(repository.getMovie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    loadingData.setValue(false);
                    errorMessageData.setValue(throwable.getMessage());
                })
                .doOnSubscribe(disposable -> loadingData.setValue(true))
                .subscribe(movie -> {
                    loadingData.setValue(false);
                    movieLiveData.setValue(movie);
                }, throwable -> {
                    loadingData.setValue(false);
                    errorMessageData.setValue(throwable.getMessage());
                }));
    }

    @Override
    public void onCleared() {
        disposable.dispose();
        super.onCleared();
    }

    static class MovieOverviewViewModelFactory implements ViewModelProvider.Factory {

        private Repository repository;

        MovieOverviewViewModelFactory(Repository repository) {
            this.repository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MovieOverviewViewModel.class))
                return (T) new MovieOverviewViewModel(repository);
            else
                throw new IllegalArgumentException("viewModel is not exist");
        }
    }
}
