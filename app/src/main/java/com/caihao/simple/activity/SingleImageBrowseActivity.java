package com.caihao.simple.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caihao.imagebrowse.utils.ImageBrowseTools;
import com.caihao.simple.MainActivity;
import com.caihao.simple.R;

/**
 * 单张图片查看
 */
public class SingleImageBrowseActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;

    private ImageView ivCover0, ivCover1, ivCover2;

    /**
     * 定义查看大图的对象
     */
    private ImageBrowseTools imageBrowseTools;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image_browse);
        activity = this;
        ivCover0 = findViewById(R.id.ivCover0);
        ivCover1 = findViewById(R.id.ivCover1);
        ivCover2 = findViewById(R.id.ivCover2);
        ivCover0.setOnClickListener(this);
        ivCover1.setOnClickListener(this);
        ivCover2.setOnClickListener(this);
        Glide.with(this).load(MainActivity.urls[0]).into(ivCover0);
        Glide.with(this).load(MainActivity.urls[1]).into(ivCover1);
        Glide.with(this).load(MainActivity.urls[2]).into(ivCover2);

        //实例化查看大图的对象 (单张图片查看只用初始化activity)
        imageBrowseTools = new ImageBrowseTools.Builder()
                .setActivity(activity).build();
    }

    @Override
    public void onClick(View view) {
        //查看大图 点击事件处理  view为点击的对象 同时也是查看大图时需要做发达动画的View
        if (view.getId() == R.id.ivCover0) {
            imageBrowseTools.start(view, MainActivity.urls[0]);
        } else if (view.getId() == R.id.ivCover1) {
            imageBrowseTools.start(view, MainActivity.urls[1]);
        } else if (view.getId() == R.id.ivCover2) {
            imageBrowseTools.start(view, MainActivity.urls[2]);
        }
    }
}
