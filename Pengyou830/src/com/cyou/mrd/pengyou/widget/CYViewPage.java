package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CYViewPage extends ViewPager {

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }
    public CYViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
      
    }
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()) {
//        case MotionEvent.ACTION_MOVE: 
//           requestDisallowInterceptTouchEvent(true);
//          break;
//        case MotionEvent.ACTION_UP:
//        case MotionEvent.ACTION_CANCEL:
//            requestDisallowInterceptTouchEvent(false);
//          break;
//       }
//        return false;
//    }
}
