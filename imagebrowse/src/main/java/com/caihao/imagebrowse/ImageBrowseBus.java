package com.caihao.imagebrowse;

import java.util.HashMap;

/**
 * 图片浏览专用数值传送bus(单利模式)
 */
public class ImageBrowseBus {

    private static ImageBrowseBus _instance = null;

    /**
     * 存放Callback回调的类
     */
    private HashMap<String, ImageBrowseCallback> map;

    public synchronized static ImageBrowseBus getInstance() {
        if (_instance == null) {
            synchronized (ImageBrowseBus.class) {
                if (_instance == null) {
                    _instance = new ImageBrowseBus();
                }
            }
        }
        return _instance;
    }

    /**
     * 获取map
     */
    private HashMap<String, ImageBrowseCallback> getValueMap() {
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    /**
     * 保存回调
     *
     * @param key
     * @param value
     */
    public void save(String key, ImageBrowseCallback value) {
        getValueMap();
        if (map.containsKey(key)) {
            remove(key);
        }
        map.put(key, value);
    }

    /**
     * 根据key值获取回调
     *
     * @param key
     * @return
     */
    public ImageBrowseCallback get(String key) {
        getValueMap();
        if (!map.containsKey(key)) {
            return null;
        }
        ImageBrowseCallback callback = null;
        for (String strKey : map.keySet()) {
            if (strKey.equals(key)) {
                callback = map.get(key);
            }
        }
        return callback;
    }

    /**
     * 移除某个回调
     *
     * @param key
     */
    public void remove(String key) {
        if (map != null) {
            for (String strKey : map.keySet()) {
                if (strKey.equals(key)) {
                    ImageBrowseCallback callback = map.remove(key);
                    callback = null;
                    break;
                }
            }
        }
    }

    /**
     * 清除掉所有回调
     */
    public void clear() {
        if (map != null) {
            map.clear();
            map = null;
        }
    }


}
