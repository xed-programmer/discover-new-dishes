package com.casestudy.discovernewdishes.businesslogic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.casestudy.discovernewdishes.Models.FoodSchedule;
import com.casestudy.discovernewdishes.sqldataaccess.SQLiteDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

public class FoodScheduleProcessor {

    SQLiteDatabase db;
    private String table = SQLiteDatabase.SCHEDULE_TABLE;
    private String[] columns = SQLiteDatabase.SCHEDULE_COLUMNS;

    public FoodScheduleProcessor(Context context){
        db = new SQLiteDatabase(context);
    }

    public long AddToSchedule(FoodSchedule data){
        ContentValues values = new ContentValues();
        Gson gson = new Gson();
        values.put(columns[1], data.getRecipe_id());
        values.put(columns[2], data.getDate_sched());
        values.put(columns[3], data.getApi());
        long rowsAffected = db.InsertData(table, values);
        return rowsAffected;
    }

    public ArrayList<FoodSchedule> PopulateSchedule(){
        String sql = String.format("SELECT * FROM %s ORDER BY %s, %s", table, columns[2], columns[0]);
        Cursor rs = db.PopulateData(sql);
        ArrayList<FoodSchedule> schedules = new ArrayList<>();

        while(!rs.isAfterLast()){
            FoodSchedule s = new FoodSchedule();
            s.setId(rs.getInt(rs.getColumnIndex(columns[0])));
            s.setRecipe_id(rs.getString(rs.getColumnIndex(columns[1])));
            s.setDate_sched(rs.getString(rs.getColumnIndex(columns[2])));
            s.setApi(rs.getString(rs.getColumnIndex(columns[3])));
            schedules.add(s);
            rs.moveToNext();
        }
        return schedules;
    }

    public int RemoveSchedule(int id){
        String where = columns[0] + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        int rowsAffected = db.DeleteData(table, where, whereArgs);
        return rowsAffected;
    }
}
