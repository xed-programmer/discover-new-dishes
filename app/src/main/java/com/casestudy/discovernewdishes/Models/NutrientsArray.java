package com.casestudy.discovernewdishes.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NutrientsArray implements Parcelable {
    @SerializedName("nutrients")
    @Expose
    private Nutrient[] nutrients;

    protected NutrientsArray(Parcel in) {
        nutrients = in.createTypedArray(Nutrient.CREATOR);
    }

    public static final Creator<NutrientsArray> CREATOR = new Creator<NutrientsArray>() {
        @Override
        public NutrientsArray createFromParcel(Parcel in) {
            return new NutrientsArray(in);
        }

        @Override
        public NutrientsArray[] newArray(int size) {
            return new NutrientsArray[size];
        }
    };

    public Nutrient[] getNutrients() {
        return nutrients;
    }

    public void setNutrients(Nutrient[] nutrients) {
        this.nutrients = nutrients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedArray(nutrients, i);
    }
}
