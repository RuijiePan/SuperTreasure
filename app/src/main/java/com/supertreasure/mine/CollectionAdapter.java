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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.supertreasure.R;
import com.supertreasure.bean.CollectionBeen;
import com.supertreasure.service.MySmallShopService;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by yum on 2016/2/3.
 */
public class CollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    public static final int TYPE_CONTENT = 0;
    public static final int TYPE_GROUP = 1;
    public CollectionAdapter(Context context) {
        this.context = context;
    }
    public interface OnItemClickListener {
        void onItemClick(ContentViewHolder holder, int position);
        void onItemLongClick(ContentViewHolder holder, int position);
    }
    private OnItemClickListener mOnItemClickListener;
    private List<CollectionBeen.Collection> list_collection;
    public CollectionAdapter(Context context, List<CollectionBeen.Collection> list_coupons, OnItemClickListener mOnItemClickListener) {
        this.context = context;
        this.list_collection = list_coupons;
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public CollectionAdapter(Context context, List<CollectionBeen.Collection> list_coupons) {
        this.context = context;
        this.list_collection = list_coupons;
    }

    @Override
    public int getItemViewType(int position) {
        return list_collection.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType){
            case TYPE_CONTENT:
                itemView = View.inflate(parent.getContext(), R.layout.item_mycollection,null);
                ContentViewHolder viewHolder = new ContentViewHolder(itemView);
                return viewHolder;
            case TYPE_GROUP:
                itemView = View.inflate(parent.getContext(), R.layout.item_mycollectiongroup,null);
                GroupViewHolder groupViewHolder = new GroupViewHolder(itemView);
                return groupViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       // System.out.println("onBindViewHolder");

        switch ( holder.getItemViewType () ) {
            case TYPE_CONTENT:
                final ContentViewHolder viewHolder = (ContentViewHolder) holder;
                ContentViewHolder holderTemp = (ContentViewHolder) holder;
                if (mOnItemClickListener!=null){
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LinearLayout linearLayout = (LinearLayout) v;
                            Log.i("ActivityMyCollection","onClick "+linearLayout.toString());
                            mOnItemClickListener.onItemClick(viewHolder,viewHolder.getLayoutPosition());
                        }
                    });
                    viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            LinearLayout linearLayout = (LinearLayout) v;
                            Log.i("ActivityMyCollection","onLongClick "+linearLayout.toString());
                            mOnItemClickListener.onItemLongClick(viewHolder,viewHolder.getLayoutPosition());
                            return false;
                        }
                    });
                }
                int width = 80, height = 80;
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(list_collection.get(position).getPic()))
                        .setResizeOptions(new ResizeOptions(width, height))
                        .build();
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setOldController(holderTemp.draweeView.getController())
                        .setImageRequest(request)
                        .build();
                holderTemp.draweeView.setController(controller);
                //AbImageLoader.getInstance(context).display(holderTemp.imageView_img,list_collection.get(position).getPic());
                holderTemp.textView_content.setText(list_collection.get(position).getIntroduction());
                holderTemp.textView_price.setText(list_collection.get(position).getPrice());
                break;
            case TYPE_GROUP:
                GroupViewHolder groupViewHolder = ( GroupViewHolder ) holder;
                groupViewHolder.textView_group.setText ( list_collection.get(position).getIntroduction());
                break;
        }
    }

    public void addData(int position,CollectionBeen.Collection collection) {
        list_collection.add(position, collection);
        //swipeRefreshLayout.setRefreshing(false);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        list_collection.remove(position);
        //swipeRefreshLayout.setRefreshing(false);
        notifyItemRemoved(position);
    }
    @Override
    public int getItemCount() {
        return list_collection==null?0:list_collection.size();
    }



    public class ContentViewHolder extends RecyclerView.ViewHolder{
        public SimpleDraweeView draweeView;
        public TextView textView_content;
        public TextView textView_price;
        public ContentViewHolder(View itemView) {
            super(itemView);
            AbViewUtil.scaleContentView((LinearLayout) itemView.findViewById(R.id.root));

            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.goods_img);//
            textView_content = (TextView) itemView.findViewById(R.id.goods_content);
            textView_price = (TextView) itemView.findViewById(R.id.goods_price);
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

    public List<CollectionBeen.Collection> getList_collection() {
        return list_collection;
    }

    public void setList_collection(List<CollectionBeen.Collection> list_collection) {
        this.list_collection = list_collection;
        notifyDataSetChanged();
        notifyItemRangeInserted(0,list_collection.size()-1);
    }
    /*  private void asyncImageLoad(final ImageView imageView,final String path) {
        AsyncImageTask asyncImageTask = new AsyncImageTask(imageView);
        asyncImageTask.execute(path);
    }
    private class AsyncImageTask extends AsyncTask<String,Integer,Uri> {
        public ImageView imageView;

        public AsyncImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Uri doInBackground(String... params) {//子线程执行，返回值作为消息发送到onPostExecute（）；
            try {
                return MySmallShopService.getImage(params[0],cache);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Uri uri) {//主线程执行  ，这个参数 从doInBackground（）返回。
            if (uri != null && imageView != null) {
                imageView.setImageURI(uri);
            }
            else if(uri == null && imageView != null){
                imageView.setBackgroundResource(R.drawable.iv_mine_head);
            }
        }
    }*/
}

