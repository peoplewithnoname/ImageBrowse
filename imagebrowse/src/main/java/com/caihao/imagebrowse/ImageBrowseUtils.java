package com.caihao.imagebrowse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageBrowseUtils {

    public interface ImageLoaderCallback {
        ImageLoader getImageLoader();
    }

    final public static String TRANSITION = "transition";

    /**
     * 图片加载框架
     */
    public static ImageLoader imageLoader = null;

    private String key;

    private int index;

    private List<String> urlList;

    public static void loadImage(Context context, String path, ImageView imageView) {
        ImageBrowseUtils.imageLoader.load(context, path, imageView);
    }

    public static void loadImage(Context context, String path, ImageLoadCallback callback) {
        ImageBrowseUtils.imageLoader.load(context, path, callback);
    }

    public static void setImageLoader(ImageLoader imageLoader) {
        ImageBrowseUtils.imageLoader = imageLoader;
    }

    public ImageBrowseUtils(String key, int index, List<String> urlList) {
        this.key = key;
        this.index = index;
        this.urlList = urlList;
    }

    /**
     * 开始图片预览
     *
     * @param activity
     * @param view
     */
    public void start(final Activity activity, View view) {
        ViewCompat.setTransitionName(view, TRANSITION + index);
        final Intent intent = new Intent(activity, ImageBrowseActivity.class);
        intent.putExtra("key", key);
        intent.putExtra("urlList", (Serializable) urlList);
        intent.putExtra("index", index);
        final ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, TRANSITION + index);
        loadImage(activity, urlList.get(index), new ImageLoadCallback() {
            @Override
            public void loadOver(Drawable drawable) {
                activity.startActivity(intent, option.toBundle());
            }
        });
    }

    public static class Builder {
        private String key;
        private int index;
        private List<String> urlList;

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setIndex(int index) {
            this.index = index;
            return this;
        }

        public Builder addUrl(String url) {
            if (urlList == null) urlList = new ArrayList<>();
            urlList.add(url);
            return this;
        }

        public Builder setUrlList(List<String> urlList) {
            this.urlList = urlList;
            return this;
        }

        public ImageBrowseUtils builder() {
            return new ImageBrowseUtils(key, index, urlList);
        }

    }

}
