package com.casestudy.discovernewdishes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.casestudy.discovernewdishes.Adapter.FoodVideoAdapter;
import com.casestudy.discovernewdishes.Api.ApiClient;
import com.casestudy.discovernewdishes.Api.ApiFailure;
import com.casestudy.discovernewdishes.Api.ApiUtils;
import com.casestudy.discovernewdishes.Api.UserService;
import com.casestudy.discovernewdishes.Models.FoodVideo;
import com.casestudy.discovernewdishes.Models.FoodVideoArray;
import com.casestudy.discovernewdishes.businesslogic.FoodVideoProcessor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchVideoActivity extends AppCompatActivity implements FoodVideoAdapter.ClickedItem{

    LinearLayout layout_search_not_found;
    EditText et_search;
    ImageView iv_btn_search, search_img;
    RecyclerView rv_res_video;
    ProgressBar progressBar;
    FloatingActionButton fab_toTop;
    NestedScrollView nestedScrollView;
    FoodVideoAdapter foodVideoAdapter;
    UserService userService;
    FoodVideoArray foodVideoArray;
    LinearLayoutManager linearLayoutManager;
    FoodVideoProcessor foodVideoProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_video);

        foodVideoProcessor = new FoodVideoProcessor(this);
        layout_search_not_found = findViewById(R.id.layout_search_not_found);
        et_search = findViewById(R.id.et_search);
        search_img = findViewById(R.id.search_img);
        iv_btn_search = findViewById(R.id.btn_search);
        rv_res_video = findViewById(R.id.rv_res_video);
        fab_toTop = findViewById(R.id.fab_toTop);
        nestedScrollView = findViewById(R.id.nestedscrollview);
        progressBar = findViewById(R.id.progressbar);
        linearLayoutManager = new LinearLayoutManager(this);

        rv_res_video.setLayoutManager(linearLayoutManager);

        fab_toTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nestedScrollView.smoothScrollTo(0,0,1500);
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY > 0){
                    fab_toTop.setVisibility(View.VISIBLE);
                }else if(scrollY == 0){
                    fab_toTop.setVisibility(View.INVISIBLE);
                }
            }
        });
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH || i == keyEvent.KEYCODE_ENTER){
                    if(!et_search.getText().toString().isEmpty()){
                        layout_search_not_found.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        getRecipes();
                        hideKeyboard();
                        return true;
                    }else{
                        Toast.makeText(getApplicationContext(), "No Item To be Search", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        iv_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_search.getText().toString().isEmpty()){
                    layout_search_not_found.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    getRecipes();
                    hideKeyboard();
                }else{
                    Toast.makeText(getApplicationContext(), "No Item To be Search", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getRecipes(){
        userService = ApiClient.getUserService();
        Call<FoodVideoArray> call = userService.getFoodVideos(et_search.getText().toString(), 20, ApiUtils.APIKEY);
        call.enqueue(new Callback<FoodVideoArray>() {
            @Override
            public void onResponse(Call<FoodVideoArray> call, Response<FoodVideoArray> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),response.code(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                foodVideoArray = response.body();
                ArrayList<FoodVideo> videos = new ArrayList<>(Arrays.asList(foodVideoArray.getVideos()));
                if(videos.size() > 0){
                    foodVideoAdapter = new FoodVideoAdapter(videos, SearchVideoActivity.this);
                    foodVideoAdapter.setSavedVideo(foodVideoProcessor.PopulateSavedItem());
                    rv_res_video.setAdapter(foodVideoAdapter);
                }else{
                    layout_search_not_found.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<FoodVideoArray> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                layout_search_not_found.setVisibility(View.VISIBLE);
                ApiFailure.onFailure(t, getApplicationContext());
                return;
            }
        });
    }

    private void hideKeyboard(){
        InputMethodManager in = (InputMethodManager)getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
    }

    @Override
    public void ClickedVideo(String youtubeLink) {
        Uri uri = Uri.parse(youtubeLink);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    @Override
    public void ClickedAddToFavorite(FoodVideo video, View view) {
        ArrayList<String> saved_video = foodVideoProcessor.PopulateSavedItem();
        if(saved_video.contains(video.getYoutubeId())){
            int rowsAffected = foodVideoProcessor.RemoveToFavorite(video.getYoutubeId());
            if(rowsAffected > 0){
                ImageView add_fav = (ImageView) view;
                add_fav.setImageResource(R.drawable.ic_action_not_save);
                Toast.makeText(getApplicationContext(), "Remove to Favorite", Toast.LENGTH_SHORT).show();
            }else if(rowsAffected == 0){
                Toast.makeText(getApplicationContext(), "Not Remove", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }else {
            long rowsAffected = foodVideoProcessor.AddToFavorite(video);
            if(rowsAffected > 0){
                ImageView add_fav = (ImageView) view;
                add_fav.setImageResource(R.drawable.ic_action_favorite);
                Toast.makeText(getApplicationContext(), "Added to Favorite", Toast.LENGTH_SHORT).show();
            }else if(rowsAffected == 0){
                Toast.makeText(getApplicationContext(), "Not Added", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}