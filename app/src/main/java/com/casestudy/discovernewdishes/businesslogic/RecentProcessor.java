package com.casestudy.discovernewdishes.businesslogic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.casestudy.discovernewdishes.Models.RecipeDetails;
import com.casestudy.discovernewdishes.sqldataaccess.SQLiteDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RecentProcessor {
    private SQLiteDatabase db;
    private String table = SQLiteDatabase.RECENT_TABLE;
    private String[] columns = SQLiteDatabase.RECENT_COLUMNS;

    public RecentProcessor(Context context){
        db = new SQLiteDatabase(context);
    }

    public void AddToRecent(RecipeDetails data) {
        if(isExist(String.valueOf(data.getId()))){
            DeleteRecent(String.valueOf(data.getId()));
        }

        Gson gson = new Gson();
        String recipe_id = String.valueOf(data.getId());
        String json = gson.toJson(data);
        ContentValues values =  new ContentValues();
        values.put(columns[1], recipe_id);
        values.put(columns[2], json);
        db.InsertData(table, values);
    }

    private boolean isExist(String id){
        ArrayList<String> recent_recipes = new ArrayList<>();
        String sql = "SELECT * FROM " + table;
        Cursor rs = db.PopulateData(sql);
        while(!rs.isAfterLast()){
            String str_id = rs.getString(rs.getColumnIndex(columns[1]));
            recent_recipes.add(str_id);
            rs.moveToNext();
        }
        return recent_recipes.contains(id);
    }

    private int DeleteRecent(String id){
        String where = columns[1] + " = ?";
        String[] whereArgs = {id};
        int rowsAffected = db.DeleteData(table, where, whereArgs);
        return rowsAffected;
    }

    public int ClearRecent(){
        int rowsAffected = db.DeleteData(table, "1", null);
        return rowsAffected;
    }
}
