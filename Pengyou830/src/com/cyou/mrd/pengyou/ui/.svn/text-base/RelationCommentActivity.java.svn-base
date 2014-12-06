package com.cyou.mrd.pengyou.ui;



import java.io.Serializable;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.DynamicCommentReplyItem;
import com.cyou.mrd.pengyou.widget.ResizeLayout;
import com.cyou.mrd.pengyou.widget.ResizeLayout.OnResizeListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RelationCommentActivity extends Activity {
	private ResizeLayout mRootRel;
	int isFirstTime = 0;
	private EditText mCommentET;
	private Button mCommentBtn;
	private boolean isFirst = true;
	private String src;
	private String commnetStr;
	private Serializable obj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relation_circle_comment);
		Intent i = getIntent();
		obj = i.getSerializableExtra("obj");
		src = i.getStringExtra("src");
		mRootRel = (ResizeLayout) findViewById(R.id.relationship_circle_root_rel);
	    mCommentET = (EditText) findViewById(R.id.relationship_circle_comment_et);
	    mCommentBtn = (Button)  findViewById(R.id.relationship_circle_comment_send_btn);
	    mCommentET.requestFocus();
		if(obj != null){
			DynamicCommentReplyItem reItem = (DynamicCommentReplyItem)obj;
			mCommentET.setHint(getString(R.string.reply)+reItem.getNickname()+":");
			mCommentBtn.setText(R.string.comment);
		}
		else {
		    mCommentET.setHint(getString(R.string.comment));
		    mCommentBtn.setText(R.string.send);
		}
		mCommentBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				commnetStr = mCommentET.getText().toString().trim();
				if (TextUtils.isEmpty(commnetStr.replaceAll("\\s*", ""))) {
					Toast.makeText(RelationCommentActivity.this, R.string.input_empty, Toast.LENGTH_SHORT).show();
					return;
				}
				backData();
			}
		});
	    mRootRel.setOnResizeListener(new OnResizeListener() {
			@Override
			public void OnResize(int w, int h, int oldw, int oldh) {
				if(isFirst){
					isFirst = false;
					return ;
				}
				int distance = oldh - h;
				int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
				//收回虚拟键盘
			    if(distance < 0 && Math.abs(distance) >screenHeight/3){
				    finish();
			    }
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    finish();
		return super.onTouchEvent(event);
	}
	 
	public void backData(){
		Intent dataResult = new Intent();
		dataResult.putExtra("COMMENT", commnetStr);
		dataResult.putExtra("obj", obj);
		if(src.equalsIgnoreCase(Contants.RELATION_CONTANTS.REL_CIRCLE_STARTCOMMENT_SRC)){
			setResult(Contants.RELATION_CONTANTS.REL_CIRCLE_BACK_COMMENT_CODE,dataResult);
		}
		else if(src.equalsIgnoreCase(Contants.RELATION_CONTANTS.REL_SQUARE_STARTCOMMENT_SRC)){
			setResult(Contants.RELATION_CONTANTS.REL_SQUARE_BACK_COMMENT_CODE,dataResult);
		}
		finish();
	}
	

}
