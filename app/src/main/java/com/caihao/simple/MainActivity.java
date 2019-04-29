package com.caihao.simple;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.caihao.imagebrowse.ActivityRegisterCallback;
import com.caihao.imagebrowse.AfterIndexCallback;
import com.caihao.imagebrowse.ImageLoadCallback;
import com.caihao.imagebrowse.utils.ImageBrowseTools;
import com.caihao.simple.activity.ManyImageBrowseActivity;
import com.caihao.simple.activity.MixImageBrowseActivity;
import com.caihao.simple.activity.SingleImageBrowseActivity;
import com.caihao.simple.adapter.GanImageAdapter;
import com.caihao.simple.utils.GlideImageLoader;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static String[] urls = {"https://ws1.sinaimg.cn/large/0065oQSqly1g0ajj4h6ndj30sg11xdmj.jpg", "https://ws1.sinaimg.cn/large/0065oQSqly1g04lsmmadlj31221vowz7.jpg"
            , "https://ws1.sinaimg.cn/large/0065oQSqgy1fze94uew3jj30qo10cdka.jpg", "https://ws1.sinaimg.cn/large/0065oQSqly1fytdr77urlj30sg10najf.jpg", "https://ws1.sinaimg.cn/large/0065oQSqly1fymj13tnjmj30r60zf79k.jpg"
            , "https://ws1.sinaimg.cn/large/0065oQSqgy1fy58bi1wlgj30sg10hguu.jpg", "https://ws1.sinaimg.cn/large/0065oQSqgy1fxno2dvxusj30sf10nqcm.jpg", "https://ws1.sinaimg.cn/large/0065oQSqgy1fxd7vcz86nj30qo0ybqc1.jpg"
            , "https://ws1.sinaimg.cn/large/0065oQSqgy1fwyf0wr8hhj30ie0nhq6p.jpg", "https://ws1.sinaimg.cn/large/0065oQSqgy1fwgzx8n1syj30sg15h7ew.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn0).setOnClickListener(this);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);

        //初始化图片加载的接口(用的glide)
        ImageBrowseTools.setImageLoader(new GlideImageLoader());

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.btn0) {//单张图片查看
            intent = new Intent(this, SingleImageBrowseActivity.class);
        } else if (v.getId() == R.id.btn1) {//多张图片查看
            intent = new Intent(this, ManyImageBrowseActivity.class);
        } else if (v.getId() == R.id.btn2) {//混合多张图片查看
            intent = new Intent(this, MixImageBrowseActivity.class);
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
