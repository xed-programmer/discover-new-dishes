package com.casestudy.discovernewdishes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.FragmentContainer;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.casestudy.discovernewdishes.Fragments.*;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    ImageView search_bar;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        search_bar = findViewById(R.id.search_btn);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                intent.putExtra("image", R.drawable.food_bg);
                intent.putExtra("mealType", "Any");
                startActivity(intent);
            }
        });

//        if(savedInstanceState == null){
//            showFragments(new HomeFragment());
//            navigationView.setCheckedItem(R.id.nav_home);
//        }
        showFragments(new HomeFragment());
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.nav_home)
            showFragments(new HomeFragment());
        if(id == R.id.nav_saved)
            showFragments(new FavoriteFragment());
        if(id == R.id.nav_video)
            startActivity(new Intent(HomeActivity.this, SearchVideoActivity.class));
        if(id == R.id.nav_meal_plan)
            showFragments(new MealPlanFragment());
        if(id == R.id.nav_meal_schedule)
            showFragments(new FoodScheduleFragment());
        if(id == R.id.developers)
            showFragments(new DevProfileFragment());

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragments(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else if(count <= 0)
            super.onBackPressed();
        else
            getSupportFragmentManager().popBackStack();
    }
}