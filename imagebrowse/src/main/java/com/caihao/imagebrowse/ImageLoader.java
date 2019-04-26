package com.caihao.imagebrowse;

import android.content.Context;
import android.widget.ImageView;


public interface ImageLoader {

    void load(Context context, String path, ImageView imageView);

}
