package com.casestudy.discovernewdishes.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeInfo {
    @SerializedName("id")
    private int id;
    private String title;
    @SerializedName("image")
    @Expose
    private String imgUrl;
    @SerializedName("sourceName")
    @Expose
    private String sourceName;
    @SerializedName("healthScore")
    @Expose
    private double healthScore;
    @SerializedName("readyInMinutes")
    @Expose
    private int readyInMinutes;
    @SerializedName("servings")
    @Expose
    private int servings;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getSourceName() {
        return sourceName;
    }

    public double getHealthScore() {
        return healthScore;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public int getServings() {
        return servings;
    }
}


