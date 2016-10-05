package com.supertreasure.util.ImageLoader;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.imagepicker.loader.ImageLoader;

public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)                             //配置上下文
                .load("file://" + path)                  //设置图片路径
                .placeholder(com.lzy.imagepicker.R.mipmap.default_image)
                .error(com.lzy.imagepicker.R.mipmap.default_image)           //设置错误图片
                .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存全尺寸
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}
