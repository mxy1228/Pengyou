package com.cyou.mrd.pengyou.widget;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.log.CYLog;

public class ContactActionBar extends ImageButton {
	
	private CYLog log = CYLog.getInstance();

//	private static final String[] letters = new String[] { "#", "A", "B", "C", "D", "E",
//			"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
//			"S", "T", "U", "V", "W", "X", "Y", "Z" };
	private Paint paint = new Paint();
	private boolean showBkg = false;
	private int choose = -1;
	private Handler mHandler;
	private int mHeight = 0;
	private Map<String,Integer> mUserLetters;
	private String[] mLetters;
	
	private TextView mDialogTV;
	private ListView mListView;
	
	public ContactActionBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ContactActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public ContactActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void init(View view){
		this.mDialogTV = (TextView)view.findViewById(R.id.contacts_dialog_tv);
		this.mDialogTV.setVisibility(View.INVISIBLE);
		this.mHandler = new Handler();
	}
	
	public void setListView(ListView listview){
		this.mListView = listview;
	}
	
	public void setUserLetters(Map<String,Integer> letters){
		this.mUserLetters = letters;
	}
	
	public void setLetter(String[] letter){
		this.mLetters = letter;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		if(mUserLetters != null){
			int singleHeight = height / mLetters.length;
			for (int i = 0; i < mUserLetters.size(); i++) {
				paint.setColor(Color.parseColor("#6a737d"));
				paint.setTextSize(20);
				paint.setTypeface(Typeface.DEFAULT_BOLD);
				paint.setAntiAlias(true);
				if (i == choose) {
					paint.setColor(Color.parseColor("#00BFFF"));//滑动时按下的字母颜色
					paint.setFakeBoldText(true);
				}
				float xPos = width / 2 - paint.measureText(mLetters[i]) / 2;
				float yPos = singleHeight * i+singleHeight;
				canvas.drawText(mLetters[i], xPos, yPos, paint);
				paint.reset();
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float y = event.getY();
		int oldChoose = choose;
		// 计算手指位置，找到对应的段，让mList移动段开头的位置上
		mHeight = getHeight();
		if(mLetters == null){
			return super.onTouchEvent(event);
		}
		int selectIndex = (int) (y / (mHeight / mLetters.length));
		if(selectIndex > -1 && selectIndex < mLetters.length){
			String key = mLetters[selectIndex];
			if(mUserLetters.containsKey(key)){
				int pot = mUserLetters.get(key);
				if(mListView.getHeaderViewsCount() > 0){
					mListView.setSelectionFromTop(mListView.getHeaderViewsCount()+pot,0);
				}else{
					mListView.setSelectionFromTop(pot,0);
				}
				mDialogTV.setText(key);
			}
		}
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			showBkg = true;

			if (oldChoose != selectIndex) {
				if (selectIndex > 0 && selectIndex < mLetters.length) {
					choose = selectIndex;
					invalidate();
				}
			}

			if (mHandler != null) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (mDialogTV != null
								&& mDialogTV.getVisibility() == View.INVISIBLE) {
							mDialogTV.setVisibility(VISIBLE);
						}
					}
				});
			}
			break;
		case MotionEvent.ACTION_UP:
			showBkg = false;
			choose = -1;
			if (mHandler != null) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (mDialogTV != null
								&& mDialogTV.getVisibility() == View.VISIBLE) {
							mDialogTV.setVisibility(INVISIBLE);
						}
					}
				});
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (oldChoose != selectIndex) {
				if (selectIndex > 0 && selectIndex < mLetters.length) {
					choose = selectIndex;
					invalidate();
				}
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}
}
