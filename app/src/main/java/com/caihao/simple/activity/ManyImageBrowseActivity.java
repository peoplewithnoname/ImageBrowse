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

import com.caihao.imagebrowse.ActivityRegisterCallback;
import com.caihao.imagebrowse.AfterIndexCallback;
import com.caihao.imagebrowse.utils.ImageBrowseTools;
import com.caihao.simple.MainActivity;
import com.caihao.simple.R;
import com.caihao.simple.adapter.GanImageAdapter;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * 多张图片查看
 */
public class ManyImageBrowseActivity extends AppCompatActivity {

    private static final String TAG = "ManyImageBrowseActivity";

    private Activity activity;

    private RecyclerView mRecyclerView;

    private GanImageAdapter adapter;

    /**
     * 定义查看大图的对象
     */
    private ImageBrowseTools imageBrowseTools;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_many_image_browse);
        activity = this;
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10;
                outRect.bottom = 10;
                outRect.left = 10;
                outRect.right = 10;
            }
        });
        mRecyclerView.setAdapter(adapter = new GanImageAdapter(activity));
        adapter.addAll(MainActivity.urls);
        adapter.setOnItemClickListener(itemClick);

        //实例化查看大图的对象 (多张图片查看需初始化activity tag activityRegisterCallback afterIndexCallback(但图片所需要的容器需要滚动到看不见区域的时候 需要这个参数))
        imageBrowseTools = new ImageBrowseTools.Builder()
                .setActivity(activity)
                .addTag(TAG)
                .setActivityRegisterCallback(activityRegisterCallback)
                .setAfterIndexCallback(afterIndexCallback).build();
    }

    ActivityRegisterCallback activityRegisterCallback = new ActivityRegisterCallback() {
        @Override
        public View getView(int index) {
            return mRecyclerView.getLayoutManager().findViewByPosition(index).findViewById(R.id.ivCover);
        }
    };

    AfterIndexCallback afterIndexCallback = new AfterIndexCallback() {
        @Override
        public void after(int index) {
            mRecyclerView.smoothScrollToPosition(index);
        }
    };

    RecyclerArrayAdapter.OnItemClickListener itemClick = new RecyclerArrayAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            View view = mRecyclerView.getLayoutManager().findViewByPosition(position).findViewById(R.id.ivCover);
            imageBrowseTools.start(view, adapter.getAllData(), position);
        }
    };


}
