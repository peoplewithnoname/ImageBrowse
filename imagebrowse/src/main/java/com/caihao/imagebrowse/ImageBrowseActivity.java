package com.caihao.imagebrowse;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.Map;

/**
 * 图片浏览的页面
 */
public class ImageBrowseActivity extends AppCompatActivity {

    private Activity activity;

    private String key;//用于对当前回调或activity的标识
    private List<String> urlList;//上个页面传递过来的url集合
    private int index;//上个页面传递过来的下标 可以更改

    private ImageView ivShare;//用于共享动画的ImageView
    private ViewPager pager;//图片的容器

    private ImageBrowseAdapter adapter;

    /**
     * 转场动画是否初始化完毕
     */
    private boolean isAnimInit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        activity = this;
        setContentView(R.layout.activity_image_browse);
        //获取前面页面传过来的数据
        key = getIntent().getStringExtra("key");
        urlList = (List<String>) getIntent().getSerializableExtra("urlList");
        index = getIntent().getIntExtra("index", 0);
        //初始化控件
        ivShare = findViewById(R.id.ivShare);
        pager = findViewById(R.id.pager);
        setShareLayout();//设置共享图片控件的大小
        ImageBrowseUtils.loadImage(this, urlList.get(index), ivShare);
        ViewCompat.setTransitionName(ivShare, ImageBrowseUtils.TRANSITION + index);
        pager.setAdapter(adapter = new ImageBrowseAdapter(activity, urlList));
        pager.setCurrentItem(index);
        //设置监听
        initLisenter();
    }

    private void initLisenter() {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                index = i;
                ImageBrowseCallback callback = ImageBrowseBus.getInstance().get(key);
                if (callback != null) callback.setIndex(index);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                pager.setVisibility(View.VISIBLE);
                isAnimInit = true;
            }

            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                if (isAnimInit) {
                    View view = adapter.getItem(index);
                    ViewCompat.setTransitionName(view, ImageBrowseUtils.TRANSITION + index);
                    names.clear();
                    names.add(ImageBrowseUtils.TRANSITION + index);
                    sharedElements.clear();
                    sharedElements.put(names.get(0), view);
                    Log.e("TAG", "name = " + names.get(0));
                }
            }
        });
    }

    public void setShareLayout() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivShare.getLayoutParams();
        layoutParams.width = getWindowWidth();
        layoutParams.height = getWindowWidth();
//        ivShare.setLayoutParams(layoutParams);
    }

    //-----------------------------------------------------------------------

    /**
     * 得到屏幕宽度
     *
     * @return
     */
    public int getWindowWidth() {
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

}