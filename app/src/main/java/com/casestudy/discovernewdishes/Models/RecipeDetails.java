package com.casestudy.discovernewdishes.Models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeDetails implements Parcelable {

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
    @SerializedName("summary")
    private String summary;
    @SerializedName("instructions")
    private String instruction;
    @SerializedName("extendedIngredients")
    @Expose
    private Ingredient[] ingredients;
    @SerializedName("nutrition")
    @Expose
    private NutrientsArray nutrients;

    protected RecipeDetails(Parcel in) {
        id = in.readInt();
        title = in.readString();
        imgUrl = in.readString();
        sourceName = in.readString();
        healthScore = in.readDouble();
        readyInMinutes = in.readInt();
        servings = in.readInt();
        summary = in.readString();
        instruction = in.readString();
        ingredients = in.createTypedArray(Ingredient.CREATOR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nutrients = in.readTypedObject(NutrientsArray.CREATOR);
        }
    }

    public static final Creator<RecipeDetails> CREATOR = new Creator<RecipeDetails>() {
        @Override
        public RecipeDetails createFromParcel(Parcel in) {
            return new RecipeDetails(in);
        }

        @Override
        public RecipeDetails[] newArray(int size) {
            return new RecipeDetails[size];
        }
    };

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

    public String getSummary() {
        return summary;
    }

    public String getInstruction() {
        return instruction;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public NutrientsArray getNutrients() {
        return nutrients;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setHealthScore(double healthScore) {
        this.healthScore = healthScore;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public void setNutrients(NutrientsArray nutrients) {
        this.nutrients = nutrients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(imgUrl);
        parcel.writeString(sourceName);
        parcel.writeDouble(healthScore);
        parcel.writeInt(readyInMinutes);
        parcel.writeInt(servings);
        parcel.writeString(summary);
        parcel.writeString(instruction);
        parcel.writeTypedArray(ingredients,0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            parcel.writeTypedObject(nutrients, 0);
        }
    }
}
