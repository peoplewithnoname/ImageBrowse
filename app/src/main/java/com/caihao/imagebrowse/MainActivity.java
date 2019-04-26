package com.caihao.imagebrowse;

import android.app.SharedElementCallback;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;

    private GanImageAdapter adapter;

    private String[] urls = {"https://ws1.sinaimg.cn/large/0065oQSqly1g0ajj4h6ndj30sg11xdmj.jpg", "https://ws1.sinaimg.cn/large/0065oQSqly1g04lsmmadlj31221vowz7.jpg"
            , "https://ws1.sinaimg.cn/large/0065oQSqgy1fze94uew3jj30qo10cdka.jpg", "https://ws1.sinaimg.cn/large/0065oQSqly1fytdr77urlj30sg10najf.jpg", "https://ws1.sinaimg.cn/large/0065oQSqly1fymj13tnjmj30r60zf79k.jpg"
            , "https://ws1.sinaimg.cn/large/0065oQSqgy1fy58bi1wlgj30sg10hguu.jpg", "https://ws1.sinaimg.cn/large/0065oQSqgy1fxno2dvxusj30sf10nqcm.jpg", "https://ws1.sinaimg.cn/large/0065oQSqgy1fxd7vcz86nj30qo0ybqc1.jpg"
            , "https://ws1.sinaimg.cn/large/0065oQSqgy1fwyf0wr8hhj30ie0nhq6p.jpg", "https://ws1.sinaimg.cn/large/0065oQSqgy1fwgzx8n1syj30sg15h7ew.jpg"};

    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageBrowseUtils.setImageLoader(new GlideImageLoader());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
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
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                View itemView = manager.findViewByPosition(position);
                View view = itemView.findViewById(R.id.ivCover);
                String transitionName = ImageBrowseUtils.TRANSITION + position;
                index = position;
                ViewCompat.setTransitionName(view, transitionName);
                List<String> urlList = new ArrayList<>();
                for (int i = 0; i < adapter.getCount(); i++) {
                    urlList.add(urls[i]);
                }
                new ImageBrowseUtils.Builder().setKey(TAG).setIndex(index).setUrlList(urlList).builder().start(MainActivity.this, view);
            }
        });
        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                View itemView = manager.findViewByPosition(index);
                View view = itemView.findViewById(R.id.ivCover);
                String transitionName = ImageBrowseUtils.TRANSITION + index;
                ViewCompat.setTransitionName(view, transitionName);
                names.clear();
                names.add(ImageBrowseUtils.TRANSITION + index);
                sharedElements.clear();
                sharedElements.put(names.get(0), view);
            }
        });
        ImageBrowseBus.getInstance().save(TAG, new ImageBrowseCallback() {
            @Override
            public void setIndex(int index) {
                MainActivity.this.index = index;
                scrollToPosition(index);
            }
        });
    }

    /**
     * RecyclerView滚动到指定位置
     *
     * @param position
     */
    public void scrollToPosition(int position) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            int firstItem = manager.findFirstVisibleItemPosition();
            int lastItem = manager.findLastVisibleItemPosition();
            if (position < firstItem || position > lastItem) {
                recyclerView.smoothScrollToPosition(index);
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            int firstItem = manager.findFirstVisibleItemPosition();
            int lastItem = manager.findLastVisibleItemPosition();
            if (position < firstItem || position > lastItem) {
                recyclerView.smoothScrollToPosition(index);
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            recyclerView.smoothScrollToPosition(index);
        }
    }

}
