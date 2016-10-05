package com.supertreasure.coupon;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.supertreasure.R;
import com.supertreasure.bean.IsPraised;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import me.itangqi.greendao.Coupon;
import me.itangqi.greendao.New;

/**
 * Created by prj on 2016/1/25.
 */
public class CouponHistoryAdapter extends RecyclerView.Adapter<CouponHistoryAdapter.RefreshViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_FOOTER = 2;
    private View FooterView ;
    private Context context;
    private List<Coupon> couponList;
    private OnItemClickListener mListener;

    public CouponHistoryAdapter(Context context){
        this.context = context;
    }

    public CouponHistoryAdapter(Context context, List<Coupon> couponList){
        this.context = context;
        this.couponList = couponList;
    }

    public void setFooterView(View FooterView){
        this.FooterView = FooterView;
        notifyDataSetChanged();
    }

    public View getFooterView() {
        return FooterView;
    }

    public void removeCoupon(int position){
        notifyItemRemoved(position);
        couponList.remove(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void addCouponList(List<Coupon> couponList){
        couponList.addAll(couponList);
        notifyDataSetChanged();
    }


    public void setCouponList(List<Coupon> couponList) {
        this.couponList = couponList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(FooterView==null){
            return TYPE_NORMAL;
        }
        if(position==getItemCount()-1)//最后一个item
            return TYPE_FOOTER;

        return TYPE_NORMAL;
    }

    @Override
    public RefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(FooterView!=null&&viewType==TYPE_FOOTER){
            return new RefreshViewHolder(FooterView);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_historycoupon,parent,false);
        AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.root));
        RefreshViewHolder viewHolder = new RefreshViewHolder(view);
        return  viewHolder;
    }

    public interface OnItemClickListener{
        public void ItemClickListener(View view, int position);
        public void ItemLongClickListener(View view, int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(final RefreshViewHolder holder, final int position) {

        if(getItemViewType(position)==TYPE_FOOTER)
            return;

        final Coupon coupon = couponList.get(position);
        holder.tv_collectNum.setText(coupon.getCollectNum());
        holder.tv_browseNum.setText(coupon.getBrowserNum()+"");
        holder.tv_priseNum.setText(coupon.getCouponPraise()+"");
        holder.tv_content.setText(coupon.getIntro());
        //holder.tv_browserNum.setText(news.getBrowserNum());

        //LoggerUtil.d("newsHistory", "tv_browserNum" + news.getBrowserNum());
        //LoggerUtil.d("newsHistory", "holder.tv_browserNum" + holder.tv_browserNum);

        /*if (news.getIsPraised()) {
            holder.iv_like.setBackgroundResource(R.drawable.like_press);
        } else {
            holder.iv_like.setBackgroundResource(R.drawable.like_normal);
        }*/

        if(mListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListener.ItemClickListener(holder.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int size = couponList ==null?0: couponList.size();
        return FooterView==null?size:size+1;
    }

    public List<Coupon> getCouponList() {
        return couponList;
    }

    class RefreshViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_content;
        public TextView tv_browseNum;
        public TextView tv_priseNum;
        public TextView tv_collectNum;


        public RefreshViewHolder(View itemView){
            super(itemView);
            if(itemView==FooterView){
                return;
            }
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_browseNum = (TextView) itemView.findViewById(R.id.tv_browseNum);
            tv_priseNum = (TextView) itemView.findViewById(R.id.tv_priseNum);
            tv_collectNum = (TextView) itemView.findViewById(R.id.tv_collectNum);
        }
    }

    public void updataView(int position,boolean isPraise,boolean isCollect, int praiseNum,int browseNum){
        couponList.get(position).setBrowserNum(browseNum);
        couponList.get(position).setCouponPraise(praiseNum);
        couponList.get(position).setIsPraise(isPraise);
        couponList.get(position).setIsCollect(isCollect);
        notifyItemChanged(position);
    }

}
