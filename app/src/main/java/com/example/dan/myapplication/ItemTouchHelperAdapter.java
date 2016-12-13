package com.example.dan.myapplication;

/**
 * Created by Dan on 10/21/2016.
 */
public interface ItemTouchHelperAdapter{
    void onItemDismiss(int position);

    void onItemMove(int fromPosition, int toPosition);
}

