package com.duma.ld.mylibrary;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by liudong on 2017/5/19.
 */

public class SwitchView extends View {
    private int bgColor;
    private String leftColor;
    private String rightColor;
    private String textLeftColor;
    private String textRightColor;

    private Paint bgPaint;//背景画笔
    private Paint clickPaint;//点击画笔
    private Paint leftTextPaint;//左边字的画笔
    private Paint rightTextPaint;//点击字的画笔

    private String clickColor;

    private float mWidth, mHeight;
    private float recWidth;//弧线宽度

    private float mClickWidth;//里面的小椭圆的宽度

    private float juxinWidth;//去掉弧线的宽度


    private Path bgPath;//背景大的路径
    private Path clickPath;//小的路径

    private ValueAnimator anim;
    private boolean checked;
    private float padding;

    private String textLeft, textRight;
    int rgb = Color.rgb(255, 255, 255);

    private float mLeftTextX, mLiftTextY, mRightTextX, mRightTexty;

    public SwitchView(Context context) {
        this(context, null);
    }

    public SwitchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        initPaint();
    }

    private void init(@Nullable AttributeSet attrs) {
        // 获取自定义属性
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SwitchView);
        bgColor = a.getColor(R.styleable.SwitchView_bgColor, rgb);
        leftColor = String.valueOf(a.getColor(R.styleable.SwitchView_leftColor, Color.rgb(34, 139, 34)));
        rightColor = String.valueOf(a.getColor(R.styleable.SwitchView_rightColor, Color.rgb(34, 139, 34)));
        textLeftColor = String.valueOf(a.getColor(R.styleable.SwitchView_textLeftColor, Color.rgb(0, 0, 0)));
        textRightColor = String.valueOf(a.getColor(R.styleable.SwitchView_textRightColor, Color.rgb(0, 0, 0)));
        checked = a.getBoolean(R.styleable.SwitchView_setChecked, false);
        textLeft = a.getString(R.styleable.SwitchView_textLeft);
        textRight = a.getString(R.styleable.SwitchView_textRight);
        padding = a.getDimension(R.styleable.SwitchView_padding, dp2px(4));
        recWidth = a.getDimension(R.styleable.SwitchView_RadiusWight, dp2px(30));
        a.recycle();
    }

    private boolean isChecked() {
        return checked;
    }

    //初始化画笔
    private void initPaint() {
        bgPaint = new Paint();
        bgPaint.setColor(bgColor);
        bgPaint.setAntiAlias(true);

        clickPaint = new Paint();
        refreshColor();
        clickPaint.setAntiAlias(true);

        leftTextPaint = new Paint();
        leftTextPaint.setTextSize(sp2px(14));
        leftTextPaint.setAntiAlias(true);
        rightTextPaint = new Paint();
        rightTextPaint.setTextSize(sp2px(14));
        rightTextPaint.setAntiAlias(true);

        if (!isChecked()) {
            leftTextPaint.setColor(rgb);
            rightTextPaint.setColor(Integer.parseInt(textRightColor));
        } else {
            rightTextPaint.setColor(rgb);
            leftTextPaint.setColor(Integer.parseInt(textLeftColor));
        }
    }


    private void refreshColor() {
        if (!isChecked()) {
            offsetWidth(0);
            setClickColor(Integer.parseInt(leftColor));
        } else {
            offsetWidth(mClickWidth);
            setClickColor(Integer.parseInt(rightColor));
        }
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        initAnim();
    }

    //初始化Path
    private void initPath() {
        mClickWidth = juxinWidth / 2 + recWidth;
        if (!isChecked()) {
            offsetWidth(0);
        } else {
            offsetWidth(mClickWidth);
        }
        bgPath = getPath(bgPath, 0, juxinWidth);
        mLeftTextX = (mClickWidth - leftTextPaint.measureText(textLeft)) / 2; //拿到字符串的宽度
        mRightTextX = mClickWidth + (mClickWidth - rightTextPaint.measureText(textRight)) / 2; //拿到字符串的宽度
        Paint.FontMetrics LfontMetrics = leftTextPaint.getFontMetrics();
        mLiftTextY = mHeight / 2 + (Math.abs(LfontMetrics.ascent) - LfontMetrics.descent) / 2;
        Paint.FontMetrics RfontMetrics = rightTextPaint.getFontMetrics();
        mRightTexty = mHeight / 2 + (Math.abs(RfontMetrics.ascent) - RfontMetrics.descent) / 2;
    }

    private void initAnim() {
        ObjectAnimator anim2;
        ObjectAnimator anim3;
        ObjectAnimator anim4;
        if (!isChecked()) {
            anim = ValueAnimator.ofFloat(0, mClickWidth);
            anim2 = ObjectAnimator.ofObject(SwitchView.this, "textLeftColor", new ColorEvaluator(),
                    toHexEncoding(rgb), toHexEncoding(Integer.parseInt(textLeftColor)));
            anim3 = ObjectAnimator.ofObject(SwitchView.this, "textRightColor", new ColorEvaluator(),
                    toHexEncoding(Integer.parseInt(textRightColor)), toHexEncoding(rgb));
            anim4 = ObjectAnimator.ofObject(SwitchView.this, "clickColor", new ColorEvaluator(),
                    toHexEncoding(Integer.parseInt(leftColor)), toHexEncoding(Integer.parseInt(rightColor)));
        } else {
            anim = ValueAnimator.ofFloat(mClickWidth, 0);
            anim2 = ObjectAnimator.ofObject(SwitchView.this, "textLeftColor", new ColorEvaluator(),
                    toHexEncoding(Integer.parseInt(leftColor)), toHexEncoding(rgb));
            anim3 = ObjectAnimator.ofObject(SwitchView.this, "textRightColor", new ColorEvaluator(),
                    toHexEncoding(rgb), toHexEncoding(Integer.parseInt(textRightColor)));
            anim4 = ObjectAnimator.ofObject(SwitchView.this, "clickColor", new ColorEvaluator(),
                    toHexEncoding(Integer.parseInt(rightColor)), toHexEncoding(Integer.parseInt(leftColor)));
        }
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offsetWidth((Float) animation.getAnimatedValue());
                invalidate();
            }
        });
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim).with(anim2).with(anim3).with(anim4);
        animSet.setDuration(800);
        animSet.start();

    }

    private void offsetWidth(float w) {
        clickPath = getPath(clickPath, padding, juxinWidth / 2 - recWidth, w);
    }

    //画圆矩形
    private Path getPath(Path path, float padding, float width, float offset) {
        if (offset < padding) {
            offset = 0;
        }
        path = new Path();
        RectF rectFTo = new RectF(0 + padding + offset, 0 + padding, recWidth * 2 + offset, mHeight - padding);
        RectF rectFTo2 = new RectF(width + offset, 0 + padding, width + recWidth * 2 - padding + offset, mHeight - padding);
        //绘制矩形
        RectF rect = new RectF(recWidth + offset, 0 + padding, width + recWidth + offset, mHeight - padding);
        path.arcTo(rectFTo, 90, 180, true);
        path.arcTo(rectFTo2, -90, 180, true);
        path.addRect(rect, Path.Direction.CW);
        return path;
    }

    //画圆矩形
    private Path getPath(Path path, float padding, float width) {
        return getPath(path, padding, width, 0);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        float heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize; //获取到当前view的宽度
            juxinWidth = mWidth - recWidth * 2;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;//获取当前view的高度
        }
        setMeasuredDimension((int) mWidth, (int) mHeight);

        initPath();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                //获取屏幕上点击的坐标
                float x = event.getX();
                float y = event.getY();
                animStart();
                return true;
        }
        return true;
    }

    private void animStart() {
        if (anim == null || !anim.isRunning()) {
            initAnim();
            checked = !checked;
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawPath(bgPath, bgPaint);
        canvas.drawPath(clickPath, clickPaint);
        /**
         * 字
         */
        canvas.drawText(textLeft, mLeftTextX, mLiftTextY, leftTextPaint);
        canvas.drawText(textRight, mRightTextX, mRightTexty, rightTextPaint);
    }


    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public String getTextRightColor() {
        return textRightColor;
    }

    public void setTextRightColor(String textRightColor) {
        rightTextPaint.setColor(Color.parseColor(textRightColor));
    }

    public String getTextLeftColor() {
        return textLeftColor;
    }

    public void setTextLeftColor(String textLeftColor) {
        leftTextPaint.setColor(Color.parseColor(textLeftColor));
    }

    public String getClickColor() {
        return clickColor;
    }

    public void setClickColor(String clickColor) {
        clickPaint.setColor(Color.parseColor(clickColor));
    }

    public void setClickColor(int clickColor) {
        clickPaint.setColor(clickColor);
    }

    public String toHexEncoding(int color) {
        String R, G, B;
        StringBuffer sb = new StringBuffer();
        R = Integer.toHexString(Color.red(color));
        G = Integer.toHexString(Color.green(color));
        B = Integer.toHexString(Color.blue(color));
        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;
        sb.append("#");
        sb.append(R.toUpperCase());
        sb.append(G.toUpperCase());
        sb.append(B.toUpperCase());
        return sb.toString();
    }
}
