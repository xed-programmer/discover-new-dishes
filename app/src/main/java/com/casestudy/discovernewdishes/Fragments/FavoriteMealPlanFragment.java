package com.casestudy.discovernewdishes.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.casestudy.discovernewdishes.Adapter.RecipeNutrientAdapter;
import com.casestudy.discovernewdishes.R;
import com.casestudy.discovernewdishes.businesslogic.NutrientsProcessor;

public class FavoriteMealPlanFragment extends Fragment {

    RecyclerView rv_res;
    NutrientsProcessor processor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_meal_plan, container, false);
        rv_res = view.findViewById(R.id.rv_res);
        rv_res.setLayoutManager(new LinearLayoutManager(container.getContext()));
        return view;
    }
}