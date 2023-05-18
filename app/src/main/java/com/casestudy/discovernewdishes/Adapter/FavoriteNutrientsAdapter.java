package com.casestudy.discovernewdishes.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteNutrientsAdapter extends RecyclerView.Adapter<FavoriteNutrientsAdapter.FavoriteNutrientsVH> {


    @NonNull
    @Override
    public FavoriteNutrientsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteNutrientsAdapter.FavoriteNutrientsVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class FavoriteNutrientsVH extends RecyclerView.ViewHolder {
        public FavoriteNutrientsVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
