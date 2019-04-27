package com.caihao.imagebrowse;

import android.content.Context;
import android.widget.ImageView;


public interface ImageLoader {

    /**
     * 图片加载 该方法必须重写
     * @param context
     * @param path
     * @param callback
     */
    void load(Context context, String path, ImageLoadCallback callback);

}
