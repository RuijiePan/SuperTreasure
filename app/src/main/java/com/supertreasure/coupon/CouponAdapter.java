package com.supertreasure.coupon;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ab.util.AbViewUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.supertreasure.R;
import com.supertreasure.bean.CouponBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.itangqi.greendao.Coupon;

/**
 * Created by Administrator on 2016/2/12.
 */
public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponViewHolder>{

    /*public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_FOOTER = 2;*/

    private OnItemClickListener mListener;
    private Context context;
    private List<Coupon> list;
    static Map<String,Integer> heightMap = new HashMap<>();
    static Map<String,Integer> widthMap = new HashMap<>();
    //private View mFootView;

    public CouponAdapter(Context context, List<Coupon> list) {
        this.context = context;
        this.list = list;
    }

    public void updataView(int position,boolean isPraise,boolean isCollect, int praiseNum,int browseNum){
        list.get(position).setBrowserNum(browseNum);
        list.get(position).setCouponPraise(praiseNum);
        list.get(position).setIsPraise(isPraise);
        list.get(position).setIsCollect(isCollect);
        notifyItemChanged(position);
    }

    /*public void setFootViewVisible(boolean isVisible){
        if(isVisible){
            mFootView.setVisibility(View.VISIBLE);
            notifyItemInserted(list.size());
        }else {
            mFootView.setVisibility(View.GONE);
            notifyItemRemoved(list.size());
        }
    }*/

    /*public View getmFootView() {
        return mFootView;
    }

    public void setmFootView(View mFootView) {
        this.mFootView = mFootView;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        if(mFootView==null){
            return TYPE_NORMAL;
        }if(position==getItemCount()-1)
            return TYPE_FOOTER;

        return TYPE_NORMAL;
    }*/

    @Override
    public CouponViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*if(mFootView!=null&&viewType==TYPE_FOOTER){
            return new CouponViewHolder(mFootView);
        }*/
        View view = LayoutInflater.from(context).inflate(R.layout.item_waterfallflow_coupon,null);
        CouponViewHolder holder = new CouponViewHolder(view);
        return holder;
    }

    public interface OnItemClickListener{
        public void ItemClickListener(View view, Coupon coupon,int position);
        public void ItemLongClickListener(View view , int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(final CouponViewHolder holder, final int position) {

        /*if(getItemViewType(position)==TYPE_FOOTER)
            return;*/

        if(mListener!=null){
            holder.draweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.ItemClickListener(holder.draweeView,list.get(position),holder.getLayoutPosition());
                    /*Log.w("hahapic",list.get(position).getCouponPics());
                    Log.w("hahaId",list.get(position).getCouponID()+"");*/
                }
            });

            holder.draweeView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mListener.ItemLongClickListener(holder.draweeView,pos);
                    return true;
                }
            });
        }

        final String[] pics = list.get(position).getCouponPics().split(",");
        if(pics.length>0) {
            int heightTarget = (int)getTargetHeight(list.get(position).getWidth(),list.get(position).getHeight(),holder.itemView,pics[0]);
            updateItemtHeight(heightTarget,holder.itemView);
            if(heightMap.containsKey(pics[0])) {
                int height = heightMap.get(pics[0]);
                if(height>0) {
                    updateItemtHeight(height,holder.itemView);
                    holder.draweeView.setImageURI(Uri.parse(pics[0]));
                    return;
                }
            }

            ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    if(imageInfo==null) {
                        return;
                    }
                    QualityInfo qualityInfo = imageInfo.getQualityInfo();
                    if(qualityInfo.isOfGoodEnoughQuality()) {
                        int heightTarget = (int)getTargetHeight(imageInfo.getWidth(),imageInfo.getHeight(),holder.itemView,pics[0]);
                        if(heightTarget<=0)
                            return;
                        heightMap.put(pics[0],heightTarget);
                        updateItemtHeight(heightTarget,holder.itemView);
                    }
                }

            };

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(pics[0]))
                    .setControllerListener(controllerListener)
                    .setOldController(holder.draweeView.getController())
                    .setTapToRetryEnabled(false)
                    .build();

            holder.draweeView.setController(controller);
          /*  Log.w("hahapic3333",list.get(position).getCouponPics());
            Log.w("hahaID2222",list.get(position).getCouponID()+"");*/
        }

    }

    private void updateItemtHeight(int height, View view) {
        CardView cardView = (CardView) view.findViewById(R.id.cardView);
        View child = view.findViewById(R.id.draweeView);
        CardView.LayoutParams layoutParams = (CardView.LayoutParams) child.getLayoutParams();
        layoutParams.height = height;
        cardView.updateViewLayout(child,layoutParams);
    }

    private float getTargetHeight(float width,float height,View view,String url) {
        View child = view.findViewById(R.id.draweeView);
        float widthTarget;
        if(widthMap.containsKey(url))
            widthTarget = widthMap.get(url);
        else {
            widthTarget = child.getMeasuredWidth();
            if (widthTarget>0) {
                widthMap.put(url,(int)widthTarget);
            }
        }

        return height*(widthTarget/width);
    }

    /*@Override
    public void onViewAttachedToWindow(CouponViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if(lp!=null&&lp instanceof StaggeredGridLayoutManager.LayoutParams){
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition()==getItemCount()-1);
        }

    }*/

    @Override
    public int getItemCount() {
        /*int size = list==null?0:list.size();
        return mFootView==null?size:size+1;*/
        return list==null?0:list.size();
    }

    public void addCoupon(List<Coupon> couponList){
        if (list != null)
            list.addAll(couponList);
        //Log.w("haha",couponList.size()+"");
        //notifyDataSetChanged();
      //  notifyItemRangeInserted(list.size()-couponList.size(),list.size());
    }

    public void setCoupon(List<Coupon> couponList){
        list = couponList;
        notifyDataSetChanged();
        notifyItemRangeInserted(0,couponList.size()-1);
    }

    class CouponViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView draweeView;
        LinearLayout root ;

        public CouponViewHolder(View itemView){
            super(itemView);
            /*if(itemView==mFootView){
                return;
            }*/
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.draweeView);
            root = (LinearLayout) itemView.findViewById(R.id.root);
            AbViewUtil.scaleContentView((LinearLayout)itemView.findViewById(R.id.root));
        }
    }
}
