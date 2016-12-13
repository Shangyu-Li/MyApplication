package com.example.dan.myapplication.SurfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.dan.myapplication.R;
import com.example.dan.myapplication.execute_win.Marquee.MarqueeAdapter;
import com.example.dan.myapplication.items;

import java.util.ArrayList;

/**
 * Created by Dan on 10/21/2016.
 */

public class viewDeliver {

    ArrayList<DetailView> dt = new ArrayList<DetailView>();

    View view;

    ArrayList<items> itemz = new ArrayList<items>();

    FrameLayout frame;

    RecyclerView rv;

    RecyclerView.OnScrollListener mOnScrollListener;

    public viewDeliver(final Context context, ArrayList<items> itemz, View view, FrameLayout frame) {
        this.view = view;
        this.itemz = itemz;
        this.frame = frame;
        deliver(context);

        if (rv != null)
            rv.addOnScrollListener((mOnScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    undraw();
                    deliver(context);
                    draw();
                }
            }));
        rv.setItemAnimator(new DefaultItemAnimator(){

//            @Override
//            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
//                super.onAnimationFinished(viewHolder);
//                undraw();
//                deliver(context);
//                draw();
//            }
        });
    }

    public void deliver(final Context context) {

//        float density = context.getResources().getDisplayMetrics().density;

        rv = (RecyclerView) view.findViewById(R.id.item);

        Rect parentRect = new Rect();
        rv.getGlobalVisibleRect(parentRect);

        Rect frameRect = new Rect();
        ((FrameLayout)view.getParent().getParent()).getGlobalVisibleRect(frameRect);

        dt.clear();

        MarqueeAdapter.VH vh;
        Rect rect = new Rect();
        Rect textRect = new Rect();
        TextView tv;
        for (int i = 0; i < itemz.size(); i++) {    //items
            if ((vh = (MarqueeAdapter.VH) rv.findViewHolderForLayoutPosition(i)) != null &&
                    (tv = vh.textview).getGlobalVisibleRect(rect)) {
//                ;  //rect

                ArrayList<String> detail = itemz.get(i).getDetail();
                String text, name, det;
                int startFrom = 0;
                Paint p = new Paint();
                float nameLength, beforeNameLength, detailLength;

                for (int j = 0; j < detail.size(); j++) { //item
                    det = detail.get(j);
                    if (det != null && det != "") {
                        text = tv.getText().toString();
                        name = itemz.get(i).getItemName(j);
                        startFrom = text.indexOf(name, startFrom) + name.length();

                        p.setTextSize(20);
                        nameLength = p.measureText(name);
                        beforeNameLength = p.measureText(text.substring(0, startFrom - name.length()));
                        detailLength = p.measureText(det);

                        p.getTextBounds(det, 0, det.length(), textRect);
                        Bitmap b = Bitmap.createBitmap(textRect.width() + 3, textRect.height() + 4, Bitmap.Config.ARGB_8888);
                        Canvas c = new Canvas(b);

                        p.setColor(Color.GREEN);
                        p.setStyle(Paint.Style.FILL);
                        c.drawPaint(p);

                        p.setColor(Color.GRAY);
                        p.setAntiAlias(true);


                        int x, w, tmp;
                        if ((tmp = (int) ((nameLength / 2 + beforeNameLength - detailLength / 2)
                                + rect.left) + textRect.width() + 3) > parentRect.right) {
                            w = textRect.width() + 3 - (tmp - parentRect.right);
                            x = (int) ((nameLength / 2 + beforeNameLength - detailLength / 2)
                                    + rect.left);
                            c.drawText(det, 0, textRect.height() - 1, p);
                        } else if ((tmp = rect.right - tv.getWidth()) < 0) {
                            x = (int) ((nameLength / 2 + beforeNameLength - detailLength / 2)
                                    + rect.left) + tmp;
                            if (x < 0) {
                                w = textRect.width() + 3 + x;
                                c.drawText(det, x, textRect.height() - 1, p);
                                x = 0;
                            } else {
                                w = textRect.width() + 3;
                                c.drawText(det, 0, textRect.height() - 1, p);
                            }
                        } else {
                            x = (int) ((nameLength / 2 + beforeNameLength - detailLength / 2)
                                    + rect.left);
                            w = textRect.width() + 3;
                            c.drawText(det, 0, textRect.height() - 1, p);
                        }

                        if (w < 0)
                            w = 0;
                        dt.add(new DetailView(context, x, (rect.bottom - frameRect.top), w, textRect.height() + 4, b, p));
                    }
                }
            }
        }
//        }
    }

    public void draw() {
        if(getDetailViewNum() > 0) {
            for (DetailView dv : dt) {
                FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(dv.width, dv.height);
                param.leftMargin = dv.x;
                param.topMargin = dv.y;
                frame.addView(dv, param);
                dv.invalidate();
                final viewDeliver vv = this;
                dv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undraw();
                        rv.removeOnScrollListener(mOnScrollListener);
                        rv = null;
                        view = null;
                        dt = null;
                        frame = null;
                        itemz = null;
                        if (mListener != null) {
                            mListener.onRemove(vv);
                        }
                    }
                });
            }
        }
    }

    public void undraw() {
        if(getDetailViewNum() > 0) {
            for (DetailView dv : dt) {
                frame.removeView(dv);
            }
        }
    }

    public RecyclerView getRecyclerView(){
        return rv;
    }

    onRemoveListener mListener;

    public void setOnRemovedListener(onRemoveListener listener){    mListener = listener;   }

    public interface onRemoveListener{
        void onRemove(viewDeliver vd);
    }

    public int getDetailViewNum(){
        if(dt == null)
            return 0;
        else
            return dt.size();
    }
}

