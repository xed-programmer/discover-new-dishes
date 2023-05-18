package com.casestudy.discovernewdishes.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.casestudy.discovernewdishes.Adapter.FavoriteFoodVideoAdapter;
import com.casestudy.discovernewdishes.Adapter.FoodVideoAdapter;
import com.casestudy.discovernewdishes.Models.FoodVideo;
import com.casestudy.discovernewdishes.R;
import com.casestudy.discovernewdishes.businesslogic.FoodVideoProcessor;

import java.util.ArrayList;

public class FoodVideosFragment extends Fragment implements FavoriteFoodVideoAdapter.ClickedItem{

    FoodVideoProcessor foodVideoProcessor;
    RecyclerView rv_food;
    FavoriteFoodVideoAdapter adapter;
    ArrayList<FoodVideo> foodVideos;
    ArrayList<String> saved_videos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_videos, container, false);
        foodVideoProcessor = new FoodVideoProcessor(container.getContext().getApplicationContext());
        rv_food = view.findViewById(R.id.rv_foodVideos);

        foodVideos = getFoodVideos();
        adapter = new FavoriteFoodVideoAdapter(foodVideos, this);
        rv_food.setLayoutManager(new LinearLayoutManager(container.getContext().getApplicationContext()));
        rv_food.setAdapter(adapter);
        return view;
    }

    private ArrayList<FoodVideo> getFoodVideos() {
        return foodVideoProcessor.PopulateData();
    }

    @Override
    public void ClickedVideo(String youtubeLink) {
        Uri uri = Uri.parse(youtubeLink);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }


    @Override
    public void ClickedRemoveToFavorite(FoodVideo foodVideo, int position) {
        int rowsAffected = foodVideoProcessor.RemoveToFavorite(foodVideo.getYoutubeId());
        if(rowsAffected > 0){
            foodVideos.remove(position);
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