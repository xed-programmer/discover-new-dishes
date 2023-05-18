package com.casestudy.discovernewdishes.Models;

import com.google.gson.annotations.SerializedName;

public class Trivia {
    @SerializedName("text")
    private String text;

    public String getText(){ return text;}
}
