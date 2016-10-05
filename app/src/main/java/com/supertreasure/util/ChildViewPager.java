package com.supertreasure.util;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ab.view.chart.Point;

/**
 * Created by prj on 2016/1/23.
 */
public class ChildViewPager extends ViewPager {

    private PointF downP = new PointF();
    private PointF curP = new PointF();
    private OnSingleTouchListener onSingleTouchListener;

    public ChildViewPager(Context context) {
        super(context);
    }

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 当拦截触摸事件到达此位置的时候，返回true，
        // 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        curP.x = ev.getX();
        curP.y = ev.getY();

        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            // 记录按下时候的坐标
            // 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
            downP.x = ev.getX();
            downP.y = ev.getY();
            // 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            // 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            if(Math.abs(curP.x-downP.x)>Math.abs(curP.y-downP.y))
                 getParent().requestDisallowInterceptTouchEvent(true);
            else {
                //向上下滑动的话执行Listview刷新
                getParent().requestDisallowInterceptTouchEvent(false);
            }
        }

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            // 在up时判断是否按下和松手的坐标为一个点
            // 如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
            if (Math.abs(downP.x-curP.x)<5 && Math.abs(downP.y-curP.y)<5) {
                onSingleTouch();
                return true;
            }
        }

        super.onTouchEvent(ev);
        return true;
    }

    /**
     单击
     **/
    public void onSingleTouch(){
        if(onSingleTouchListener!=null){
            onSingleTouchListener.onSingleTouch();
        }
    }

    /**
     * 创建点击事件接口
     */
    public interface OnSingleTouchListener{
        public void onSingleTouch();
    }

    public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener){
        this.onSingleTouchListener = onSingleTouchListener;
    }

}
