package com.nirwal.epoint.customViews;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.nirwal.epoint.R;


public class CustomRow extends View {
    RectF arc;
    Paint mCircle ,mArc,mTextPaint;
    float mStrokeSize=0.0f;
    int mcolor;
    int mAnimationDuration=1000;
    long mAnimationStartTime;
    int endAngle = 100;
    float mCurrentAngle=0;
    float mAnimationSpeed;

float radius=0;
    public CustomRow(Context context) {
        super(context);
        init();
    }

    public CustomRow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomRow,0,0);
        mStrokeSize = a.getFloat(R.styleable.CustomRow_strokeSize,4.0f);
        mcolor = a.getColor(R.styleable.CustomRow_backgroundColor,Color.rgb(1,1,1));

        init();
    }

    public CustomRow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomRow,0,0);
        mStrokeSize = a.getFloat(R.styleable.CustomRow_strokeSize,4.0f);
        mcolor = a.getColor(R.styleable.CustomRow_backgroundColor,Color.rgb(1,1,1));
        init();
    }

    @TargetApi(21)
    public CustomRow(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomRow,0,0);
        mStrokeSize = a.getFloat(R.styleable.CustomRow_strokeSize,4.0f);
        mcolor = a.getColor(R.styleable.CustomRow_backgroundColor,Color.rgb(1,1,1));
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if( mAnimationStartTime==0){ mAnimationStartTime= System.currentTimeMillis();}

        canvas.drawCircle(getWidth()/2,getHeight()/2,radius,mCircle);
        canvas.drawArc(arc,0,getNextAngle(),false,mArc);
        canvas.drawText("hi sdjf",getWidth()/2,getHeight()/2,mTextPaint);
invalidate();
requestLayout();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Try for a width based on our minimum
       int paddingX = getPaddingLeft()+getPaddingRight();
       int paddingY = getPaddingTop()+getPaddingBottom();

        int minWidth = getSuggestedMinimumWidth() + paddingX;
        int width = resolveSizeAndState(minWidth, widthMeasureSpec, 1);
        int minHeight = MeasureSpec.getSize(width)+paddingY;
        int height = resolveSizeAndState(MeasureSpec.getSize(width) , heightMeasureSpec, 1);


        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        setMeasuredDimension(width, height);

        radius = (Math.min(width-paddingX,height-paddingY)/2)-mStrokeSize;
        arc = new RectF(0+mStrokeSize+paddingX/2,0+mStrokeSize+paddingY/2,width-mStrokeSize-paddingX/2,height-mStrokeSize-paddingY/2);
    }

    void init(){
        mCircle=new Paint();
        mCircle.setColor(mcolor);
        mCircle.setStyle(Paint.Style.STROKE);
        mCircle.setStrokeWidth(mStrokeSize);

        mArc = new Paint();
        mArc.setColor(getResources().getColor(R.color.colorAccent));
        mArc.setStyle(Paint.Style.STROKE);
        mArc.setStrokeWidth(mStrokeSize);
        mArc.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new TextPaint();
        mTextPaint.setStrokeWidth(2);
        mTextPaint.setColor(getResources().getColor(R.color.colorAccent));
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(22);
        mTextPaint.setTextAlign(Paint.Align.CENTER);


    }

    float getNextAngle(){
        long now = System.currentTimeMillis();

        float pathGone =(float) (now - mAnimationStartTime)/mAnimationDuration;
        if(pathGone<1.0f){
            mCurrentAngle=endAngle*pathGone;

        }else {
            mCurrentAngle = endAngle;
        }

        return mCurrentAngle;
    }


    void setAnimationSpeed(){
        float seconds = (float) mAnimationDuration / 1000;
        int i = (int) (seconds * 60);
        mAnimationSpeed = (float) endAngle / i;
    }

}
