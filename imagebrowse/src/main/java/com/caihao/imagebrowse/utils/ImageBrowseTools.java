package com.caihao.imagebrowse.utils;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.caihao.imagebrowse.ActivityRegisterCallback;
import com.caihao.imagebrowse.AfterIndexCallback;
import com.caihao.imagebrowse.ImageBrowseActivity;
import com.caihao.imagebrowse.ImageBrowseBus;
import com.caihao.imagebrowse.ImageBrowseCallback;
import com.caihao.imagebrowse.ImageLoadCallback;
import com.caihao.imagebrowse.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageBrowseTools {

    final public static String TRANSITION = "transition";

    private Activity activity;

    private List<String> urls;

    private int index;

    private String tag;//key

    private ActivityRegisterCallback activityRegisterCallback;

    private AfterIndexCallback afterIndexCallback;

    public ImageBrowseTools(Activity activity, int index, String tag, ActivityRegisterCallback activityRegisterCallback, AfterIndexCallback afterIndexCallback) {
        this.activity = activity;
        this.index = index;
        this.tag = tag;
        this.activityRegisterCallback = activityRegisterCallback;
        this.afterIndexCallback = afterIndexCallback;
        createCallback();
    }

    /**
     * 创建当前Activity的回调
     */
    private void createCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.setExitSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    super.onMapSharedElements(names, sharedElements);
                    if (activityRegisterCallback == null) return;
                    View view = activityRegisterCallback.getView(index);
                    String transitionName = TRANSITION + index;
                    ViewCompat.setTransitionName(view, transitionName);
                    names.clear();
                    names.add(transitionName);
                    sharedElements.clear();
                    sharedElements.put(names.get(0), view);
                }

                @Override
                public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                    super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                    for (int i = 0; i < sharedElements.size(); i++) {
                        ViewCompat.setTransitionName(sharedElements.get(i), TRANSITION);
                    }
                    for (int i = 0; i < sharedElementSnapshots.size(); i++) {
                        ViewCompat.setTransitionName(sharedElementSnapshots.get(i), TRANSITION);
                    }
                }
            });
            ImageBrowseBus.getInstance().save(tag, new ImageBrowseCallback() {
                @Override
                public void setIndex(int i) {
                    index = i;
                    if (afterIndexCallback != null) {
                        afterIndexCallback.after(index);
                    }
                }
            });
        }
    }

    public static class Builder {

        private Activity activity;

        private int index;

        private String tag;//key

        private ActivityRegisterCallback activityRegisterCallback;

        private AfterIndexCallback afterIndexCallback;

        public Builder setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder setActivityRegisterCallback(ActivityRegisterCallback activityRegisterCallback) {
            this.activityRegisterCallback = activityRegisterCallback;
            return this;
        }

        public Builder setAfterIndexCallback(AfterIndexCallback afterIndexCallback) {
            this.afterIndexCallback = afterIndexCallback;
            return this;
        }

        public Builder setIndex(int index) {
            this.index = index;
            return this;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public ImageBrowseTools build() {
            return new ImageBrowseTools(activity, 0, tag, activityRegisterCallback, afterIndexCallback);
        }

    }

    /**
     * 开始图片预览
     *
     * @param view
     * @param index
     */
    public void start(View view, List<String> urlList, int index) {
        this.urls = urlList;
        this.index = index;
        ViewCompat.setTransitionName(view, TRANSITION + index);
        final Intent intent = new Intent(activity, ImageBrowseActivity.class);
        intent.putExtra("key", tag);
        intent.putExtra("urlList", (Serializable) urls);
        intent.putExtra("index", index);
        final ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, TRANSITION + index);
        loadImage(activity, urls.get(index), new ImageLoadCallback() {
            @Override
            public void loadOver(Drawable drawable) {
                activity.startActivity(intent, option.toBundle());
            }
        });
    }

    /**
     * 开始图片预览
     *
     * @param view
     * @param index
     */
    public void start(View view, String url) {
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(activity, "图片链接不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        List<String> urlList = new ArrayList<>();
        urlList.add(url);
        start(view, urlList, 0);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setActivityRegisterCallback(ActivityRegisterCallback activityRegisterCallback) {
        this.activityRegisterCallback = activityRegisterCallback;
    }

    public void setAfterIndexCallback(AfterIndexCallback afterIndexCallback) {
        this.afterIndexCallback = afterIndexCallback;
    }

    //--------------------------------------------图片加载------------------------------------------------

    public static ImageLoader imageLoader = null;

    public static void loadImage(Context context, String path, ImageLoadCallback callback) {
        imageLoader.load(context, path, callback);
    }

    public static void setImageLoader(ImageLoader imageLoader) {
        ImageBrowseTools.imageLoader = imageLoader;
    }

    //--------------------------------------------图片加载------------------------------------------------

}
