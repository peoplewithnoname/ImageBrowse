# ImageBrowse
大图查看器

## 导入
根目录下的build.gradle添加
```javascript
allprojects-repositories————》

 maven { url 'https://jitpack.io' }
 
```

再在需要依赖的目录下的build.gradle添加
```javascript
dependencies————》

implementation 'com.github.peoplewithnoname:ImageBrowse:1.0.0'
 
```

## 使用方法（有点麻烦）

### 2个Activity   ActtivityA  ActivityB(查看大图的activity)

### 先在manifest里面添加

```javascript
<activity
            android:name="com.caihao.imagebrowse.ImageBrowseActivity"
            android:screenOrientation="portrait" />

 <uses-permission android:name="android.permission.INTERNET" /> (权限别忘了)
 
```

### 假设我们存放图片的容器是RecyclerView 需要在ActivityA 里面重写几个方法

#### 
```javascript
 
 这是1个回调  多张图片的情况下需要记录每个图片的下标index   TAG就是一个标记回调的字符串
 
  ImageBrowseBus.getInstance().save(TAG, new ImageBrowseCallback() {
            @Override
            public void setIndex(int index) {
                MainActivity.this.index = index;
                scrollToPosition(index);
            }
        });
        
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
        }else if(layoutManager instanceof LinearLayoutManager){
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            int firstItem = manager.findFirstVisibleItemPosition();
            int lastItem = manager.findLastVisibleItemPosition();
            if (position < firstItem || position > lastItem) {
                recyclerView.smoothScrollToPosition(index);
            }
        }else if(layoutManager instanceof StaggeredGridLayoutManager){
            recyclerView.smoothScrollToPosition(index);
        }
    }
        
```

```javascript

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
        
        setExitSharedElementCallback 这个方法只能在Activity下调用  重复调用会覆盖上一次的  （我吃过这个坑  记忆犹新啊）
        
        ImageBrowseUtils.TRANSITION 是一个常量  index为下标
        
        不知道onMapSharedElements的同学可以去补补5.0的转场动画  叫共享元素 来的
 
```

```javascript

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
        
       列表的点击事件  主要代码就是  new ImageBrowseUtils.Builder().setKey(TAG).setIndex(index).setUrlList(urlList).builder().start(MainActivity.this, view);
        之前的代码全部都是为了这一句
        
        TAG index 之前已经说过了  urlList时图片的链接集合  
    
        用builder创建参数  最终执行的时这句
        
        ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, TRANSITION + index);
        
        REANSITION常量  index是下标
 
```

## 重点（有关于图片加载的使用）

### 1.实现ImageLoader接口创建类  我用的Glide


```javascript
   @Override
    public void load(Context context, String path, final ImageView imageView) {
        Glide.with(context).asDrawable().load(path).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (resource != null) imageView.setImageDrawable(resource);
            }
        });
    }
```

### 2.在查看大图之前要调用
```javascript
   ImageBrowseUtils.setImageLoader(new GlideImageLoader());
```

## 访问链接可以下载demo

### https://fir.im/1cmn

 
