package com.example.dan.myapplication.display_win;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dan.myapplication.ItemTouchHelperAdapter;
import com.example.dan.myapplication.R;

/**
 * Created by Dan on 10/21/2016.
 */

public class RecyclerViewGridViewAdapter extends RecyclerView.Adapter<RecyclerViewGridViewAdapter.VH> implements ItemTouchHelperAdapter {
    private SharedPreferences mSettings;
    private Context mContext;

    RecyclerViewGridViewAdapter(SharedPreferences mSettings, Context context){
        this.mSettings = mSettings;
        mContext = context;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_button, parent, false);
        VH vh = new VH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        if(position == (getItemCount()-1)){
            holder.button.setText("ADD");
        }else {
            holder.button.setText(mSettings.getString("name" + Integer.toString(position), "none"));
        }
    }

    @Override
    public int getItemCount() {
        return mSettings.getInt("num", 0) + 1;
    }

    @Override
    public void onItemDismiss(int position) {
        int j;
        SharedPreferences.Editor editor = mSettings.edit();
        String name = mSettings.getString("name" + Integer.toString(position),"none");
        editor.remove(name);
        editor.putInt("num", getItemCount() - 2);
        for (j = position; j < mSettings.getInt("num", 0) - 1; j++) {
            editor.putString("name" + Integer.toString(j), mSettings.getString(
                    "name" + Integer.toString(j+1), "none"));
        }
        editor.remove("name" + Integer.toString(j));
        editor.commit();
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        swap(fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
    }

    public void swap(int from, int to){
        SharedPreferences.Editor editor = mSettings.edit();
        String name;
        name = mSettings.getString("name" + Integer.toString(from), "none");
        editor.putString("name" + Integer.toString(from), mSettings.getString("name" + Integer.toString(to), "none"));
        editor.putString("name" + Integer.toString(to), name);
        editor.commit();
//        notifyDataSetChanged();
        notifyItemMoved(from, to);
    }

    public static class VH extends RecyclerView.ViewHolder{
        public Button button;

        public VH(View v){
            super(v);
            button = (Button)v.findViewById(R.id.tag);
        }
    }

}
