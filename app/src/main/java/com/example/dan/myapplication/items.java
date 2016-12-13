package com.example.dan.myapplication;

import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by Dan on 10/21/2016.
 */

public class items {
    int num = -1;         // num is add last not in initiation
    ArrayList<item> itemz;
    SharedPreferences mSettings;

    public items(ArrayList<item> i, SharedPreferences settings) {
        if (i != null) {
            itemz = i;
        } else {
            itemz = new ArrayList<item>();
        }
        mSettings = settings;
    }

    public items(item i, SharedPreferences settings) {
        itemz = new ArrayList<item>();
        if (i != null) {
            itemz.add(i);
        }
        mSettings = settings;
    }

    public items() {
        itemz = new ArrayList<item>();
    }

    public items(String i, SharedPreferences settings) {
        itemz = new ArrayList<item>();
        if (i != null) {
            itemz.add(new item(i, null, settings));
        }
        mSettings = settings;
    }

    public String wrapString() {
        String st = "";
        for (item i : itemz) {
            st += i.getWrapString();
        }
        return st + "X" + Integer.toString(num);
    }

    public String getFrontString() {
        String st = "";
        for (item i : itemz) {
            st += i.getString();
        }
        return st;
    }

    public String getItemString() {
        if (num != -1)
            return getFrontString() + " X " + Integer.toString(num);
        else
            return getFrontString();
    }

//    public String getPriceItems() {
//        String it = "";
//        for (item i : itemz) {
//            it += i.getItem() + " X ";
//        }
//        return it;
//    }

    public void setmSettings(SharedPreferences settings) {
        mSettings = settings;
    }

    public ArrayList<String> getItemName() {
        ArrayList<String> it = new ArrayList<String>();
        for (item i : itemz) {
            it.add(i.getItem());
        }
        return it;
    }

    public int getPrice() {
        if (num >= -1) {
            int total = 0;
            for (item i : itemz) {
                total += i.getPrice();
            }
            return total;
        }
        return 0;
    }

    public void addDetail(String name, String detail) {
        for (item i : itemz) {
            if (i.getItem().equals(name)) {
                i.addDetail(detail);
            }
        }
    }

    public ArrayList<String> getDetail(){
        ArrayList<String> detail = new ArrayList<String>();
        for(item i : itemz){
            detail.add(i.getDetail());
        }
        return detail;
    }

    public int getNum() {
        if (num >= -1)
            return num;
        else
            return 0;
    }

    public void setNum(int obj) {
        num = obj;
    }

    public void addItem(item it) {
        itemz.add(it);
    }

    public String getItemName(int idx){  return itemz.get(idx).getItem();    }

    public boolean equals(items its){
        return getItemString().equals(its.getItemString());
    }

    public int find(String name){
        int count = 0;
        for(item it : itemz){
            if(name.equals(it.getString()))
                count++;
        }
        return count * (num > 0 ? num : 0);
    }
}
