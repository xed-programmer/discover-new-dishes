 package com.casestudy.discovernewdishes.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.casestudy.discovernewdishes.Models.RecipeNutrients;
import com.casestudy.discovernewdishes.R;

import java.util.ArrayList;

public class RecipeNutrientAdapter extends RecyclerView.Adapter<RecipeNutrientAdapter.RecipeNutrientVH> {

    private Context context;
    private ArrayList<RecipeNutrients> recipeNutrients;
    private ArrayList<String> saved_recipe;
    private ClickedItem clickedItem;

    public RecipeNutrientAdapter(ArrayList<RecipeNutrients> recipeNutrients, ClickedItem clickedItem) {
        this.recipeNutrients = recipeNutrients;
        this.clickedItem = clickedItem;
    }

    public void setSavedRecipe(ArrayList<String> saved_recipe){
        this.saved_recipe = saved_recipe;
    }

    @NonNull
    @Override
    public RecipeNutrientVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new RecipeNutrientVH(LayoutInflater.from(context).inflate(R.layout.nutrient_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeNutrientAdapter.RecipeNutrientVH holder, int position) {
        RecipeNutrients recipe = recipeNutrients.get(position);

        holder.title.setText(recipe.getTitle());
        holder.carbs.setText(recipe.getCarbs());
        holder.calories.setText(recipe.getCalories());
        holder.cholesterol.setText(recipe.getCholesterol());
        holder.fat.setText(recipe.getFat());
        holder.potassium.setText(recipe.getPotassium());
        holder.protein.setText(recipe.getProtein());
        holder.sugar.setText(recipe.getSugar());

        if(saved_recipe.contains(String.valueOf(recipe.getId()))){
            holder.add_fav.setImageResource(R.drawable.ic_action_favorite);
        }else{
            holder.add_fav.setImageResource(R.drawable.ic_action_not_save);
        }

        Glide.with(context).load(recipe.getImage())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .into(holder.img);

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickedRecipe(recipe.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeNutrients.size();
    }

    public interface ClickedItem{
        public void ClickedRecipe(int id);
        public void ClickedAddToFavorite(RecipeNutrients recipe, View view);
        public void ClickedAddToSchedule(RecipeNutrients recipe);
    }

    public class RecipeNutrientVH extends RecyclerView.ViewHolder {
        ImageView img, add_fav;
        TextView title, carbs, calories, cholesterol, fat, potassium, protein, sugar, add_sched;
        public RecipeNutrientVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_recipe);
            add_fav = itemView.findViewById(R.id.add_fav);
            title = itemView.findViewById(R.id.title_recipe);
            carbs = itemView.findViewById(R.id.carbs_nutri);
            calories = itemView.findViewById(R.id.calories_nutri);
            cholesterol = itemView.findViewById(R.id.cholesterol_nutri);
            fat = itemView.findViewById(R.id.fat_nutri);
            potassium = itemView.findViewById(R.id.potassium_nutri);
            protein = itemView.findViewById(R.id.protein_nutri);
            sugar = itemView.findViewById(R.id.sugar_nutri);
            add_sched = itemView.findViewById(R.id.et_add_schedule);
        }
    }
}
