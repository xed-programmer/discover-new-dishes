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

public class RecentRecipeAdapter extends RecyclerView.Adapter<RecentRecipeAdapter.RecentRecipeVH> {
    private ArrayList<RecipeDetails> recipes;
    private Context context;
    private ArrayList<String> saved_data;
    private CLickedRecentItem cLickedRecentItem;

    public RecentRecipeAdapter(ArrayList<RecipeDetails> recipes, CLickedRecentItem cLickedRecentItem) {
        this.recipes = recipes;
        this.cLickedRecentItem = cLickedRecentItem;
    }

    public void setSavedData(ArrayList<String> saved_data){
        this.saved_data = saved_data;
    }
    @NonNull
    @Override
    public RecentRecipeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new RecentRecipeVH(LayoutInflater.from(context).inflate(R.layout.recipe_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentRecipeAdapter.RecentRecipeVH holder, int position) {
        RecipeDetails recipe = recipes.get(position);

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
                cLickedRecentItem.ClickedRecentRecipe(recipe.getId());
            }
        });
        if(saved_data.contains(String.valueOf(recipe.getId()))){
            holder.add_fav.setImageResource(R.drawable.ic_action_favorite);
        }else{
            holder.add_fav.setImageResource(R.drawable.ic_action_not_save);
        }
        holder.add_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cLickedRecentItem.ClickedAddToFavorite(recipe, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public interface CLickedRecentItem{
        public void ClickedRecentRecipe(int id);
        public void ClickedAddToFavorite(RecipeDetails recipe, View view);
    }
    public class RecentRecipeVH extends RecyclerView.ViewHolder {
        ImageView img, add_fav;
        TextView title, health, readyMinutes, servings, source;
        public RecentRecipeVH(@NonNull View itemView) {
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
