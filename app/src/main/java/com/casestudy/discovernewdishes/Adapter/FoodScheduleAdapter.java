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
import com.casestudy.discovernewdishes.Models.FoodSchedule;
import com.casestudy.discovernewdishes.Models.RecipeDetails;
import com.casestudy.discovernewdishes.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class FoodScheduleAdapter extends RecyclerView.Adapter<FoodScheduleAdapter.FoodScheduleVH> {

    private ArrayList<FoodSchedule> schedules;
    private Context context;
    private ClickedItem clickedItem;

    public FoodScheduleAdapter(ArrayList<FoodSchedule> schedules, ClickedItem clickedItem) {
        this.schedules = schedules;
        this.clickedItem = clickedItem;
    }

    @NonNull
    @Override
    public FoodScheduleVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new FoodScheduleVH(LayoutInflater.from(context).inflate(R.layout.row_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodScheduleAdapter.FoodScheduleVH holder, int position) {

        FoodSchedule schedule = schedules.get(position);
        Gson gson = new Gson();
        RecipeDetails recipe = gson.fromJson(schedule.getApi(), RecipeDetails.class);
        holder.et_date.setText(schedule.getDate_sched());
        holder.title.setText(recipe.getTitle());
        Glide.with(context).load(recipe.getImgUrl())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .into(holder.img);

        holder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem. ClickedRemoveToSchedule(schedule, position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickedSchedule(schedule);
            }
        });
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public interface ClickedItem{
        public void ClickedSchedule(FoodSchedule schedule);
        public void ClickedRemoveToSchedule(FoodSchedule schedule, int position);
    }

    public class FoodScheduleVH extends RecyclerView.ViewHolder {
        ImageView img, iv_remove;
        TextView title, et_date;
        public FoodScheduleVH(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_recipe);
            iv_remove = itemView.findViewById(R.id.remove_sched);
            title = itemView.findViewById(R.id.title_recipe);
            et_date = itemView.findViewById(R.id.et_date_sched);
        }
    }
}
