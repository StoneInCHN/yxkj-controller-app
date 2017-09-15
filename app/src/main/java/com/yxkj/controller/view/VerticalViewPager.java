package com.yxkj.controller.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 竖直ViewPager
 */

public class VerticalViewPager extends ViewPager {
    private boolean isPagingEnabled = false;

    public VerticalViewPager(Context context) {
        super(context);
        init();
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // The majority of the magic happens here
        setPageTransformer(true, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private class VerticalPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                view.setAlpha(1);

                // Counteract the default slide transition
                view.setTranslationX(view.getWidth() * -position);

                //set Y position to swipe in from top
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = super.onInterceptTouchEvent(ev);
        return intercepted && isPagingEnabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev) && isPagingEnabled;
    }
}
