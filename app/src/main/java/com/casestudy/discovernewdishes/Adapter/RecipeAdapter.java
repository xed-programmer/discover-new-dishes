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
import com.casestudy.discovernewdishes.Models.RecipeInfo;
import com.casestudy.discovernewdishes.Models.RecipeNutrients;
import com.casestudy.discovernewdishes.R;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<RecipeInfo> recipeInfos;
    private Context context;
    private ClickedItem clickedItem;
    private ArrayList<String> saved_recipe = new ArrayList<>();

    public RecipeAdapter(ArrayList<RecipeInfo> recipeInfos, ClickedItem clickedItem) {
        this.recipeInfos = recipeInfos;
        this.clickedItem = clickedItem;
    }

    public void setSavedRecipe(ArrayList<String> saved_recipe){
        this.saved_recipe = saved_recipe;
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new RecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder holder, int position) {

        RecipeInfo recipe = recipeInfos.get(position);

        holder.title.setText(recipe.getTitle());
        holder.health.setText(String.valueOf(recipe.getHealthScore()));
        holder.readyMinutes.setText(String.valueOf(recipe.getReadyInMinutes()));
        holder.source.setText(recipe.getSourceName());
        holder.servings.setText(String.valueOf(recipe.getServings()));
        Glide.with(context).load(recipe.getImgUrl())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickedRecipe(recipe.getId());
            }
        });
        if(saved_recipe.contains(String.valueOf(recipe.getId()))){
            holder.add_fav.setImageResource(R.drawable.ic_action_favorite);
        }else{
            holder.add_fav.setImageResource(R.drawable.ic_action_not_save);
        }

        holder.add_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickedAddToFavorite(recipe, view);
            }
        });

        holder.add_sched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedItem.ClickedAddToSchedule(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeInfos.size();
    }

    public interface ClickedItem{
        public void ClickedRecipe(int recipeId);
        public void ClickedAddToFavorite(RecipeInfo recipeInfo, View v);
        public void ClickedAddToSchedule(RecipeInfo recipe);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder{
        ImageView img, add_fav;
        TextView title, health, readyMinutes, servings, source, add_sched;

        public RecipeViewHolder(@NonNull View itemView) {

            super(itemView);
            img = itemView.findViewById(R.id.img_recipe);
            add_fav = itemView.findViewById(R.id.add_fav);
            title = itemView.findViewById(R.id.title_recipe);
            health = itemView.findViewById(R.id.health_recipe);
            servings = itemView.findViewById(R.id.servings_recipe);
            readyMinutes = itemView.findViewById(R.id.readyMinutes_recipe);
            source = itemView.findViewById(R.id.source_recipe);
            add_sched = itemView.findViewById(R.id.et_add_schedule);
        }
    }
}
