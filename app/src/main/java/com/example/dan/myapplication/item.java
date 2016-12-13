package com.example.dan.myapplication;

import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by Dan on 10/21/2016.
 */

public class item {
    private SharedPreferences mSettings;
    String mItem;
    ArrayList<String> detail;
    String deString = "";

    public item(String item, ArrayList<String> detail, SharedPreferences settings) {
        this.mItem = item;
        if (detail != null) {
            this.detail = detail;
            setDetail();
        } else {
            this.detail = new ArrayList<String>();
        }
        mSettings = settings;
    }

    public item(){
        this.detail = new ArrayList<String>();
    }

    public item(SharedPreferences settings){
        mSettings = settings;
        this.detail = new ArrayList<String>();
    }

    public String getDetail() {
        return deString;
    }

    public void addDetail(String obj) {
        detail.add(obj);
        setDetail();
    }

    public void setItem(String obj){
        mItem = obj;
    }

    public void clearDetail() {
        detail.clear();
        setDetail();
    }

    public String getString(){
        return mItem;
    }

    public String setDetail() {
        deString = "";
        if (detail != null) {
            for (String tmp : detail) {
                deString += tmp;
            }
        }
        return deString;
    }

    public int getPrice(){
        return mSettings.getInt(mItem, 0);
    }

    public String getWrapString(){
        String st = "";
        if (detail != null) {
            for (String tmp : detail) {
                st += tmp + ".";
            }
        }
        return st + mItem + "_";
    }

    public String getItem() {
        return mItem;
    }

//    public boolean equal(item it){
//        return (mItem.equals(it.getItem()) && setDetail().equals(it.setDetail())) ? true : false;
//    }
}

