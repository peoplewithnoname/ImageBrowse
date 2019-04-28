package com.caihao.imagebrowse;

/**
 * 图片浏览的回调
 */
public interface AfterIndexCallback {

    /**
     * 设置浏览图片的下标之后也许会执行的操作（该操作在不可见的情况下 有写代码不能执行）
     *
     * @param index
     */
    void after(int index);

}
