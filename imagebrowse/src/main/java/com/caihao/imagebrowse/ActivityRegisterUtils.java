package com.caihao.imagebrowse;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;

import java.util.List;
import java.util.Map;

/**
 * activity需要注册的事件
 * Created by admin on 2019/4/27.
 */
public class ActivityRegisterUtils {

    private Activity activity = null;

    private ActivityRegisterCallback activityRegisterCallback;

    private AfterIndexCallback afterIndexCallback;

    private int index = -1;

    public ActivityRegisterUtils(Activity activity, ActivityRegisterCallback activityRegisterCallback, AfterIndexCallback afterIndexCallback, int index) {
        this.activity = activity;
        this.activityRegisterCallback = activityRegisterCallback;
        this.afterIndexCallback = afterIndexCallback;
        this.index = index;
    }

    private ActivityRegisterUtils(Builder builder) {
        activity = builder.activity;
        activityRegisterCallback = builder.activityRegisterCallback;
        afterIndexCallback = builder.afterIndexCallback;
        index = builder.index;
    }

    public void register(String tag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.setExitSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    super.onMapSharedElements(names, sharedElements);
                    if (index != -1) {
                        View view = activityRegisterCallback.getView(index);
                        String transitionName = ImageBrowseUtils.TRANSITION + index;
                        ViewCompat.setTransitionName(view, transitionName);
                        names.clear();
                        names.add(transitionName);
                        sharedElements.clear();
                        sharedElements.put(names.get(0), view);
                        index = -1;
                    }
                }
            });
            ImageBrowseBus.getInstance().save(tag, new ImageBrowseCallback() {
                @Override
                public void setIndex(int index) {
                    ActivityRegisterUtils.this.index = index;
                    if (afterIndexCallback != null) {
                        afterIndexCallback.after(index);
                    }
                }
            });
        }
    }


    public static final class Builder {
        private Activity activity;
        private ActivityRegisterCallback activityRegisterCallback;
        private AfterIndexCallback afterIndexCallback;
        private int index = -1;

        public Builder() {
        }

        public Builder activity(Activity val) {
            activity = val;
            return this;
        }

        public Builder activityRegisterCallback(ActivityRegisterCallback val) {
            activityRegisterCallback = val;
            return this;
        }

        public Builder afterIndexCallback(AfterIndexCallback val) {
            afterIndexCallback = val;
            return this;
        }

        public ActivityRegisterUtils build() {
            return new ActivityRegisterUtils(this);
        }
    }
}
