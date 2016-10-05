package com.supertreasure.home;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.ab.util.AbViewUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.supertreasure.R;
import com.supertreasure.bean.CommentBean;
import com.supertreasure.util.DateUtils;
import com.supertreasure.util.PrjBase64;
import com.supertreasure.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/1/25.
 */
public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MoodViewHolder> {

    /*public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View HeaderView ;*/
    private Context context;
    private List<CommentBean.Comment> commentList;
    private OnItemClickListener mListener;

    public MoodAdapter(Context context, List<CommentBean.Comment> commentList ){
        this.context = context;
        this.commentList = commentList;
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

    public void removeComment(int position){
        notifyItemRemoved(position);
        /*if(HeaderView!=null) {
            commentList.remove(position - 1);
            notifyItemRangeChanged(position,getItemCount());
        }else{*/
            commentList.remove(position);
            notifyItemRangeChanged(position, getItemCount());
        //}
    }

    public void addCommentList(List<CommentBean.Comment> commentList){
        for (int i=0;i<commentList.size();i++){
            notifyItemInserted(getItemCount()+1);
            this.commentList.add(commentList.get(i));
        }
        notifyDataSetChanged();
    }

    public void setCommentList(List<CommentBean.Comment> commentList){
        this.commentList = commentList;
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
    public MoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*if(HeaderView!=null&&viewType==TYPE_HEADER){
           return new MoodViewHolder(HeaderView);
        }*/
        View view = LayoutInflater.from(context).inflate(R.layout.school_say_comment,parent,false);
        AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.ll_comment));
        MoodViewHolder viewHolder = new MoodViewHolder(view);
        return  viewHolder;
    }

    public interface OnItemClickListener{
        public void ItemClickListener(View ll_comment,String toCommentId);
    }

    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(final MoodViewHolder holder, int position) {

        /*if(getItemViewType(position)==TYPE_HEADER)
            return;

        //要加这一句，不然添加头图后会少一条数据
        position = getRealPosition(holder);*/
        CommentBean.Comment comment = commentList.get(position);
        int width = 80, height = 80;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(comment.getFromUser().getUserPic()))
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(holder.draweeView.getController())
                .setImageRequest(request)
                .build();
        holder.draweeView.setController(controller);
        //holder.draweeView.setImageURI(Uri.parse(comment.getFromUser().getUserPic()));
        //AbImageLoader.getInstance(context).display(holder.civ_photo, comment.getFromUser().getUserPic());
        holder.tv_username.setText(comment.getFromUser().getNickName());
        holder.tv_time.setText(DateUtils.fromToday(comment.getCommentTime()));

        String content = PrjBase64.decode(comment.getContent());
        if(comment.getToUser()!=null) {
            if (!comment.getFromUser().getNickName().equals(comment.getToUser().getNickName())) {
                String touser_name = comment.getToUser().getNickName();
                int start = 2;
                int end = touser_name.length() + 2;
                SpannableString sst = new SpannableString("回复" + comment.getToUser().getNickName() + ":" + content);
                sst.setSpan(new CommentClickSpan(context, touser_name), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tv_content.setText(sst);
                holder.tv_content.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                holder.tv_content.setText(content);
            }
        }else{
            holder.tv_content.setText(content);
        }

        final String toCommentID = comment.getCommentId();
        if(mListener!=null){
            holder.ll_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(context,"点击了评论",Toast.LENGTH_SHORT).show();
                    mListener.ItemClickListener(v,toCommentID);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        //return HeaderView==null?commentList.size():commentList.size()+1;
        return commentList==null?0:commentList.size();
    }

    class MoodViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView draweeView;
        TextView tv_username;
        TextView tv_time;
        EmojiconTextView tv_content;
        LinearLayout ll_comment;

        public MoodViewHolder(View itemView){
            super(itemView);
            /*if(itemView==HeaderView){
                return;
            }*/
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.civ_photo);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_content = (EmojiconTextView) itemView.findViewById(R.id.tv_content);
            ll_comment = (LinearLayout) itemView.findViewById(R.id.ll_comment);
        }
    }
}
