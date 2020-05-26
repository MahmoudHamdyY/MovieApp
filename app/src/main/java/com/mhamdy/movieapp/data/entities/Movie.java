package com.mhamdy.movieapp.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity(tableName = "movies")
public class Movie implements Serializable {
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo
    private int id;
    @SerializedName("name")
    @ColumnInfo
    private String name;
    @SerializedName("image")
    @ColumnInfo
    private String image;
    @SerializedName("description")
    @ColumnInfo
    private String description;

    public Movie(int id, String name, String image, String description) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getImageUrl() {
        return extractImageUrl(image);
    }

    public String getDescription() {
        return description;
    }

    private String extractImageUrl(String url) {
        String pattern = "file/d/(.*).view";
        Pattern generalDriveUrlPattern = Pattern.compile(pattern);
        Matcher generalDriveUrlMatcher = generalDriveUrlPattern.matcher(url);
        if (generalDriveUrlMatcher.find()) {
            String id = generalDriveUrlMatcher.group(1);
            return "https://docs.google.com/uc?id=" + id;
        }
        return null;
    }
}
