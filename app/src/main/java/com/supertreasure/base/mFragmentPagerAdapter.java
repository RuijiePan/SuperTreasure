package com.supertreasure.base;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class mFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragmentsList;
    public String[] title ;

    public mFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public mFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments,String[] title) {
        super(fm);
        this.fragmentsList = fragments;
        this.title = title;
    }

    public mFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentsList.get(arg0);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}