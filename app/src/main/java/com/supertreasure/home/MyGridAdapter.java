package com.supertreasure.home;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ab.util.AbViewUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.supertreasure.R;
import com.supertreasure.main.TheApp;

/**
 * Created by Administrator on 2015/9/26.
 */
public class MyGridAdapter extends BaseAdapter{
    private String[] files;
    private LayoutInflater mLayoutInflater;
    private Context context;

    public MyGridAdapter(String[] files,Context context){
        this.files = files;
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return files==null?0:files.length;
    }

    @Override
    public Object getItem(int position) {
        return files[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyGridViewHolder viewHolder;

        if(convertView==null){
            viewHolder = new MyGridViewHolder();
            if(files.length!=1)
                convertView = mLayoutInflater.inflate(R.layout.mood_item_gridview,parent, false);
            else
                convertView = mLayoutInflater.inflate(R.layout.mood_item_single_gridview,parent, false);
            viewHolder.draweeView = (SimpleDraweeView) convertView.findViewById(R.id.album_image);
            AbViewUtil.scaleContentView((LinearLayout)convertView.findViewById(R.id.root));
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (MyGridViewHolder) convertView.getTag();
        }

        String url = (String) getItem(position);
        int width = 185, height = 185;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(viewHolder.draweeView.getController())
                .setImageRequest(request)
                .build();
        viewHolder.draweeView.setController(controller);
        //viewHolder.draweeView.setImageURI(Uri.parse(url));

        return convertView;
    }

    private class MyGridViewHolder{
        SimpleDraweeView draweeView;
    }
}
