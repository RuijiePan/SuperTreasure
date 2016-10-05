package com.supertreasure.trade;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ab.image.AbImageLoader;
import com.ab.util.AbViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.supertreasure.R;
import com.supertreasure.bean.Banner;
import com.supertreasure.util.GetHeightResUrl;

import java.util.List;

/**
 * Created by Administrator on 2016/1/23.
 */
public class GoodPicAdapter extends PagerAdapter {

    private ImageView iv;
    private String[] pics;
    private Context context;
    //private AbImageLoader abImageLoader = null;
    private OnItemClickListener mListener;
    private LayoutInflater mLayoutInflater;

    public GoodPicAdapter(Context context,String[] pics){
        this.context = context;
        this.pics = pics;
        mLayoutInflater = LayoutInflater.from(context);
        //abImageLoader = AbImageLoader.getInstance(context);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View  view = mLayoutInflater.inflate(R.layout.item_trade_banner_view,null, false);
        SimpleDraweeView draweeView = (SimpleDraweeView) view.findViewById(R.id.draweeView);
        AbViewUtil.scaleContentView((LinearLayout)view.findViewById(R.id.root));

        String url = pics[position];
        draweeView.setImageURI(Uri.parse(GetHeightResUrl.getHeightUrl(url)));

        ((ViewPager) container).addView(view, 0);
        return view;
        /*iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String main_pic = pics[position];
        abImageLoader.setEmptyImage(R.drawable.image_placeholder);
        abImageLoader.setErrorImage(R.drawable.icon_failure);
        abImageLoader.display(iv, main_pic);

        if(mListener!=null) {
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.ItemClickListener(iv,position);
                }
            });
        }

        ((ViewPager) container).addView(iv, 0);
        return iv;*/
    }

    public void updateView(String[] pics){
        this.pics = pics;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        public void ItemClickListener(View view,int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public int getCount() {
        return pics ==null?0:pics.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==(View)object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

}
