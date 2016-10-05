package com.supertreasure.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ab.image.AbImageLoader;
import com.orhanobut.logger.Logger;
import com.supertreasure.R;
import com.supertreasure.bean.Banner;
import com.supertreasure.home.ActivityNewsDetail;
import com.supertreasure.main.TheApp;

import java.util.List;

/**
 * Created by Administrator on 2016/1/23.
 */
public class BannerAdapter extends PagerAdapter {

    private List<me.itangqi.greendao.Banner> list;
    private Context context;
    private AbImageLoader abImageLoader = null;
 /*   private OnItemClickListener listener;
    public  interface OnItemClickListener{
        void onItemShortClick(View v ,int position);
        void onItemLongClick(View v ,int position);
    }*/

    public BannerAdapter(Context context,List<me.itangqi.greendao.Banner> list){
        this.context = context;
        this.list = list;
        abImageLoader = AbImageLoader.getInstance(context);
    }

    @Override
    public Object instantiateItem(ViewGroup container,final int position) {
        ImageView iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        String main_pic = list.get(position).getPhoto();
        abImageLoader.setEmptyImage(R.drawable.image_placeholder);
        abImageLoader.setErrorImage(R.drawable.icon_failure);
        abImageLoader.display(iv, main_pic);
        //Logger.i("BannerAdapter","instantiateItem position="+position);
        //这种做法会无效
        /*if (listener!=null) {
            Logger.i("BannerAdapter","listener!=null and setOnClickListener()");
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemShortClick(v, position);
                }
            });
        }*/
        ((ViewPager) container).addView(iv, 0);
        return iv;
    }

    public void updateView(List<me.itangqi.greendao.Banner> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==(View)object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
/*
    public OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }*/

    public List<me.itangqi.greendao.Banner> getList() {
        return list;
    }

    public void setList(List<me.itangqi.greendao.Banner> list) {
        this.list = list;
    }
}
