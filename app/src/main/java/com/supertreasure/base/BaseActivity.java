
package com.supertreasure.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View.OnClickListener;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setListener();//设置监听器

	}

	//设置监听器
	public abstract void setListener();

	// 替换片段
	protected void replace(int id, BaseFragment fragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(id, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	// 移除片段
	protected void remove(BaseFragment fragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.remove(fragment);
	}


}
