package com.casestudy.discovernewdishes.Models;

import com.google.gson.annotations.SerializedName;

public class FoodVideo {

    @SerializedName("youTubeId")
    private String youtubeId;
    @SerializedName("title")
    private String title;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("views")
    private long views;

    public String getYoutubeId() {
        return youtubeId;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public long getViews() {
        return views;
    }
}
