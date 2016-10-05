package com.supertreasure.home;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ab.image.AbImageLoader;
import com.ab.util.AbViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.supertreasure.R;

import java.util.List;

/**
 * Created by Administrator on 2016/1/23.
 */
public class TopnewBannerAdapter extends PagerAdapter {

    private final int FAKE_BANNER_SIZE=100;
    private LayoutInflater mLayoutInflater;
    private List<me.itangqi.greendao.Banner> list;
    private Context context;

    public TopnewBannerAdapter(Context context, List<me.itangqi.greendao.Banner> list){
        this.context = context;
        this.list = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void updateView(List<me.itangqi.greendao.Banner> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
        //return FAKE_BANNER_SIZE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==(View)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position) {
        //position %= list.size();
        View  view = mLayoutInflater.inflate(R.layout.item_banner_view,null, false);
        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.draweeView);
        AbViewUtil.scaleContentView((LinearLayout)view.findViewById(R.id.root));

        String url = list.get(position).getPhoto();
        draweeView.setImageURI(Uri.parse(url));

        ((ViewPager) container).addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    public List<me.itangqi.greendao.Banner> getList() {
        return list;
    }

    public void setList(List<me.itangqi.greendao.Banner> list) {
        this.list = list;
    }

}
