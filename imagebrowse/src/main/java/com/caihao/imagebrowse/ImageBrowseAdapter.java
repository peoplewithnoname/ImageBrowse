package com.caihao.imagebrowse;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;

public class ImageBrowseAdapter extends PagerAdapter {

    private Context context;

    private List<String> urlList;

    private List<View> viewList;

    public ImageBrowseAdapter(final Context context, List<String> urlList) {
        this.context = context;
        this.urlList = urlList;
        viewList = new ArrayList<>();
        for (int i = 0; i < urlList.size(); i++) {
            ImageView imageView = new TouchImageView(context);
            ImageBrowseUtils.loadImage(context, urlList.get(i), imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) context).finishAfterTransition();
                }
            });
            viewList.add(imageView);
        }
    }

    public View getItem(int position) {
        return viewList.get(position);
    }

    @Override
    public int getCount() {
        return urlList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = (ImageView) viewList.get(position);
        container.removeView(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        container.removeView(viewList.get(position));
    }
}
