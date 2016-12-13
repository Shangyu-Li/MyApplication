package com.example.dan.myapplication.execute_win.Marquee;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dan.myapplication.ItemTouchHelperAdapter;
import com.example.dan.myapplication.R;
import com.example.dan.myapplication.items;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dan on 10/21/2016.
 */

public class MarqueeAdapter extends RecyclerView.Adapter<MarqueeAdapter.VH> implements ItemTouchHelperAdapter {
    ArrayList<items> itemz;
    int total = 0;
    private int select = -1;
    SharedPreferences mSettings;
    static ContentValues values = new ContentValues();

    public scrollListener scListener;

    public interface scrollListener {
        void onScroll(View view);
    }

    public void setScrollListener(scrollListener sl) {
        scListener = sl;
    }

    public MarqueeAdapter(ArrayList<items> i, SharedPreferences settings) {
        itemz = i;
        mSettings = settings;
        if (i == null)
            itemz = new ArrayList<items>();
    }

    public MarqueeAdapter(SharedPreferences settings) {
        mSettings = settings;
        itemz = new ArrayList<items>();
    }

    public String getFrontString(int position) {
        return itemz.get(position).getFrontString();
    }

    public String getItemString(int position) {
        return itemz.get(position).getItemString();
    }

    public int calTotal() {
        total = 0;
        for (items i : itemz) {
            if (i.getNum() >= 0)
                total += i.getPrice() * i.getNum();
        }
        return total;
    }

    public void setSelect(int s) {
        int tmp = select;
        select = s;
        if (tmp != -1)
            notifyItemChanged(tmp);
        if(s != -1)
            notifyItemChanged(s);
    }

    public int getSelect() {
        return select;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.arrayitem, parent, false);
        VH vh = new VH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (position != 0 && position != (getItemCount() - 1)) {
            holder.interview.setText(" + ");
            holder.textview.setText(getItemString(position));
        } else if (position == itemz.size()) {
            holder.interview.setText(" = ");
            holder.textview.setText(Integer.toString(calTotal()));
        } else {
            holder.interview.setText("");
            holder.textview.setText(getItemString(position));
        }

        if (select == position)
            holder.textview.setSelected(true);
        else
            holder.textview.setSelected(false);
    }

    public ArrayList<items> getItems() {
        return itemz;
    }

    @Override
    public int getItemCount() {
        return itemz.size() + 1;
    }

    public int find(items obj) {
        for (int i = 0; i < getSize(); i++) {
            if (getFrontString(i).equals(obj.getFrontString())) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(items obj) {
        return itemz.indexOf(obj);
    }

    public int getItemPrice(int position) {
        return itemz.get(position).getPrice();
    }

    public void additem(items obj) {
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
        boolean find = false;
        try {
            for (int i = 0; i < getSize(); i++) {
                if (getFrontString(i).equals(obj.getFrontString())) {
                    getItem(i).setNum(obj.getNum() + getItem(i).getNum());
                    find = true;
                }
            }
        } catch (Exception e) {
        }
        if (!find) {
            itemz.add(obj);
        }
        notifyDataSetChanged();
    }

    public void addStraightItem(items obj) {
        itemz.add(obj);
        notifyDataSetChanged();
    }

    public void additem(items obj, int position) {
        itemz.set(position, obj);
        notifyDataSetChanged();
    }

    public void addDetail(int position, String name, String detail) {
        getItem(position).addDetail(name, detail);
        notifyDataSetChanged();
    }

    public void setNum(int position, int num) {
        getItem(position).setNum(num);
        notifyDataSetChanged();
//        scListener.onScroll(null);
    }

    public int getSize() {
        return itemz.size();
    }

    public items getItem(int id) {
        return itemz.get(id);
    }

    public void remove(int id) {
        itemz.remove(id);
        notifyDataSetChanged();
    }

    public ContentValues wrap() {
        int i;
        String str = "";
        values.clear();
        for (i = 0; i < itemz.size(); i++) {
            if (i == 0) {
                str += itemz.get(i).wrapString();
            } else {
                str += "+" + itemz.get(i).wrapString();
            }
        }
        values.put("items", str);
        return values;
    }

    public static class VH extends RecyclerView.ViewHolder {
        public TextView textview, interview;

        public VH(View v) {
            super(v);
            textview = (TextView) v.findViewById(R.id.item);
            interview = (TextView) v.findViewById(R.id.plus);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
//        if (fromPosition < toPosition) {
//            for (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(itemz, i, i + 1);
//            }
//        } else {
//            for (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(itemz, i, i - 1);
//            }
//        }
        Collections.swap(itemz, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void onItemDismiss(int position) {
        itemz.remove(position);
        notifyDataSetChanged();
    }

    public void clear() {
        itemz.clear();
        notifyDataSetChanged();
    }

    public int getTotal() {
        return total;
    }
//
//    public int getItemPrice(int position) {
//        int total = 0;
//        ArrayList<String> it = getItemName(position);
//        for (String i : it) {
//            total += mSettings.getInt(i, 0);
//        }
//        return total;
//    }
//
//    public ArrayList<String> getItemName(int position) {
//        items item = itemz.get(position);
//        return item.getItemName();
//    }

    public int getItemNum(int position) {
        items item = itemz.get(position);
        return item.getNum();
    }

    public ArrayList<items> getItemz() {
        return itemz;
    }

    public ArrayList<String> copyList() {
        return (ArrayList<String>) itemz.clone();
    }

    public boolean equal(MarqueeAdapter adapter) {
        if (adapter != null) {
            for (int i = 0; i < itemz.size(); i++) {
                if (!itemz.get(i).equals(adapter.getItem(i)))
                    return false;
            }
            return itemz.size() == adapter.getItemz().size() ? true : false;
        } else
            return false;
    }

    public int itemCount(String name){
        int count = 0;
        for(items it : itemz){
            count += it.find(name);
        }
        return count;
    }
}

