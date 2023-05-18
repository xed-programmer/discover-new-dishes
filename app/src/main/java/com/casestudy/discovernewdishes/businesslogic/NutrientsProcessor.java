package com.casestudy.discovernewdishes.businesslogic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.casestudy.discovernewdishes.Models.RecipeDetails;
import com.casestudy.discovernewdishes.Models.RecipeNutrients;
import com.casestudy.discovernewdishes.sqldataaccess.SQLiteDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NutrientsProcessor implements CRUD<RecipeNutrients> {

    private SQLiteDatabase db;
    private String table = SQLiteDatabase.TABLE;
    private String[] columns = SQLiteDatabase.COLUMNS;
    private String Section = SQLiteDatabase.SECTIONS[2];

    private String recent_table = SQLiteDatabase.RECENT_TABLE;
    private String[] recent_columns = SQLiteDatabase.RECENT_COLUMNS;

    public NutrientsProcessor(Context context){
        db = new SQLiteDatabase(context);
    }

    @Override
    public long AddToFavorite(RecipeNutrients data) {
        Gson gson = new Gson();
        int id = data.getId();
        String json = gson.toJson(data);
        ContentValues values = new ContentValues();
        values.put(columns[1], id);
        values.put(columns[2], json);
        values.put(columns[3], Section);
        long rowsAffected = db.InsertData(table, values);
        return rowsAffected;
    }

    public long AddToFavorite(RecipeDetails data) {
        Gson gson = new Gson();
        int id = data.getId();
        String json = gson.toJson(data);
        ContentValues values = new ContentValues();
        values.put(columns[1], id);
        values.put(columns[2], json);
        values.put(columns[3], Section);
        long rowsAffected = db.InsertData(table, values);
        return rowsAffected;
    }

    @Override
    public ArrayList<RecipeNutrients> PopulateData() {
        String sql = String.format("SELECT * FROM %s WHERE %s = '%s'",table, columns[3], Section);
        Cursor rs = db.PopulateData(sql);
        ArrayList<RecipeNutrients> res = new ArrayList<>();
        while (!rs.isAfterLast()){
            RecipeNutrients r = new RecipeNutrients();
            String api = rs.getString(rs.getColumnIndex(columns[2]));
            Gson gson = new Gson();
            r = gson.fromJson(api, RecipeNutrients.class);
            res.add(r);
            rs.moveToNext();
        }
        return res;
    }

    @Override
    public ArrayList<String> PopulateSavedItem() {
        String sql = String.format("SELECT * FROM %s WHERE %s = '%s'",table, columns[3], Section);
        Cursor rs = db.PopulateData(sql);
        ArrayList<String> res = new ArrayList<>();
        while (!rs.isAfterLast()){
            String id = rs.getString(rs.getColumnIndex(columns[1]));
            res.add(id);
            rs.moveToNext();
        }
        return res;
    }

    @Override
    public int RemoveToFavorite(String id) {
        String where = String.format("%s = ?", columns[1]);
        String[] whereArgs = {id};
        int rowsAffected = db.DeleteData(table, where, whereArgs);
        return rowsAffected;
    }

}
