package com.casestudy.discovernewdishes.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.casestudy.discovernewdishes.R;

public class FavoriteFragment extends Fragment {

    TextView et_recipes, et_foodvideos, et_recent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        et_recipes = view.findViewById(R.id.et_recipes);
        et_foodvideos = view.findViewById(R.id.et_foodvideos);
        et_recent = view.findViewById(R.id.et_recent);

        et_recipes.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                showFragment(new RecipesFragment());
                et_recipes.setTextColor(R.color.black);
                et_foodvideos.setTextColor(R.color.material_on_primary_disabled);
                et_recent.setTextColor(R.color.material_on_primary_disabled);
            }
        });

        et_foodvideos.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                showFragment(new FoodVideosFragment());
                et_recipes.setTextColor(R.color.material_on_primary_disabled);
                et_foodvideos.setTextColor(R.color.black);
                et_recent.setTextColor(R.color.material_on_primary_disabled);
            }
        });

        et_recent.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                showFragment(new RecentRecipeFragment());
                et_recipes.setTextColor(R.color.material_on_primary_disabled);
                et_foodvideos.setTextColor(R.color.material_on_primary_disabled);
                et_recent.setTextColor(R.color.black);
            }
        });

        showFragment(new RecipesFragment());
        et_recipes.setTextColor(R.color.colorBlack);
        et_foodvideos.setTextColor(R.color.material_on_background_disabled);
        return view;
    }

    private void showFragment(Fragment fragment){
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.fav_framelayout, fragment);
        ft.commit();
    }
}