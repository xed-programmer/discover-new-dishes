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
import com.casestudy.discovernewdishes.Api.ApiUtils;
import com.casestudy.discovernewdishes.Models.Ingredient;
import com.casestudy.discovernewdishes.R;

import java.lang.annotation.Target;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientVH> {
    private List<Ingredient> ingredients;
    private Context context;

    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new IngredientAdapter.IngredientVH(LayoutInflater.from(context).inflate(R.layout.ingredient_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientVH holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        holder.name.setText(ingredient.getName());
        holder.amount.setText(String.valueOf(ingredient.getAmount()));
        holder.units.setText(ingredient.getUnit());
        Glide.with(context).load(ApiUtils.INGREDIENTIMAGEURL + ingredient.getImage())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class IngredientVH extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name, amount, units;
        public IngredientVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_ingredient);
            name = itemView.findViewById(R.id.name_ingredient);
            amount = itemView.findViewById(R.id.amount_ingredient);
            units = itemView.findViewById(R.id.units_ingredient);
        }
    }
}
