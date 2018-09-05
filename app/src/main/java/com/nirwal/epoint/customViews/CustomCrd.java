package com.nirwal.epoint.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CustomCrd extends View {
    public CustomCrd(Context context) {
        super(context);
    }

    public CustomCrd(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCrd(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }



}
