package com.cyou.mrd.pengyou.widget.CoinAnimation;

import java.io.IOException;
import java.util.ArrayList;

import com.cyou.mrd.pengyou.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

public class CoinView extends View {
	private static final String TAG = "CoinView";
	private static final boolean SHOW_LOGS = false;
	
	public static final int POSITIVEX = 1;
	public static final int NEGATIVEX = -1;
	public static final int POSITIVEY = 1;
	public static final int NEGATIVEY = -1;
	
	private int mDirectionX;
	private int mDirectionY;
	private Thread mThread;
	private Context mContext;
	private ArrayList<Coin> mCoins = new ArrayList<Coin>();
	private static final Object slock = new Object();
	
	private long mInterval = 150;
	private int mFrameNumber = 0;
	private int mCoinNumber = 3;
	private int mShouldStartNum = 0;
	private float mBasicScale = 1;

//	private int mStartX;
//	private int mEndX;
//	private int mStartY;
//	private int mEndY;
	
	private boolean mIsLayout = false;
	private volatile boolean mIsRunning = false;
	private volatile boolean mIsDrawed = false;
	private volatile boolean mStop = false;
	
	private static final int RETRY = 1;
	private static final int DONE = 2;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			removeMessages(msg.what);
			switch (msg.what) {
			case RETRY:
				if(mIsLayout){
					act();
				}else {
					sendEmptyMessageDelayed(RETRY, 50);
				}
				break;
			case DONE:
				ViewParent parent =  getParent();
				View child = CoinView.this;
				if (mOnAnimationEndListener != null) {
					mOnAnimationEndListener.onAnimationEnd();
				}
				while (parent != null && !(parent instanceof FrameLayout)) {
					((ViewGroup) parent).removeView(child);
					child = (View) parent;
					parent = child.getParent();
				}
				break;
			default:
				break;
			}
		};
	};
	
	public interface OnAnimationEndListener{
		public void onAnimationEnd();
	}
	
	private OnAnimationEndListener mOnAnimationEndListener;

	
	public CoinView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	
	public CoinView(Context context, AttributeSet attrs) {
		super(context, attrs, -1);
		init(context);
	}
	
	public CoinView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		mContext = context;
		
	}
	
	public void setFrameNumber(int n){
		if (n <= 0) {
			throw new IllegalArgumentException("frame number must > 0");
		}
		mFrameNumber = n;
	}
	
	public void setCoinNumber(int n){
		if (n <= 0) {
			throw new IllegalArgumentException("coin number must > 0");
		}
		mCoinNumber = n;
	}
	
	public void setBasicScale(float scale){
		if (scale < 0) {
			throw new IllegalArgumentException("basic scale must > 0");
		}
		mBasicScale = scale;
	}
	
	public void setInterval(long time){
		if (time <= 0) {
			throw new IllegalArgumentException("interval must > 0");
		}
		mInterval = time;
	}
	
	
	public void start(int directionX, int directionY, OnAnimationEndListener l){
		if (directionX != POSITIVEX && directionX != NEGATIVEX) {
			throw new IllegalArgumentException("invalid x dircetion:" + directionX);
		}
		mDirectionX = directionX;
		
		if (directionY != POSITIVEY && directionY != NEGATIVEY) {
			throw new IllegalArgumentException("invalid y dircetion:" + directionY);
		}
		mDirectionY = directionY;
		mOnAnimationEndListener = l;
		if (mIsRunning) {
			return;
		}
		bringToFront();
		mHandler.sendEmptyMessage(RETRY);
		playUnlockSound();
	}
	
	public void stop(){
		mStop = true;
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
		
	}
	
	protected void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		int height = View.MeasureSpec.getSize(heightMeasureSpec);
		int width = View.MeasureSpec.getSize(widthMeasureSpec);
		this.setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mIsLayout = true;
		if (SHOW_LOGS) Log.v(TAG, "onLayout R:" + getRight() + " B:" + getBottom() + " L:" + getLeft() + " T:" + getTop() );
//		if (!mIsRunning) {
//			mIsRunning = true;
//			act();
//		}
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		stop();
	}
	
	private void act(){
		if(mIsRunning){
			return;
		}
		mIsRunning = true;
		
		int startx=0, starty=0, endx=0, endy=0;
		if (mDirectionX == POSITIVEX) {
			startx = getLeft();
			endx = getRight();
		}else if(mDirectionX == NEGATIVEX){
			startx = getRight();
			endx = getLeft();
		}
		if (SHOW_LOGS)Log.v(TAG, "act startx:" + startx + " endx:" + endx);
		
		if(mDirectionY == POSITIVEY){
			starty = getTop();
			endy = getBottom();
		}else if(mDirectionY == NEGATIVEY){
			starty = getBottom();
			endy = getTop();
		}
		if (SHOW_LOGS)Log.v(TAG, "act starty:" + starty + " endy:" + endy);
		

		synchronized (slock) {
			long current = System.currentTimeMillis();
			int n = mFrameNumber;
			float bs = mBasicScale;
			for (int i = 0; i < mCoinNumber; i++) {
				long s = current + mInterval * i;
				Coin c = new Coin(mContext, 
						startx, 
						starty, 
						endx, 
						endy, 
						s, 
						n,
						bs);
				c.setIndex(i);
				mCoins.add(c);
			};
		}

		mThread = new Thread(){
		public void run() {
			if (SHOW_LOGS)Log.v(TAG, "run() begin");
				try{
					while(!mStop){
						makeCoins();
						postInvalidate();
						
						while(!mIsDrawed){
								Thread.sleep(50);
						}
						

						Thread.sleep(mInterval);
						
						int n = -1;
						synchronized (slock) {
							if (SHOW_LOGS)Log.v(TAG, "run() mCoins.size:" + mCoins.size());
						    n = mCoins.size();
							if(mCoins.get(n - 1).isDone()){
								postInvalidate();
								mHandler.sendEmptyMessage(DONE);
								mIsRunning = false;
								return;
							}
						}
					}
				} catch (InterruptedException e) {
					mStop = true;
					e.printStackTrace();
				}
			};
		};
		if (mThread != null) {
			mThread.start();
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (SHOW_LOGS)Log.v(TAG, "onDraw begin");
		super.onDraw(canvas);
		mIsDrawed = true;
		if (mIsRunning) {
			synchronized (slock) {
				for (int i=0; i < mCoins.size(); i++) {
					Coin coin = mCoins.get(i);
					if(coin != null){
						if (SHOW_LOGS)Log.v(TAG, "onDraw drawCoin at:" + i);
						drawCoin(coin, canvas);
					}
				}
			}
		}else{
//			Paint paint = new Paint();  
//		    paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));  
//		    canvas.drawPaint(paint);  
//		    paint.setXfermode(null);  
			canvas.drawColor(0x00111111);
		}
		if (SHOW_LOGS)Log.v(TAG, "onDraw end");
	}
	
	private void makeCoins(){
		if (SHOW_LOGS)Log.v(TAG, "makeCoins begin");
		synchronized (slock) {
			
			for (int i=0; i < mCoins.size(); i++) {
				Coin coin = mCoins.get(i);
				if (coin != null) {
					if (i == mShouldStartNum) {
						coin.setShouldStart(true);
					}
					coin.setStatus();
				}
			}
			mShouldStartNum++;
		}
		if (SHOW_LOGS)Log.v(TAG, "makeCoins end");
	}
	
	private void drawCoin(Coin coin, Canvas canvas){
		if(null == coin){
			return;
		}
		if (SHOW_LOGS)Log.v(TAG, "drawCoin begin");
		Bitmap b = coin.getCoinBitmap();
		int x = coin.getX() -getLeft();
		int y = coin.getY() - getTop();
		if( b != null ){
			if (SHOW_LOGS)Log.v(TAG, "drawCoin x: " + x + " y:" + y + " getWidth():" + getWidth() + " Bwidth:" + b.getWidth());
			canvas.drawBitmap(b, x, y, null );
		}

		if (SHOW_LOGS)Log.v(TAG, "drawCoin end");
	}
	
	private static MediaPlayer mp = null;

    void playUnlockSound() {
        mp = MediaPlayer.create(mContext, R.raw.coins_out);
       
         AudioManager mgr = (AudioManager) mContext.getSystemService(
                 Context.AUDIO_SERVICE);
        int streamVolume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        try {
            mp.setVolume(streamVolume, streamVolume);
            //mp.attachAuxEffect(effectId)
            //mp.setVideoScalingMode(mode);
            mp.prepare();
           
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mp.start();
        mp.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.release();
                mp = null;
            }

        });
    }
//    SoundPool soundPool;
//    int sound_id;
//    void initUnlockSound2()
//    {
//        soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 1); 
//        sound_id = soundPool.load(mContext, R.raw.coins_out, 1);
//    }
//    void playUnlockSound2() {
//        soundPool.play(sound_id, 1.0f, 1.0f, 1, 0, 1.0f); 
//    }
	
}
