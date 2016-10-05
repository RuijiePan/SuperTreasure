package com.supertreasure.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.supertreasure.R;


public class RefreshDialog extends Dialog {

    public RefreshDialog(Context context) {
        super(context, R.style.dialog_fade);
        setContentView(R.layout.refresh_dialog_layout);
        setCanceledOnTouchOutside(false);
    }

    public RefreshDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.refresh_dialog_layout);
        setCanceledOnTouchOutside(false);
    }
    
    public void setTitle(String title){
        TextView tv = (TextView) findViewById(R.id.title);
        if(null != title){
            tv.setText(title);
            tv.setVisibility(View.VISIBLE);
        }else{
            tv.setText("");
            tv.setVisibility(View.GONE);
        }
    }
}
