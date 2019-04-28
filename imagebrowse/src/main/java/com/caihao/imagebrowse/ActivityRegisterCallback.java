package com.caihao.imagebrowse;

import android.view.View;

/**
 * Created by admin on 2019/4/27.
 */

public interface ActivityRegisterCallback {

    /**
     * 获取需要执行动画的View
     *
     * @param index
     * @return
     */
    View getView(int index);

}
