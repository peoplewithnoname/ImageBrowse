package com.caihao.simple.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.caihao.imagebrowse.ImageLoadCallback;
import com.caihao.imagebrowse.ImageLoader;

public class GlideImageLoader implements ImageLoader {

    @Override
    public void load(Context context, String path, final ImageLoadCallback callback) {
        Glide.with(context).asDrawable().load(path).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (resource != null && callback != null) callback.loadOver(resource);
            }
        });
    }
}
