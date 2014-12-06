package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;

import com.cyou.mrd.pengyou.R;

public class CommentBar extends LinearLayout implements OnClickListener{

	private Context mContext;
	private EditText mET;
	private Button mButton;
	private LinearLayout mLL;
	private SendListener mListener;
	
	public CommentBar(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	
	public CommentBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	
	private void init(){
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.comment_bar, null);
		addView(view);
		this.mET = (EditText)view.findViewById(R.id.comment_bar_et);
		this.mButton = (Button)view.findViewById(R.id.comment_bar_send_btn);
		this.mLL = (LinearLayout)view.findViewById(R.id.comment_bar_ll);
		this.mButton.setOnClickListener(this);
	}
	
	public Button getSendButton(){
		return this.mButton;
	}

	public EditText getEditText(){
		return this.mET;
	}
	
	public LinearLayout getLL(){
		return this.mLL;
	}
	
	public String getContent(){
		return this.mET.getText().toString().trim();
	}
	
	public void setOnSendListener(SendListener listener){
		this.mListener = listener;
	}
	
	public interface SendListener{
		public void send(String content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment_bar_send_btn:
			if(mListener == null){
				return;
			}
			String content = mET.getText().toString().trim();
			this.mListener.send(content);
			break;

		default:
			break;
		}
	}
}
