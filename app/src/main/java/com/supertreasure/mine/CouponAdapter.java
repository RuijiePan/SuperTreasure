package com.supertreasure.mine;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.ab.util.AbViewUtil;
import com.supertreasure.R;
import com.supertreasure.bean.CouponBeen;
import com.supertreasure.service.MySmallShopService;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by yum on 2016/2/3.
 */
public class CouponAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    public static final int TYPE_CONTENT = 0;
    public static final int TYPE_GROUP = 1;
    private OnItemClickListener mOnItemClickListener;
    private List<CouponBeen.Coupon> list_coupons;

    public CouponAdapter(Context context) {
        this.context = context;
    }
    public interface OnItemClickListener {
        void onItemClick(ContentViewHolder holder, int position);
        void onItemLongClick(ContentViewHolder holder, int position);
    }
    public CouponAdapter(Context context, List<CouponBeen.Coupon> list_coupons, OnItemClickListener mOnItemClickListener) {
        this.context = context;
        this.list_coupons = list_coupons;
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public CouponAdapter(Context context,List<CouponBeen.Coupon> list_coupons) {
        this.context = context;
        this.list_coupons = list_coupons;
    }

    @Override
    public int getItemViewType(int position) {
        return list_coupons.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType){
            case TYPE_CONTENT:
                itemView = View.inflate(parent.getContext(), R.layout.item_mycoupon,null);
                ContentViewHolder viewHolder = new ContentViewHolder(itemView);
                return viewHolder;
            case TYPE_GROUP:
                itemView = View.inflate(parent.getContext(), R.layout.item_mycoupon_group,null);
                GroupViewHolder groupViewHolder = new GroupViewHolder(itemView);
                return groupViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        System.out.println("onBindViewHolder");

        switch ( holder.getItemViewType () ) {
            case TYPE_CONTENT:
                final ContentViewHolder viewHolder = (ContentViewHolder) holder;
                ContentViewHolder holderTemp = (ContentViewHolder) holder;
                if (mOnItemClickListener!=null){
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LinearLayout linearLayout = (LinearLayout) v;
                            Log.i("ActivityMyCoupon","onClick "+linearLayout.toString());
                            mOnItemClickListener.onItemClick(viewHolder,viewHolder.getLayoutPosition());
                        }
                    });
                    viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            LinearLayout linearLayout = (LinearLayout) v;
                            Log.i("ActivityMyCoupon","onLongClick "+linearLayout.toString());
                            mOnItemClickListener.onItemLongClick(viewHolder,viewHolder.getLayoutPosition());
                            return false;
                        }
                    });
                }
                //holder.imageView_img.setBackgroundResource(R.drawable.iv_mine_head);
                //asyncImageLoad(holderTemp.imageView_img,list_Moodmessage.get(position).getCouponPics());
                AbImageLoader.getInstance(context).display(holderTemp.imageView_img,list_coupons.get(position).getCouponPics());

                CouponBeen.Coupon coupon = list_coupons.get(position);
                holderTemp.textView_content.setText(coupon.getIntro());
                holderTemp.textView_fromdate.setText(coupon.getBeginTime().trim().split(" ")[0]);
                holderTemp.textView_todate.setText(coupon.getLastTime().trim().split(" ")[0]);
                break;
            case TYPE_GROUP:
                GroupViewHolder groupViewHolder = ( GroupViewHolder ) holder;
                groupViewHolder.textView_group.setText ( list_coupons.get(position).getIntro());
                break;
        }
    }

    public void addData(int position,CouponBeen.Coupon coupon) {
        list_coupons.add(position, coupon);
        //swipeRefreshLayout.setRefreshing(false);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        list_coupons.remove(position);
        //swipeRefreshLayout.setRefreshing(false);
        notifyItemRemoved(position);
    }
    @Override
    public int getItemCount() {
        return list_coupons==null?0:list_coupons.size();
    }


    public class ContentViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView_img;
        public TextView textView_content;
        public TextView textView_fromdate;
        public TextView textView_todate;
        public ContentViewHolder(View itemView) {
            super(itemView);
            AbViewUtil.scaleContentView((LinearLayout) itemView.findViewById(R.id.root));
            imageView_img = (ImageView) itemView.findViewById(R.id.goods_img);//
            textView_content = (TextView) itemView.findViewById(R.id.goods_content);
            textView_fromdate = (TextView) itemView.findViewById(R.id.coupon_fromdate);
            textView_todate = (TextView) itemView.findViewById(R.id.coupon_todate);
        }
    }
    public class GroupViewHolder extends RecyclerView.ViewHolder{
        public TextView textView_group;
        public GroupViewHolder(View itemView) {
            super(itemView);
            AbViewUtil.scaleContentView((LinearLayout) itemView.findViewById(R.id.root));
            textView_group = (TextView) itemView.findViewById(R.id.text_collection);
        }
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public List<CouponBeen.Coupon> getList_coupons() {
        return list_coupons;
    }

    public void setList_coupons(List<CouponBeen.Coupon> list_coupons) {
        this.list_coupons = list_coupons;
        notifyDataSetChanged();
        notifyItemRangeInserted(0,list_coupons.size()-1);
    }
}

