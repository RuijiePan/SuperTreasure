package com.supertreasure.home;

import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ab.view.chart.Point;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.supertreasure.R;
import com.supertreasure.util.GetHeightResUrl;
import com.supertreasure.util.photoview.PhotoViewAttacher;

import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.OnViewTapListener;
import me.relex.photodraweeview.PhotoDraweeView;


/**
 * Created by Administrator on 2015/9/26.
 */
public class FrescoDetailFragment extends Fragment {

    private PointF downP = new PointF();
    private PointF curP = new PointF();
    private String ImageUrl;
    private PhotoDraweeView draweeView;
    private ProgressBar progressBar;
    private PhotoViewAttacher Attacher;
    private int progressBarHeight;
    private int progressBarWidth;

    public static FrescoDetailFragment newInstance(String imageUrl) {
        final FrescoDetailFragment f = new FrescoDetailFragment();

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
        View v = inflater.inflate(R.layout.fresco_detail_fragment,container,false);
        draweeView = (PhotoDraweeView) v.findViewById(R.id.draweeView);
        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        progressBarHeight = progressBar.getHeight();
        progressBarWidth = progressBar.getWidth();
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressBar.setVisibility(View.VISIBLE);

        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {

                if (animatable!=null) {
                    animatable.start();
                }
                if (imageInfo == null || draweeView == null) {
                    return;
                }
                draweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
            }

        };

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(GetHeightResUrl.getHeightUrl(ImageUrl)))
                .setAutoRotateEnabled(true).build();

        DraweeController mController1 = Fresco.newDraweeControllerBuilder()
                .setUri(GetHeightResUrl.getHeightUrl(ImageUrl))
                .setControllerListener(controllerListener)
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .build();

        draweeView.setController(mController1);
        draweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                getActivity().finish();
            }
        });
        draweeView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                getActivity().finish();
            }
        });
        /*draweeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                curP.x = event.getX();
                curP.y = event.getY();

                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    downP.x = event.getX();
                    downP.y = event.getY();
                    return true;
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    if (downP.x == curP.x && downP.y == curP.y) {
                        getActivity().finish();
                        return true;
                    }
                }

                return false;
            }
        });*/
        //Log.w("haha222222",GetHeightResUrl.getHeightUrl(ImageUrl));
        //TheApp.mAbImageLoader.display(imageView,progressBar,ImageUrl,progressBarWidth,progressBarHeight);
    }
}
