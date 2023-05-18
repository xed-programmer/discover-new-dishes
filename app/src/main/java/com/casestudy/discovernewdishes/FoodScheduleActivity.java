package com.casestudy.discovernewdishes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.casestudy.discovernewdishes.Api.ApiClient;
import com.casestudy.discovernewdishes.Api.ApiFailure;
import com.casestudy.discovernewdishes.Api.ApiUtils;
import com.casestudy.discovernewdishes.Api.UserService;
import com.casestudy.discovernewdishes.Models.FoodSchedule;
import com.casestudy.discovernewdishes.Models.RecipeDetails;
import com.casestudy.discovernewdishes.businesslogic.FoodScheduleProcessor;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodScheduleActivity extends AppCompatActivity {

    ImageView img;
    TextView title, health, readyMinutes, servings, source;
    Button btn_add_schedule;
    DatePicker datePicker;
    private int recipe_id;
    private RecipeDetails recipe;
    private FoodScheduleProcessor processor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_schedule);

        processor = new FoodScheduleProcessor(this);
        recipe_id = getIntent().getExtras().getInt("recipeId", 0);

        if(recipe_id != 0){
            getRecipeById(recipe_id);
        }
        img = findViewById(R.id.img_recipe);
        title = findViewById(R.id.title_recipe);
        health = findViewById(R.id.health_recipe);
        servings = findViewById(R.id.servings_recipe);
        readyMinutes = findViewById(R.id.readyMinutes_recipe);
        source = findViewById(R.id.source_recipe);
        datePicker = findViewById(R.id.dp_schedule);
        btn_add_schedule = findViewById(R.id.btn_add_schedule);

        btn_add_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_add_schedule.setEnabled(false);

                int year = datePicker.getYear();
                int month = datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();
                String str_date = String.format("%d-%02d-%02d", year, month,day);

                FoodSchedule schedule = new FoodSchedule();
                Gson gson = new Gson();
                String api = gson.toJson(recipe);
                schedule.setRecipe_id(String.valueOf(recipe.getId()));
                schedule.setDate_sched(str_date);
                schedule.setApi(api);

                ArrayList<FoodSchedule> schedules = processor.PopulateSchedule();
                int count = schedules.size();
                if(schedules.size()>0){
                    for(FoodSchedule s : schedules){
                        if(!s.getDate_sched().equals(schedule.getDate_sched()) || !s.getRecipe_id().equals(schedule.getRecipe_id())){
                            count--;
                        }else{
                            Toast.makeText(getApplicationContext(), "The data is already been added", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
                if(schedules.size()==0 || count == 0){
                    long rowsAffected = processor.AddToSchedule(schedule);
                    if(rowsAffected > 0){
                        Toast.makeText(getApplicationContext(), "Add Successfully", Toast.LENGTH_SHORT).show();
                    }
                }

                btn_add_schedule.setEnabled(true);
            }
        });
    }

    private void getRecipeById(int id) {
        UserService userService = ApiClient.getUserService();
        Call<RecipeDetails> call = userService.getRecipeDetails(id, true, ApiUtils.APIKEY);

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
                ApiFailure.onFailure(t, getApplicationContext());
            }
        });
    }

    private void initData(){
        title.setText(recipe.getTitle());
        health.setText(String.valueOf(recipe.getHealthScore()));
        readyMinutes.setText(String.valueOf(recipe.getReadyInMinutes()));
        source.setText(recipe.getSourceName());
        servings.setText(String.valueOf(recipe.getServings()));
        Glide.with(this).load(recipe.getImgUrl())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .into(img);
    }
}