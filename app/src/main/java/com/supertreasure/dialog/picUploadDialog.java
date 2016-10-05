package com.supertreasure.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.supertreasure.R;

/**
 * Created by Administrator on 2016/2/19.
 */
public class picUploadDialog extends Dialog{
    private TextView tv;

    public picUploadDialog(Context context){
        super(context, R.style.picUploadDialogStyle);
    }

    public picUploadDialog(Context context,int theme){
        super(context,theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_dialog_loading);
        tv = (TextView) findViewById(R.id.tv);
        tv.setText("正在上传");
        LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(210);
    }

    public void setProcessbar(String processbar){
        tv.setText(processbar);
    }
}
