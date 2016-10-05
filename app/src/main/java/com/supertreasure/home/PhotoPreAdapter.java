package com.supertreasure.home;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ab.util.AbViewUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.supertreasure.R;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Bimp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prj on 2016/4/24.
 */
public class PhotoPreAdapter extends BaseAdapter {

    private Context context;
    private List<String> paths;
    private LayoutInflater mLayoutInflater;

    public PhotoPreAdapter(Context context){
        this.context = context;
        paths = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public PhotoPreAdapter(Context context,List<String> paths){
        this.paths = paths;
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void update(List<String> paths){
        this.paths = paths;
        notifyDataSetChanged();
    }

    public void clear(){
        this.paths.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return paths==null?1:paths.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView==null){
            convertView = mLayoutInflater.inflate(R.layout.mood_item_gridview,parent,false);
            AbViewUtil.scaleContentView((LinearLayout) convertView.findViewById(R.id.root));
            holder = new ViewHolder();
            holder.image = (SimpleDraweeView) convertView.findViewById(R.id.album_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position==paths.size()){
            int width = 170, height = 170;
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(
                    "res://com.supertreasure/"+R.drawable.icon_addpic_unfocused))
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(holder.image.getController())
                    .setImageRequest(request)
                    .build();
            holder.image.setController(controller);
           /* Glide.with(TheApp.instance)
                    //配置上下文
                    .load(R.drawable.icon_addpic_unfocused)                  //设置图片路径
                    .thumbnail(0.1f)
                    .placeholder(com.lzy.imagepicker.R.mipmap.default_image)
                    .error(com.lzy.imagepicker.R.mipmap.default_image)           //设置错误图片
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存全尺寸
                    .into(holder.image);*/
            //holder.image.setImageResource(R.drawable.icon_addpic_unfocused);
            if(position==9){
                holder.image.setVisibility(View.GONE);
            }
        }else {
            //Log.w("hahasize","2");
            /*Glide.with(TheApp.instance)                             //配置上下文
                    .load(paths.get(position))                  //设置图片路径
                    .thumbnail(0.1f)
                    .placeholder(com.lzy.imagepicker.R.mipmap.default_image)
                    .error(com.lzy.imagepicker.R.mipmap.default_image)           //设置错误图片
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存全尺寸
                    .into(holder.image);*/
            int width = 170, height = 170;
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(
                    "file://"+paths.get(position)))
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(holder.image.getController())
                    .setImageRequest(request)
                    .build();
            holder.image.setController(controller);
        }

        return convertView;
    }

    public class ViewHolder{
        public SimpleDraweeView image;
    }
}
