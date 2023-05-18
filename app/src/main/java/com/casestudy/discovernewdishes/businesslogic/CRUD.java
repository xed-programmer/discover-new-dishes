package com.casestudy.discovernewdishes.businesslogic;

import com.casestudy.discovernewdishes.Models.RecipeDetails;

import java.util.ArrayList;

public interface CRUD<T> {
    public long AddToFavorite(T data);
    public ArrayList<T> PopulateData();
    public ArrayList<String> PopulateSavedItem();
    public int RemoveToFavorite(String id);
}
