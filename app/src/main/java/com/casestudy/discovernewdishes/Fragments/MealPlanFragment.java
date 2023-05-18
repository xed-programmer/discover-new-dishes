package com.casestudy.discovernewdishes.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.casestudy.discovernewdishes.Adapter.RecipeNutrientAdapter;
import com.casestudy.discovernewdishes.Api.ApiClient;
import com.casestudy.discovernewdishes.Api.ApiFailure;
import com.casestudy.discovernewdishes.Api.ApiUtils;
import com.casestudy.discovernewdishes.Api.UserService;
import com.casestudy.discovernewdishes.FoodScheduleActivity;
import com.casestudy.discovernewdishes.Models.RecipeDetails;
import com.casestudy.discovernewdishes.Models.RecipeNutrients;
import com.casestudy.discovernewdishes.R;
import com.casestudy.discovernewdishes.RecipeDetailsActivity;
import com.casestudy.discovernewdishes.businesslogic.NutrientsProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealPlanFragment extends Fragment implements View.OnClickListener, RecipeNutrientAdapter.ClickedItem{

    LinearLayout layout_search_not_found;
    EditText min_carb, max_carb, min_protein, max_protein, min_calories, max_calories,
            min_fat, max_fat, min_cholesterol, max_cholesterol, min_potassium, max_potassium, min_sugar, max_sugar;
    EditText previousEditText = null;
    Button btn_search;
    RecyclerView rv_res;
    ProgressBar progressBar;
    RecipeNutrientAdapter adapter;
    NutrientsProcessor processor;

    int minCarbs, minCalories, minCholesterol, minFat, minPotassium, minProtein, minSugar,
            maxCarbs, maxCalories, maxCholesterol, maxFat, maxPotassium, maxProtein, maxSugar;
    String strminCarbs, strminCalories, strminCholesterol, strminFat, strminPotassium, strminProtein, strminSugar,
            strmaxCarbs, strmaxCalories, strmaxCholesterol, strmaxFat, strmaxPotassium, strmaxProtein, strmaxSugar;
    private View add_to_fav_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_plan, container, false);

        processor = new NutrientsProcessor(container.getContext());
        layout_search_not_found = view.findViewById(R.id.layout_search_not_found);
        rv_res = view.findViewById(R.id.rv_res_plan);
        rv_res.setLayoutManager(new LinearLayoutManager(container.getContext()));
        btn_search = view.findViewById(R.id.btn_search_plan);
        progressBar = view.findViewById(R.id.progressbar);

        min_carb = view.findViewById(R.id.min_carb);
        min_protein = view.findViewById(R.id.min_protein);
        min_calories = view.findViewById(R.id.min_calories);
        min_fat = view.findViewById(R.id.min_fat);
        min_cholesterol = view.findViewById(R.id.min_cholesterol);
        min_potassium = view.findViewById(R.id.min_potassium);
        min_sugar = view.findViewById(R.id.min_sugar);

        max_carb = view.findViewById(R.id.max_carb);
        max_protein = view.findViewById(R.id.max_protein);
        max_calories = view.findViewById(R.id.max_calories);
        max_fat = view.findViewById(R.id.max_fat);
        max_cholesterol = view.findViewById(R.id.max_cholesterol);
        max_potassium = view.findViewById(R.id.max_potassium);
        max_sugar = view.findViewById(R.id.max_sugar);

        min_carb.addTextChangedListener(new MyTextWatcher(min_carb));
        min_protein.addTextChangedListener(new MyTextWatcher(min_protein));
        min_calories.addTextChangedListener(new MyTextWatcher(min_calories));
        min_fat.addTextChangedListener(new MyTextWatcher(min_fat));
        min_cholesterol.addTextChangedListener(new MyTextWatcher(min_cholesterol));
        min_potassium.addTextChangedListener(new MyTextWatcher(min_potassium));
        min_sugar.addTextChangedListener(new MyTextWatcher(min_sugar));

        max_carb.addTextChangedListener(new MyTextWatcher(max_carb));
        max_protein.addTextChangedListener(new MyTextWatcher(max_protein));
        max_calories.addTextChangedListener(new MyTextWatcher(max_calories));
        max_fat.addTextChangedListener(new MyTextWatcher(max_fat));
        max_cholesterol.addTextChangedListener(new MyTextWatcher(max_cholesterol));
        max_potassium.addTextChangedListener(new MyTextWatcher(max_potassium));
        max_sugar.addTextChangedListener(new MyTextWatcher(max_sugar));

        btn_search.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        UserService userService = ApiClient.getUserService();
        if(isReady()){
            Call<RecipeNutrients[]> call = userService.getRecipeByNutrients(minCarbs,maxCarbs,minCalories,maxCalories,minProtein,maxProtein,
                    minFat, maxFat, minCholesterol, maxCholesterol, minPotassium, maxPotassium, minSugar, maxSugar, ApiUtils.NUMBER, ApiUtils.APIKEY);

            call.enqueue(new Callback<RecipeNutrients[]>() {
                @Override
                public void onResponse(Call<RecipeNutrients[]> call, Response<RecipeNutrients[]> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(getContext().getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ArrayList<RecipeNutrients> recipes  = new ArrayList<>(Arrays.asList(response.body()));
                    if(recipes.size() > 0){
                        adapter = new RecipeNutrientAdapter(recipes, MealPlanFragment.this);
                        adapter.setSavedRecipe(processor.PopulateSavedItem());
                        rv_res.setAdapter(adapter);
                    }else{
                        layout_search_not_found.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<RecipeNutrients[]> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    layout_search_not_found.setVisibility(View.VISIBLE);
                    ApiFailure.onFailure(t, getContext().getApplicationContext());
                }
            });
        }
    }

    private boolean isReady(){
        progressBar.setVisibility(View.VISIBLE);
        layout_search_not_found.setVisibility(View.GONE);

        strminCarbs = min_carb.getText().toString();
        strminCalories = min_calories.getText().toString();
        strminCholesterol = min_cholesterol.getText().toString();
        strminFat = min_fat.getText().toString();
        strminPotassium = min_potassium.getText().toString();
        strminProtein = min_protein.getText().toString();
        strminSugar = min_sugar.getText().toString();

        strmaxCarbs = max_carb.getText().toString();
        strmaxCalories = max_calories.getText().toString();
        strmaxCholesterol = max_cholesterol.getText().toString();
        strmaxFat = max_fat.getText().toString();
        strmaxPotassium = max_potassium.getText().toString();
        strmaxProtein = max_protein.getText().toString();
        strmaxSugar = max_sugar.getText().toString();

//        if(strminCarbs.isEmpty()){
//            minCarbs = Integer.valueOf(min_carb.getHint().toString());
//        }
//        if(strminCalories.isEmpty()){
//            minCalories = Integer.valueOf(min_calories.getHint().toString());
//        }
//        if(strminCholesterol.isEmpty()){
//            minCholesterol = Integer.valueOf(min_cholesterol.getHint().toString());
//        }
//        if(strminFat.isEmpty()){
//            minFat = Integer.valueOf(min_fat.getHint().toString());
//        }
//        if(strminPotassium.isEmpty()){
//            minPotassium = Integer.valueOf(min_potassium.getHint().toString());
//        }
//        if(strminProtein.isEmpty()){
//            minProtein = Integer.valueOf(min_protein.getHint().toString());
//        }
        if(strminSugar.isEmpty()){
            minSugar = Integer.valueOf(min_sugar.getHint().toString());
        }

        minCarbs = (strminCarbs.isEmpty())? Integer.valueOf(min_carb.getHint().toString()) : Integer.valueOf(strminCarbs);
        minCalories = (strminCalories.isEmpty())? Integer.valueOf(min_calories.getHint().toString()) : Integer.valueOf(strminCalories);
        minCholesterol = (strminCholesterol.isEmpty())? Integer.valueOf(min_cholesterol.getHint().toString()) : Integer.valueOf(strminCholesterol);
        minFat = (strminFat.isEmpty())? Integer.valueOf(min_fat.getHint().toString()) : Integer.valueOf(strminFat);
        minPotassium = (strminPotassium.isEmpty())? Integer.valueOf(min_potassium.getHint().toString()) : Integer.valueOf(strminPotassium);
        minProtein = (strminProtein.isEmpty())? Integer.valueOf(min_protein.getHint().toString()) : Integer.valueOf(strminProtein);
        minSugar = (strminSugar.isEmpty())? Integer.valueOf(min_sugar.getHint().toString()) : Integer.valueOf(strminSugar);

        minCarbs = (minCarbs < Integer.valueOf(min_carb.getHint().toString()))? Integer.valueOf(min_carb.getHint().toString()) : minCarbs;
        minCalories = (minCalories < Integer.valueOf(min_calories.getHint().toString()))? Integer.valueOf(min_calories.getHint().toString()) : minCalories;
        minCholesterol = (minCholesterol < Integer.valueOf(min_cholesterol.getHint().toString()))? Integer.valueOf(min_cholesterol.getHint().toString()) : minCholesterol;
        minFat = (minFat < Integer.valueOf(min_fat.getHint().toString()))? Integer.valueOf(min_fat.getHint().toString()) : minFat;
        minPotassium = (minPotassium < Integer.valueOf(min_potassium.getHint().toString()))? Integer.valueOf(min_potassium.getHint().toString()) : minPotassium;
        minProtein = (minProtein < Integer.valueOf(min_protein.getHint().toString()))? Integer.valueOf(min_protein.getHint().toString()) : minProtein;
        minSugar = (minSugar < Integer.valueOf(min_sugar.getHint().toString()))? Integer.valueOf(min_sugar.getHint().toString()) : minSugar;

//        if(strmaxCarbs.isEmpty()){
//            maxCarbs = Integer.valueOf(max_carb.getHint().toString());
//        }
//        if(strmaxCalories.isEmpty()){
//            maxCalories = Integer.valueOf(max_calories.getHint().toString());
//        }
//        if(strmaxCholesterol.isEmpty()){
//            maxCholesterol = Integer.valueOf(max_cholesterol.getHint().toString());
//        }
//        if(strmaxFat.isEmpty()){
//            maxFat = Integer.valueOf(max_fat.getHint().toString());
//        }
//        if(strmaxPotassium.isEmpty()){
//            maxPotassium = Integer.valueOf(max_potassium.getHint().toString());
//        }
//        if(strmaxProtein.isEmpty()){
//            maxProtein = Integer.valueOf(max_protein.getHint().toString());
//        }
//        if(strmaxSugar.isEmpty()){
//            maxSugar = Integer.valueOf(max_sugar.getHint().toString());
//        }

        maxCarbs = (strmaxCarbs.isEmpty())? Integer.valueOf(max_carb.getHint().toString()) : Integer.valueOf(strmaxCarbs);
        maxCalories = (strmaxCalories.isEmpty())? Integer.valueOf(max_calories.getHint().toString()) : Integer.valueOf(strmaxCalories);
        maxCholesterol = (strmaxCholesterol.isEmpty())? Integer.valueOf(max_cholesterol.getHint().toString()) : Integer.valueOf(strmaxCholesterol);
        maxFat = (strmaxFat.isEmpty())? Integer.valueOf(max_fat.getHint().toString()) : Integer.valueOf(strmaxFat);
        maxPotassium = (strmaxPotassium.isEmpty())? Integer.valueOf(max_potassium.getHint().toString()) : Integer.valueOf(strmaxPotassium);
        maxProtein = (strmaxProtein.isEmpty())? Integer.valueOf(max_protein.getHint().toString()) : Integer.valueOf(strmaxProtein);
        maxSugar = (strmaxSugar.isEmpty())? Integer.valueOf(max_sugar.getHint().toString()) : Integer.valueOf(strmaxSugar);

        maxCarbs = (maxCarbs > Integer.valueOf(max_carb.getHint().toString()))? Integer.valueOf(max_carb.getHint().toString()) : maxCarbs;
        maxCalories = (maxCalories > Integer.valueOf(max_calories.getHint().toString()))? Integer.valueOf(max_calories.getHint().toString()) : maxCalories;
        maxCholesterol = (maxCholesterol > Integer.valueOf(max_cholesterol.getHint().toString()))? Integer.valueOf(max_cholesterol.getHint().toString()) : maxCholesterol;
        maxFat = (maxFat > Integer.valueOf(max_fat.getHint().toString()))? Integer.valueOf(max_fat.getHint().toString()) : maxFat;
        maxPotassium = (maxPotassium > Integer.valueOf(max_potassium.getHint().toString()))? Integer.valueOf(max_potassium.getHint().toString()) : maxPotassium;
        maxProtein = (maxProtein > Integer.valueOf(max_protein.getHint().toString()))? Integer.valueOf(max_protein.getHint().toString()) : maxProtein;
        maxSugar = (maxSugar > Integer.valueOf(max_sugar.getHint().toString()))? Integer.valueOf(max_sugar.getHint().toString()) : maxSugar;
        return true;
    }

    @Override
    public void ClickedRecipe(int id) {
        Intent intent = new Intent(getContext().getApplicationContext(), RecipeDetailsActivity.class);
        intent.putExtra("recipeId", id);
        startActivity(intent);
    }

    @Override
    public void ClickedAddToFavorite(RecipeNutrients recipe, View v) {
        ArrayList<String> saved_recipes = processor.PopulateSavedItem();
        if(saved_recipes.contains(String.valueOf(recipe.getId()))){
            int rowsAffected = processor.RemoveToFavorite(String.valueOf(recipe.getId()));
            if(rowsAffected > 0){
                ImageView add_fav = (ImageView) v;
                add_fav.setImageResource(R.drawable.ic_action_not_save);
                Toast.makeText(getContext().getApplicationContext(), "Remove to Favorite", Toast.LENGTH_SHORT).show();
            }else if(rowsAffected == 0){
                Toast.makeText(getContext().getApplicationContext(), "Not Remove", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext().getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }else {
            add_to_fav_view = v;
            getRecipeById(recipe.getId());
        }
    }

    @Override
    public void ClickedAddToSchedule(RecipeNutrients recipe) {
        Intent intent = new Intent(getContext().getApplicationContext(), FoodScheduleActivity.class);
        intent.putExtra("recipeId", recipe.getId());
        startActivity(intent);
    }

    private void getRecipeById(int id) {
        UserService userService = ApiClient.getUserService();
        Call<RecipeDetails> call = userService.getRecipeDetails(id, true, ApiUtils.APIKEY);

        call.enqueue(new Callback<RecipeDetails>() {
            @Override
            public void onResponse(Call<RecipeDetails> call, Response<RecipeDetails> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext().getApplicationContext(),response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                RecipeDetails recipe = response.body();
                long rowsAffected = processor.AddToFavorite(recipe);
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

    class MyTextWatcher implements TextWatcher{

        private EditText mEditText;

        public MyTextWatcher(EditText editText){
            mEditText = editText;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            int et_id = mEditText.getId();

            if(mEditText.getText().toString().isEmpty()){
                return;
            }
            if(previousEditText == null){
                previousEditText = mEditText;
            }

            if(previousEditText.getId() != mEditText.getId()){
                int value = Integer.valueOf(previousEditText.getText().toString());
                int hint = Integer.valueOf(previousEditText.getHint().toString());

                //check if the editText in minimum
                if(hint >= 0 && hint <= 50){
                    if(value < hint){ //if value entered is lower than minimum
                        String message = "The minimum value for " + previousEditText.getTag().toString() + " is " + hint +"\n" +
                                "Or keep it blank";
                        previousEditText.setError(message);
                        //Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        previousEditText.setText(String.valueOf(hint));
                    }
                }
                previousEditText = mEditText;
                previousEditText.setSelection(mEditText.getText().length());
            }
        }
    }
}
