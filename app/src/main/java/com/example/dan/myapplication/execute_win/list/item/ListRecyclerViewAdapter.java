package com.example.dan.myapplication.execute_win.list.item;

import android.content.SharedPreferences;
import android.database.Cursor;

import com.example.dan.myapplication.execute_win.Marquee.MarqueeAdapter;
import com.example.dan.myapplication.item;
import com.example.dan.myapplication.items;

import java.util.ArrayList;

/**
 * Created by Dan on 10/21/2016.
 */

public class ListRecyclerViewAdapter extends MarqueeAdapter {
    int mId = -1;
    SharedPreferences mSettings;

    public ListRecyclerViewAdapter(SharedPreferences settings, Cursor cursor) {
        super(settings);
        mSettings = settings;
        if (cursor != null)
            decode(cursor);
    }

    public void setCursor(Cursor cursor) {
        clear();
        decode(cursor);
    }

    private void decode(Cursor cursor) {
        String st;
        mId = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("_id")));

        st = cursor.getString(cursor.getColumnIndexOrThrow("items"));
        ArrayList<item> list;
        ArrayList<String> deString;
        int stidx;
        String split[] = st.split("\\+");
        for (String i : split) {
            list = new ArrayList<item>();
            String spplit[] = i.split("_");
            for (String j : spplit) {
                if (!j.equals(spplit[spplit.length - 1])) {
                    deString = new ArrayList<String>();
                    String spt[] = j.split("\\.");
                    for (String k : spt) {
                        if (!k.equals(spt[spt.length - 1]))
                            deString.add(k);
                        else
                            list.add(new item(k, deString, mSettings));
                    }
                } else {
                    items its = new items(list, mSettings);
                    its.setNum(Integer.parseInt(j.substring(1)));
                    additem(its);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (position != 0) {
            holder.interview.setText(" + ");
            holder.textview.setText(getItemString(position));
        } else {
            holder.interview.setText("");
            holder.textview.setText(getItemString(position));
        }

        if (position == getSelect())
            holder.textview.setSelected(true);
        else
            holder.textview.setSelected(false);
    }

    public int getmId() {
        return mId;
    }
}

