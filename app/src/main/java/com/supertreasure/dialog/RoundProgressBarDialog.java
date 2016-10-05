package com.supertreasure.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.supertreasure.R;
import com.supertreasure.util.RoundProgressBar;

/**
 * Created by prj on 2016/4/22.
 */
public class RoundProgressBarDialog extends ProgressDialog {

    private int mTotalProgress;
    private int mCurrentProgress;
    private RoundProgressBar roundProgressBar;

    public RoundProgressBarDialog(Context context) {
        super(context, R.style.picUploadDialogStyle);
    }

    public RoundProgressBarDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_round_progressbar);
        roundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);
    }

    public void setProgress(long current,long all){
        roundProgressBar.setProgress((int) (current*100/all));
    }
}
