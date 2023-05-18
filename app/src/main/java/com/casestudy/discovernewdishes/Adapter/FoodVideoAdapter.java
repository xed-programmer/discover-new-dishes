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
import com.casestudy.discovernewdishes.Models.FoodVideo;
import com.casestudy.discovernewdishes.R;

import java.util.ArrayList;

public class FoodVideoAdapter extends RecyclerView.Adapter<FoodVideoAdapter.FoodVideoVH> {

    private String youtubeBaseUrl = "https://www.youtube.com/watch?v=";
    private ArrayList<FoodVideo> foodVideos;
    private ArrayList<String> saved_video;
    private Context context;
    private ClickedItem clickedItem;

    public FoodVideoAdapter(ArrayList<FoodVideo> foodVideos, ClickedItem clickedItem) {
        this.foodVideos = foodVideos;
        this.clickedItem = clickedItem;
    }

    public void setSavedVideo(ArrayList<String> saved_video){
        this.saved_video = saved_video;
    }
    @NonNull
    @Override
    public FoodVideoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new FoodVideoVH(LayoutInflater.from(context).inflate(R.layout.video_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodVideoAdapter.FoodVideoVH holder, int position) {

        FoodVideo video = foodVideos.get(position);
        holder.title.setText(video.getTitle());
        holder.views.setText(String.valueOf(video.getViews()) + " views");
        Glide.with(context).load(video.getThumbnail())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickedVideo(youtubeBaseUrl + video.getYoutubeId());
            }
        });

        if(saved_video.contains(video.getYoutubeId())){
            holder.add_fav.setImageResource(R.drawable.ic_action_favorite);
        }else{
            holder.add_fav.setImageResource(R.drawable.ic_action_not_save);
        }
        holder.add_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickedAddToFavorite(video, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodVideos.size();
    }

    public interface ClickedItem{
        public void ClickedVideo(String youtubeLink);
        public void ClickedAddToFavorite(FoodVideo video, View view);
    }

    public class FoodVideoVH extends RecyclerView.ViewHolder {
        ImageView img, add_fav;
        TextView title, views;
        public FoodVideoVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.iv_thumbnail);
            add_fav = itemView.findViewById(R.id.add_fav);
            title = itemView.findViewById(R.id.et_title);
            views = itemView.findViewById(R.id.et_views);
        }
    }
}
