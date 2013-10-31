package com.leaf8.zishao.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 搜索条件的列表
 * 
 * @author 紫韶
 * @date Jun 28, 2013
 **/
public class ZsConditionListView extends ListView implements Runnable {
    private float mLastDownY = 0f;
    private int mDistance = 0;
    private int mStep = 10;
    // private boolean mPositive = false;
    private boolean mOverUp = false;
    private boolean mOverDown = false;
    private int mDestY = 0;

    public ZsConditionListView(Context context) {
        super(context);
    }

    public ZsConditionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZsConditionListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (mLastDownY == 0f && mDistance == 0) {
                mLastDownY = ev.getY();
                return true;
            }
            break;

        case MotionEvent.ACTION_CANCEL:
            break;

        case MotionEvent.ACTION_UP:
            if (mDistance != 0) {
                mStep = 1;
                mOverUp = (mDestY > 0);
                mOverDown =
                    (mDistance < 0 && getFirstVisiblePosition() == 0 && null != getChildAt(0) && getChildAt(
                        0).getTop() == 0);
                this.post(this);
                return true;
            }
            mLastDownY = 0f;
            mDistance = 0;
            break;

        case MotionEvent.ACTION_MOVE:
            if (mLastDownY != 0f) {
                mDistance = (int) (mLastDownY - ev.getY());
                if ((mDistance < 0 && getFirstVisiblePosition() == 0 && null != getChildAt(0) && getChildAt(
                    0).getTop() == 0)
                    || (mDistance > 0 && getLastVisiblePosition() == getCount() - 1)) {
                    if (mDistance > 0 && getLastVisiblePosition() == (getCount() - 1)
                        && null != getChildAt(getCount() - 1)) {
                        mDestY = getChildAt(getCount() - 1).getBottom();
                        return true;
                    }
                    mDistance /= 2;
                    scrollTo(0, mDistance);
                    return true;
                }
            }
            mDistance = 0;
            break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void run() {
        if (mOverUp) {
            mDistance = 0;
            mLastDownY = 0f;
            scrollTo(0, mDestY);
            mOverUp = false;
            return;
        } else if (mOverDown) {
            scrollTo(0, 0);
            mDistance = 0;
            mLastDownY = 0f;
            mOverDown = false;
            return;
        } else {
            mDistance += mDistance > 0 ? -mStep : mStep;
            scrollTo(0, mDistance);
            mStep += 1;
        }
        this.postDelayed(this, 10);
    }
}
