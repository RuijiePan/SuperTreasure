package com.supertreasure.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.supertreasure.util.GetHeightResUrl;

/**
 * Created by Administrator on 2015/9/26.
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    public String[] fileList;

    public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
        super(fm);
        this.fileList = fileList;
    }

    @Override
    public Fragment getItem(int position) {
        String url = fileList[position];
        return FrescoDetailFragment.newInstance(url);
    }

    @Override
    public int getCount() {
        return fileList == null ? 0 : fileList.length;
    }
}
