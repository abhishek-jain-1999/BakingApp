package com.example.abhishek.bakingapp.dataHolder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Awesome Pojo Generator
 */
public class Steps {
    @SerializedName("videoURL")
    @Expose
    private String videoURL;
    @SerializedName("description")
    @Expose
    private String description;

    public Steps(String videoURL, String description, Integer id, String shortDescription, String thumbnailURL) {
        this.videoURL = videoURL;
        this.description = description;
        this.id = id;
        this.shortDescription = shortDescription;
        this.thumbnailURL = thumbnailURL;
    }

    @SerializedName("id")
    @Expose

    private Integer id;
    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;
    @SerializedName("thumbnailURL")
    @Expose
    private String thumbnailURL;

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}