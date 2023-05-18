package com.casestudy.discovernewdishes.Api;

import com.casestudy.discovernewdishes.Models.FoodVideoArray;
import com.casestudy.discovernewdishes.Models.RecipeDetails;
import com.casestudy.discovernewdishes.Models.RecipeInfoArray;
import com.casestudy.discovernewdishes.Models.RecipeNutrients;
import com.casestudy.discovernewdishes.Models.Trivia;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("recipes/complexSearch")
    Call<RecipeInfoArray> getRecipes(@Query("query") String query, @Query("addRecipeInformation") boolean addRecipeInfo, @Query("number") int number, @Query("apiKey") String apiKey);

    @GET("recipes/complexSearch")
    Call<RecipeInfoArray> getMealTypeRecipes(@Query("query") String query, @Query("type") String mealType, @Query("addRecipeInformation") boolean addRecipeInfo, @Query("number") int number, @Query("apiKey") String apiKey);

    @GET("food/videos/search")
    Call<FoodVideoArray> getFoodVideos(@Query("query") String query, @Query("number") int number, @Query("apiKey") String apiKey);
    @GET("recipes/findByNutrients")
    Call<RecipeNutrients[]> getRecipeByNutrients(@Query("minCarbs") int minCarbs, @Query("maxCarbs") int maxCarbs, @Query("minCalories") int minCalories, @Query("maxCalories") int maxCalories,
    @Query("minProtein") int minProtein, @Query("maxProtein") int maxProtein, @Query("minFat") int minFat, @Query("maxFat") int maxFat,
    @Query("minCholesterol") int minCholesterol, @Query("maxCholesterol") int maxCholesterol, @Query("minPotassium") int minPotassium, @Query("maxPotassium") int maxPotassium,
    @Query("minSugar") int minSugar, @Query("maxSugar") int maxSugar, @Query("number") int number, @Query("apiKey") String apiKey);

    @GET("recipes/{id}/information")
    Call<RecipeDetails> getRecipeDetails(@Path("id") int id,@Query("includeNutrition") boolean includeNutrition, @Query("apiKey") String apiKey);

    @GET("food/trivia/random")
    Call<Trivia> getFoodTrivia(@Query("apiKey") String apiKey);
}
