package com.example.dan.myapplication.execute_win.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.dan.myapplication.R;

/**
 * Created by Dan on 10/21/2016.
 */

public class RecyclerViewContentProviderItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;
    GestureDetector mGestureDetector;

    public interface OnItemClickListener {
        void onSingleTapConfirmed(View view, int position);

        void onLongItemClick(View view, int position);

        void onDoubleTapItem(View view, int position, Context context);
    }

    public RecyclerViewContentProviderItemClickListener(final Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                try {
                    TextView v = (TextView) childView.findViewById(R.id.price);
                    if (e.getX() > v.getLeft() && e.getX() < v.getRight() &&
                            e.getY() % childView.getHeight() < v.getBottom() && e.getY() % childView.getHeight() > v.getTop()) {
                        mListener.onSingleTapConfirmed(childView, recyclerView.getChildAdapterPosition(childView));
                        return true;
                    }
                } catch (Exception ev) {
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                try {
                    TextView v = (TextView) childView.findViewById(R.id.price);
                    if (e.getX() > v.getLeft() && e.getX() < v.getRight() &&
                            e.getY() % childView.getHeight() < v.getBottom() && e.getY() % childView.getHeight() > v.getTop()) {
                        mListener.onLongItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                    }
                } catch (Exception ev) {
                    throw ev;
                }
            }

            @Override
            public boolean onDoubleTap(MotionEvent e){
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                try {
                    TextView v = (TextView) childView.findViewById(R.id.price);
                    if (e.getX() > v.getLeft() && e.getX() < v.getRight() &&
                            e.getY() % childView.getHeight() < v.getBottom() && e.getY() % childView.getHeight() > v.getTop()) {
                        mListener.onDoubleTapItem(childView, recyclerView.getChildAdapterPosition(childView), context);
                    }
                } finally {
                    return false;
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
//        View childView = view.findChildViewUnder(e.getX(), e.getY());
//        try {
//            if(childView.findViewById(R.id.price) instanceof TextView) {
//                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
//                return true;
//            }
//        }catch(Exception ev){
//        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

