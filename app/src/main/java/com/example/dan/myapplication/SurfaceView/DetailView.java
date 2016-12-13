package com.example.dan.myapplication.SurfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Dan on 10/21/2016.
 */

public class DetailView extends View {

    public int x = 0, y = 0;
    Paint paint = new Paint();
    Bitmap bitmap;
    public int width, height;

    public DetailView(Context context, int x, int y, int w, int h, Bitmap b, Paint p) {
        super(context);
        this.x = x;
        this.y = y;
        paint = p;
        bitmap = b;
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

//    public FrameLayout.LayoutParams getParams(){
//        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(textRect.width() + 3, textRect.height() + 4);
//        param.leftMargin = dt.get(0).getX();
//        param.topMargin = dt.get(0).getY();
//        return param;
//    }
}


