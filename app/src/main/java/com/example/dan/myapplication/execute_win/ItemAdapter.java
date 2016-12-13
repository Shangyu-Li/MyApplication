package com.example.dan.myapplication.execute_win;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dan.myapplication.R;

/**
 * Created by Dan on 10/21/2016.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.VH> {
    private SharedPreferences mSettings;
    enum which{NORMAL, NUM, DETAIL}
    private Context mContext;
    private String mItem;
    private which wh = which.NORMAL;

    ItemAdapter(SharedPreferences mSettings, Context context) {
        this.mSettings = mSettings;
        mContext = context;
    }

    public void setItem(String item){
        mItem = String.copyValueOf(item.toCharArray());
        notifyDataSetChanged();
    }

    public String getItem(){
        return mItem;
    }

    public void setWhich(which wh){
        this.wh = wh;
        notifyDataSetChanged();
    }

    public which getWhich(){return wh;}

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_button, parent, false);
        VH vh = new VH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        switch(wh){
            case NORMAL:
                holder.button.setText(mSettings.getString("name" + Integer.toString(position), "none"));
                break;
            case NUM:
                holder.button.setText(Integer.toString(position));
                break;
            case DETAIL:
                holder.button.setText(mSettings.getString(mItem + Integer.toString(position), "none"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        switch(wh){
            case NORMAL:
                return mSettings.getInt("num", 0);
            case NUM:
                return 10;
            case DETAIL:
                if(mItem == null){  return 0;}
                else{   return mSettings.getInt(mItem + "num", 0);}
            default:
                return 0;
        }
    }

    public static class VH extends RecyclerView.ViewHolder {
        public Button button;

        public VH(View v) {
            super(v);
            button = (Button) v.findViewById(R.id.tag);
        }
    }
}

