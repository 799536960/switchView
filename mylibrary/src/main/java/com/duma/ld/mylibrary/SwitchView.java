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

    private float mClickWidth;//偏移量

    private RectF roundRect;//圆角矩形坐标

    private Path bgPath;//背景大的路径
    private Path clickPath;//小的路径

    private ValueAnimator anim;
    private boolean checked;
    private float padding;

    private int time;
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
        time = a.getInteger(R.styleable.SwitchView_time, 400);
        a.recycle();
    }

    private boolean isChecked() {
        return checked;
    }

    //初始化画笔
    private void initPaint() {
        roundRect = new RectF();
        clickPath = new Path();
        bgPath = new Path();
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
            setClickColor(Integer.parseInt(leftColor));
        } else {
            setClickColor(Integer.parseInt(rightColor));
        }
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        initAnim();
    }

    //初始化Path
    private void initPath() {
        mClickWidth = (mWidth - padding * 2) / 2;
        if (!isChecked()) {
            offsetWidth(0);
        } else {
            offsetWidth(mClickWidth);
        }
        getPath(bgPath, 0, mWidth, 0);
        mLeftTextX = (mClickWidth - leftTextPaint.measureText(textLeft)) / 2 + padding; //拿到字符串的宽度
        mRightTextX = mClickWidth + padding + (mClickWidth - rightTextPaint.measureText(textRight)) / 2; //拿到字符串的宽度
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
        animSet.setDuration(time);
        animSet.start();

    }

    private void offsetWidth(float w) {
        getPath(clickPath, padding, mClickWidth, w);
    }

    //绘制圆角矩形
    private void getPath(Path path, float padding, float width, float offset) {
        //矩形坐标
        roundRect.set(0 + padding + offset, 0 + padding, width + padding + offset, mHeight - padding);
        path.rewind();
        //绘制
        path.addRoundRect(roundRect, mHeight / 2, mHeight / 2, Path.Direction.CW);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        float heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize; //获取到当前view的宽度
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
