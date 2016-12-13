package com.example.dan.myapplication.execute_win.list.item;

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

public class RecyclerViewContentMarqueeItemClickListener implements RecyclerView.OnItemTouchListener{
    OnItemClickListener mListener;
    GestureDetector mGestureDetector;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onLongItemClick(View view, int position);
    }

    public RecyclerViewContentMarqueeItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(childView == null)
                    return false;
                try {
                    Integer.parseInt(((TextView) childView.findViewById(R.id.item)).getText().toString().substring(3));
                } catch (Exception ev) {
                    TextView tv = (TextView) childView.findViewById(R.id.item);
                    if (childView != null && mListener != null && tv != null && e.getX() > (tv.getLeft() + childView.getLeft()) &&
                            e.getX() < (tv.getRight() + childView.getLeft()) && e.getY()%childView.getHeight() > tv.getTop() &&
                            e.getY()%childView.getHeight() < tv.getBottom()) {
                        mListener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(childView == null)
                    return;
                TextView tv = (TextView) childView.findViewById(R.id.item);
                if (childView != null && mListener != null && tv != null && e.getX() > (tv.getLeft() + childView.getLeft()) &&
                        e.getX() < (tv.getRight() + childView.getLeft()) && e.getY()%childView.getHeight() > tv.getTop() &&
                        e.getY()%childView.getHeight() < tv.getBottom()) {
                    mListener.onLongItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        View childView = view.findChildViewUnder(e.getX(), e.getY());

//        try {
//            Integer.parseInt(((TextView)childView.findViewById(R.id.item)).getText().toString().substring(3));
//        }catch(Exception ev){
//            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
//                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
//                return true;
//            }
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

