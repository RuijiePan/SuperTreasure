package com.supertreasure.util;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.supertreasure.R;


/**
 * Created by walidake on 2016/2/27.
 */
public class CustomToolBar extends Toolbar {

    private LayoutInflater layoutInflater;
    private View view;
    private TextView leftView;
    private TextView centerView;
    private TextView rightView;
    private TextView leftrightView;

    public CustomToolBar(Context context) {
        this(context, null);
    }

    public CustomToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
        setContentInsetsRelative(0, 0);
        if (attrs != null) {
            TintTypedArray array = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.CustomToolBar, defStyleAttr, 0);
            String leftText = array.getString(R.styleable.CustomToolBar_leftText);
            String centerText = array.getString(R.styleable.CustomToolBar_centerText);
            String rightText = array.getString(R.styleable.CustomToolBar_rightText);
            Drawable leftIcon = array.getDrawable(R.styleable.CustomToolBar_leftIcon);
            Drawable rightIcon = array.getDrawable(R.styleable.CustomToolBar_rightIcon);
            Drawable leftrightIcon = array.getDrawable(R.styleable.CustomToolBar_leftrightIcon);
            setLeftText(leftText);
            setCenterText(centerText);
            setRightText(rightText);
            setLeftIcon(leftIcon);
            setRightIcon(rightIcon);
            setLeftRightIcon(leftrightIcon);
            array.recycle();
        }
    }

    /**
     * 设置左中右OnClickListener
     */
    public void setLeftViewOnClickListener(OnClickListener listener) {
        leftView.setOnClickListener(listener);
    }

    public void setCenterViewOnClickListener(OnClickListener listener) {
        centerView.setOnClickListener(listener);
    }

    public void setRightViewOnClickListener(OnClickListener listener) {
        rightView.setOnClickListener(listener);
    }

    public void setLeftRightViewOnClickListener(OnClickListener listener) {
        leftrightView.setOnClickListener(listener);
    }

    private void initViews() {
        if (view == null) {
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.item_toolbar, null);
            leftView = (TextView) view.findViewById(R.id.tv_left);
            centerView = (TextView) view.findViewById(R.id.tv_center);
            rightView = (TextView) view.findViewById(R.id.tv_right);
            leftrightView = (TextView) view.findViewById(R.id.tv_left_right);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.NO_GRAVITY);
            addView(view, layoutParams);
        }
    }

    /**
     * 设置左中右文本      不得在外部调用
     */
    public void setLeftText(CharSequence leftText) {
        leftView.setText(leftText);
    }

    public void setCenterText(CharSequence centerText) {
        centerView.setText(centerText);
    }

    public void setRightText(CharSequence rightText) {
        rightView.setText(rightText);
    }

    /**
     * 设置左右图标       不得在外部调用
     * 设置文本的情况下，无法应用图标
     */
    public void setLeftIcon(Drawable leftIcon) {
        String leftText = leftView.getText().toString();
        if (leftText == null || "".equals(leftText)) {
            if (leftIcon != null) {
                leftView.setWidth(leftIcon.getIntrinsicWidth());
                leftView.setHeight(leftIcon.getIntrinsicHeight());
                leftView.setBackgroundDrawable(leftIcon);
                leftView.setClickable(true);
            }
        }
    }

    public void setRightIcon(Drawable rightIcon) {
        String rightText = rightView.getText().toString();
        if (rightText == null || "".equals(rightText)) {
            if (rightIcon != null) {
                rightView.setWidth(rightIcon.getIntrinsicWidth());
                rightView.setHeight(rightIcon.getIntrinsicHeight());
                rightView.setBackgroundDrawable(rightIcon);
                rightView.setClickable(true);
            }
        }
    }

    public void setLeftRightIcon(Drawable rightIcon) {
        String rightText = leftrightView.getText().toString();
        if (rightText == null || "".equals(rightText)) {
            if (rightIcon != null) {
                leftrightView.setWidth(rightIcon.getIntrinsicWidth());
                leftrightView.setHeight(rightIcon.getIntrinsicHeight());
                leftrightView.setBackgroundDrawable(rightIcon);
                leftrightView.setClickable(true);
            }
        }
    }

    /**
     * created by wsj
     * params 跟据资源Id设置textview背景
     * function
     * at Time 2016/3/6 11:48
     */

    public void setLeftIcon(int ResId) {
        if (ResId > 0) {
            setLeftIcon(getResources().getDrawable(ResId));
        }
    }

    public void setRightIcon(int ResId) {
        if (ResId > 0) {
            setRightIcon(getResources().getDrawable(ResId));
        }
    }

    public void setLeftRightIcon(int ResId) {
        if (ResId > 0) {
            setLeftRightIcon(getResources().getDrawable(ResId));
        }
    }

    public void setCenterDrawableRight(int ResId) {
        Drawable drawable = getResources().getDrawable(ResId);
        //要加这句才能显示
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        centerView.setCompoundDrawables(null, null, drawable, null);
        centerView.setClickable(true);
    }

    public void setRightDrawableRight(int ResId) {
        Drawable drawable = getResources().getDrawable(ResId);
        //要加这句才能显示
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        rightView.setCompoundDrawables(null, null, drawable, null);
        rightView.setClickable(true);
    }

    public void setLeftDrawableRight(int ResId) {
        Drawable drawable = getResources().getDrawable(ResId);
        //要加这句才能显示
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        leftView.setCompoundDrawables(null, null, drawable, null);
        leftView.setClickable(true);
    }

    public void setRightViewEnable(boolean flag) {
        rightView.setEnabled(flag);
    }

    public void setCenterViewVisibility(boolean flag) {
        if (flag) {
            centerView.setVisibility(View.VISIBLE);
        }else {
            centerView.setVisibility(View.GONE);
        }
    }

}
