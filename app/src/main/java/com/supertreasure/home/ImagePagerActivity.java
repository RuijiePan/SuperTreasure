package com.supertreasure.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.DateSorter;
import android.widget.FrameLayout;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.supertreasure.util.Config;
import com.supertreasure.util.FileUtil;
import com.supertreasure.util.GetHeightResUrl;
import com.supertreasure.util.HackyViewPager;
import com.supertreasure.R;
import com.supertreasure.util.SnackbarUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by prj on 2015/9/26.
 */
public class ImagePagerActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    private FrameLayout root;
    private String[] urls;
    private HackyViewPager mPager;
    private ImagePagerAdapter mAdapter;
    private int pagerPosition;
    private Toolbar toolbar;
    private CircleIndicator indicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_detail_pager);

        initView();
        initData();
        initToolbar();
        setDownLoadListener();
    }

    private void initView() {
        root = (FrameLayout) findViewById(R.id.root);
        mPager = (HackyViewPager) findViewById(R.id.imagepager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
    }

    private void initData() {
        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);

        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        indicator.setViewPager(mPager);
        mPager.setCurrentItem(pagerPosition);
        if (urls == null || urls.length == 1)
            indicator.setVisibility(View.GONE);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
    }

    private void setDownLoadListener() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.download:
                        savePicToLocal();
                        break;
                }
                return false;
            }
        });
    }

    private void savePicToLocal() {

        final int position = mPager.getCurrentItem();
        final String pic = GetHeightResUrl.getHeightUrl(urls[position]);

        File picDir = new File(Config.IMAGE_PIC_CACHE_DIR);
        if (!picDir.exists()) {
            picDir.mkdir();
        }
        /*通过CacheKey查找磁盘中有没有缓存文件：如果配置了
        如果返回的localFile不为空的话，将文件拷贝到相应下载目录，重命名就可以了。
        如果返回的localFile为空的话，去获取缓存中的bitmap，并保存到下载目录即可。*/
        CacheKey cacheKey =DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(pic));
        File cacheFile = getCachedImageOnDisk(cacheKey);
        if(cacheFile!=null){
            FileUtil.copyTo(cacheFile,picDir,FileUtil.getFileName(pic));
            SnackbarUtils.show(root, "图片已经保存至SuperTreasure/cacheImage文件下", 0, null);
        }else {
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(pic))
                    .setProgressiveRenderingEnabled(true)
                    .build();

            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<CloseableImage>>
                    dataSourse = imagePipeline.fetchDecodedImage(imageRequest, this);

            dataSourse.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                protected void onNewResultImpl(Bitmap bitmap) {
                    if (bitmap == null) {
                        SnackbarUtils.show(root, "图片保存失败，无法下载图片", 0, null);
                    }
                    try {
                        FileUtil.saveBitmap(bitmap,FileUtil.getFileName(pic));
                        SnackbarUtils.show(root, "图片已经保存至superTreasure/cacheImage文件下", 0, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                    SnackbarUtils.show(root, "图片保存失败，无法下载图片", 0, null);
                }
            }, CallerThreadExecutor.getInstance());
        }
    }

    public static File getCachedImageOnDisk(CacheKey cacheKey) {
        File localFile = null;
        if (cacheKey != null) {
            if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_download, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
