package com.casestudy.discovernewdishes.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FoodVideoArray {
    @SerializedName("videos")
    @Expose
    private FoodVideo[] videos;

    public FoodVideo[] getVideos() {
        return videos;
    }
}
