package com.supertreasure.home;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.image.AbImageLoader;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.supertreasure.R;
import com.supertreasure.bean.IsPraised;
import com.supertreasure.bean.Topnews;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;

import java.util.ArrayList;
import java.util.List;

import me.itangqi.greendao.New;

/**
 * Created by prj on 2016/1/25.
 */
public class RefreshAdapter extends RecyclerView.Adapter<RefreshAdapter.RefreshViewHolder> {

    /*public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View HeaderView ;*/
    private Context context;
    private List<New> newses;
    private OnItemClickListener mListener;

    public RefreshAdapter(Context context){
        this.context = context;
    }

    public RefreshAdapter(Context context,List<New> newses){
        this.context = context;
        this.newses = newses;
    }

    public void updateItem(String browserNum,String praiseNum,boolean isPraised,int position){
        newses.get(position).setBrowserNum(browserNum);
        newses.get(position).setPraise(praiseNum);
        newses.get(position).setIsPraised(isPraised);
        notifyItemChanged(position);
    }
    /*public void setHeaderView(View HeaderView){
        this.HeaderView = HeaderView;
        notifyDataSetChanged();
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return HeaderView == null ? position : position - 1;
    }

    public View getHeaderView() {
        return HeaderView;
    }*/

    public void removeTopnews(int position){
        notifyItemRemoved(position);
        //if(HeaderView!=null) {
            newses.remove(position - 1);
            notifyItemRangeChanged(position,getItemCount());
        /*}else{
            newses.remove(position);
            notifyItemRangeChanged(position, getItemCount());
        }*/
    }

    public void addTopnews(List<New> news){
        newses.addAll(news);
        notifyDataSetChanged();
    }

    public void setTopnews(List<New> news){
        this.newses = news;
        notifyDataSetChanged();
    }

    /*@Override
    public int getItemViewType(int position) {
        if(HeaderView==null)
            return TYPE_NORMAL;
        if(position==0) {
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }*/

    @Override
    public RefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*if(HeaderView!=null&&viewType==TYPE_HEADER){
           return new RefreshViewHolder(HeaderView);
        }*/
        View view = LayoutInflater.from(context).inflate(R.layout.item_topnews,parent,false);
        RefreshViewHolder viewHolder = new RefreshViewHolder(view);
        return  viewHolder;
    }

    public interface OnItemClickListener{
        public void ItemClickListener(View iv_like,TextView tv_like,String topId,int position);
        public void ItemClickListener(View view,String topId,String url,String adminID,int position);
        public void ItemLongClickListener(View view,int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(final RefreshViewHolder holder, final int position) {

        /*if(getItemViewType(position)==TYPE_HEADER)
            return;

        //要加这一句，不然添加头图后会少一条数据
        position = getRealPosition(holder);*/

        New news = newses.get(position);
        holder.draweeView.setImageURI(Uri.parse(news.getPics()));
        holder.tv_adminName.setText(news.getAdminName());
        holder.tv_like.setText(news.getPraise());
        holder.tv_summary.setText(news.getTitle());
        //holder.tv_browserNum.setText(news.getBrowserNum());
        if (news.getIsPraised()){
            holder.iv_like.setBackgroundResource(R.drawable.like_press);
        }else {
            holder.iv_like.setBackgroundResource(R.drawable.like_normal);
        }

        final int pos = holder.getLayoutPosition()-1;
        if(mListener!=null){
            holder.draweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String topId = newses.get(pos).getTopId();
                    String url = newses.get(pos).getUrl();
                    String adminID = newses.get(pos).getTopId();
                    mListener.ItemClickListener(holder.draweeView, topId,url,adminID,pos);
                }
            });

            holder.ll_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String topId = newses.get(pos).getTopId();
                    mListener.ItemClickListener(holder.iv_like,holder.tv_like, topId,position);
                }
            });

            holder.draweeView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //int position = holder.getLayoutPosition();
                    mListener.ItemLongClickListener(holder.draweeView,pos+1);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return newses==null?0:newses.size();
        /*int size = newses==null?0:newses.size();
        return HeaderView==null?size:size+1;*/
    }

    class RefreshViewHolder extends RecyclerView.ViewHolder{
        TextView tv_adminName;
        TextView tv_summary;
        TextView tv_like;
        //TextView tv_browserNum;
        SimpleDraweeView draweeView;
        LinearLayout ll_praise;
        LinearLayout root;
        ImageView iv_like;

        public RefreshViewHolder(View itemView){
            super(itemView);
            /*if(itemView==HeaderView){
                return;
            }*/
            root = (LinearLayout) itemView.findViewById(R.id.root);
            AbViewUtil.scaleContentView(root);
            ll_praise = (LinearLayout) itemView.findViewById(R.id.ll_praise);
            tv_adminName = (TextView) itemView.findViewById(R.id.tv_adminName);
            tv_summary = (TextView) itemView.findViewById(R.id.tv_summary);
            tv_like = (TextView) itemView.findViewById(R.id.tv_like);
            //tv_browserNum = (TextView) itemView.findViewById(R.id.tv_browse);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.draweeView);
            iv_like = (ImageView) itemView.findViewById(R.id.iv_like);
        }
    }

    public List<New> getNewses() {
        return newses;
    }

    public void setNewses(List<New> newses) {
        this.newses = newses;
    }
}
