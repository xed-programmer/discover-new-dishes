package com.casestudy.discovernewdishes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.casestudy.discovernewdishes.Adapter.RecipeAdapter;
import com.casestudy.discovernewdishes.Api.ApiClient;
import com.casestudy.discovernewdishes.Api.ApiFailure;
import com.casestudy.discovernewdishes.Api.ApiUtils;
import com.casestudy.discovernewdishes.Api.UserService;
import com.casestudy.discovernewdishes.Models.RecipeDetails;
import com.casestudy.discovernewdishes.Models.RecipeInfo;
import com.casestudy.discovernewdishes.Models.RecipeInfoArray;
import com.casestudy.discovernewdishes.businesslogic.RecipeProcessor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements RecipeAdapter.ClickedItem{

    LinearLayout layout_search_not_found;
    EditText et_search;
    ImageView iv_btn_search, search_img;
    RecyclerView rv_recipe_res;
    ProgressBar progressBar;
    FloatingActionButton fab_toTop;
    NestedScrollView nestedScrollView;
    RecipeAdapter recipeAdapter;
    UserService userService;
    RecipeInfoArray recipeInfoArray;
    LinearLayoutManager linearLayoutManager;
    View add_to_fav_view;
    int imgRes;
    String mealType;
    //if nameMealTypes changed, change also in MealTypeAdapter
    String[] nameMealTypes = {"Main Course", "Side Dish", "Dessert", "Appetizer", "Breakfast", "Soup", "Beverage", "Sauce", "Marinade"};
    RecipeProcessor recipeProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recipeProcessor = new RecipeProcessor(this);
        imgRes = getIntent().getIntExtra("image", 0);
        mealType = getIntent().getStringExtra("mealType");

        layout_search_not_found = findViewById(R.id.layout_search_not_found);
        et_search = findViewById(R.id.et_search);
        search_img = findViewById(R.id.search_img);
        iv_btn_search = findViewById(R.id.btn_search);
        rv_recipe_res = findViewById(R.id.rv_res_recipe);
        fab_toTop = findViewById(R.id.fab_toTop);
        nestedScrollView = findViewById(R.id.nestedscrollview);
        progressBar = findViewById(R.id.progressbar);
        linearLayoutManager = new LinearLayoutManager(this);

        if(imgRes != 0){
            search_img.setImageResource(imgRes);
        }

        rv_recipe_res.setLayoutManager(linearLayoutManager);

        fab_toTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nestedScrollView.smoothScrollTo(0,0,1500);
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY > 0){
                    fab_toTop.setVisibility(View.VISIBLE);
                }else if(scrollY == 0){
                    fab_toTop.setVisibility(View.INVISIBLE);
                }
            }
        });
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH || i == keyEvent.KEYCODE_ENTER){
                    if(!et_search.getText().toString().isEmpty()){
                        layout_search_not_found.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getRecipes();
                        hideKeyboard();
                        return true;
                    }else{
                        Toast.makeText(getApplicationContext(), "No Item To be Search", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        iv_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_search.getText().toString().isEmpty()){
                    layout_search_not_found.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    getRecipes();
                    hideKeyboard();
                }else{
                    Toast.makeText(getApplicationContext(), "No Item To be Search", Toast.LENGTH_SHORT).show();
                }
            }
        });

        List<String> mealTypeList = Arrays.asList(nameMealTypes);
        if(mealTypeList.contains(mealType)){
            progressBar.setVisibility(View.VISIBLE);
            getRecipes();
        }
    }


    private void getRecipes(){

        ArrayList<String> saved_recipes = recipeProcessor.PopulateSavedItem();
        userService = ApiClient.getUserService();
        Call<RecipeInfoArray> call;

        List<String> mealTypeList = Arrays.asList(nameMealTypes);
        if(mealTypeList.contains(mealType)){
            call = userService.getMealTypeRecipes(et_search.getText().toString(), mealType.toLowerCase(),true, 20,ApiUtils.APIKEY);
        }else{
            call = userService.getRecipes(et_search.getText().toString(), true, 20,ApiUtils.APIKEY);
        }
        call.enqueue(new Callback<RecipeInfoArray>() {
            @Override
            public void onResponse(Call<RecipeInfoArray> call, Response<RecipeInfoArray> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),response.code(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                recipeInfoArray = response.body();
                ArrayList<RecipeInfo> recipes =new ArrayList<>(Arrays.asList(recipeInfoArray.getRecipeInfos()));
                if(recipes.size() > 0){
                    recipeAdapter = new RecipeAdapter(recipes, SearchActivity.this);
                    recipeAdapter.setSavedRecipe(saved_recipes);
                    rv_recipe_res.setAdapter(recipeAdapter);
                }else{
                    layout_search_not_found.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<RecipeInfoArray> call, Throwable t) {
                ApiFailure.onFailure(t, getApplicationContext());
                progressBar.setVisibility(View.INVISIBLE);
                layout_search_not_found.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void ClickedRecipe(int recipeId) {
        Intent intent = new Intent(SearchActivity.this, RecipeDetailsActivity.class);
        intent.putExtra("recipeId", recipeId);
        startActivity(intent);
    }

    @Override
    public void ClickedAddToFavorite(RecipeInfo recipeInfo, View v) {
        ArrayList<String> saved_recipes = recipeProcessor.PopulateSavedItem();
        if(saved_recipes.contains(String.valueOf(recipeInfo.getId()))){
            int rowsAffected = recipeProcessor.RemoveToFavorite(String.valueOf(recipeInfo.getId()));
            if(rowsAffected > 0){
                ImageView add_fav = (ImageView) v;
                add_fav.setImageResource(R.drawable.ic_action_not_save);
                Toast.makeText(getApplicationContext(), "Remove to Favorite", Toast.LENGTH_SHORT).show();
            }else if(rowsAffected == 0){
                Toast.makeText(getApplicationContext(), "Not Remove", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }else {
            add_to_fav_view = v;
            getRecipeById(recipeInfo.getId());
        }
    }

    @Override
    public void ClickedAddToSchedule(RecipeInfo recipe) {
        Intent intent = new Intent(getApplicationContext(), FoodScheduleActivity.class);
        intent.putExtra("recipeId", recipe.getId());
        startActivity(intent);
    }

    private void hideKeyboard(){
        InputMethodManager in = (InputMethodManager)getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
    }

    private void getRecipeById(int id){
        userService = ApiClient.getUserService();
        Call<RecipeDetails> call = userService.getRecipeDetails(id, true, ApiUtils.APIKEY);

        call.enqueue(new Callback<RecipeDetails>() {
            @Override
            public void onResponse(Call<RecipeDetails> call, Response<RecipeDetails> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                RecipeDetails recipeDetails = response.body();
                long rowsAffected = recipeProcessor.AddToFavorite(recipeDetails);
                if(rowsAffected > 0){
                    ImageView add_fav = (ImageView) add_to_fav_view;
                    add_fav.setImageResource(R.drawable.ic_action_favorite);
                    Toast.makeText(getApplicationContext(), "Added to Favorite", Toast.LENGTH_SHORT).show();
                }else if(rowsAffected == 0){
                    Toast.makeText(getApplicationContext(), "Not Added", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeDetails> call, Throwable t) {
                ApiFailure.onFailure(t, getApplicationContext());
            }
        });
    }


}