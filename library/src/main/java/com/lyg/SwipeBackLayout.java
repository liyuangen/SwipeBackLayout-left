package com.lyg;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.lyg.library.R;

/**
 * 侧滑返回主类
 */
public class SwipeBackLayout extends FrameLayout {

    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;
    private static final int FULL_ALPHA = 255;
    /**
     * 滑动帮助类
     */
    private ViewDragHelper mDrawHelper;
    /**
     * 滑动返回手势开关，默认开启
     */
    private boolean isEnabled = true;
    /**
     * 默认左边滑动手势
     */
    private int mTrackingEdges = ViewDragHelper.EDGE_LEFT;
    /**
     * contentView
     */
    private View mContentView;
    /**
     * 滚动的百分比
     */
    private float mScrollPercent;
    /**
     * 当前的activity
     */
    private Activity mActivity;
    /**
     * 遮布的透明度
     */
    private float mScrimOpacity;
    /**
     * 默认的遮布颜色
     */
    private int mScrimColor = DEFAULT_SCRIM_COLOR;
    /**
     * 阴影drawable
     */
    private Drawable mShadowDrawable;

    ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mDrawHelper.isEdgeTouched(mTrackingEdges, pointerId);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            final int childWidth = changedView.getWidth();
            invalidate();
            mScrollPercent = (float) left / (float) childWidth;
            if (mScrollPercent >= 1) {
                mActivity.finish();
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //手指松开时回调
            final int childWidth = releasedChild.getWidth();
            int left = 0, top = 0;
            left = xvel > 0 || mScrollPercent >= 0.5f ? childWidth : 0;
            mDrawHelper.settleCapturedViewAt(left, top);
            invalidate();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //设置滑动边界
            int ret = 0;
            ret = Math.min(child.getWidth(), Math.max(left, 0));
            return ret;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mTrackingEdges & ViewDragHelper.EDGE_LEFT;
        }

    };

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mDrawHelper = ViewDragHelper.create(this, mCallback);
        //默认左边滑动手势
        mDrawHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        //设置阴影
        setShadow(getResources().getDrawable(R.drawable.shadow));
    }

    /**
     * 注入activity
     *
     * @param activity
     */
    public void attachActivity(Activity activity) {
        mActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowBackground});
        int resourceId = a.getResourceId(0, 0);
        a.recycle();
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View childView = decorView.getChildAt(0);
        childView.setBackgroundResource(resourceId);
        decorView.removeView(childView);
        setContentView(childView);
        addView(childView);
        decorView.addView(this);
    }

    /**
     * 设置是否开启滑动返回手势 默认开启
     *
     * @param enabled true开启，false关闭
     */
    public void setEnabledGesture(boolean enabled) {
        isEnabled = enabled;
    }

    /**
     * 设置阴影
     *
     * @param shadowDrawable
     */
    public void setShadow(Drawable shadowDrawable) {
        mShadowDrawable = shadowDrawable;
    }

    /**
     * 平滑的滚动到finish Activity
     */
    public void scrollToFinishActivity() {
        int width = mContentView.getWidth();
        mContentView.getHeight();
        int left = 0, top = 0;
        left = width;
        mDrawHelper.smoothSlideViewTo(mContentView, left, top);
        invalidate();
    }

    public void setContentView(View view) {
        mContentView = view;
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final boolean drawContent = child == mContentView;

        boolean ret = super.drawChild(canvas, child, drawingTime);
        if (mScrimOpacity > 0 && drawContent && mDrawHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
            drawScrim(canvas, child);
            drawShadow(canvas, child);
        }
        return ret;
    }
    //画阴影
    private void drawShadow(Canvas canvas, View child) {
        //获取View的bounds
        Rect rect = new Rect();
        child.getHitRect(rect);
        mShadowDrawable.setBounds(rect.left - mShadowDrawable.getIntrinsicWidth(), rect.top, rect.left, rect.bottom);
        mShadowDrawable.setAlpha((int) (FULL_ALPHA * mScrimOpacity));
        mShadowDrawable.draw(canvas);
    }
    //画遮布
    private void drawScrim(Canvas canvas, View child) {
        final int baseAlpha = (mScrimColor & 0xff000000) >>> 24;
        final int alpha = (int) (baseAlpha * mScrimOpacity);
        final int color = alpha << 24 | (mScrimColor & 0xffffff);
        canvas.clipRect(0, 0, child.getLeft(), child.getHeight());
        canvas.drawColor(color);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        mScrimOpacity = 1 - mScrollPercent;
        if (mDrawHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled) {
            return super.onInterceptTouchEvent(ev);
        }
        return mDrawHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled) {
            return false;
        }
        mDrawHelper.processTouchEvent(event);
        return true;
    }

}
