package com.casestudy.discovernewdishes.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.casestudy.discovernewdishes.Adapter.FoodScheduleAdapter;
import com.casestudy.discovernewdishes.Models.FoodSchedule;
import com.casestudy.discovernewdishes.R;
import com.casestudy.discovernewdishes.SearchActivity;
import com.casestudy.discovernewdishes.businesslogic.FoodScheduleProcessor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FoodScheduleFragment extends Fragment implements FoodScheduleAdapter.ClickedItem {

    FloatingActionButton fab_add_sched;
    RecyclerView rv_schedules;
    FoodScheduleAdapter adapter;
    ArrayList<FoodSchedule> schedules;
    FoodScheduleProcessor processor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_schedule, container, false);

        processor = new FoodScheduleProcessor(container.getContext().getApplicationContext());
        fab_add_sched = view.findViewById(R.id.fab_add_sched);
        rv_schedules = view.findViewById(R.id.rv_schedules);

        schedules = processor.PopulateSchedule();
        adapter = new FoodScheduleAdapter(schedules, this);
        rv_schedules.setLayoutManager(new LinearLayoutManager(container.getContext().getApplicationContext()));
        rv_schedules.setAdapter(adapter);

        initView();
        return view;
    }

    private void initView(){
        fab_add_sched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.frame_layout, new MealPlanFragment());
//                ft.addToBackStack(null);
//                ft.commit();
                Intent intent = new Intent(getContext().getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void ClickedSchedule(FoodSchedule schedule) {
        Toast.makeText(getContext().getApplicationContext(), "Item clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ClickedRemoveToSchedule(FoodSchedule schedule, int position) {
        AlertDialog.Builder alrt = new AlertDialog.Builder(getContext());
                alrt.setTitle("Food Schedule").setCancelable(false)
                .setMessage("Are you sure to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int rowsAffected = processor.RemoveSchedule(schedule.getId());
                        if(rowsAffected>0){
                            schedules.remove(schedule);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getContext().getApplicationContext(), "Remove Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else if (rowsAffected == 0){
                            Toast.makeText(getContext().getApplicationContext(), "No item removed", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getContext().getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null);

        AlertDialog dialog = alrt.create();
        dialog.show();
        Button btn_positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button btn_negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
//        btn_positive.setBackgroundColor(getContext().getResources().getColor(R.color.bg_success));
//        btn_negative.setBackgroundColor(getContext().getResources().getColor(R.color.bg_danger));

        btn_positive.setTextColor(getContext().getResources().getColor(R.color.bg_success));
        btn_negative.setTextColor(getContext().getResources().getColor(R.color.bg_danger));
    }

}