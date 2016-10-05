package com.supertreasure.home;

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
import com.supertreasure.bean.Topnews;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;
import com.supertreasure.util.LoggerUtil;

import java.util.List;

import me.itangqi.greendao.New;

/**
 * Created by prj on 2016/1/25.
 */
public class NewsHistoryAdapter extends RecyclerView.Adapter<NewsHistoryAdapter.RefreshViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_FOOTER = 2;
    private View FooterView ;
    private Context context;
    private List<New> newses;
    private OnItemClickListener mListener;

    public NewsHistoryAdapter(Context context){
        this.context = context;
    }

    public NewsHistoryAdapter(Context context, List<New> newses){
        this.context = context;
        this.newses = newses;
    }

    public void setFooterView(View FooterView){
        this.FooterView = FooterView;
        notifyDataSetChanged();
    }

    public View getFooterView() {
        return FooterView;
    }

    public void removeTopnews(int position){
        notifyItemRemoved(position);
        newses.remove(position);
        notifyItemRangeChanged(position,getItemCount());
    }

    public void addTopnews(List<New> news){
        newses.addAll(news);
        notifyDataSetChanged();
    }

    public void setTopnews(List<New> news){
        this.newses = news;
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

        View view = LayoutInflater.from(context).inflate(R.layout.item_topnews,parent,false);
        AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.root));
        RefreshViewHolder viewHolder = new RefreshViewHolder(view);
        return  viewHolder;
    }

    public interface OnItemClickListener{
        public void ItemClickListener(View iv_like, TextView tv_like, String topId,int position);
        public void ItemClickListener(View view, String topId, String url,boolean isShowHistory,int position);
        public void ItemLongClickListener(View view, int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(final RefreshViewHolder holder, final int position) {

        if(getItemViewType(position)==TYPE_FOOTER)
            return;

        String url = Config.getAPI(Config.IsPraise);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(context, Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(context, Config.Token));
        params.put(Config.TopID, newses.get(position).getTopId());
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                IsPraised isPraised = gson.fromJson(content, IsPraised.class);
                if (isPraised.isPraised()) {
                    holder.iv_like.setBackgroundResource(R.drawable.like_press);
                } else {
                    holder.iv_like.setBackgroundResource(R.drawable.like_normal);
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Toast.makeText(context, "点赞图片初始化失败", Toast.LENGTH_SHORT).show();
            }
        });

        final New news = newses.get(position);
        holder.draweeView.setImageURI(Uri.parse(news.getPics()));
        holder.tv_adminName.setText(news.getAdminName());
        holder.tv_like.setText(news.getPraise());
        holder.tv_summary.setText(news.getTitle());
        //holder.tv_browserNum.setText(news.getBrowserNum());

        //LoggerUtil.d("newsHistory", "tv_browserNum" + news.getBrowserNum());
        //LoggerUtil.d("newsHistory", "holder.tv_browserNum" + holder.tv_browserNum);

        /*if (news.getIsPraised()) {
            holder.iv_like.setBackgroundResource(R.drawable.like_press);
        } else {
            holder.iv_like.setBackgroundResource(R.drawable.like_normal);
        }*/

        if(mListener!=null){
            holder.draweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String topId = news.getTopId();
                    String url = news.getUrl();
                    mListener.ItemClickListener(holder.draweeView, topId,url,true,position);
                }
            });

            holder.iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String topId = news.getTopId();
                    mListener.ItemClickListener(holder.iv_like,holder.tv_like, topId,position);
                }
            });

            holder.draweeView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //int position = holder.getLayoutPosition();
                    mListener.ItemLongClickListener(holder.draweeView,position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int size = newses==null?0:newses.size();
        return FooterView==null?size:size+1;
    }

    public List<New> getNewses() {
        return newses;
    }

    class RefreshViewHolder extends RecyclerView.ViewHolder{
        TextView tv_adminName;
        TextView tv_summary;
        TextView tv_like;
        TextView tv_browserNum;
        SimpleDraweeView draweeView;
        ImageView iv_like;

        public RefreshViewHolder(View itemView){
            super(itemView);
            if(itemView==FooterView){
                return;
            }
            tv_adminName = (TextView) itemView.findViewById(R.id.tv_adminName);
            tv_summary = (TextView) itemView.findViewById(R.id.tv_summary);
            tv_like = (TextView) itemView.findViewById(R.id.tv_like);
            //tv_browserNum = (TextView) itemView.findViewById(R.id.tv_browse);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.draweeView);
            iv_like = (ImageView) itemView.findViewById(R.id.iv_like);
        }
    }
}
