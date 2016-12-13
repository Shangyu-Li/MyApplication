package com.example.dan.myapplication.SQLiteDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dan on 10/21/2016.
 */

public class MenuDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MenuDatabase.db";
    private static final String CONTENT_TABLE_NAME = "menu";
    private static final String CONTENT_SQL_CREATE = " CREATE TABLE " + CONTENT_TABLE_NAME + "( _id INTEGER PRIMARY KEY, items TEXT)";
    private static final String HIS_TABLE_NAME = "his";
    private static final String HIS_SQL_CREATE = " CREATE TABLE " + HIS_TABLE_NAME + "( _id INTEGER PRIMARY KEY, items TEXT)";

    public MenuDatabase(Context context){
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CONTENT_SQL_CREATE);
        db.execSQL(HIS_SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HIS_TABLE_NAME);
        onCreate(db);
    }
}


