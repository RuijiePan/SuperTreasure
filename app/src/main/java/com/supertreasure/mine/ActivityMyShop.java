package com.supertreasure.mine;

import android.graphics.Color;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.util.AbViewUtil;
import com.supertreasure.R;
import com.supertreasure.base.mFragmentPagerAdapter;
import com.supertreasure.mine.fragment.PublishFragment;
import com.supertreasure.mine.fragment.RemoveFragment;
import com.supertreasure.mine.fragment.SoldFragment;
import com.supertreasure.util.MyUtils;
import com.supertreasure.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ActivityMyShop extends AbActivity implements View.OnClickListener {
    //private ListView listview_goods;
    //private PopupWindow popupWindow;
    private String[] title;
    private TabLayout tabLayout;
    private Button bt_title_left;
    private Button bt_title_right;
    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private ArrayList<Fragment> fragmentList;
    private SoldFragment soldFragment;
    private PublishFragment publishFragment;
    private RemoveFragment removeFragment;
    private ViewPager myShopViewPager;
    private mFragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshop);
        initView();
        initData();
        initViewPager();
        setListener();
    }

    private void initView() {
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));
        /*anim_rotate180 = AnimationUtils.loadAnimation(this, R.anim.rotate180);
        anim_rotate180.setFillAfter(true);//保留运动后的状态*/
       /* swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeColors(R.color.text_color_title);
*/
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        ll_title_right.setVisibility(View.INVISIBLE);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        myShopViewPager = (ViewPager) findViewById(R.id.myshopViewPager);
    }
    private void initData() {
        fragmentList = new ArrayList<>();
        soldFragment = new SoldFragment();
        publishFragment = new PublishFragment();
        removeFragment = new RemoveFragment();

        fragmentList.add(publishFragment);
        fragmentList.add(soldFragment);
        fragmentList.add(removeFragment);

    }
    private void initViewPager(){
        title = new String[]{"已发布","已出售","已下架"};
        mFragmentPagerAdapter = new mFragmentPagerAdapter(getSupportFragmentManager(),fragmentList,title);
        myShopViewPager.setAdapter(mFragmentPagerAdapter);
        myShopViewPager.setOffscreenPageLimit(2);//设置缓存个数，这里缓存2个，正在显示的一个。就3个
        tabLayout.setupWithViewPager(myShopViewPager);
    }


    private void setListener() {
        bt_title_left.setOnClickListener(this);
        bt_title_right.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);
        ll_title_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_title_left:
                finish();
                break;
            case R.id.ll_title_left:
                finish();
                break;
        }
    }
}

/*
    private void asyncImageLoad(final ImageView imageView,final String path) {
        AsyncImageTask asyncImageTask = new AsyncImageTask(imageView);
        asyncImageTask.execute(path);
    }
    private class AsyncImageTask extends AsyncTask<String,Integer,Uri> {
        public ImageView imageView;

        public AsyncImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Uri doInBackground(String... params) {//子线程执行，返回值作为消息发送到onPostExecute（）；
            try {
                return MySmallShopService.getImage(params[0],cache);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Uri uri) {//主线程执行  ，这个参数 从doInBackground（）返回。
            if (uri != null && imageView != null)
                imageView.setImageURI(uri);
            else if(uri == null && imageView != null){
                imageView.setBackgroundResource(R.drawable.iv_mine_head);
            }
        }
    }*/
/*    private final class DataWrapper{
        public ImageView imageView_img;
        public TextView textView_content;
        public TextView textView_price;
    }*/




   /* private void showCouponChooseDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_coupon_choose, null);
        TextView textview_hadsold = (TextView) view.findViewById(R.id.tv_hadsold);
        TextView textview_hadselling = (TextView) view.findViewById(R.id.tv_hadselling);
        TextView textview_hadsoldout = (TextView) view.findViewById(R.id.tv_hadsoldout);


        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        View.OnClickListener onClickListener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tv_hadsold:
                        ToastUtil.showToast(ActivityMyShop.this,"已出售");
                        LodingMore(Config.Content_Goods_Sold);
                        dialog.dismiss();
                        break;
                    case R.id.tv_hadselling:
                        ToastUtil.showToast(ActivityMyShop.this,"已发布");
                        LodingMore(Config.Content_Goods_Publish);
                        dialog.dismiss();
                        break;
                    case R.id.tv_hadsoldout:
                        ToastUtil.showToast(ActivityMyShop.this,"已下架");
                        LodingMore(Config.Content_Goods_Remove);
                        dialog.dismiss();
                        break;
                }
            }
        };

        textview_hadsold.setOnClickListener(onClickListener);
        textview_hadselling.setOnClickListener(onClickListener);
        textview_hadsoldout.setOnClickListener(onClickListener);
    }*/
/*    private void showCouponChoosePopWindow(View parent){
        //int[] location = new int[2];
        //parent.getLocationOnScreen(location);

        popupWindow.showAsDropDown(parent);
        //popupWindow.showAtLocation(parent, Gravity.BOTTOM,0,0 );//parent就是当前的主窗口linearlaout，放在下面。偏移量0，0
        //popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY, location[0], location[1]-popupWindow.getHeight());//parent就是当前的主窗口linearlaout，放在下面。偏移量0，0

    }*/
/*    private class GoodAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list_goods_publish.size();
        }

        @Override
        public Object getItem(int position) {
            return list_goods_publish.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DataWrapper datawrapper = new DataWrapper();
            if (convertView == null){
                convertView = inflater.inflate(R.layout.item_mygoods,null);
                datawrapper.imageView_img = (ImageView) convertView.findViewById(R.id.goods_img);//
                datawrapper.textView_content = (TextView) convertView.findViewById(R.id.goods_content);
                datawrapper.textView_price = (TextView) convertView.findViewById(R.id.goods_price);
                convertView.setTag(datawrapper);
            }else{
                datawrapper = (DataWrapper) convertView.getTag();
            }
            datawrapper.textView_content.setText(list_goods_publish.get(position).getContent());
            datawrapper.textView_price.setText(list_goods_publish.get(position).getPrice());

            //...  加载图片，过程比较慢，如果直接在这里完成，很容易报错，应用程序无响应。应该采用异步数据加载。
            asyncImageLoad(datawrapper.imageView_img,list_goods_publish.get(position).getPath_img());//
            return convertView;
        }
    }*/
/*    public void getDataFromServer(final String status){
        isPullToRefresh = true;
        swipeRefreshLayout.setRefreshing(true);
        AbSharedUtil.putInt(this,Config.MyShopPage,1);


        //这里 应该是从网络中获取数据
        String url = Config.getAPI(Config.APIMyOwnGoods);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(this,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(this,Config.Token));
        params.put(Config.Status, status);
        params.put(Config.Rows, Config.MyShopRowNumber);

        if(isPullToRefresh){//第一次刷新，就是 第一页
            params.put(Config.Page,1);
        }else {
            params.put(Config.Page, AbSharedUtil.getInt(this, Config.MyShopPage));
        }

        TheApp.mAbHttpUtil.post(url,params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                GoodsBeen goodsBeen = gson.fromJson(content, GoodsBeen.class);
                if (goodsBeen.getStatus().equals(Config.SUCCESS)){
                    if (goodsBeen.getList()!=null&&goodsBeen.getList().size()!=0) {
                        switch (status){
                            case Config.Content_Goods_Publish:

                                list_goods_publish.addAll(goodsBeen.getList());
                                adapter.setList_goods_publish(list_goods_publish);
                                break;
                            case Config.Content_Goods_Sold:
                                list_goods_sold.addAll(goodsBeen.getList());
                                adapter.setList_goods(list_goods_sold);
                                break;
                            case Config.Content_Goods_Remove:
                                list_goods_remove.addAll(goodsBeen.getList());
                                adapter.setList_goods_remove(list_goods_remove);
                                break;
                        }
                    }
                    AbSharedUtil.putInt(ActivityMyShop.this, Config.MyShopPage, AbSharedUtil.getInt(ActivityMyShop.this, Config.MyShopPage) + 1);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                isLoadingMore = false;
                isPullToRefresh = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                isPullToRefresh = false;
                isLoadingMore = false;
                swipeRefreshLayout.setRefreshing(false);

                Toast.makeText(ActivityMyShop.this, content, Toast.LENGTH_LONG).show();

            }
        });
    }

    public void updateDataSource(final String status){
        //这里 应该是从网络中获取数据
        String url = Config.getAPI(Config.APIMyOwnGoods);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(this,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(this,Config.Token));
        params.put(Config.Status, status);
        params.put(Config.Page, 1);
        params.put(Config.Rows, 5);

        TheApp.mAbHttpUtil.post(url,params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                GoodsBeen goodsBeen = gson.fromJson(content, GoodsBeen.class);
                if (goodsBeen.getStatus().equals(Config.SUCCESS)){
                    if (goodsBeen.getList()!=null&&goodsBeen.getList().size()!=0) {
                        switch (status){
                            case Config.Content_Goods_Publish:
                                list_goods_publish.addAll(goodsBeen.getList());
                                adapter.setList_goods_publish(list_goods_publish);
                                adapter.updateData(GoodsBeen.HadPublish, list_goods_publish);
                                break;
                            case Config.Content_Goods_Sold:
                                list_goods_sold.addAll(goodsBeen.getList());
                                adapter.setList_goods(list_goods_sold);
                                adapter.updateData(GoodsBeen.HadSold, list_goods_sold);
                                break;
                            case Config.Content_Goods_Remove:
                                list_goods_remove.addAll(goodsBeen.getList());
                                adapter.setList_goods_remove(list_goods_remove);
                                adapter.updateData(GoodsBeen.HadRemove, list_goods_remove);
                                break;
                        }
                    }

                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {

                Toast.makeText(ActivityMyShop.this, content, Toast.LENGTH_LONG).show();

            }
        });
    }*/

