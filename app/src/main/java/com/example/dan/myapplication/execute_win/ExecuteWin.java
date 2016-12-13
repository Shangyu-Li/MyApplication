package com.example.dan.myapplication.execute_win;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dan.myapplication.R;
import com.example.dan.myapplication.SQLiteDataBase.MenuProvider;
import com.example.dan.myapplication.SurfaceView.viewDeliver;
import com.example.dan.myapplication.dialog.ListDialogFragment;
import com.example.dan.myapplication.execute_win.Marquee.MarqueeAdapter;
import com.example.dan.myapplication.execute_win.Marquee.RecyclerItemClickListener;
import com.example.dan.myapplication.execute_win.Marquee.SimpleItemTouchHelperCallback;
import com.example.dan.myapplication.execute_win.list.ContentListItemTouchHelperCallback;
import com.example.dan.myapplication.execute_win.list.RecyclerViewContentProviderAdapter;
import com.example.dan.myapplication.execute_win.list.RecyclerViewContentProviderItemClickListener;
import com.example.dan.myapplication.execute_win.list.item.ListRecyclerViewAdapter;
import com.example.dan.myapplication.execute_win.list.item.RecyclerViewContentMarqueeItemClickListener;
import com.example.dan.myapplication.items;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Stack;

import static com.example.dan.myapplication.R.id.marquee;


/**
 * Created by Dan on 10/21/2016.
 */

public class ExecuteWin extends FragmentActivity {
    public final String PREFS_NAME = "MENU_PREFS";
    private SharedPreferences settings;

    private boolean MODIFYING = false;
    private int oldNum = -1;
//    private boolean FIND;

    private enum select {Marquee, List, Normal, ListNormal}

    ;
    private select mSelect = select.Normal;
    private boolean CONFIRM = true;
    private ListRecyclerViewAdapter mListRecyclerViewAdapter;
    private int mItemSelectedId = -1, mSelectedId = -1;
    private int mLastItemSelectedId = -1, mLastSelectedId = -1;
    private ContentValues values;

    private items item;
    private String name;
    private ItemAdapter itemAdapter;

    private RecyclerView result;
    private MarqueeAdapter marqueeAdapter;
    private MarqueeAdapter lastSelectedMarquee;
    private LinearLayoutManager layoutManager;

    private ItemTouchHelper.Callback callback;
    private ItemTouchHelper touchHelper;

    //    private int price;
    private int k = 0;
    private RecyclerView gridview;

    private RecyclerView listview;
    private RecyclerViewContentProviderAdapter ContentAdapter;
    private LinearLayoutManager LlayoutManager;

    private Stack<cacheWrap> cacheMarquee = new Stack<cacheWrap>();

    void pushCache(MarqueeAdapter marqueeAdapter, ItemAdapter.which wh, select s, items item, String name,
                   int oldN, int selectedId, int ItemSelectedId) {
        for (Iterator<cacheWrap> i = cacheMarquee.iterator(); i.hasNext(); ) {
            cacheWrap tmp = i.next();
            if ((tmp.Select == select.Marquee || tmp.Select == select.Normal) &&
                    tmp.Select == s && tmp.ItemSelectedId == ItemSelectedId)
                i.remove();
            else if ((tmp.Select == select.List || tmp.Select == select.ListNormal) &&
                    tmp.Select == s && tmp.SelectedId == selectedId && tmp.ItemSelectedId == ItemSelectedId)
                i.remove();
        }

        if (cacheMarquee.size() > 5)
            cacheMarquee.remove(0);
        cacheMarquee.push(new cacheWrap(marqueeAdapter, wh, s, item, name, oldN, selectedId, ItemSelectedId));
    }

    void checkOverlapCur(cacheWrap wrap) {
        if(wrap == null)    return;
        for (Iterator<cacheWrap> i = cacheMarquee.iterator(); i.hasNext(); ) {
            cacheWrap tmp = i.next();
            if ((tmp.Select == select.Marquee || tmp.Select == select.Normal) && tmp.Select == mSelect &&
                    tmp.ItemSelectedId == mItemSelectedId)
                i.remove();
            else if ((tmp.Select == select.List || tmp.Select == select.ListNormal) && tmp.Select == mSelect &&
                    tmp.SelectedId == mSelectedId && tmp.ItemSelectedId == mItemSelectedId)
                i.remove();
        }
    }

    void pushCache(cacheWrap wrap) {
        for (Iterator<cacheWrap> i = cacheMarquee.iterator(); i.hasNext(); ) {
            cacheWrap tmp = i.next();
            if ((tmp.Select == select.Marquee || tmp.Select == select.Normal) &&
                    tmp.Select == wrap.Select && tmp.ItemSelectedId == wrap.ItemSelectedId)
                i.remove();
            else if ((tmp.Select == select.List || tmp.Select == select.ListNormal) && tmp.Select == wrap.Select &&
                    tmp.SelectedId == wrap.SelectedId && tmp.ItemSelectedId == wrap.ItemSelectedId)
                i.remove();
        }

        if (cacheMarquee.size() > 5)
            cacheMarquee.remove(0);
        cacheMarquee.push(wrap);
    }

    boolean popCache() {
        if (!cacheMarquee.isEmpty()) {
            cacheMarquee.pop().restoreData();
            return true;
        }
        return false;
    }

    cacheWrap tmpWrap;

    private View vview;

    private FrameLayout frame;

    private TextView date;

//    Gson gson;

    private LoaderManager.LoaderCallbacks<Cursor> menuLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    CursorLoader cursorLoader = new CursorLoader(ExecuteWin.this, MenuProvider.CONTENT_URI,
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.execute_win);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
//                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        settings = getSharedPreferences(PREFS_NAME, 0);

        frame = (FrameLayout) findViewById(R.id.frame);

        gridview = (RecyclerView) findViewById(R.id.gridView);
        gridview.setHasFixedSize(true);
        gridview.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));

        itemAdapter = new ItemAdapter(settings, this);
        gridview.setAdapter(itemAdapter);

        gridview.addOnItemTouchListener(new RecyclerItemClickListener(this, gridview,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        switch (itemAdapter.getWhich()) {
                            case NORMAL:
                                switch (mSelect) {
                                    case Normal:
                                        setSelect(marqueeAdapter, -1, marqueeAdapter.getItemCount() - 1);
                                        mItemSelectedId = marqueeAdapter.getItemCount() - 1;

                                        name = settings.getString("name" + Integer.toString(position), "none");
                                        item = new items(name, settings);
                                        marqueeAdapter.addStraightItem(item);
//                        str = name + " ";//
//                                price = settings.getInt(name, 0);
                                        if (settings.getInt(name + "num", 0) > 0) {
                                            itemAdapter.setItem(name);
                                            itemAdapter.setWhich(ItemAdapter.which.DETAIL);
                                        } else {
                                            itemAdapter.setWhich(ItemAdapter.which.NUM);
                                        }
                                        result.scrollToPosition(marqueeAdapter.getItemCount() - 1);
                                        break;
                                    case ListNormal:
                                        mListRecyclerViewAdapter.setSelect(mListRecyclerViewAdapter.getItemCount());
                                        mItemSelectedId = mListRecyclerViewAdapter.getItemCount() - 1;

                                        name = settings.getString("name" + Integer.toString(position), "none");
                                        item = new items(name, settings);
                                        mListRecyclerViewAdapter.addStraightItem(item);
                                        if (settings.getInt(name + "num", 0) > 0) {
                                            itemAdapter.setItem(name);
                                            itemAdapter.setWhich(ItemAdapter.which.DETAIL);
                                        } else {
                                            itemAdapter.setWhich(ItemAdapter.which.NUM);
                                        }

                                        ((RecyclerView) vview.findViewById(R.id.item)).scrollToPosition(mListRecyclerViewAdapter.getItemCount());
                                        break;
                                }
                                break;
                            case DETAIL:
                                switch (mSelect) {
                                    case Normal:
                                        result.scrollToPosition(marqueeAdapter.getItemCount() - 1);
                                        marqueeAdapter.addDetail(marqueeAdapter.find(item), name,
                                                settings.getString(itemAdapter.getItem() + Integer.toString(position), "none"));
                                        itemAdapter.setWhich(ItemAdapter.which.NUM);
                                        break;
                                    case ListNormal:
                                        ((RecyclerView) vview.findViewById(R.id.item)).
                                                scrollToPosition(mListRecyclerViewAdapter.getItemCount() - 1);
                                        mListRecyclerViewAdapter.addDetail(mListRecyclerViewAdapter.find(item),
                                                name, settings.getString(itemAdapter.getItem() + Integer.toString(position), "none"));
                                        itemAdapter.setWhich(ItemAdapter.which.NUM);
                                        break;
                                }
                                break;
                            case NUM:
                                CONFIRM = false;
                                String total;
                                int num, idx;
                                switch (mSelect) {
                                    case Normal:
                                        result.scrollToPosition(marqueeAdapter.getItemCount() - 1);

                                        idx = marqueeAdapter.indexOf(item);/**/
                                        if ((num = marqueeAdapter.getItemNum(idx)) == -1)
                                            num = 0;
                                        total = Integer.toString(num) + Integer.toString(position);
                                        marqueeAdapter.setNum(idx, Integer.parseInt(total));
//                                marqueeAdapter.setNum(marqueeAdapter.find(wrapItem), Integer.parseInt(total));
                                        break;
                                    case Marquee:
                                        if (oldNum == -1) {
                                            oldNum = 0;
                                            marqueeAdapter.setNum(mItemSelectedId, 0);
                                        }
                                        total = Integer.toString(marqueeAdapter.getItemNum(mItemSelectedId)) +
                                                Integer.toString(position);
                                        marqueeAdapter.setNum(mItemSelectedId, Integer.parseInt(total));

                                        result.scrollToPosition(mItemSelectedId + 1);
                                        break;
                                    case List:
//                                ContentAdapter.scrollTo(SelectedId, ItemSelectedId);
//                                if(k == 0) {k++;
                                        mListRecyclerViewAdapter = (ListRecyclerViewAdapter)
                                                ((RecyclerView) listview.getChildAt(mSelectedId).findViewById(R.id.item)).getAdapter();

                                        if (oldNum == -1) {
                                            oldNum = 0;
                                            mListRecyclerViewAdapter.setNum(mItemSelectedId, 0);
                                        }
                                        total = Integer.toString(mListRecyclerViewAdapter.getItemNum(mItemSelectedId)) +
                                                Integer.toString(position);
                                        mListRecyclerViewAdapter.setNum(mItemSelectedId, Integer.parseInt(total));
//                                }
//                                ContentAdapter.scrollTo(SelectedId, ItemSelectedId);
//                                ((RecyclerView) listview.getChildAt(SelectedId).findViewById(R.id.wrapItem)).scrollToPosition(ItemSelectedId);
//                                ContentAdapter.setScroll(SelectedId, ItemSelectedId);
//                                ContentAdapter.notifyDataSetChanged();
                                        break;
                                    case ListNormal:
//                                ((RecyclerView) vview.findViewById(R.id.wrapItem)).scrollToPosition(mListRecyclerViewAdapter.getItemCount()-1);

                                        idx = mListRecyclerViewAdapter.indexOf(item);
                                        if ((num = mListRecyclerViewAdapter.getItemNum(idx)) == -1)
                                            num = 0;
                                        total = Integer.toString(num) + Integer.toString(position);
                                        mListRecyclerViewAdapter.setNum(idx, Integer.parseInt(total));
//                                mListRecyclerViewAdapter.setNum(mListRecyclerViewAdapter.find(wrapItem), Integer.parseInt(total));

                                        ContentAdapter.notifyItemChanged(mListRecyclerViewAdapter.getItemCount() - 1);
                                        ContentAdapter.scrollTo(mSelectedId, mListRecyclerViewAdapter.getItemCount() - 1);
                                        break;
                                }
                                break;
                        }
                        Toast.makeText(getBaseContext(), "ItemClick", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(getBaseContext(), "LongItemClick", Toast.LENGTH_LONG).show();

                    }
                }));

        result = (RecyclerView) findViewById(marquee);
        result.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        result.setLayoutManager(layoutManager);

        marqueeAdapter = new MarqueeAdapter(settings);
        result.setAdapter(marqueeAdapter);

        callback = new SimpleItemTouchHelperCallback(marqueeAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(result);

        result.addOnItemTouchListener(new RecyclerViewContentMarqueeItemClickListener(this, result,
                new RecyclerViewContentMarqueeItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        /**
                         *  store incomplete wrapItem
                         */
                        if (itemAdapter.getWhich() != ItemAdapter.which.NORMAL) {
                            if (mSelect == select.ListNormal || mSelect == select.List) {
                                tmpWrap = new cacheWrap(mListRecyclerViewAdapter, itemAdapter.getWhich(), mSelect, item,
                                        name, oldNum, mSelectedId, mItemSelectedId);
                                pushCache(tmpWrap);
                            } else if (mSelect == select.Normal || mSelect == select.Marquee) {
                                tmpWrap = new cacheWrap(marqueeAdapter, itemAdapter.getWhich(), mSelect, item,
                                        name, oldNum, mSelectedId, mItemSelectedId);
                                pushCache(tmpWrap);
                            }
                        }
                        setSelect(marqueeAdapter, -1, position);
                        marqueeAdapter.notifyDataSetChanged();

                        ((RecyclerView) view.getParent()).getLayoutManager().scrollToPosition(position);
                        mSelect = select.Marquee;
                        mItemSelectedId = position;
                        itemAdapter.setWhich(ItemAdapter.which.NUM);
                        MODIFYING = true;

                        checkOverlapCur(tmpWrap);
                        Toast.makeText(getBaseContext(), "ItemClick", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(getBaseContext(), "LongItemClick", Toast.LENGTH_LONG).show();

                    }
                }));

        listview = (RecyclerView) findViewById(R.id.ListView);
        listview.setHasFixedSize(true);

        listview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ContentAdapter = new RecyclerViewContentProviderAdapter(this, null, settings);
        listview.setAdapter(ContentAdapter);

        touchHelper = new ItemTouchHelper(new ContentListItemTouchHelperCallback(ContentAdapter));
        touchHelper.attachToRecyclerView(listview);

        listview.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                super.onAnimationFinished(viewHolder);
//                ContentAdapter.drawDetail();
            }
        });

        listview.addOnItemTouchListener(new RecyclerViewContentProviderItemClickListener(this, listview,
                new RecyclerViewContentProviderItemClickListener.OnItemClickListener() {
                    @Override
                    public void onSingleTapConfirmed(View view, int position) {
                        /**
                         *  store incomplete wrapItem
                         */
                        if (mSelect == select.ListNormal || mSelect == select.List) {
                            tmpWrap = new cacheWrap(mListRecyclerViewAdapter, itemAdapter.getWhich(), mSelect, item,
                                    name, oldNum, mSelectedId, mItemSelectedId);
                        } else if (mSelect == select.Normal || mSelect == select.Marquee) {
                            tmpWrap = new cacheWrap(marqueeAdapter, itemAdapter.getWhich(), mSelect, item,
                                    name, oldNum, mSelectedId, mItemSelectedId);
                        }


                        if (mActionMode == null) {
                            mActionMode = startActionMode(mActionModeCallback);
//                    view.setSelected(true);
                        }
                        mSelectedId = position;
                        mListRecyclerViewAdapter = (ListRecyclerViewAdapter) ((RecyclerView)
                                view.findViewById(R.id.item)).getAdapter();
                        vview = view;
//                ContentAdapter.scrollTo(SelectedId, mListRecyclerViewAdapter.getItemCount() - 1);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        ContentAdapter.onItemDismiss(position);
                        Toast.makeText(getBaseContext(), "LongItemClick", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDoubleTapItem(View view, int position, Context context) {
                        viewDeliver vd = new viewDeliver(context, ((ListRecyclerViewAdapter) ((RecyclerView) view.
                                findViewById(R.id.item)).getAdapter()).getItemz(), view, frame);
                        vd.draw();
                        ContentAdapter.addViewDeliver(vd);
                    }
                }));

        ContentAdapter.setOnItemTouchListener(new RecyclerViewContentProviderAdapter.onTouchListener() {
            @Override
            public void onItemClick(View view, int position, ListRecyclerViewAdapter adapter) {
                /**
                 *  store incomplete wrapItem
                 */
                if (itemAdapter.getWhich() != ItemAdapter.which.NORMAL) {
                    if (mSelect == select.ListNormal || mSelect == select.List) {
                        tmpWrap = new cacheWrap(mListRecyclerViewAdapter, itemAdapter.getWhich(), mSelect, item,
                                name, oldNum, mSelectedId, mItemSelectedId);
                        pushCache(tmpWrap);
                    } else if (mSelect == select.Normal || mSelect == select.Marquee) {
                        tmpWrap = new cacheWrap(marqueeAdapter, itemAdapter.getWhich(), mSelect, item,
                                name, oldNum, mSelectedId, mItemSelectedId);
                        pushCache(tmpWrap);
                    }
                }


                mSelect = select.List;
                mItemSelectedId = position;
                mSelectedId = ((RecyclerView) view.getParent().getParent().getParent()).
                        getChildAdapterPosition((LinearLayout) view.getParent().getParent());
                ;

                itemAdapter.setWhich(ItemAdapter.which.NUM);
                mListRecyclerViewAdapter = adapter;
                MODIFYING = true;

                setSelect(null, mSelectedId, position);
                adapter.notifyItemChanged(position);
                
                checkOverlapCur(tmpWrap);
                Toast.makeText(getBaseContext(), "ItemClick", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongItemClick(View view, int position, ListRecyclerViewAdapter adapter) {
                mListRecyclerViewAdapter = adapter;
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
//
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(items.class, new itemsDeserializer());
//        gsonBuilder.registerTypeAdapter(wrapItem.class, new itemDeserializer());
//        gson = gsonBuilder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.execute_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.mini_statistic:
                showStatis();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void showStatis(){
        DialogFragment dialog = new ListDialogFragment(settings);
        dialog.show(getSupportFragmentManager(), "Mod");
    }

    public void OnAddButtonClicked(View view) {
        if (MODIFYING) {
            Toast.makeText(this, "Data modifying", Toast.LENGTH_SHORT).show();
        } else if (marqueeAdapter.calTotal() != 0) {
            Uri uri = getContentResolver().insert(MenuProvider.CONTENT_URI, marqueeAdapter.wrap());
            Cursor c = getContentResolver().query(MenuProvider.CONTENT_URI, new String[]{"_id", "items"}, null, null, null);
            ContentAdapter.swapCursor(c);
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
            marqueeAdapter.clear();
//            str = "";
            itemAdapter.setWhich(ItemAdapter.which.NORMAL);
        } else {
            Toast.makeText(this, "Please insert something", Toast.LENGTH_LONG).show();
        }
    }

    public void OnClear(View view) {
        MenuProvider.reset();
        MenuProvider.ruin();
        Cursor c = getContentResolver().query(MenuProvider.CONTENT_URI, new String[]{"_id", "items"}, null, null, null);
        ContentAdapter.swapCursor(c);
    }

    public void OnBack(View view) {
        marqueeAdapter.clear();
//        str = "";
//        FIRST_TIME = true;
        itemAdapter.setWhich(ItemAdapter.which.NORMAL);
    }

    public void OnConfirm(View view) {
        if (!CONFIRM) {
            int newNum, findPos, originPos;
            switch (mSelect) {
                case Normal:
                    findPos = marqueeAdapter.find(item);
                    originPos = marqueeAdapter.getItems().indexOf(item);

                    if (originPos != findPos) {
                        if (marqueeAdapter.getItemNum(originPos) == 0) {
                            marqueeAdapter.remove(originPos);
                        } else {
                            marqueeAdapter.additem(marqueeAdapter.getItem(originPos));
                            marqueeAdapter.remove(originPos);
                        }
                    } else if (marqueeAdapter.getItemNum(originPos) == 0) {
                        marqueeAdapter.remove(originPos);
                    }
//                    str = "";
//                    FIRST_TIME = true;
                    setSelect(marqueeAdapter, -1, -1);
                    marqueeAdapter.notifyDataSetChanged();
                    break;
                case Marquee:
//                    price = marqueeAdapter.getItemPrice(ItemSelectedId);
                    newNum = marqueeAdapter.getItemNum(mItemSelectedId);
                    if (newNum == 0)
                        marqueeAdapter.remove(mItemSelectedId);
                    MODIFYING = false;
                    mSelect = select.Normal;
//                    FIRST_TIME = true;
                    oldNum = -1;

                    setSelect(marqueeAdapter, -1, -1);
                    marqueeAdapter.notifyDataSetChanged();
                    break;
                case List:
//                    price = mListRecyclerViewAdapter.getItemPrice(ItemSelectedId);
                    newNum = mListRecyclerViewAdapter.getItemNum(mItemSelectedId);
                    if (newNum == 0)
                        mListRecyclerViewAdapter.remove(mItemSelectedId);
                    if (mListRecyclerViewAdapter.calTotal() == 0) {
                        ContentAdapter.onItemDismiss(mItemSelectedId);
                    } else {
                        values = mListRecyclerViewAdapter.wrap();
//                        values.put("price", Integer.toString(total));
                        Uri uri = Uri.parse(MenuProvider.CONTENT_URL_STRING + "/" + ContentAdapter.getCursorId(mSelectedId));
                        getContentResolver().update(uri, values, null, null);
//                        getContentResolver().notifyChange(uri, null);
                        Cursor c = getContentResolver().query(MenuProvider.CONTENT_URI,
                                new String[]{"_id", "items"}, null, null, null);
                        ContentAdapter.swapCursor(c);
                    }
//                    FIRST_TIME = true;
                    MODIFYING = false;
                    mSelect = select.Normal;
                    oldNum = -1;

                    setSelect(mListRecyclerViewAdapter, mSelectedId, -1);
                    ContentAdapter.setScroll(-1, -1);
                    mListRecyclerViewAdapter.notifyDataSetChanged();
                    break;
                case ListNormal:
                    findPos = mListRecyclerViewAdapter.find(item);
                    originPos = mListRecyclerViewAdapter.getItems().indexOf(item);
                    if (originPos != findPos) {
                        if (mListRecyclerViewAdapter.getItemNum(originPos) == 0) {
                            mListRecyclerViewAdapter.remove(originPos);
                            Cursor c = getContentResolver().query(MenuProvider.CONTENT_URI,
                                    new String[]{"_id", "items"}, null, null, null);
                            ContentAdapter.swapCursor(c);
                        } else {
                            mListRecyclerViewAdapter.additem(mListRecyclerViewAdapter.getItem(originPos));
                            mListRecyclerViewAdapter.remove(originPos);

                            values = mListRecyclerViewAdapter.wrap();
                            Uri uri = Uri.parse(MenuProvider.CONTENT_URL_STRING + "/" + ContentAdapter.getCursorId(mSelectedId));
                            getContentResolver().update(uri, values, null, null);
                            Cursor c = getContentResolver().query(MenuProvider.CONTENT_URI,
                                    new String[]{"_id", "items"}, null, null, null);
                            ContentAdapter.swapCursor(c);
                        }
                    } else if (mListRecyclerViewAdapter.getItemNum(originPos) == 0) {
                        mListRecyclerViewAdapter.remove(originPos);
                    } else {
                        values = mListRecyclerViewAdapter.wrap();
                        Uri uri = Uri.parse(MenuProvider.CONTENT_URL_STRING + "/" + ContentAdapter.getCursorId(mSelectedId));
                        getContentResolver().update(uri, values, null, null);
                        Cursor c = getContentResolver().query(MenuProvider.CONTENT_URI,
                                new String[]{"_id", "items"}, null, null, null);
                        ContentAdapter.swapCursor(c);
                    }

                    setSelect(null, mSelectedId, -1);
                    ContentAdapter.setScroll(-1, -1);
                    mListRecyclerViewAdapter.notifyDataSetChanged();
                    break;
            }
            CONFIRM = true;
            if (!popCache()) {
                mSelect = select.Normal;
                itemAdapter.setWhich(ItemAdapter.which.NORMAL);
            }
        }
    }

    public void setSelect(MarqueeAdapter marqueeAdapter, int select, int position) {
        if (lastSelectedMarquee != null) {
            lastSelectedMarquee.setSelect(-1);
            lastSelectedMarquee = null;
        } else if (mLastItemSelectedId != -1) {
            ContentAdapter.setSelect(mLastItemSelectedId, -1);
            ContentAdapter.notifyItemChanged(mLastSelectedId);
            mLastItemSelectedId = -1;
            mLastSelectedId = -1;
        }

        if (marqueeAdapter == null) {
            ContentAdapter.setSelect(select, position);
            ContentAdapter.notifyItemChanged(select);
            mLastItemSelectedId = position;
            mLastSelectedId = select;
        } else {
            lastSelectedMarquee = marqueeAdapter;
            marqueeAdapter.setSelect(position);
        }
    }

    LinearLayout switchLinear;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            LinearLayout parent;
            switch (item.getItemId()) {
                case R.id.add:
                    pushCache(tmpWrap);
                    itemAdapter.setWhich(ItemAdapter.which.NORMAL);
                    mSelect = select.ListNormal;
                    MODIFYING = true;
                    mode.finish();
                    return true;
                case R.id.delete:
                    parent = (LinearLayout) findViewById(R.id.parentLinear);
                    parent.removeViewAt(1);
                    parent.addView(switchLinear);
                    mode.finish();
                    return true;
                case R.id.detail:
                    switchLinear = (LinearLayout) findViewById(R.id.linearSwitch);
                    parent = (LinearLayout) findViewById(R.id.parentLinear);
                    parent.removeView(switchLinear);

                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0 ,ViewGroup.LayoutParams.MATCH_PARENT);
                    param.weight = 1;
                    LinearLayout child = new LinearLayout(getBaseContext());
                    child.setLayoutParams(param);
                    child.setOrientation(LinearLayout.VERTICAL);
                    parent.addView(child);
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };
    private ActionMode mActionMode;

    class cacheWrap {
        ItemAdapter.which which;

        select Select;

        MarqueeAdapter marquee;

        items wrapItem;
        //setitem(name)
        String wrapName;

        int oldNum;

        int SelectedId;

        int ItemSelectedId;

        cacheWrap(MarqueeAdapter adapter, ItemAdapter.which wh, select s, items wrapItem, String name, int oldN,
                  int selectedId, int ItemSelectedId) {
            which = wh;
            Select = s;
            marquee = adapter;
            this.wrapItem = wrapItem;
            this.wrapName = name;
            oldNum = oldN;
            SelectedId = selectedId;
            if (mSelect == select.Normal || mSelect == select.ListNormal)
                this.ItemSelectedId = adapter.getItemCount() - 2;
            else
                this.ItemSelectedId = ItemSelectedId;
        }

        public void restoreData() {
            if (Select == select.ListNormal || Select == select.List) {
                mListRecyclerViewAdapter = (ListRecyclerViewAdapter) marquee;
                setSelect(null, SelectedId, ItemSelectedId);
            } else {
                setSelect(marqueeAdapter, -1, ItemSelectedId);
            }
            itemAdapter.setWhich(which);
            mSelect = Select;
            item = this.wrapItem;
            name = wrapName;
            mSelectedId = SelectedId;
            mItemSelectedId = ItemSelectedId;
        }
    }
}

