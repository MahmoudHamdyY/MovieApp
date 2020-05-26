package com.mhamdy.movieapp.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mhamdy.movieapp.data.SingleLiveEvent;
import com.mhamdy.movieapp.data.entities.Movie;
import com.mhamdy.movieapp.data.repo.Repository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

class MoviesViewModel extends ViewModel {

    SingleLiveEvent<List<Movie>> moviesLiveData = new SingleLiveEvent<>();
    SingleLiveEvent<Boolean> loadingData = new SingleLiveEvent<>();
    SingleLiveEvent<String> errorMessageData = new SingleLiveEvent<>();

    private CompositeDisposable disposable = new CompositeDisposable();
    private Repository repository;

    private MoviesViewModel(Repository repository) {
        this.repository = repository;
    }

    void getMovies() {
        disposable.add(repository.getMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    loadingData.setValue(false);
                    errorMessageData.setValue(throwable.getMessage());
                })
                .doOnSubscribe(disposable -> {
                    loadingData.setValue(true);
                })
                .subscribe(movies -> {
                    loadingData.setValue(false);
                    moviesLiveData.setValue(movies);
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

    static class MoviesViewModelFactory implements ViewModelProvider.Factory {

        private Repository repository;

        MoviesViewModelFactory(Repository repository) {
            this.repository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MoviesViewModel.class))
                return (T) new MoviesViewModel(repository);
            else
                throw new IllegalArgumentException("viewModel is not exist");
        }
    }
}
