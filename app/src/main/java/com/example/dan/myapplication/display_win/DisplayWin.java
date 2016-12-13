package com.example.dan.myapplication.display_win;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.dan.myapplication.R;

/**
 * Created by Dan on 10/21/2016.
 */

public class DisplayWin extends Activity {
    public static final String PREFS_NAME = "MENU_PREFS";
    private SharedPreferences settings;
    LinearLayout parent;
    private RecyclerView gridview, gridDetail, gridCate;
    private RecyclerViewGridViewAdapter GridViewAdapter;
    private GridViewOnItemTouchListener GridViewListener;
    private DetailGridViewAdapter detailGridViewAdapter;
    private ItemTouchHelper.Callback callback;
    private ItemTouchHelper touchHelper;
    private int original_num;
    private int pos1 = -1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_win);

        settings = getSharedPreferences(PREFS_NAME, 0);
        original_num = settings.getInt("num", 0);

        gridview = (RecyclerView) findViewById(R.id.gridview);

        parent = (LinearLayout) gridview.getParent();

        gridview.setHasFixedSize(true);

        gridview.setLayoutManager(new GridLayoutManager(this, 3, 1, false));

        GridViewAdapter = new RecyclerViewGridViewAdapter(settings, this);
        gridview.setAdapter(GridViewAdapter);

        callback = new GridViewItemTouchHelperCallback(GridViewAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(gridview);
//        GridViewAdapter.setItemTouchHelper(touchHelper);

        GridViewListener = new GridViewOnItemTouchListener(this, gridview, new GridViewOnItemTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Context context) {
                if (position == (GridViewAdapter.getItemCount() - 1)) {
                    startActivity(new Intent(context, SetWin.class));
                } else {
                    detailGridViewAdapter.setItem(((Button) view.findViewById(R.id.tag)).getText().toString());
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
//                if (GridViewAdapter.getItemCount() - 1 != position)
//                    GridViewAdapter.onItemDismiss(position);
                Toast.makeText(getBaseContext(), "LongItemClick", Toast.LENGTH_LONG).show();
            }
        });
        gridview.addOnItemTouchListener(GridViewListener);


        gridDetail = (RecyclerView) findViewById(R.id.gridview2);
        gridDetail.setHasFixedSize(true);

        gridDetail.setLayoutManager(new GridLayoutManager(this, 3, 1, false));

        detailGridViewAdapter = new DetailGridViewAdapter(this, settings);
        gridDetail.setAdapter(detailGridViewAdapter);

        ItemTouchHelper ITH = new ItemTouchHelper(new GridViewItemTouchHelperCallback(
                detailGridViewAdapter));
        ITH.attachToRecyclerView(gridDetail);

        gridDetail.addOnItemTouchListener(new GridViewOnItemTouchListener(
                this, gridDetail, new GridViewOnItemTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Context context) {
                if (gridDetail.getAdapter().getItemCount() - 1 == position) {
                    Intent intent = new Intent(context, SetDetail.class);
                    intent.putExtra("item", ((DetailGridViewAdapter) gridDetail.getAdapter()).getmItem());
                    startActivity(intent);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                if (gridDetail.getAdapter().getItemCount() - 1 != position)
                    ((DetailGridViewAdapter) gridDetail.getAdapter()).onItemDismiss(position);
            }
        }));


        gridCate = new RecyclerView(this);
        gridCate.setHasFixedSize(true);

        gridCate.setLayoutManager(new GridLayoutManager(this, 3, 1, false));

        gridCate.setAdapter(new CategoryAdapter(this, settings));

        ItemTouchHelper cITH = new ItemTouchHelper(new GridViewItemTouchHelperCallback(
                (CategoryAdapter) gridCate.getAdapter()));
        cITH.attachToRecyclerView(gridCate);

        gridCate.addOnItemTouchListener(new GridViewOnItemTouchListener(this, gridCate,
                new GridViewOnItemTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, Context context) {
                        if (position == (gridCate.getAdapter().getItemCount() - 1)) {
                            startActivity(new Intent(context, SetCategory.class));
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        if (gridCate.getAdapter().getItemCount() - 1 != position)
                            ((CategoryAdapter) gridCate.getAdapter()).onItemDismiss(position);
                        Toast.makeText(getBaseContext(), "LongItemClick", Toast.LENGTH_LONG).show();
                    }
                }));

// initiate data base
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString("name" + Integer.toString(0), "燒餅");
//        editor.putInt("燒餅", 15);
//        editor.putString("name" + Integer.toString(1), "油條");
//        editor.putInt("油條", 13);
//        editor.putString("name" + Integer.toString(2), "豆漿");
//        editor.putInt("豆漿", 13);
//        editor.putString("name" + Integer.toString(3), "韭菜盒");
//        editor.putInt("韭菜盒", 25);
//        editor.putString("name" + Integer.toString(4), "饅頭");
//        editor.putInt("饅頭", 10);
//        editor.putInt("num", 5);
//        editor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        GridViewAdapter.notifyDataSetChanged();
        detailGridViewAdapter.notifyDataSetChanged();
    }

    public void showPopMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.setting_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemdetail:
//                        if()
//                        parent.getChildAt(0).getId()
                        return true;
                    case R.id.cate:
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }


}
