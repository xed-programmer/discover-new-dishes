package com.casestudy.discovernewdishes;

import android.content.ContentValues;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.casestudy.discovernewdishes.Adapter.IngredientAdapter;
import com.casestudy.discovernewdishes.Api.ApiClient;
import com.casestudy.discovernewdishes.Api.ApiUtils;
import com.casestudy.discovernewdishes.Api.UserService;
import com.casestudy.discovernewdishes.Models.Ingredient;
import com.casestudy.discovernewdishes.Models.Nutrient;
import com.casestudy.discovernewdishes.Models.NutrientsArray;
import com.casestudy.discovernewdishes.Models.RecipeDetails;
import com.casestudy.discovernewdishes.businesslogic.RecentProcessor;
import com.casestudy.discovernewdishes.sqldataaccess.SQLiteDatabase;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailsActivity extends AppCompatActivity {


    ImageView img;
    TextView title, health, readyMinutes, source, servings, summary, instruction, nutri;
    RecyclerView rv_ingredients;
    int recipeId;
    ArrayList<NutrientsArray> nutrients;
    RecipeDetails recipe = null;
    UserService userService;
    private RecentProcessor processor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        recipeId = getIntent().getExtras().getInt("recipeId",0);
        recipe = getIntent().getParcelableExtra("recipeDetails");
        processor = new RecentProcessor(this);
        
        img = findViewById(R.id.view_img_recipe);
        title = findViewById(R.id.view_title_recipe);
        health = findViewById(R.id.view_health_recipe);
        readyMinutes = findViewById(R.id.view_readyMinutes_recipe);
        servings = findViewById(R.id.view_servings_recipe);
        source = findViewById(R.id.view_source_recipe);
        summary = findViewById(R.id.view_summary_recipe);
        instruction = findViewById(R.id.view_instruction_recipe);
        nutri = findViewById(R.id.tv_nutri);
        rv_ingredients = findViewById(R.id.rv_ingredient_recipe);
        rv_ingredients.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if(recipeId != 0)
            getRecipeDetails();
        if(recipe != null)
            initData();
    }

    private void getRecipeDetails(){
        userService = ApiClient.getUserService();
        Call<RecipeDetails> call = userService.getRecipeDetails(recipeId, true, ApiUtils.APIKEY);

        call.enqueue(new Callback<RecipeDetails>() {
            @Override
            public void onResponse(Call<RecipeDetails> call, Response<RecipeDetails> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                recipe = response.body();
                initData();
            }

            @Override
            public void onFailure(Call<RecipeDetails> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initData(){
        processor.AddToRecent(recipe);

        title.setText(recipe.getTitle());
        health.setText(String.valueOf(recipe.getHealthScore()));
        readyMinutes.setText("Ready In " + recipe.getReadyInMinutes() + " minutes");
        servings.setText(String.valueOf(recipe.getServings()));
        source.setText(recipe.getSourceName());
        Glide.with(getApplicationContext()).load(recipe.getImgUrl())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .into(img);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            if(recipe.getSummary() != null)
                summary.setText(Html.fromHtml(recipe.getSummary(), Html.FROM_HTML_MODE_LEGACY));
            if(recipe.getInstruction() != null)
                instruction.setText(Html.fromHtml(recipe.getInstruction(), Html.FROM_HTML_MODE_LEGACY));
        }else{
            if(recipe.getSummary() != null)
                summary.setText(Html.fromHtml(recipe.getSummary()));
            if(recipe.getInstruction() != null)
                instruction.setText(Html.fromHtml(recipe.getInstruction()));
        }

        List<Ingredient> ingredients = Arrays.asList(recipe.getIngredients());
        IngredientAdapter ingredientAdapter = new IngredientAdapter(ingredients);
        rv_ingredients.setAdapter(ingredientAdapter);

        ArrayList<Nutrient> nutrients = new ArrayList<>(Arrays.asList(recipe.getNutrients().getNutrients()));
        String values = "";
        for (Nutrient n : nutrients){
            String str = n.toString() + "\n";
            values+=str;
        }
        nutri.setText(values);
    }
}