package com.caihao.simple;

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
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;

    private ImageView ivClick;

    private GanImageAdapter adapter;

    private String[] urls = {"https://ws1.sinaimg.cn/large/0065oQSqly1g0ajj4h6ndj30sg11xdmj.jpg", "https://ws1.sinaimg.cn/large/0065oQSqly1g04lsmmadlj31221vowz7.jpg"
            , "https://ws1.sinaimg.cn/large/0065oQSqgy1fze94uew3jj30qo10cdka.jpg", "https://ws1.sinaimg.cn/large/0065oQSqly1fytdr77urlj30sg10najf.jpg", "https://ws1.sinaimg.cn/large/0065oQSqly1fymj13tnjmj30r60zf79k.jpg"
            , "https://ws1.sinaimg.cn/large/0065oQSqgy1fy58bi1wlgj30sg10hguu.jpg", "https://ws1.sinaimg.cn/large/0065oQSqgy1fxno2dvxusj30sf10nqcm.jpg", "https://ws1.sinaimg.cn/large/0065oQSqgy1fxd7vcz86nj30qo0ybqc1.jpg"
            , "https://ws1.sinaimg.cn/large/0065oQSqgy1fwyf0wr8hhj30ie0nhq6p.jpg", "https://ws1.sinaimg.cn/large/0065oQSqgy1fwgzx8n1syj30sg15h7ew.jpg"};

    private String simpleUrl = "https://ws1.sinaimg.cn/large/0065oQSqgy1fze94uew3jj30qo10cdka.jpg";

    private ImageBrowseTools imageBrowseTools;//查看大图的工具类;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 1.初始化ImageLoader 图片加载器
        ImageBrowseTools.setImageLoader(new GlideImageLoader());
        //---------------------------------

        ImageBrowseTools.loadImage(this, simpleUrl, new ImageLoadCallback() {
            @Override
            public void loadOver(Drawable drawable) {
                ivClick.setImageDrawable(drawable);
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        ivClick = findViewById(R.id.ivClick);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildLayoutPosition(view);
                outRect.top = 10;
                outRect.bottom = 10;
                outRect.left = 10;
                outRect.right = 10;
            }
        });
        recyclerView.setAdapter(adapter = new GanImageAdapter(this));
        for (int i = 0; i < urls.length; i++) {
            adapter.add(urls[i]);
        }
        ivClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始浏览大图 前三句可以不要
                imageBrowseTools.setTag("1" + TAG);
                imageBrowseTools.setAfterIndexCallback(null);
                imageBrowseTools.setActivityRegisterCallback(null);
                imageBrowseTools.start(ivClick, simpleUrl, 0);
                //----------------------------------------
            }
        });
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                View itemView = manager.findViewByPosition(position);
                View view = itemView.findViewById(R.id.ivCover);
                List<String> urlList = new ArrayList<>();
                for (int i = 0; i < adapter.getCount(); i++) {
                    urlList.add(urls[i]);
                }
                //开始浏览大图 前三句可以不要
                imageBrowseTools.setTag(TAG);
                imageBrowseTools.setAfterIndexCallback(afterIndexCallback);
                imageBrowseTools.setActivityRegisterCallback(activityRegisterCallback);
                imageBrowseTools.start(view, urlList, position);
                //----------------------------------------
            }
        });
        //初始化ImageBrowseTools管理类
        imageBrowseTools = new ImageBrowseTools.Builder()
                .setActivity(this)
                .setTag(TAG)
                .setActivityRegisterCallback(activityRegisterCallback)
                .setAfterIndexCallback(afterIndexCallback).build();
        //---------------------------------------------------
    }

    ActivityRegisterCallback activityRegisterCallback = new ActivityRegisterCallback() {
        @Override
        public View getView(int index) {
            GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
            View itemView = manager.findViewByPosition(index);
            View view = itemView.findViewById(R.id.ivCover);
            return view;
        }
    };

    AfterIndexCallback afterIndexCallback = new AfterIndexCallback() {
        @Override
        public void after(int index) {
            recyclerView.smoothScrollToPosition(index);
        }
    };

}
