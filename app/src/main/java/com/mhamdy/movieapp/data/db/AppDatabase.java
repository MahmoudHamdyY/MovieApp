package com.mhamdy.movieapp.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mhamdy.movieapp.BuildConfig;
import com.mhamdy.movieapp.data.entities.Movie;

@Database(entities = Movie.class, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            synchronized (AppDatabase.class) {
                if (appDatabase == null) appDatabase = buildDatabase(context);
            }
        }
        return appDatabase;
    }

    private static AppDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, BuildConfig.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract MovieDao getMovieDao();
}
