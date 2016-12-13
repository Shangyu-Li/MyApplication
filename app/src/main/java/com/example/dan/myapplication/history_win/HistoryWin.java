package com.example.dan.myapplication.history_win;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dan.myapplication.R;
import com.example.dan.myapplication.SQLiteDataBase.MenuProvider;
import com.example.dan.myapplication.execute_win.list.RecyclerViewContentProviderAdapter;
import com.example.dan.myapplication.execute_win.list.RecyclerViewContentProviderItemClickListener;
import com.example.dan.myapplication.execute_win.list.item.ListRecyclerViewAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dan on 10/26/2016.
 */

public class HistoryWin extends Activity {
    private final String PREFS_NAME = "MENU_PREFS";
    private SharedPreferences settings;

    private RecyclerViewContentProviderAdapter ContentAdapter;

    private RecyclerView hisList;

    private LoaderManager.LoaderCallbacks<Cursor> menuLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    CursorLoader cursorLoader = new CursorLoader(HistoryWin.this, Uri.parse("content://"+MenuProvider.PROVIDER_NAME+"/"+date.getText()),
                            new String[]{"_id", MenuProvider.ITEMS},
                            null,
                            null,
                            null);
                    return cursorLoader;
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    ContentAdapter.swapCursor(null);
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    ContentAdapter.swapCursor(cursor);
                }
            };

    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_win);

        settings = getSharedPreferences(PREFS_NAME, 0);

        hisList = (RecyclerView) findViewById(R.id.recyclerView);
        hisList.setHasFixedSize(true);
        
        hisList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ContentAdapter = new RecyclerViewContentProviderAdapter(this, null, settings);
        hisList.setAdapter(ContentAdapter);

//        touchHelper = new ItemTouchHelper(new ContentListItemTouchHelperCallback(ContentAdapter));
//        touchHelper.attachToRecyclerView(hisList);

//        hisList.setItemAnimator(new DefaultItemAnimator() {
//            @Override
//            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
//                super.onAnimationFinished(viewHolder);
////                ContentAdapter.drawDetail();
//            }
//        });

        hisList.addOnItemTouchListener(new RecyclerViewContentProviderItemClickListener(this, hisList,
                new RecyclerViewContentProviderItemClickListener.OnItemClickListener() {
                    @Override
                    public void onSingleTapConfirmed(View view, int position) {
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(getBaseContext(), "LongItemClick", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDoubleTapItem(View view, int position, Context context) {
                    }
                }));

        ContentAdapter.setOnItemTouchListener(new RecyclerViewContentProviderAdapter.onTouchListener() {
            @Override
            public void onItemClick(View view, int position, ListRecyclerViewAdapter adapter) {
                Toast.makeText(getBaseContext(), "ItemClick", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongItemClick(View view, int position, ListRecyclerViewAdapter adapter) {
                Toast.makeText(getBaseContext(), "LongItemClick", Toast.LENGTH_LONG).show();

            }
        });
        Date da = new Date();
        DateFormat dt = DateFormat.getDateInstance(DateFormat.SHORT);

        ((SimpleDateFormat)dt).applyPattern("MM.dd.yyyy");
        String dateString = dt.format(da);

        date = (TextView) findViewById(R.id.date);
        date.setText(dateString);
        
        getLoaderManager().initLoader(0, null, menuLoader);
    }
}
