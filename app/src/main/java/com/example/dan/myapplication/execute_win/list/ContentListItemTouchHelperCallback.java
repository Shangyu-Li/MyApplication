package com.example.dan.myapplication.execute_win.list;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.dan.myapplication.ItemTouchHelperAdapter;
import com.example.dan.myapplication.execute_win.Marquee.SimpleItemTouchHelperCallback;

/**
 * Created by Dan on 10/21/2016.
 */

public class ContentListItemTouchHelperCallback extends SimpleItemTouchHelperCallback {
    private ItemTouchHelperAdapter mAdapter;
    private int drag = -1;
    private int drop = -1;

    public ContentListItemTouchHelperCallback(ItemTouchHelperAdapter adapter){
        super(adapter);
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        try {
//            Integer.parseInt(((MarqueeAdapter.VH) viewHolder).textview.getText().toString().substring(3));
//            return 0;
//        }catch(Exception e) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = 0; //  ItemTouchHelper.START;
        return makeMovementFlags(dragFlags, swipeFlags);
//        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int to = target.getAdapterPosition();
        int from  = viewHolder.getAdapterPosition();
//        if(drag == -1)
//            drag = from;
//        drop = to;
        mAdapter.onItemMove(from, to);
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled(){
        return true;
    }

}

