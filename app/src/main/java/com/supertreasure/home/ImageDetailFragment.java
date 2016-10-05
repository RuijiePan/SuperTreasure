package com.supertreasure.home;

import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.supertreasure.R;

import com.supertreasure.main.TheApp;
import com.supertreasure.util.GetHeightResUrl;
import com.supertreasure.util.photoview.PhotoViewAttacher;


/**
 * Created by Administrator on 2015/9/26.
 */
public class ImageDetailFragment extends Fragment {

    private String ImageUrl;
    private ImageView imageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher Attacher;
    private int progressBarHeight;
    private int progressBarWidth;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageUrl = getArguments()!=null?getArguments().getString("url"):null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.image_detail_fragment,container,false);
        imageView = (ImageView) v.findViewById(R.id.image);
        Attacher = new PhotoViewAttacher(imageView);

        Attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                getActivity().finish();
            }
        });

        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        progressBarHeight = progressBar.getHeight();
        progressBarWidth = progressBar.getWidth();
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String url = GetHeightResUrl.getHeightUrl(ImageUrl);
        /*Glide.with(this)                             //配置上下文
                .load(url)                  //设置图片路径
                .placeholder(com.lzy.imagepicker.R.mipmap.default_image)
                .error(com.lzy.imagepicker.R.mipmap.default_image)           //设置错误图片
                .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存全尺寸
                .into(imageView);*/
        TheApp.mAbImageLoader.display(imageView,progressBar,url,progressBarWidth,progressBarHeight);
        //TheApp.mAbImageLoader.setLoadingImage(url);
    }
}
