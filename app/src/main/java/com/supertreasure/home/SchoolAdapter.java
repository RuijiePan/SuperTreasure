package com.supertreasure.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.supertreasure.R;
import com.supertreasure.bean.IsPraised;
import com.supertreasure.bean.MoodBean;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;
import com.supertreasure.util.DateUtils;
import com.supertreasure.util.NoScrollGridView;
import com.supertreasure.util.NoScrollViewPager;
import com.supertreasure.util.PrjBase64;
import com.supertreasure.util.photoview.log.Logger;
import com.supertreasure.widget.CircleImageView;

import java.util.List;

import me.itangqi.greendao.Mood;

/**
 * Created by Administrator on 2016/1/25.
 */
public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.SchoolViewHolder> {

    private Context context;
    private List<Mood> moodList;
    private OnItemClickListener mListener;
    public SchoolAdapter(Context context,List<Mood> moodList){
        this.context = context;
        this.moodList = moodList;
    }

    public SchoolAdapter(Context context){
        this.context = context;
    }

    @Override
    public SchoolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.school_item,parent,false);
        SchoolViewHolder viewHolder = new SchoolViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SchoolViewHolder holder, final int position) {
        final Mood mood = moodList.get(position);
        holder.tv_username.setText(mood.getMe().getNickName());
        holder.tv_time.setText(DateUtils.fromToday(mood.getPublishtime()));
    /*    Log.d("tv_time",holder.tv_time.getText().toString());
        Log.d("tv_time",DateUtils.fromToday(mood.getPublishtime()));*/
        holder.tv_school.setText(mood.getMe().getBelongschool());
        holder.tv_content.setText(PrjBase64.decode(mood.getContent()));
        holder.tv_like.setText(mood.getPraiseTimes());
        holder.tv_message.setText(mood.getCommentCount());
        int width = 80, height = 80;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(mood.getMe().getUserPic()))
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(holder.draweeView.getController())
                .setImageRequest(request)
                .build();
        holder.draweeView.setController(controller);
        //holder.draweeView.setImageURI(Uri.parse(mood.getMe().getUserPic()));

        if(mood.getIsPraised()) {
            holder.iv_like.setBackgroundResource(R.drawable.like_press);
        }else {
            holder.iv_like.setBackgroundResource(R.drawable.like_normal);
        }

        holder.gridView.setVisibility(View.GONE);
        if(mood.getPaths()!=null){
            final String[] pics = mood.getPaths().split(",");
            if(pics.length>0){
                holder.gridView.setVisibility(View.VISIBLE);
                if(pics.length==1)
                    holder.gridView.setNumColumns(1);
                else{
                    holder.gridView.setNumColumns(3);
                }
                holder.gridView.setAdapter(new MyGridAdapter(pics, context));

                holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        imageBrower(position,pics);
                    }
                });
            }
        }

        if(mListener!=null){
            holder.ll_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int moodID = mood.getMoodId();
                    mListener.ItemClickListener(v,holder.iv_like,holder.tv_like,moodID,position);
                }
            });

            holder.ll_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.ItemClickListener(v,mood,position);
                }
            });
        }
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(TheApp.instance, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return moodList==null?0:moodList.size();
    }

    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public interface OnItemClickListener{
        public void ItemClickListener(View ll_like,View iv_like,TextView tv_like,int moodID,int position);
        public void ItemClickListener(View ll_messgae,Mood mood,int position);
    }

    public void addMood(List<Mood> moodList){
        this.moodList.addAll(moodList);
        notifyDataSetChanged();
    }

    public void setMoodList(List<Mood> moodList){
        this.moodList = moodList;
        notifyDataSetChanged();
    }

    public void updateItem(int position,boolean isPraised,String praiseTimes,String commentCount){
        moodList.get(position).setIsPraised(isPraised);
        moodList.get(position).setPraiseTimes(praiseTimes);
        moodList.get(position).setCommentCount(commentCount);
        notifyDataSetChanged();
    }

    public void updateIsPraised(int position,boolean isPraised,String praiseTimes){
        moodList.get(position).setIsPraised(isPraised);
        moodList.get(position).setPraiseTimes(praiseTimes);
       // notifyItemChanged(position);
    }

    class SchoolViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView draweeView;
        ImageView iv_like;
        ImageView iv_message;
        TextView tv_username;
        TextView tv_time;
        TextView tv_school;
        EmojiconTextView tv_content;
        TextView tv_like;
        TextView tv_message;
        LinearLayout ll_message;
        LinearLayout ll_like;
        LinearLayout root;
        NoScrollGridView gridView;

        public SchoolViewHolder(View itemView){
            super(itemView);

            root = (LinearLayout) itemView.findViewById(R.id.root);
            ll_like = (LinearLayout) itemView.findViewById(R.id.ll_like);
            ll_message = (LinearLayout) itemView.findViewById(R.id.ll_message);
            gridView = (NoScrollGridView) itemView.findViewById(R.id.gridView);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.civ_photo);
            tv_like = (TextView) itemView.findViewById(R.id.tv_like);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_school = (TextView) itemView.findViewById(R.id.tv_school);
            tv_content = (EmojiconTextView) itemView.findViewById(R.id.tv_content);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            iv_like = (ImageView) itemView.findViewById(R.id.iv_like);
            iv_message = (ImageView) itemView.findViewById(R.id.iv_message);
            AbViewUtil.scaleContentView(root);
        }
    }
}
