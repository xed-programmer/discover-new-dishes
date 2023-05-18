package com.casestudy.discovernewdishes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.casestudy.discovernewdishes.R;

public class MealTypeAdapter extends RecyclerView.Adapter<MealTypeAdapter.MealViewHolder> {
    int[] imgMealTypes = {R.drawable.maincourse, R.drawable.sidedish, R.drawable.dessert, R.drawable.appetizer, R.drawable.breakfast, R.drawable.soup, R.drawable.beverages, R.drawable.sauce, R.drawable.marinade};
    String[] nameMealTypes = {"Main Course", "Side Dish", "Dessert", "Appetizer", "Breakfast", "Soup", "Beverage", "Sauce", "Marinade"};

    private ClickedItem clickedItem;
    Context context;

    public MealTypeAdapter(ClickedItem clickedItem) {
        this.clickedItem = clickedItem;
    }

    @NonNull
    @Override
    public MealTypeAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MealTypeAdapter.MealViewHolder(LayoutInflater.from(context).inflate(R.layout.meal_types_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MealTypeAdapter.MealViewHolder holder, int position) {

        holder.img.setImageResource(imgMealTypes[position]);
        holder.meal.setText(nameMealTypes[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickedMealTypes(imgMealTypes[position], nameMealTypes[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgMealTypes.length;
    }

    public interface ClickedItem{
        public void ClickedMealTypes(int imgRes, String name);
    }
    public class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView meal;
        public MealViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.iv_img_meal);
            meal = itemView.findViewById(R.id.tv_meal);
        }
    }
}
