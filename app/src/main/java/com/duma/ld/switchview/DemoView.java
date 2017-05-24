package com.duma.ld.switchview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liudong on 2017/5/24.
 */

public class DemoView extends View {
    public DemoView(Context context) {
        this(context, null);
    }

    public DemoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DemoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint mPaint;
    int rgb = Color.rgb(140, 193, 82);
    private Path path;

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(rgb);
        mPaint.setAntiAlias(true);

        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制圆角矩形
        RectF roundRect = new RectF(100, 850, 1000, 1200);
//第一种方法绘制圆角矩形
        path.addRoundRect(roundRect, 175, 175, Path.Direction.CW);
//第三种方法绘制圆角矩形
//float[] radii中有8个值，依次为左上角，右上角，右下角，左下角的rx,ry
//        RectF roundRectT = new RectF(600, 950, 800, 1100);
//        path.addRoundRect(roundRectT, new float[]{200, 200, 200, 200, 200, 200, 200, 200}, Path.Direction.CCW);
        canvas.drawPath(path, mPaint);
    }
}
