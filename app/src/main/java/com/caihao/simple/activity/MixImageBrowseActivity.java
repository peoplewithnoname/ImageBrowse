package com.caihao.simple.activity;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caihao.imagebrowse.ActivityRegisterCallback;
import com.caihao.imagebrowse.AfterIndexCallback;
import com.caihao.imagebrowse.ImageBrowseBus;
import com.caihao.imagebrowse.utils.ImageBrowseTools;
import com.caihao.simple.MainActivity;
import com.caihao.simple.R;
import com.caihao.simple.adapter.GanImageAdapter;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * 混合多张图片查看（一个单张图片  多个多张图片）
 */
public class MixImageBrowseActivity extends AppCompatActivity {

    private static final String TAG0 = "MixImageBrowseActivity0";

    private static final String TAG1 = "MixImageBrowseActivity1";

    private Activity activity;

    private ImageView ivCover0;

    private RecyclerView mRecyclerView0, mRecyclerView1;

    private GanImageAdapter adapter0, adapter1;

    /**
     * 定义查看大图的对象
     */
    private ImageBrowseTools imageBrowseTools;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix_image_browse);
        activity = this;
        ivCover0 = findViewById(R.id.ivCover0);
        mRecyclerView0 = findViewById(R.id.mRecyclerView0);
        mRecyclerView1 = findViewById(R.id.mRecyclerView1);
        mRecyclerView0.setLayoutManager(new GridLayoutManager(activity, 2));
        mRecyclerView0.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10;
                outRect.bottom = 10;
                outRect.left = 10;
                outRect.right = 10;
            }
        });
        mRecyclerView0.setAdapter(adapter0 = new GanImageAdapter(activity));
        mRecyclerView1.setLayoutManager(new GridLayoutManager(activity, 2));
        mRecyclerView1.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10;
                outRect.bottom = 10;
                outRect.left = 10;
                outRect.right = 10;
            }
        });
        mRecyclerView1.setAdapter(adapter1 = new GanImageAdapter(activity));
        adapter0.addAll(MainActivity.urls);
        adapter0.setOnItemClickListener(itemClick0);
        adapter1.addAll(MainActivity.urls);
        adapter1.setOnItemClickListener(itemClick1);
        Glide.with(activity).load(MainActivity.urls[5]).into(ivCover0);
        ivCover0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBrowseTools.start(v, MainActivity.urls[5]);
            }
        });

        //实例化查看大图的对象 (混合多张图片查看需初始化activity tag activityRegisterCallback afterIndexCallback(当图片所需要的容器需要滚动到看不见区域的时候 需要这个参数))
        imageBrowseTools = new ImageBrowseTools.Builder()
                .setActivity(activity)
                .addTag(TAG0)
                .addTag(TAG1)
                .setActivityRegisterCallback(activityRegisterCallback)
                .setAfterIndexCallback(afterIndexCallback).build();
    }

    ActivityRegisterCallback activityRegisterCallback = new ActivityRegisterCallback() {
        @Override
        public View getView(int index) {
            View view = null;
            if (TAG0.equals(imageBrowseTools.getTag())) {
                view = mRecyclerView0.getLayoutManager().findViewByPosition(index).findViewById(R.id.ivCover);
            } else if (TAG1.equals(imageBrowseTools.getTag())) {
                view = mRecyclerView1.getLayoutManager().findViewByPosition(index).findViewById(R.id.ivCover);
            }
            return view;
        }
    };

    AfterIndexCallback afterIndexCallback = new AfterIndexCallback() {
        @Override
        public void after(int index) {
            if (TAG0.equals(imageBrowseTools.getTag())) {
                mRecyclerView0.smoothScrollToPosition(index);
            } else if (TAG1.equals(imageBrowseTools.getTag())) {
                mRecyclerView1.smoothScrollToPosition(index);
            }
        }
    };

    RecyclerArrayAdapter.OnItemClickListener itemClick0 = new RecyclerArrayAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            View view = mRecyclerView0.getLayoutManager().findViewByPosition(position).findViewById(R.id.ivCover);
            imageBrowseTools.start(view, adapter0.getAllData(), position, TAG0);
        }
    };


    RecyclerArrayAdapter.OnItemClickListener itemClick1 = new RecyclerArrayAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            View view = mRecyclerView1.getLayoutManager().findViewByPosition(position).findViewById(R.id.ivCover);
            imageBrowseTools.start(view, adapter1.getAllData(), position, TAG1);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageBrowseBus.getInstance().remove(TAG0);
        ImageBrowseBus.getInstance().remove(TAG1);
    }
}
