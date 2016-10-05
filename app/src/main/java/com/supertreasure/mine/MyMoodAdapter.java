package com.supertreasure.mine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.ab.util.AbViewUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.supertreasure.R;
import com.supertreasure.bean.MoodBean;
import com.supertreasure.bean.MyMoodBean;
import com.supertreasure.home.ImagePagerActivity;
import com.supertreasure.home.MyGridAdapter;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.DateUtils;
import com.supertreasure.util.NoScrollGridView;
import com.supertreasure.util.PrjBase64;
import com.supertreasure.widget.CircleImageView;

import java.util.List;

import me.itangqi.greendao.Mood;

/**
 * Created by prj on 2016/1/25.
 */
public class MyMoodAdapter extends RecyclerView.Adapter<MyMoodAdapter.MyMoodViewHolder> {

    private Context context;
    private List<Mood> moodList;
    private OnItemClickListener mListener;
    
    public MyMoodAdapter(Context context, List<Mood> moodList){
        this.context = context;
        this.moodList = moodList;
    }

    public MyMoodAdapter(Context context){
        this.context = context;
    }

    @Override
    public MyMoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_my_mood,parent,false);
        //AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.root));
        MyMoodViewHolder viewHolder = new MyMoodViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyMoodViewHolder holder, final int position) {

        final Mood mood = moodList.get(position);

        holder.tv_time.setText(DateUtils.fromToday(mood.getPublishtime()));
        holder.tv_content.setText(PrjBase64.decode(mood.getContent()));
        holder.tv_username.setText(mood.getMe().getNickName());
        holder.tv_message.setText(mood.getCommentCount());
        holder.tv_like.setText(mood.getPraiseTimes()+"");
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

        if(mood.getPaths()!=null){
            final String[] pics = mood.getPaths().split(",");
            if(pics.length>0){
                holder.gridView.setVisibility(View.VISIBLE);
                holder.gridView.setAdapter(new MyGridAdapter(pics, context));

                if(pics.length==1)
                    holder.gridView.setNumColumns(1);
                else{
                    holder.gridView.setNumColumns(3);
                }
                holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        imageBrower(position,pics);
                    }
                });
            }else {
                holder.gridView.setVisibility(View.GONE);
            }
        }

        if(mListener!=null){
            /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.ItemClickListener(holder.itemView,mood,position);
                }
            });*/
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
        //public void ItemClickListener(View itemView,MyMoodBean.Mood mood,int position);
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
        notifyItemChanged(position);
    }

    public void updateIsPraised(int position,boolean isPraised,String praiseTimes){
        moodList.get(position).setIsPraised(isPraised);
        moodList.get(position).setPraiseTimes(praiseTimes);
        notifyItemChanged(position);
    }

    class MyMoodViewHolder extends RecyclerView.ViewHolder{
        LinearLayout root;
        LinearLayout ll_message;
        LinearLayout ll_like;
        SimpleDraweeView draweeView;
        TextView tv_username;
        TextView tv_time;
        EmojiconTextView tv_content;
        TextView tv_message;
        TextView tv_like;
        ImageView iv_like;
        ImageView iv_message;
        NoScrollGridView gridView;

        public MyMoodViewHolder(View itemView){
            super(itemView);

            root = (LinearLayout) itemView.findViewById(R.id.root);
            ll_like = (LinearLayout) itemView.findViewById(R.id.ll_like);
            ll_message = (LinearLayout) itemView.findViewById(R.id.ll_message);
            gridView = (NoScrollGridView) itemView.findViewById(R.id.gridView);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.civ_photo);
            iv_like = (ImageView) itemView.findViewById(R.id.iv_like);
            iv_message = (ImageView) itemView.findViewById(R.id.iv_message);
            tv_like = (TextView) itemView.findViewById(R.id.tv_like);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_content = (EmojiconTextView) itemView.findViewById(R.id.tv_content);
            AbViewUtil.scaleContentView(root);
        }
    }
}
