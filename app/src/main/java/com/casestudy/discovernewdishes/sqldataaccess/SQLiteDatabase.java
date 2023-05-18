package com.casestudy.discovernewdishes.sqldataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app.db";
    public static final String TABLE = "dish";
    public static final String[] COLUMNS = {"id", "dish_id", "api", "section"};
    public static final String[] SECTIONS = {"Recipe", "Video", "Nutrients"};

    public static final String RECENT_TABLE = "recent";
    public static final String[] RECENT_COLUMNS = {"id", "dish_id", "api"};

    public static final String SCHEDULE_TABLE = "tbl_schedule";
    public static final String[] SCHEDULE_COLUMNS = {"id", "dish_id", "date_sched","api"};
    android.database.sqlite.SQLiteDatabase conn;
    public SQLiteDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase conn) {
        String sql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT)", TABLE, COLUMNS[0], COLUMNS[1], COLUMNS[2], COLUMNS[3]);
        String sql1 = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)", RECENT_TABLE, RECENT_COLUMNS[0], RECENT_COLUMNS[1], RECENT_COLUMNS[2]);
        String sql2 = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT)", SCHEDULE_TABLE, SCHEDULE_COLUMNS[0], SCHEDULE_COLUMNS[1], SCHEDULE_COLUMNS[2], SCHEDULE_COLUMNS[3]);
        conn.execSQL(sql);
        conn.execSQL(sql1);
        conn.execSQL(sql2);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase conn, int i, int i1) {
        conn.execSQL("DROP TABLE IF EXISTS " + TABLE);
        conn.execSQL("DROP TABLE IF EXISTS " + RECENT_TABLE);
        conn.execSQL("DROP TABLE IF EXISTS " + SCHEDULE_TABLE);
        onCreate(conn);
    }

    public long InsertData(String table, ContentValues values) {
        conn = this.getWritableDatabase();
        long rowsAffected = conn.insert(table,null, values);
        return rowsAffected;
    }

    public Cursor PopulateData(String sql) {
        conn = this.getReadableDatabase();
        Cursor rs = conn.rawQuery(sql, null);
        rs.moveToFirst();
        return rs;
    }

    public int DeleteData(String table, String where, String[] whereArgs) {
        conn = this.getWritableDatabase();
        int rowsAffected = conn.delete(table, where, whereArgs);
        return rowsAffected;
    }
}
