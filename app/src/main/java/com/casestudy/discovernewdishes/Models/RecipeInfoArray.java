package com.casestudy.discovernewdishes.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeInfoArray {
    @SerializedName("results")
    @Expose
    private RecipeInfo[] recipeInfos;

    public RecipeInfo[] getRecipeInfos() {
        return recipeInfos;
    }

    public void setRecipeInfos(RecipeInfo[] recipeInfos) {
        this.recipeInfos = recipeInfos;
    }
}
