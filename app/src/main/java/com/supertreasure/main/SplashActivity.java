package com.supertreasure.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.supertreasure.login.ActivityLogin;

/**
 * Created by prj on 2016/4/17.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
        finish();
    }
}
