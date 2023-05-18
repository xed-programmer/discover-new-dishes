package com.casestudy.discovernewdishes.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.casestudy.discovernewdishes.Adapter.RecentRecipeAdapter;
import com.casestudy.discovernewdishes.Api.ApiClient;
import com.casestudy.discovernewdishes.Api.ApiFailure;
import com.casestudy.discovernewdishes.Api.ApiUtils;
import com.casestudy.discovernewdishes.Api.UserService;
import com.casestudy.discovernewdishes.Models.RecipeDetails;
import com.casestudy.discovernewdishes.R;
import com.casestudy.discovernewdishes.RecipeDetailsActivity;
import com.casestudy.discovernewdishes.SearchActivity;
import com.casestudy.discovernewdishes.businesslogic.RecentProcessor;
import com.casestudy.discovernewdishes.businesslogic.RecipeProcessor;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentRecipeFragment extends Fragment implements RecentRecipeAdapter.CLickedRecentItem, View.OnClickListener {

    private TextView et_clear;
    private ImageView iv_clear;
    private RecyclerView rv_recent;
    private RecentRecipeAdapter adapter;
    private ArrayList<RecipeDetails> recipes;
    private ArrayList<String> saved_data;
    private RecipeProcessor processor;
    private View add_to_fav_view;
    private UserService userService;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent_recipe, container, false);
        processor = new RecipeProcessor(container.getContext().getApplicationContext());
        et_clear = view.findViewById(R.id.et_clear);
        iv_clear = view.findViewById(R.id.iv_clear);
        rv_recent = view.findViewById(R.id.rv_recent);

        iv_clear.setOnClickListener(this);
        et_clear.setOnClickListener(this);

        rv_recent.setLayoutManager(new LinearLayoutManager(container.getContext()));
        saved_data = processor.PopulateSavedItem();
        recipes = processor.PopulateRecentData();
        adapter = new RecentRecipeAdapter(recipes, this);
        adapter.setSavedData(saved_data);
        rv_recent.setAdapter(adapter);

        return view;
    }

    @Override
    public void ClickedRecentRecipe(int id) {
        Intent intent = new Intent(getContext().getApplicationContext(), RecipeDetailsActivity.class);
        intent.putExtra("recipeId", id);
        startActivity(intent);
    }

    @Override
    public void ClickedAddToFavorite(RecipeDetails recipe, View view) {
        ArrayList<String> saved_recipes = processor.PopulateSavedItem();
        if(saved_recipes.contains(String.valueOf(recipe.getId()))){
            int rowsAffected = processor.RemoveToFavorite(String.valueOf(recipe.getId()));
            if(rowsAffected > 0){
                ImageView add_fav = (ImageView) view;
                add_fav.setImageResource(R.drawable.ic_action_not_save);
                Toast.makeText(getContext().getApplicationContext(), "Remove to Favorite", Toast.LENGTH_SHORT).show();
            }else if(rowsAffected == 0){
                Toast.makeText(getContext().getApplicationContext(), "Not Remove", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext().getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }else {
            add_to_fav_view = view;
            getRecipeById(recipe.getId());
        }
    }

    private void getRecipeById(int id){
        userService = ApiClient.getUserService();
        Call<RecipeDetails> call = userService.getRecipeDetails(id, true, ApiUtils.APIKEY);

        call.enqueue(new Callback<RecipeDetails>() {
            @Override
            public void onResponse(Call<RecipeDetails> call, Response<RecipeDetails> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext().getApplicationContext(),response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                RecipeDetails recipeDetails = response.body();
                long rowsAffected = processor.AddToFavorite(recipeDetails);
                if(rowsAffected > 0){
                    ImageView add_fav = (ImageView) add_to_fav_view;
                    add_fav.setImageResource(R.drawable.ic_action_favorite);
                    Toast.makeText(getContext().getApplicationContext(), "Added to Favorite", Toast.LENGTH_SHORT).show();
                }else if(rowsAffected == 0){
                    Toast.makeText(getContext().getApplicationContext(), "Not Added", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext().getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeDetails> call, Throwable t) {
                ApiFailure.onFailure(t, getContext().getApplicationContext());
            }
        });
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder alrt = new AlertDialog.Builder(getContext());
        alrt.setTitle("Food Schedule").setCancelable(false)
                .setMessage("Are you sure to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int rowsAffected = processor.ClearRecent();
                        if(rowsAffected > 0){
                            recipes.clear();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext().getApplicationContext(), rowsAffected + " item/s has been cleared", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getContext().getApplicationContext(), "No item has been cleared", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", null);

        AlertDialog dialog = alrt.create();
        dialog.show();
        Button btn_positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button btn_negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        btn_positive.setTextColor(getContext().getResources().getColor(R.color.bg_success));
        btn_negative.setTextColor(getContext().getResources().getColor(R.color.bg_danger));
    }
}