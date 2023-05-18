package com.casestudy.discovernewdishes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.casestudy.discovernewdishes.Models.RecipeDetails;
import com.casestudy.discovernewdishes.R;

import java.util.ArrayList;

public class HomeRecentRecipeAdapter extends RecyclerView.Adapter<HomeRecentRecipeAdapter.RecentRecipeVH> {
    private ArrayList<RecipeDetails> recipes;
    private Context context;
    private ClickedRecentRecipe clickedRecentRecipe;

    public HomeRecentRecipeAdapter(ArrayList<RecipeDetails> recipes, ClickedRecentRecipe clickedRecentRecipe) {
        this.recipes = recipes;
        this.clickedRecentRecipe = clickedRecentRecipe;
    }

    @NonNull
    @Override
    public RecentRecipeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new RecentRecipeVH(LayoutInflater.from(context).inflate(R.layout.meal_types_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecentRecipeAdapter.RecentRecipeVH holder, int position) {
        RecipeDetails recipe = recipes.get(position);
        holder.title.setText(recipe.getTitle());
        Glide.with(context).load(recipe.getImgUrl())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedRecentRecipe.ClickedRecent(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public interface ClickedRecentRecipe{
        public void ClickedRecent(RecipeDetails recipe);
    }
    public class RecentRecipeVH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;
        public RecentRecipeVH(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.iv_img_meal);
            title = itemView.findViewById(R.id.tv_meal);
        }
    }
}
