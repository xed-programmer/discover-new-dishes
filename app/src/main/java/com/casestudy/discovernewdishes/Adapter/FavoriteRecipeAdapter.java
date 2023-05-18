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
import com.casestudy.discovernewdishes.R;

import java.lang.annotation.Target;
import java.util.ArrayList;

public class FavoriteRecipeAdapter extends RecyclerView.Adapter<FavoriteRecipeAdapter.FavoriteRecipeVH> {

    private ArrayList<RecipeDetails> recipes;
    private Context context;
    private ClickedItem clickedItem;

    public FavoriteRecipeAdapter(ArrayList<RecipeDetails> recipes, ClickedItem clickedItem) {
        this.recipes = recipes;
        this.clickedItem = clickedItem;
    }

    @NonNull
    @Override
    public FavoriteRecipeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new FavoriteRecipeVH(LayoutInflater.from(context).inflate(R.layout.recipe_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRecipeAdapter.FavoriteRecipeVH holder, int position) {

        RecipeDetails recipe = recipes.get(position);

        holder.title.setText(recipe.getTitle());
        holder.health.setText(String.valueOf(recipe.getHealthScore()));
        holder.readyMinutes.setText(String.valueOf(recipe.getReadyInMinutes()));
        holder.source.setText(recipe.getSourceName());
        holder.servings.setText(String.valueOf(recipe.getServings()));
        holder.add_fav.setImageResource(R.drawable.ic_action_favorite);
        Glide.with(context).load(recipe.getImgUrl())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickRecipe(recipe);
            }
        });
        holder.add_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickRemoveToFavorite(recipe, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public interface ClickedItem{
        public void ClickRecipe(RecipeDetails recipeDetails);
        public void ClickRemoveToFavorite(RecipeDetails recipeDetails, int position);
    }

    public class FavoriteRecipeVH extends RecyclerView.ViewHolder {
        ImageView img, add_fav;
        TextView title, health, readyMinutes, servings, source;
        public FavoriteRecipeVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_recipe);
            add_fav = itemView.findViewById(R.id.add_fav);
            title = itemView.findViewById(R.id.title_recipe);
            health = itemView.findViewById(R.id.health_recipe);
            servings = itemView.findViewById(R.id.servings_recipe);
            readyMinutes = itemView.findViewById(R.id.readyMinutes_recipe);
            source = itemView.findViewById(R.id.source_recipe);
        }
    }
}
