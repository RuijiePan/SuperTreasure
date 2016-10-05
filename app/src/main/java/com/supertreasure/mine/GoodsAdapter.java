package com.supertreasure.mine;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.supertreasure.R;
import com.supertreasure.bean.GoodBean;
import com.supertreasure.bean.GoodsBeen;

import java.util.List;

/**
 * Created by yum on 2016/2/3.
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder>{

    private Context context;
    private OnItemClickListener mOnItemClickListener;
    private List<GoodsBeen.Goods> list_goods;

    public GoodsAdapter(Context context) {
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, int position ,String sellID);
        void onItemLongClick(ViewHolder holder, int position ,String sellID);
    }

    public GoodsAdapter(Context context,OnItemClickListener mOnItemClickListener) {
        this.context = context;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_mygoods,null);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final GoodsBeen.Goods good = list_goods.get(position);

        if (mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder,holder.getLayoutPosition(),good.getSellID());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder,holder.getLayoutPosition(),good.getSellID());
                    return false;
                }
            });
        }
        int width = 135, height = 135;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(list_goods.get(position).getPic()))
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(holder.draweeView.getController())
                .setImageRequest(request)
                .build();
        holder.draweeView.setController(controller);
        holder.textView_content.setText(good.getIntroduction());
        holder.textView_price.setText(good.getPrice());
    }

    public void updateData(List<GoodsBeen.Goods> goodsBeenList){
        list_goods = goodsBeenList;
        notifyItemRangeInserted(0,goodsBeenList.size()-1);
        notifyDataSetChanged();
    }

    public void addData(int position,List<GoodsBeen.Goods> goodsBeen) {
        list_goods.add(position, goodsBeen.get(position));
        notifyItemInserted(position);
    }

    public void addList(List<GoodsBeen.Goods> goodsBeen) {
        list_goods.addAll(goodsBeen);
        notifyDataSetChanged();
    }


    public void removeData(int position) {
        list_goods.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public int getItemCount() {
        return list_goods ==null?0: list_goods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public SimpleDraweeView draweeView;
        public TextView textView_content;
        public TextView textView_price;
        public ViewHolder(View itemView) {
            super(itemView);
            AbViewUtil.scaleContentView((LinearLayout) itemView.findViewById(R.id.root));
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.goods_img);//
            textView_content = (TextView) itemView.findViewById(R.id.goods_content);
            textView_price = (TextView) itemView.findViewById(R.id.goods_price);
        }
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public List<GoodsBeen.Goods> getList_goods() {
        return list_goods;
    }

    public void setList_goods(List<GoodsBeen.Goods> list_goods) {
        this.list_goods = list_goods;
        notifyDataSetChanged();
    }


}
