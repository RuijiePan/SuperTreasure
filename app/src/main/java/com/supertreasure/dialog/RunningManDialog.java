package com.supertreasure.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.supertreasure.R;
/**
 * Created by Administrator on 2016/3/9.
 */
public class RunningManDialog extends ProgressDialog{

    private AnimationDrawable mAnimation;
    private ImageView mImageView;
    private TextView mLoadingTv;
    private Context context;
    private String mLoadingTip;
    private int Resid;

    public RunningManDialog(Context context,String content,int Resid){
        super(context,R.style.RuningProgressDialog);
        this.context = context;
        this.mLoadingTip = content;
        this.Resid = Resid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        mImageView.setBackgroundResource(Resid);
        // 通过ImageView对象拿到背景显示的AnimationDrawable
        mAnimation = (AnimationDrawable) mImageView.getBackground();
        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();

            }
        });
        mLoadingTv.setText(mLoadingTip);
    }

    private void initView() {
        setContentView(R.layout.running_man_progress_dialog);
        mLoadingTv = (TextView) findViewById(R.id.loadingTv);
        mImageView = (ImageView) findViewById(R.id.loadingIv);
    }

    public void setProgressBar(String progressBar){
        mLoadingTv.setText(progressBar);
    }
}
