package com.nirwal.epoint.customViews;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.nirwal.epoint.R;
import com.nirwal.epoint.models.ParentChildListItem;

import java.util.ArrayList;

public class CustomCrd extends View {



    Rect _topHeadRect;
    Paint _topHeadRectPaint, _primaryTextPaint, _secondaryTextPaint,_linePaint,_sectionBackgroundPaint;
    Drawable _headerIconDrawable, _listIconDrawable;
    private int _width;
    private int _height;
    private String _title ="";

    private int _headerHeight =150;
    private int _sectionHeight = 60;


    private ArrayList<Rect> _sectionBlockRectList;
    private ArrayList<ParentChildListItem> _list;

    private OnClickSectionItemListener _listener;


    public CustomCrd(Context context) {
        super(context);
        init(null);
    }

    public CustomCrd(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomCrd(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }

    public void setList(ArrayList<ParentChildListItem> list) {
        this._list = list;
        invalidate();
    }


    public int getHeaderHeight() {
        return _headerHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        this._headerHeight = headerHeight;
        invalidate();
    }

    public int getSectionHeight() {
        return _sectionHeight;
    }

    public void setSectionHeight(int sectionHeight) {
        this._sectionHeight = sectionHeight;
        invalidate();
    }

    public void setOnClickSectionItemListener(OnClickSectionItemListener listener){
        this._listener =listener;
    }

    public void setTitle(String title){
        this._title=title;
    }


    public void init(AttributeSet attrs){

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCrd,0,0);
        float headerHeight = a.getDimension(R.styleable.CustomCrd_fontSize,150);
        int headerColor = a.getColor(R.styleable.CustomCrd_headerColor,Color.GRAY);
        a.recycle();

        _list = new ArrayList<>();
        _sectionBlockRectList = new ArrayList<>();

        _headerHeight = (int)headerHeight;
        _sectionHeight = (int)(_headerHeight * 0.9);

        _headerIconDrawable = getResources().getDrawable(R.drawable.ic_widgets_black_48dp);
        _listIconDrawable = getResources().getDrawable(R.drawable.ic_document);

        _topHeadRect = new Rect();
        _topHeadRectPaint = new Paint();
        _topHeadRectPaint.setColor(headerColor);

        _primaryTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        _primaryTextPaint.setColor(Color.WHITE);
        _primaryTextPaint.setStyle(Paint.Style.FILL);



        _secondaryTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        _secondaryTextPaint.setColor(Color.DKGRAY);
        _secondaryTextPaint.setStyle(Paint.Style.FILL);



        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        _linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _linePaint.setColor(Color.DKGRAY);
        float[] x ={(float)(_sectionHeight*18)/100,(float) (_sectionHeight*8)/100};
        _linePaint.setPathEffect(new DashPathEffect(x,1));
        _linePaint.setStrokeWidth(1);

        _sectionBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _sectionBackgroundPaint.setColor(Color.TRANSPARENT);
        _sectionBackgroundPaint.setStyle(Paint.Style.FILL);
    }





    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTopBar(canvas,_headerHeight,_width);
        int startX = 0;
        int startY = _headerHeight;


        //Log.v("CustomCrd", "list size :"+_list.size());
        for(int i = 1; i < (_list.size()+1) ;i++){

           // Log.v("CustomCrd", "i :"+i);
           // Log.v("CustomCrd", "i-1 :"+(i-1));

            drawSection(canvas,_list.get(i-1).Title,startX,startY+((_sectionHeight)*(i-1)),_width,_sectionHeight,i-1);


        }



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMeasurement = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasurement = MeasureSpec.getSize(heightMeasureSpec);



        switch (MeasureSpec.getMode(heightMeasureSpec)){
            case MeasureSpec.UNSPECIFIED:{
                _height = _headerHeight+_sectionHeight*_list.size()+5;
                break;
            }
            case MeasureSpec.EXACTLY:{
                _height = heightMeasurement;
                break;
            }
            case MeasureSpec.AT_MOST:{
                _height = Math.max(heightMeasurement,_headerHeight+_sectionHeight*_list.size()+5);
                break;
            }

        }


        switch (MeasureSpec.getMode(widthMeasureSpec)){
            case MeasureSpec.UNSPECIFIED:{
                _width = widthMeasurement;
                break;
            }
            case MeasureSpec.EXACTLY:{
                _width = widthMeasurement;
                break;
            }
            case MeasureSpec.AT_MOST:{
                _width = widthMeasurement;
                break;
            }
        }

        setMeasuredDimension(_width,_height);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int xPos = (int)event.getX();
        int yPos = (int)event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_UP:{
                for(int i=0; i < _list.size(); i++){
                    if(_sectionBlockRectList.get(i).contains(xPos, yPos)){
                        Log.v("CustomCrd:", _list.get(i).Title);
                        if(_listener!=null)_listener.onClick(_list.get(i));
                    }

                }
            }
            case MotionEvent.ACTION_DOWN:{}

        }

        return true;
    }

    void drawTopBar(Canvas canvas, int height, int width){
        int headerPadding=8;
        int textLeftMargin=0;

        _topHeadRect.right=width;
        _topHeadRect.bottom=height;


        // draw header background
        canvas.drawRect(_topHeadRect,_topHeadRectPaint);

        //draw text
        _primaryTextPaint.setTextSize(height-(2*headerPadding));
        canvas.drawText(_title,
                height + headerPadding+textLeftMargin,
                (height/2)-((_primaryTextPaint.descent()+_primaryTextPaint.ascent())/2),
                _primaryTextPaint);


        // draw Header Icon left
        _headerIconDrawable.setBounds(0 + headerPadding, 0 + headerPadding,
                height - headerPadding,height - headerPadding);

        canvas.save();
        _headerIconDrawable.draw(canvas);
        canvas.restore();
    }


    void drawSection(Canvas canvas, String text, int startX, int startY, int width, int height, int index){
        int margin=(height*50)/100;
        int imageMargin=(height*15)/100;
        int iconHeight = height-imageMargin;
        int iconWidth = height-imageMargin;

        if(_sectionBlockRectList.size()<=index){
            _sectionBlockRectList.add(index, new Rect(startX,startY,
                    startX+width, startY + height));
        }
        Rect rect =_sectionBlockRectList.get(index);

        //draw background
        canvas.drawRect(rect,_sectionBackgroundPaint);

        //draw icon
        _listIconDrawable.setBounds(startX+imageMargin, startY + imageMargin,
                startX + iconWidth, startY + iconHeight);

        canvas.save();
        _listIconDrawable.draw(canvas);
        canvas.restore();




        _secondaryTextPaint.setTextSize(height-margin);
        canvas.drawText(text,startX +imageMargin +iconWidth ,
                startY+(height)/2-((_secondaryTextPaint.ascent()+_secondaryTextPaint.descent())/2)
                ,_secondaryTextPaint);

        if(index<_list.size()-1) {
            canvas.drawLine(startX, startY + height,
                    startX + width, startY + height, _linePaint);
        }
    }


    public int spToPx(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

//    public int dpToPx(float dp) {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
//    }

    private float dpToPixel(float dp) {
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi/160f);
        return px;
    }


    public interface OnClickSectionItemListener{
        void onClick(ParentChildListItem item);
    }

}
