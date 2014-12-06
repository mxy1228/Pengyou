package com.cyou.mrd.pengyou.widget.CoinAnimation;

import com.cyou.mrd.pengyou.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

public class Coin {
	private static final String TAG = "Coin";
	private static final boolean SHOW_LOGS = false;
	
	private static int IMG[] = {R.drawable.coin1, 
						R.drawable.coin2,
						R.drawable.coin3,
						R.drawable.coin4,
						R.drawable.coin5,
						R.drawable.coin6,
						R.drawable.coin7,
						R.drawable.coin8
						};
	
//	private static int LIGHT[] = {R.drawable.play_coin_light1, 
//								R.drawable.play_coin_light2,
//								R.drawable.play_coin_light3,
//								R.drawable.play_coin_light4,
//								R.drawable.play_coin_light5,
//								R.drawable.play_coin_light6,
//								R.drawable.play_coin_light7,
//								R.drawable.play_coin_light8
//							};
	
	private static int DEGREE[] = {0, 60, 150, 90, 0, 60, 150, 90};
	private static float SCALE[] = {1, 0.9f, 0.8f, 0.7f, 0.65f, 0.6f, 0.55f, 0.5f};
	
	private Context mContext;
	private Bitmap mCoinBitmap;
	private Bitmap mLightBitmap;
	private long mStartTime;
	private int mX;
	private int mY;
	private int mStartX;
	private int mStartY;
	private int mEndX;
	private int mEndY;
	private float mBasicScale = 1;
	private float mScale = SCALE[0];
	private int mDegree = DEGREE[0];
	private int mCount;
	private int mCurrentCount;

	private int mIndex;
	private int mStepX;
	private int mStepY;
	private int mScaleShift;
//	private int mCoinWidth;
//	private int mCoinHeight;
	private volatile boolean mShouldStart = false;
	
	public Coin(Context c, int startX, int startY, int endX, int endY, 
			long startT, int count, float basicScale){
		
		if(startT < 0 ){
			throw new IllegalStateException("bad input params: startT >= 0 is desired");
		}
		
		mContext = c;
		mX = mStartX = startX;
		mY = mStartY = startY;
		mEndX = endX;
		mEndY = endY;
		mBasicScale = basicScale;
		
		int nc = 0;
		for (float scale : SCALE) {
			if (mBasicScale >= scale) {
				break;
			}
			nc++;
		}
		mScaleShift = nc;
		
		Bitmap b = BitmapFactory.decodeResource(mContext.getResources(), IMG[0]);
		if (b == null) {
//			Log.e(TAG, "Fail to decode coin bitmap!");
			return;
		}

		if(count  <= 0){
			int bw = b.getWidth();
			int bh = b.getHeight();
			int cx = Math.abs(startX - endX) / bw;
			int cy = Math.abs(startY - endY) / bh;
			mCount = Math.max(3, Math.max(cx, cy));
//			Log.e(TAG, "mCount:" + mCount + "cx:" + cx + "cy:" + cy);
		}else{
			mCount = count;
		}
		
		mX = mEndX > mStartX ? mStartX : (mStartX - b.getWidth());
		mY = mEndY > mStartY ? mStartY : (mStartY - b.getHeight());
		mStepX = (mEndX - mX) / mCount;
		mStepY = (mEndY - mY) / mCount;
		b = null;
		mStartTime = startT;
		mCurrentCount = 0;
		if (SHOW_LOGS)Log.d(TAG, "mStartX:" + mStartX + " mStartY:" + mStartY + " mEndX:" + mEndX + " mEndY:" + mEndY );
		if (SHOW_LOGS)Log.d(TAG, "mCount:" + mCount + "mStartTime:" + mStartTime);
	}
	
	void setShouldStart(boolean b){
		mShouldStart = b;
	}
	
	void setIndex(int index){
		mIndex = index;
	}
	
	void setStatus(){
		if (mShouldStart) {
//			CalculateCoordinate();
			if(mCurrentCount <= mCount){
				mDegree = DEGREE[mIndex%DEGREE.length];
				int nscale = mCurrentCount + mScaleShift;
				mScale = SCALE[nscale > SCALE.length - 1 ? SCALE.length - 1: nscale];
				if (SHOW_LOGS)Log.d(TAG, "mScale:" + mScale + " mDegree:" + mDegree);
				
				Bitmap b = BitmapFactory.decodeResource(mContext.getResources(), IMG[mCurrentCount%IMG.length]);
				Matrix m = new Matrix();
				m.preScale(mScale, mScale);
				m.preRotate(mDegree);
				Bitmap bm1 = Bitmap.createBitmap(b, 
						0, 
						0, 
						(int)(b.getWidth()), 
						(int)(b.getHeight()), 
						m, 
						true);
				
//				Bitmap b2 = BitmapFactory.decodeResource(mContext.getResources(), LIGHT[mCurrentCount%LIGHT.length]);

				if(mCurrentCount != 0){
					mX += mStepX;
					mY += mStepY;
					if (SHOW_LOGS)Log.d(TAG, "x:" + mX + " y:" + mY);
				}
				mCurrentCount++;
				
				synchronized (this) {
					mCoinBitmap = bm1;
				}
//				synchronized (slock2) {
//					mLightBitmap = b2;
//				}
//				if (SHOW_LOGS)Log.d(TAG, "mBitmap:" + mCoinBitmap);
			}else {
				synchronized (this) {
					mCoinBitmap = null;
				}
			}
			
		}

	}
//	
//	private void CalculateCoordinate(){
//		if( mCount > mCurrentCount){
////			mCurrentCount++;
////			mX += mStepX;
////			mY += mStepY;
////			if (SHOW_LOGS)Log.d(TAG, "x:" + mX + " y:" + mY);
//		}else{
//			mDone = true;
//		}
//	}
	
	int getX(){
		return mX;
	}
	
	int getY(){
		return mY;
	}
	
	Bitmap getCoinBitmap(){
		synchronized (this) {
			return mCoinBitmap;
		}
	}
	
	Bitmap getLightBitmap(){
//		synchronized (slock2) {
			return mLightBitmap;
//		}
	}
	
//	int getCoinWidth(){
//		return mCoinWidth;
//	}
//	
//	int getCoinHeight(){
//		return mCoinHeight;
//	}
	
	boolean isDone(){
		return mCurrentCount == mCount;
	}
}
