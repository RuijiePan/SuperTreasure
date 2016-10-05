package com.supertreasure.mine;

import android.content.Context;
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

import java.util.List;

import me.itangqi.greendao.MoodMessage;

/**
 * Created by yum on 2016/2/3.
 */
public class MoodMessageAdapter extends RecyclerView.Adapter<MoodMessageAdapter.MoodmessageViewHolder>{

    private Context context;
    public MoodMessageAdapter(Context context) {
        this.context = context;
    }
    public interface OnItemClickListener {
        void onItemClick(MoodmessageViewHolder holder, int position);
        void onItemLongClick(MoodmessageViewHolder holder, int position);
    }
    private OnItemClickListener mOnItemClickListener;
    private List<MoodMessage> list_moodmessage;
    public MoodMessageAdapter(Context context, List<MoodMessage> list_moodmessage, OnItemClickListener mOnItemClickListener) {
        this.context = context;
        this.list_moodmessage = list_moodmessage;
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public MoodMessageAdapter(Context context, List<MoodMessage> list_moodmessage) {
        this.context = context;
        this.list_moodmessage = list_moodmessage;
    }

    @Override
    public MoodmessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_moodmessage,null);
        MoodmessageViewHolder viewHolder = new MoodmessageViewHolder(itemView);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(MoodmessageViewHolder holder, int position) {
        System.out.println("onBindViewHolder");

        final MoodmessageViewHolder viewHolder = holder;
        MoodmessageViewHolder holderTemp =  holder;
        if (mOnItemClickListener!=null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout linearLayout = (LinearLayout) v;
                    //Log.i("ActivityMyCollection","onClick "+linearLayout.toString());
                    mOnItemClickListener.onItemClick(viewHolder,viewHolder.getLayoutPosition());
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LinearLayout linearLayout = (LinearLayout) v;
                    //Log.i("ActivityMyCollection","onLongClick "+linearLayout.toString());
                    mOnItemClickListener.onItemLongClick(viewHolder,viewHolder.getLayoutPosition());
                    return false;
                }
            });
        }

        holderTemp.textView_content.setText(list_moodmessage.get(position).getMessage());
        //AbImageLoader.getInstance(context).display(holderTemp.imageView_img, good.getPic());

        holderTemp.textView_from.setText(list_moodmessage.get(position).getFromNickName());
        //holderTemp.textView_to.setText(list_moodmessage.get(position).get);
    }

    public void addData(int position,MoodMessage collection) {
        list_moodmessage.add(position, collection);
        //swipeRefreshLayout.setRefreshing(false);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        list_moodmessage.remove(position);
        //swipeRefreshLayout.setRefreshing(false);
        notifyItemRemoved(position);
    }
    @Override
    public int getItemCount() {
        return list_moodmessage ==null?0: list_moodmessage.size();
    }



    public class MoodmessageViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView_img;
        public TextView textView_from;
        public TextView textView_to;
        public TextView textView_content;
        public MoodmessageViewHolder(View itemView) {
            super(itemView);
            AbViewUtil.scaleContentView((LinearLayout) itemView.findViewById(R.id.root));
            imageView_img = (ImageView) itemView.findViewById(R.id.headImg);//
            textView_from = (TextView) itemView.findViewById(R.id.name_from);
            textView_to = (TextView) itemView.findViewById(R.id.name_to);
            textView_content = (TextView) itemView.findViewById(R.id.moodcontent);
        }
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public List<MoodMessage> getList_moodmessage() {
        return list_moodmessage;
    }

    public void setList_moodmessage(List<MoodMessage> list_moodmessage) {
        this.list_moodmessage = list_moodmessage;
        notifyDataSetChanged();
        notifyItemRangeInserted(0, list_moodmessage.size()-1);
    }
}

