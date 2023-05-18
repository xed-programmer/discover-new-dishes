   package com.casestudy.discovernewdishes.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.casestudy.discovernewdishes.Adapter.HomeRecentRecipeAdapter;
import com.casestudy.discovernewdishes.Adapter.MealTypeAdapter;
import com.casestudy.discovernewdishes.Api.ApiClient;
import com.casestudy.discovernewdishes.Api.ApiUtils;
import com.casestudy.discovernewdishes.Api.UserService;
import com.casestudy.discovernewdishes.Models.RecipeDetails;
import com.casestudy.discovernewdishes.Models.Trivia;
import com.casestudy.discovernewdishes.R;
import com.casestudy.discovernewdishes.RecipeDetailsActivity;
import com.casestudy.discovernewdishes.SearchActivity;
import com.casestudy.discovernewdishes.businesslogic.RecipeProcessor;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements MealTypeAdapter.ClickedItem, HomeRecentRecipeAdapter.ClickedRecentRecipe {
    View view;
    TextView tv_trivia;
    RecyclerView rv_mealTypes, rv_recent;
    LinearLayout trivia_layout, recent_layout;
    UserService userService;
    MealTypeAdapter mMealTypeAdapter;
    HomeRecentRecipeAdapter recentAdapter;
    ArrayList<RecipeDetails> recent_recipes;
    RecipeProcessor processor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        processor = new RecipeProcessor(container.getContext());
        rv_mealTypes = view.findViewById(R.id.layout_meal_types);
        tv_trivia = view.findViewById(R.id.food_trivia);
        trivia_layout = view.findViewById(R.id.layout_trivia);
        recent_layout = view.findViewById(R.id.layout_recent);
        rv_recent = view.findViewById(R.id.rv_recent_home);

        getTrivia();
        mMealTypeAdapter = new MealTypeAdapter(this::ClickedMealTypes);
        recent_recipes = processor.PopulateRecentData();
        if(recent_recipes.size() > 0){
            recent_layout.setVisibility(View.VISIBLE);
            recentAdapter = new HomeRecentRecipeAdapter(recent_recipes, this::ClickedRecent);

            LinearLayoutManager mMealLayoutManager1 = new LinearLayoutManager(container.getContext().getApplicationContext());
            mMealLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);

            rv_recent.setLayoutManager(mMealLayoutManager1);
            rv_recent.setItemAnimator(new DefaultItemAnimator());
            rv_recent.setAdapter(recentAdapter);
        }else{
            recent_layout.setVisibility(View.GONE);
        }

        LinearLayoutManager mMealLayoutManager = new LinearLayoutManager(container.getContext().getApplicationContext());
        mMealLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rv_mealTypes.setLayoutManager(mMealLayoutManager);
        rv_mealTypes.setItemAnimator(new DefaultItemAnimator());
        rv_mealTypes.setAdapter(mMealTypeAdapter);
        return view;
    }

    @Override
    public void ClickedMealTypes(int imgRes, String name) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("image", imgRes);
        intent.putExtra("mealType", name);
        startActivity(intent);
    }

    public void getTrivia(){
        userService = ApiClient.getUserService();
        Call<Trivia> call = userService.getFoodTrivia(ApiUtils.APIKEY);

        call.enqueue(new Callback<Trivia>() {
            @Override
            public void onResponse(Call<Trivia> call, Response<Trivia> response) {
                if(response.isSuccessful()){
                    Trivia trivia = response.body();
                    tv_trivia.setText(trivia.getText());
                    trivia_layout.setVisibility(View.VISIBLE);
                    return;
                }
                trivia_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Trivia> call, Throwable t) {
                trivia_layout.setVisibility(View.GONE);
                return;
            }
        });
    }

    @Override
    public void ClickedRecent(RecipeDetails recipe) {
        Intent intent = new Intent(getContext().getApplicationContext(), RecipeDetailsActivity.class);
        intent.putExtra("recipeDetails", recipe);
        startActivity(intent);
    }
}