package com.supertreasure.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ab.activity.AbActivity;
import com.ab.util.AbViewUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.supertreasure.R;
import com.supertreasure.eventbus.RefreshMarketGridView;
import com.supertreasure.eventbus.RefreshMoodGridView;
import com.supertreasure.util.Config;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/4.
 */
public class ActivityPhoto extends AbActivity {

    private ArrayList<View> listViews = null;
    private ViewPager viewPager;
    private MyPageAdapter adapter;
    private int count;
    private String[] paths;
    private String type;
    private ArrayList<String> pathList = new ArrayList<>();
    RelativeLayout rl_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        AbViewUtil.scaleContentView((RelativeLayout) findViewById(R.id.root));
        rl_photo = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
        rl_photo.setBackgroundColor(0x70000000);

        type = getIntent().getStringExtra(Config.Type);
        paths = getIntent().getStringArrayExtra("urls");
        for (int i=0;i<paths.length;i++)
            pathList.add(paths[i]);

        Button photo_bt_exit = (Button) findViewById(R.id.photo_bt_exit);
        photo_bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button photo_bt_del = (Button) findViewById(R.id.photo_bt_del);
        photo_bt_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.removeAllViews();
                listViews.remove(count);
                pathList.remove(count);
                if(listViews.size()!=0) {
                    adapter.setListViews(listViews);
                    adapter.notifyDataSetChanged();
                }
                //Log.w("hahaevent",pathList.toString());
                if(type.equals(Config.Mood))
                    EventBus.getDefault().post(new RefreshMoodGridView(pathList));
                else
                    EventBus.getDefault().post(new RefreshMarketGridView(pathList));
                if(listViews.size()==0)
                    finish();
            }
        });

        Button photo_bt_enter = (Button) findViewById(R.id.photo_bt_enter);
        photo_bt_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(pageChangeListener);

        //Log.w("hahayy",paths.toString());
        for (int i=0;i<paths.length;i++){
            initListViews(paths[i]);
        }

        adapter = new MyPageAdapter(listViews);
        viewPager.setAdapter(adapter);
        Intent intent = getIntent();
        int id = intent.getIntExtra("ID",0);
        viewPager.setCurrentItem(id);
    }

    private void initListViews(String path) {
        if(listViews==null)
            listViews = new ArrayList<>();

        ImageView img = new ImageView(this);
        img.setBackgroundColor(0xff000000);
        Glide.with(this)                             //配置上下文
                .load(path)                  //设置图片路径
                .placeholder(com.lzy.imagepicker.R.mipmap.default_image)
                .error(com.lzy.imagepicker.R.mipmap.default_image)           //设置错误图片
                .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存全尺寸
                .into(img);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listViews.add(img);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            count = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    class MyPageAdapter extends PagerAdapter{
        private ArrayList<View> listViews;// content
        private int size;// 页数

        public MyPageAdapter(ArrayList<View> listViews){
            this.listViews = listViews;
            size = listViews==null?0:listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews==null?0:listViews.size();
        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(position==0)
                ((ViewPager) container).removeView(listViews.get(0));
            else
                ((ViewPager) container).removeView(listViews.get(position % size));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            try {
                container.addView(listViews.get(position % size), 0);
            }catch (Exception e){
                e.printStackTrace();
            }
            return listViews.get(position % size);
        }

    }
}
