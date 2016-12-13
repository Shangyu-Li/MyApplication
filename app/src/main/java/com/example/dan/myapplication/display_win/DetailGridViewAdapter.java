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
public class DetailGridViewAdapter extends RecyclerView.Adapter<DetailGridViewAdapter.VH> implements ItemTouchHelperAdapter {
    private Context mContext;
    private String mItem;
    private SharedPreferences mSettings;

    DetailGridViewAdapter(Context context, SharedPreferences settings){
        mContext = context;
        mSettings = settings;
    }

    public void setItem(String item){
        mItem = String.copyValueOf(item.toCharArray());
        notifyDataSetChanged();
    }

    @Override
    public DetailGridViewAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_button, parent, false);
        VH vh = new VH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if(position == (getItemCount()-1)){
            holder.button.setText("ADD");
        }else {
            holder.button.setText(mSettings.getString(mItem + Integer.toString(position), "none"));
        }
    }


    @Override
    public int getItemCount() {
        if(mItem == null){  return 0;}
        else{   return mSettings.getInt(mItem + "num", 0) + 1;}
    }

    @Override
    public void onItemDismiss(int position) {
        int j;
        SharedPreferences.Editor editor = mSettings.edit();
        String name = mSettings.getString(mItem + Integer.toString(position),"none");
        editor.remove(name);
        editor.putInt(mItem + "num", getItemCount() - 2);
        for (j = position; j < mSettings.getInt(mItem + "num", 0) - 1; j++) {
            editor.putString(mItem + Integer.toString(j), mSettings.getString(
                    mItem + Integer.toString(j+1), "none"));
        }
        editor.remove(mItem + Integer.toString(j));
        editor.commit();
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        SharedPreferences.Editor editor = mSettings.edit();
        String name;
        name = mSettings.getString(mItem + Integer.toString(fromPosition), "none");
        editor.putString(mItem + Integer.toString(fromPosition), mSettings.getString(mItem + Integer.toString(toPosition), "none"));
        editor.putString(mItem + Integer.toString(toPosition), name);
        editor.commit();
//        notifyDataSetChanged();
        notifyItemMoved(fromPosition, toPosition);
    }

    public class VH extends RecyclerView.ViewHolder{
        public Button button;

        public VH(View view){
            super(view);
            button = (Button) view.findViewById(R.id.tag);
        }
    }

    public String getmItem(){   return mItem;}
}

