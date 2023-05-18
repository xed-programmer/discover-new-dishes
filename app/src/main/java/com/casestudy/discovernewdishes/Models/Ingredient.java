package com.casestudy.discovernewdishes.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {
    @SerializedName("name")
    private String name;
    @SerializedName("amount")
    private double amount;
    @SerializedName("unit")
    private String unit;
    @SerializedName("image")
    private String image;

    protected Ingredient(Parcel in) {
        name = in.readString();
        amount = in.readDouble();
        unit = in.readString();
        image = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }
    public String getImage(){
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(amount);
        parcel.writeString(unit);
        parcel.writeString(image);
    }
}

