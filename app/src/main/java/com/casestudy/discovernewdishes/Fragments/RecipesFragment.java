package com.casestudy.discovernewdishes.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.casestudy.discovernewdishes.Adapter.FavoriteRecipeAdapter;
import com.casestudy.discovernewdishes.Models.RecipeDetails;
import com.casestudy.discovernewdishes.R;
import com.casestudy.discovernewdishes.RecipeDetailsActivity;
import com.casestudy.discovernewdishes.businesslogic.RecipeProcessor;

import java.util.ArrayList;

public class RecipesFragment extends Fragment implements FavoriteRecipeAdapter.ClickedItem{

    RecyclerView rv_recipes;
    FavoriteRecipeAdapter adapter;
    RecipeProcessor recipeProcessor;
    ArrayList<RecipeDetails> recipeDetails;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        recipeProcessor = new RecipeProcessor(container.getContext().getApplicationContext());
        rv_recipes = view.findViewById(R.id.rv_recipes);

        recipeDetails = getRecipeDetails();
        adapter = new FavoriteRecipeAdapter(recipeDetails, this);
        rv_recipes.setLayoutManager(new LinearLayoutManager(container.getContext().getApplicationContext()));
        rv_recipes.setAdapter(adapter);
        return view;
    }

    private ArrayList<RecipeDetails> getRecipeDetails(){
        ArrayList<RecipeDetails> res = recipeProcessor.PopulateData();
        return res;
    }

    @Override
    public void ClickRecipe(RecipeDetails recipeDetails) {
        Intent intent = new Intent(getContext().getApplicationContext(), RecipeDetailsActivity.class);
        intent.putExtra("recipeDetails", recipeDetails);
        startActivity(intent);
    }

    @Override
    public void ClickRemoveToFavorite(RecipeDetails recipeDetails, int position) {
        int rowsAffected = recipeProcessor.RemoveToFavorite(String.valueOf(recipeDetails.getId()));
        if(rowsAffected > 0){
            this.recipeDetails.remove(position);
            adapter.notifyItemRemoved(position);
            //adapter.notifyDataSetChanged();
            Toast.makeText(getContext().getApplicationContext(), "Remove to Favorite", Toast.LENGTH_SHORT).show();
        }else if(rowsAffected == 0){
            Toast.makeText(getContext().getApplicationContext(), "Not Remove", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext().getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }


}