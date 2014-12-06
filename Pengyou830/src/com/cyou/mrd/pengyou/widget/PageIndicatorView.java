/**
 * TODO
 * Couple--PageIndicatorView.java
 * Copyright qianbaiyang
 * @2011-8-12
 */
package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.cyou.mrd.pengyou.R;

/**
 * @author xumengyang PageIndicatorView.java 2011-8-12
 * 
 */
public class PageIndicatorView extends View {

    private int mCurrentPage = -1;
    private int mTotalPage = 0;
    private Context mContext;
    /**
     * @param context
     */
    public PageIndicatorView(Context context) {
        super(context);
        this.mContext = context;
    }
    /**
     * @param context
     * @param attrs
     */
    public PageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }
    public int getmCurrentPage() {
        return mCurrentPage;
    }
    public void setmCurrentPage(int nPageIndex) {
        if (nPageIndex < 0 || nPageIndex >= mTotalPage)
            return;

        if (mCurrentPage != nPageIndex) {
            mCurrentPage = nPageIndex;
            this.invalidate();
        }
    }
    public int getmTotalPage() {
        return mTotalPage;
    }
    public void setmTotalPage(int mTotalPage) {
        this.mTotalPage = mTotalPage;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);

        Rect r = new Rect();
        this.getDrawingRect(r);

        int iconWidth = 14;
        int iconHeight = 14;
        int space = 12;

        int x = (r.width() - (iconWidth * mTotalPage + space * (mTotalPage - 1))) / 2;
        int y = (r.height() - iconHeight) / 2;

        for (int i = 0; i < mTotalPage; i++) {

            int resid = R.drawable.dot_unselected;

            if (i == mCurrentPage) {
                resid = R.drawable.dot_selected;
            }

            Rect r1 = new Rect();
            r1.left = x;
            r1.top = y;
            r1.right = x + iconWidth;
            r1.bottom = y + iconHeight;

            Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), resid);
            canvas.drawBitmap(bmp, null, r1, paint);

            x += iconWidth + space;

        }

    }

    public void DrawImage(Canvas canvas, Bitmap mBitmap, int x, int y, int w,
            int h, int bx, int by) {
            Rect src = new Rect();// 图片裁剪区域
            Rect dst = new Rect();// 屏幕裁剪区域
            src.left = bx;
            src.top = by;
            src.right = bx + w;
            src.bottom = by + h;

            dst.left = x;
            dst.top = y;
            dst.right = x + w;
            dst.bottom = y + h;

            // canvas.drawBitmap(mBitmap, src, dst, mPaint);
            src = null;
            dst = null;
    }
}
