package com.caihao.imagebrowse;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caihao.imagebrowse.utils.ImageBrowseTools;

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
    private TextView tvCurr;

    private ImageBrowseAdapter adapter;

    /**
     * 转场动画是否初始化完毕
     */
    private boolean isAnimInit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparentForWindow(this);

        activity = this;
        setContentView(R.layout.activity_image_browse);
        //获取前面页面传过来的数据
        key = getIntent().getStringExtra("key");
        urlList = (List<String>) getIntent().getSerializableExtra("urlList");
        index = getIntent().getIntExtra("index", 0);
        //初始化控件
        ivShare = findViewById(R.id.ivShare);
        pager = findViewById(R.id.pager);
        tvCurr = findViewById(R.id.tvCurr);
        ivShare.setMinimumHeight(getWindowWidth());
//        setShareLayout();//设置共享图片控件的大小
        ImageBrowseTools.loadImage(this, urlList.get(index), new ImageLoadCallback() {
            @Override
            public void loadOver(Drawable drawable) {
                ivShare.setImageDrawable(drawable);
            }
        });
        ViewCompat.setTransitionName(ivShare, ImageBrowseTools.TRANSITION + index);
        pager.setAdapter(adapter = new ImageBrowseAdapter(activity, urlList));
        pager.setCurrentItem(index);
        ImageBrowseCallback callback = ImageBrowseBus.getInstance().get(key);
        if (callback != null) callback.setIndex(index);
        if (urlList.size() > 1) {
            tvCurr.setText((index + 1) + "/" + urlList.size());
        }
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
                if (urlList.size() > 1) {
                    tvCurr.setText((index + 1) + "/" + urlList.size());
                }
                if (callback != null) callback.setIndex(index);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
                        ViewCompat.setTransitionName(view, ImageBrowseTools.TRANSITION + index);
                        names.clear();
                        names.add(ImageBrowseTools.TRANSITION + index);
                        sharedElements.clear();
                        sharedElements.put(names.get(0), view);
                        Log.e("TAG", "name = " + names.get(0));
                    }
                }
            });
        }
    }

    public void setShareLayout() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivShare.getLayoutParams();
        layoutParams.width = getWindowWidth();
        layoutParams.height = getWindowWidth();
        ivShare.setLayoutParams(layoutParams);
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

    /**
     * 设置透明
     */
    private void setTransparentForWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
