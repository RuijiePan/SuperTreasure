package com.supertreasure.home;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import com.supertreasure.util.ToastUtil;

/**
 * Created by Administrator on 2016/2/29.
 */
public class CommentClickSpan extends ClickableSpan{

    private Context context;
    private String name;
    private String color = "#6a8694";

    public CommentClickSpan(Context context, String name, String color){
        this.context = context;
        this.name = name;
        this.color = color;
    }

    public CommentClickSpan(Context context, String name){
        this.context = context;
        this.name = name;
    }

    @Override
    public void onClick(View widget) {
        //ToastUtil.showToast(context,"点击了："+name);
        //Log.w("haha","mo");
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.parseColor(color));
        ds.setUnderlineText(false);
    }
}
