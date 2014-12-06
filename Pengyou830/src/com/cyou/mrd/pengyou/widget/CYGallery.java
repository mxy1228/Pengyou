package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class CYGallery extends Gallery {
private int totalSize;

    public CYGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CYGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {

        int kEvent;
        if (isScrollingLeft(e1, e2)) {
            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(kEvent, null);

//        if (getSelectedItemPosition() == 0) {// 实现后退功能
//            setSelection(totalSize-1);
//        }
//        if (getSelectedItemPosition() > totalSize - 1){
//            setSelection(0);
//        }
        return false;

    }

    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
            float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

}
