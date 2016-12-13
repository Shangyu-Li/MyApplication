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
 * Created by Dan on 12/13/2016.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> implements ItemTouchHelperAdapter {
    Context mContext;
    SharedPreferences mSettings;

    CategoryAdapter(Context context, SharedPreferences settings){
        mContext = context;
        mSettings = settings;
    }

    @Override
    public CategoryAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_button, parent, false);
        VH vh = new VH(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if(position == (getItemCount() - 1)){
            holder.button.setText("ADD");
        }else{
            holder.button.setText(mSettings.getString("cate" + Integer.toString(position), "none"));
        }
    }

    @Override
    public int getItemCount() {
        return mSettings.getInt("cate_num", 0) + 1;
    }

    @Override
    public void onItemDismiss(int position) {
        int j;
        SharedPreferences.Editor editor = mSettings.edit();
        String name = mSettings.getString("Cate" + Integer.toString(position),"none");
        editor.remove(name);
        editor.putInt("CateNum", getItemCount() - 2);
        for (j = position; j < mSettings.getInt("CateNum", 0) - 1; j++) {
            editor.putString("Cate" + Integer.toString(j), mSettings.getString(
                    "Cate" + Integer.toString(j+1), "none"));
        }
        editor.remove("Cate" + Integer.toString(j));
        editor.commit();
        notifyDataSetChanged();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        SharedPreferences.Editor editor = mSettings.edit();
        String name;
        name = mSettings.getString("Cate" + Integer.toString(fromPosition), "none");
        editor.putString("Cate" + Integer.toString(fromPosition), mSettings.getString("Cate" + Integer.toString(toPosition), "none"));
        editor.putString("Cate" + Integer.toString(toPosition), name);
        editor.commit();
//        notifyDataSetChanged();
        notifyItemMoved(fromPosition, toPosition);
    }

    class VH extends RecyclerView.ViewHolder{
        Button button;

        public VH(View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.tag);
        }
    }
}
