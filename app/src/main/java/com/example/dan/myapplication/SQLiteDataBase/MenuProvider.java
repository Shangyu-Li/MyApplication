package com.example.dan.myapplication.SQLiteDataBase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by Dan on 10/21/2016.
 */

public class MenuProvider extends ContentProvider {
    public static final String PROVIDER_NAME = "com.example.dan.shop";
    public static final String CONTENT_URL_STRING = "content://" + PROVIDER_NAME + "/menu";
    public static final String HIS_URL_STRING = "content://" + PROVIDER_NAME + "/his";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/menu");
    public static final Uri HIS_URI = Uri.parse("content://" + PROVIDER_NAME + "/his");

    private static final int MENU = 1;
    private static final int MENU_ID = 2;
    private static final int HIS = 3;
    private static final int HIS_ID = 4;

    public static final String ITEMS = "items";
    public static final String PRICE = "price";

    static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI("com.example.dan.shop", "menu/#", MENU_ID);
        sUriMatcher.addURI("com.example.dan.shop", "menu", MENU);
        sUriMatcher.addURI("com.example.dan.shop", "his", HIS);
        sUriMatcher.addURI("com.example.dan.shop", "his/#", HIS_ID);
    }

    private static MenuDatabase dbHelper = null;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new MenuDatabase(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = null;

        List<String> list = uri.getPathSegments();

        if (!isTable(list.get(0))) {
            dbHelper.getWritableDatabase().execSQL("CREATE TABLE [" + list.get(0) + "] ( _id INTEGER PRIMARY KEY, items TEXT )");
        }

        String sql = "";

        for (int i = 0; i <= list.size(); i++) {
            if (i == 1) {
                sql = "select _id, items from [" + list.get(0) + "] ";
            } else if (i == 2) {
                sql += " WHERE _id=" + list.get(1);
            }
//            "select items from " + list.get(0) + " WHERE _id=" + list.get(1)
        }

        c = db.rawQuery(sql, null);
        if (c.moveToFirst() && c.getCount() > 0 && c != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MENU:
                return "vnd.android.cursor.dir/vnd.com.example.dan.shop.menu";
            case MENU_ID:
                return "vnd.android.cursor.item/vnd.com.example.dan.shop.menu";
            case HIS:
                return "vnd.android.cursor.dir/vnd.com.example.dan.shop.his";
            case HIS_ID:
                return "vnd.android.cursor.item/vnd.com.example.dan.shop.his";
        }
        return "";
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        List<String> list = uri.getPathSegments();

        if (!isTable(list.get(0))) {
            dbHelper.getWritableDatabase().execSQL("CREATE TABLE [" + list.get(0) + "] ( _id INTEGER PRIMARY KEY, items TEXT )");
        }
        long rowID = dbHelper.getWritableDatabase().insert('[' + uri.getPathSegments().get(0) + ']', "", values);
        if (rowID > 0) {
            try {
                Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
//                getContext().getContentResolver().notifyChange(_uri, null);
                return _uri;
            } catch (Exception e) {
                System.out.println("SQLException Failed");
            }
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<String> list = uri.getPathSegments();


        if (isEmpty(list.get(0))) {
            db.execSQL("DROP TABLE IF EXISTS [" + list.get(0) + ']');
        }


        return db.delete(list.get(0), "_id=?", new String[]{list.get(1)});
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<String> list = uri.getPathSegments();

        return db.update(list.get(0), values, "_id=?", new String[]{list.get(1)});
    }

    public static void reset() {
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 1);
    }

    public boolean isTable(String table) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select DISTINCT name from " +
                        "sqlite_master WHERE type='table' AND name=?",
                new String[]{table});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean isEmpty(String table) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select _id from " + table, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public static void ruin() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.rawQuery("select 'drop table ' || name || ';' from sqlite_master where type = 'table'", null);
        db.execSQL("PRAGMA writable_schema = 1;");
        db.execSQL("delete from sqlite_master where type = 'table';");
        db.execSQL("PRAGMA writable_schema = 0;");
    }
}
