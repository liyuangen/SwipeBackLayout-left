package com.lyg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * 要实现侧滑返回的Activity继承SwipeBackActivity
 */
public class SwipeBackActivity extends AppCompatActivity {

    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onCreate();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    /**
     * 设置是否开启滑动返回手势 默认开启
     *
     * @param enabled true开启，false关闭
     */
    public void setSwipeEnabled(boolean enabled) {
        getSwipeBackLayout().setEnabledGesture(enabled);
    }

    /**
     * 关闭Activity
     */
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
