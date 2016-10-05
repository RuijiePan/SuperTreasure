package com.supertreasure.base;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;

public abstract class BaseFragment extends Fragment implements OnClickListener {
	protected boolean isVisible;
	protected Activity mActivity;
	protected View view;
	private ImageView iv_back;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// 获取依赖的activity对象,也可做上下文常量
		mActivity = getActivity();
		// 解决fragment片段丢失的问题
		if (view == null) {
			view = initView();
		} else {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
		}
		initData();
		setListener();// 初始化监听器
		return view;
	}

	// 初始化布局
	public abstract View initView();

	// 初始化数据
	public abstract void initData();

	// 初始化监听器
	public abstract void setListener();

	// 替换片段
	protected void replace(int id, BaseFragment fragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(id, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	// 移除片段
	protected void remove(BaseFragment fragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.remove(fragment);
	}


	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if(getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}


	/**
	 * 可见
	 */
	protected void onVisible() {
		lazyLoad();
	}


	/**
	 * 不可见
	 */
	protected void onInvisible() {


	}
	protected  void lazyLoad(){
		Logger.i("fragment","lazyLoad");
	};
}
