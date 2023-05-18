package com.casestudy.discovernewdishes.businesslogic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.casestudy.discovernewdishes.Models.RecipeDetails;
import com.casestudy.discovernewdishes.Models.RecipeInfo;
import com.casestudy.discovernewdishes.sqldataaccess.SQLiteDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RecipeProcessor extends RecentProcessor implements CRUD<RecipeDetails>{
    SQLiteDatabase db;
    private String table = SQLiteDatabase.TABLE;
    private String[] columns = SQLiteDatabase.COLUMNS;
    private String Section = SQLiteDatabase.SECTIONS[0];
    private String SectionNutri = SQLiteDatabase.SECTIONS[2];

    private String recent_table = SQLiteDatabase.RECENT_TABLE;
    private String[] recent_columns = SQLiteDatabase.RECENT_COLUMNS;

    public RecipeProcessor(Context context){
        super(context);
        db = new SQLiteDatabase(context);
    }

    @Override
    public long AddToFavorite(RecipeDetails data) {
        Gson gson = new Gson();
        String recipe_id = String.valueOf(data.getId());
        String json = gson.toJson(data);
        ContentValues values =  new ContentValues();
        values.put(columns[1], recipe_id);
        values.put(columns[2], json);
        values.put(columns[3], Section);
        return db.InsertData(table, values);
    }

    @Override
    public ArrayList<RecipeDetails> PopulateData() {
        String sql = String.format("SELECT * FROM %s WHERE %s = '%s' OR %s = '%s' ORDER BY id DESC", table, columns[3], Section,columns[3], SectionNutri);
        Cursor rs = db.PopulateData(sql);
        ArrayList<RecipeDetails> recipes = new ArrayList<>();
        while(rs.isAfterLast() == false){
            String api = rs.getString(rs.getColumnIndex(columns[2]));
            Gson gson = new Gson();
            RecipeDetails recipe = gson.fromJson(api, RecipeDetails.class);
            recipes.add(recipe);
            rs.moveToNext();
        }

        return recipes;
    }

    @Override
    public ArrayList<String> PopulateSavedItem() {
        String sql = String.format("SELECT * FROM %s WHERE %s = '%s' OR %s = '%s'", table, columns[3], Section,columns[3], SectionNutri);
        Cursor rs = db.PopulateData(sql);
        ArrayList<String> res = new ArrayList<>();
        while(rs.isAfterLast() == false){
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

    public ArrayList<RecipeDetails> PopulateRecentData() {
        String sql = String.format("SELECT * FROM %s ORDER BY id DESC", recent_table);
        Cursor rs = db.PopulateData(sql);
        ArrayList<RecipeDetails> recipes = new ArrayList<>();
        while(rs.isAfterLast() == false){
            String api = rs.getString(rs.getColumnIndex(columns[2]));
            Gson gson = new Gson();
            RecipeDetails recipe = gson.fromJson(api, RecipeDetails.class);
            recipes.add(recipe);
            rs.moveToNext();
        }

        return recipes;
    }
}
