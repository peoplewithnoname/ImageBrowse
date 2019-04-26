package com.caihao.imagebrowse;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideImageLoader implements ImageLoader {

    @Override
    public void load(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }
}
