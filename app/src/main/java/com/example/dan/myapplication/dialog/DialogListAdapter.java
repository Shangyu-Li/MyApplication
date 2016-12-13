package com.example.dan.myapplication.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dan.myapplication.R;
import com.example.dan.myapplication.SQLiteDataBase.MenuProvider;
import com.example.dan.myapplication.execute_win.list.RecyclerViewContentProviderAdapter;
import com.example.dan.myapplication.execute_win.list.item.ListRecyclerViewAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dan on 12/9/2016.
 */

public class DialogListAdapter extends BaseAdapter {
    SharedPreferences mSettings;
    Context mContext;
    Uri uri;
    ArrayList<pair> list = new ArrayList<pair>();

    DialogListAdapter(Context context, SharedPreferences settings) {
        mSettings = settings;
        mContext = context;
        Date da = new Date();
        DateFormat dt = DateFormat.getDateInstance(DateFormat.SHORT);

        ((SimpleDateFormat) dt).applyPattern("MM.dd.yyyy");
        String dateString = dt.format(da);
        uri = Uri.parse("content://" + MenuProvider.PROVIDER_NAME + "/" + dateString);

        for(int i = 0; i < mSettings.getInt("num", 0); i++){
            list.add(new pair(mSettings.getString("name" + Integer.toString(i), "none"), 0));
        }

        do_statistic();
    }

    @Override
    public int getCount() {
        return mSettings.getInt("num", 0);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView == null)
            view = LayoutInflater.from(mContext).inflate(R.layout.dialog_item, parent, false);
        else
            view = convertView;

        TextView item = (TextView)view.findViewById(R.id.item);
        TextView num = (TextView)view.findViewById(R.id.number);

        item.setText(list.get(position).name);
        num.setText(Integer.toString(list.get(position).num));

        return view;
    }

    void do_statistic(){
        Cursor cursor = mContext.getContentResolver().query(uri, new String[]{"_id", "items"}, null, null, null);
        if(cursor != null) {
            RecyclerViewContentProviderAdapter Adapter = new RecyclerViewContentProviderAdapter(mContext, cursor, mSettings);

            for (pair p : list) {
                p.num = 0;
            }

            ListRecyclerViewAdapter decodeAdapter = new ListRecyclerViewAdapter(mSettings, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                decodeAdapter.setCursor(cursor);
                for (pair p : list) {
                    p.num += decodeAdapter.itemCount(p.name);
                }
            }
        }
    }

    class pair{
        public String name;
        public int num;

        pair(String name, int num){
            this.name = name;
            this.num = num;
        }
    }
}
