package com.example.dan.myapplication.execute_win.list;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dan.myapplication.ItemTouchHelperAdapter;
import com.example.dan.myapplication.R;
import com.example.dan.myapplication.SQLiteDataBase.MenuProvider;
import com.example.dan.myapplication.SurfaceView.viewDeliver;
import com.example.dan.myapplication.execute_win.list.item.ListRecyclerViewAdapter;
import com.example.dan.myapplication.execute_win.list.item.RecyclerViewContentMarqueeItemClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by Dan on 10/21/2016.
 */

public class RecyclerViewContentProviderAdapter extends RecyclerView.Adapter<RecyclerViewContentProviderAdapter.VH> implements ItemTouchHelperAdapter {

    private Cursor mCursor;
    private Context mContext;
    private SharedPreferences mSettings;
    private ArrayList<recycView> recycList = new ArrayList<recycView>();
    private String date;

    class recycView {
        public RecyclerView rv;
        public RecyclerView.OnItemTouchListener listener;

        recycView(RecyclerView v, RecyclerView.OnItemTouchListener listener) {
            rv = v;
            this.listener = listener;
        }
    }

    //    private ArrayList<RecyclerView.Adapter> adapterList = new ArrayList<RecyclerView.Adapter>();
    private int idx;
    private int row = -1, column = -1;

    private onTouchListener mListener;
    private Map scrollMap = new HashMap();

    private int mSelectedId = -1;
    private int mItemSelectedId = -1;

    public void setSelect(int select, int itemSelect) {
        mSelectedId = select;
        mItemSelectedId = itemSelect;
    }

    public interface onTouchListener {
        void onItemClick(View view, int position, ListRecyclerViewAdapter adapter);

        void onLongItemClick(View view, int position, ListRecyclerViewAdapter adapter);
    }

    public void setOnItemTouchListener(onTouchListener listener) {
        mListener = listener;
    }

    public RecyclerViewContentProviderAdapter(Context context, Cursor cursor, SharedPreferences settings) {
        mCursor = cursor;
        mContext = context;
        mSettings = settings;
    }

    @Override
    public RecyclerViewContentProviderAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cursor_layout, parent, false);
        VH vh = new VH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        mCursor.moveToPosition(position);

        holder.recyclervie.setHasFixedSize(true);

        holder.recyclervie.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);

                int idx = -1;
                RecyclerView rv = null;
                for (recycView j : recycList) {
                    if (equals(j.rv)) {
                        rv = j.rv;
                        idx = ((ListRecyclerViewAdapter) j.rv.getAdapter()).getmId();
                        break;
                    }
                }
                if (idx == 7)
                    idx = 7;
                if (idx == 9)
                    idx = 9;
                scrollPair sp;
                if (idx != -1 && (sp = (scrollPair) scrollMap.get(idx)) != null) {
                    scrollToPositionWithOffset(sp.getIdx(), sp.getOffset());
                }
//                scrollToPosition(column);
//                TextView tv = (TextView)getChildAt(1).findViewById(R.id.item);
            }

            public boolean equals(RecyclerView rv) {
                if (rv.getChildCount() == getChildCount()) {
                    for (int i = 0; i < rv.getChildCount(); i++) {
                        if (!rv.getChildAt(i).equals(getChildAt(i)))
                            return false;
                    }
                    return true;
                }
                return false;
            }
        });

        final ListRecyclerViewAdapter mAdapter = new ListRecyclerViewAdapter(mSettings, mCursor);

        holder.recyclervie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) {
                    int id = ((ListRecyclerViewAdapter) recyclerView.getAdapter()).getmId();
                    int idx = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    View child = recyclerView.getChildAt(0);

                    scrollMap.put(id, new scrollPair(idx, child.getLeft()));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                int id = ((ListRecyclerViewAdapter) recyclerView.getAdapter()).getmId();
//                int idx = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
//                View child = recyclerView.getLayoutManager().getChildAt(idx);
//
//                scrollMap.put(id, new scrollPair(idx, child.getLeft()));
            }
        });

//        layoutMap.put(linearLayoutManager, mAdapter.getmId());

        holder.recyclervie.setAdapter(mAdapter);

        RecyclerViewContentMarqueeItemClickListener onItemTouchListener = new RecyclerViewContentMarqueeItemClickListener(
                mContext, holder.recyclervie, new RecyclerViewContentMarqueeItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mListener.onItemClick(view, position, mAdapter);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                mListener.onLongItemClick(view, position, mAdapter);
            }
        });
        //  remove all OnItemTouchListener from holder.recyclervie
        for (Iterator<recycView> i = recycList.iterator(); i.hasNext(); ) {
            recycView rv = i.next();
            if (rv.rv == holder.recyclervie) {
                rv.rv.removeOnItemTouchListener(rv.listener);
                i.remove();
            }
        }

        recycList.add(new recycView(holder.recyclervie, onItemTouchListener));
        holder.recyclervie.addOnItemTouchListener(onItemTouchListener);

        if (mSelectedId == position) {
            mAdapter.setSelect(mItemSelectedId);
        }

        holder.textview.setText(" = " + mAdapter.calTotal());
    }

    public void scrollTo(int row, int col) {
        this.row = row;
        column = col;
//            ((LinearLayoutManager) recycList.get(row).getLayoutManager()).scrollToPositionWithOffset(col, 50);
    }

    public void setScroll(int row, int col) {
        this.row = row;
        column = col;
    }

//    public ListRecyclerViewAdapter getAdapter(int idx) {
//        return (ListRecyclerViewAdapter) recycList.get(idx).getAdapter();
//    }

    public ArrayList<String> FromCursor() {
        String split[] = mCursor.getString(mCursor.getColumnIndex("items")).split("\\+");
        ArrayList<String> items = new ArrayList<String>();
        items.addAll(Arrays.asList(split));
        return items;
    }

    public int getCursorId(int idx) {
        mCursor.moveToPosition(idx);
        return Integer.parseInt(mCursor.getString(mCursor.getColumnIndexOrThrow("_id")));
    }

    @Override
    public void onItemDismiss(int position) {
        mCursor.moveToPosition(position);
        mContext.getContentResolver().delete(Uri.parse(MenuProvider.CONTENT_URL_STRING + "/" + mCursor.getString(0)), null, null);
        for (viewDeliver i : detailList) {
            if (i.getRecyclerView().equals(recycList.get(position))) {
                detailList.remove(i);
                i.undraw();
            }
        }
//        Cursor c = mContext.getContentResolver().query(Uri.parse(MenuProvider.CONTENT_URL_STRING + "/" +
//                mCursor.getString(0)), new String[]{"_id", "items"}, null, null, null);
        ContentValues value = new ContentValues();
        value.put("items", mCursor.getString(mCursor.getColumnIndexOrThrow("items")));

        Date da = new Date();
        DateFormat dt = DateFormat.getDateInstance(DateFormat.SHORT);

        ((SimpleDateFormat)dt).applyPattern("MM.dd.yyyy");
        String dateString = dt.format(da);

        mContext.getContentResolver().insert(Uri.parse("content://" + MenuProvider.PROVIDER_NAME + "/" + dateString),
                value);

        mCursor = mContext.getContentResolver().query(MenuProvider.CONTENT_URI, new String[]{"_id", "items"}, null, null, null);

        notifyItemRemoved(position);
    }

    public void setDate(String date){   this.date = date;}

    public String getDate(){ return date;}

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
//        if (fromPosition < toPosition) {
//            for (int i = fromPosition; i < toPosition; i++) {
//                swap(mCursor, i, i+1);
//            }
//        } else {
//            for (int i = fromPosition; i > toPosition; i--) {
//                swap(mCursor, i, i-1);
//            }
//        }
        swap(mCursor, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public boolean swap(Cursor cursor, int from, int to) {
        ContentValues values = new ContentValues(), values2 = new ContentValues();
        try {
            // FromCursor
            cursor.moveToPosition(from);
            DatabaseUtils.cursorRowToContentValues(cursor, values);
            String id = mCursor.getString(0);
            // ToCursor
            cursor.moveToPosition(to);
            DatabaseUtils.cursorRowToContentValues(cursor, values2);

            scrollPair s = (scrollPair) scrollMap.get(Integer.parseInt(id));
            scrollMap.put(Integer.parseInt(id), scrollMap.get(Integer.parseInt(mCursor.getString(0))));
            scrollMap.put(Integer.parseInt(mCursor.getString(0)), s);


            values.remove("_id");
            values2.remove("_id");
            mContext.getContentResolver().update(Uri.parse(MenuProvider.CONTENT_URL_STRING + "/" + id), values2, null, null);
            mContext.getContentResolver().update(Uri.parse(MenuProvider.CONTENT_URL_STRING + "/" + mCursor.getString(0)), values, null, null);
            mCursor = mContext.getContentResolver().query(MenuProvider.CONTENT_URI, new String[]{"_id", "items"}, null, null, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor != null)
            return mCursor.getCount();
        return 0;
    }

    public static class VH extends RecyclerView.ViewHolder {
        public TextView textview;
        public RecyclerView recyclervie;

        public VH(View v) {
            super(v);
            textview = (TextView) v.findViewById(R.id.price);
            recyclervie = (RecyclerView) v.findViewById(R.id.item);
        }

    }


    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (newCursor != null) {
//            mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
//            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
//            mRowIDColumn = -1;
//            mDataValid = false;
            // notify the observers about the lack of a data set
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    public class scrollPair {
        int idx;
        int offset;

        scrollPair(int idx, int off) {
            this.idx = idx;
            offset = off;
        }

        public int getIdx() {
            return idx;
        }

        public int getOffset() {
            return offset;
        }
    }

    ArrayList<viewDeliver> detailList = new ArrayList<viewDeliver>();

    public void addViewDeliver(viewDeliver vd) {
        if (vd.getDetailViewNum() > 0) {
            detailList.add(vd);
            vd.setOnRemovedListener(new viewDeliver.onRemoveListener() {
                @Override
                public void onRemove(viewDeliver vd) {
                    detailList.remove(vd);
                }
            });
        }
    }

    public void drawDetail() {
        for (viewDeliver i : detailList) {
            i.undraw();
            i.deliver(mContext);
            i.draw();
        }
    }
}

