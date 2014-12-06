package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.log.CYLog;

public class SearchBar extends LinearLayout implements OnClickListener{

	private CYLog log = CYLog.getInstance();
	
	private EditText mET;
	private ImageButton mCancelIBtn;
	private Button mCancellButton;
	private Context mContext;
	private TextAndActionListsner mListener;
	private Animation mInAnim;
	private Animation mOutAnim;
	
	public SearchBar(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	
	public SearchBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	
	private void init(){
		this.mInAnim = new AlphaAnimation(0, 1);
		this.mInAnim.setDuration(200);
		this.mInAnim.setFillAfter(true);
		this.mOutAnim = new AlphaAnimation(1, 0);
		this.mOutAnim.setDuration(200);
		this.mOutAnim.setFillAfter(true);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		View view = inflate(mContext, R.layout.search_bar, null);
		view.setLayoutParams(params);
		addView(view);
		this.mET = (EditText)view.findViewById(R.id.search_bar_et);
		this.mET.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(TextUtils.isEmpty(mET.getText().toString())){
					if(mListener != null){
						mListener.onEmpty();
					}
					if(mCancelIBtn.isShown()){
						mCancelIBtn.startAnimation(mOutAnim);
						mCancelIBtn.setVisibility(View.GONE);
					}
				}else{
					if(mListener != null){
						mListener.onText();
					}
					if(!mCancelIBtn.isShown()){
						mCancelIBtn.startAnimation(mInAnim);
						mCancelIBtn.setVisibility(View.VISIBLE);
					}
				}
			}
		});
		this.mET.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				long nowTime=System.currentTimeMillis();
				if(nowTime-lastTime<1000){
					lastTime=nowTime;
					return false;
				}
				lastTime=nowTime;
				if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
					if(mListener != null){
						log.e("search");
						String key = mET.getText().toString();
						if(TextUtils.isEmpty(key.trim())){
							Toast.makeText(getContext(), R.string.game_search_nokey, Toast.LENGTH_LONG).show();
							return false;
						}
						mListener.onAction(key);
					}
				}
				return false;
			}
		});
		this.mCancelIBtn = (ImageButton)view.findViewById(R.id.search_bar_ibtn);
		this.mCancelIBtn.setOnClickListener(this);
		this.mCancelIBtn.setVisibility(View.GONE);
		this.mCancellButton = (Button)view.findViewById(R.id.search_button_cancel);
		this.mCancellButton.setOnClickListener(this);
		this.mCancellButton.setVisibility(View.GONE);
	}
	long  lastTime=0;
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public void onClick(View v) {
		mET.setText("");
	}
	
	public String getTextValue() {
		return mET.getText().toString().trim();
	}

	public void addCancelButton(boolean visible){
		this.mCancellButton.setVisibility(visible?View.VISIBLE:View.GONE);
	}

	public interface TextAndActionListsner {
		public void onEmpty();
		public void onText();
		public void onAction(String key);
	}
	
	public void setTextAndActionListener(TextAndActionListsner listener){
		this.mListener = listener;
	}
	
	public EditText getEditText(){
		return this.mET;
	}
	
	public Button getCancelButton(){
		return this.mCancellButton;
	}
}
