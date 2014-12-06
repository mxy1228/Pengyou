package com.cyou.mrd.pengyou.widget;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

public class SearchTagView extends LinearLayout implements OnClickListener {
	private View rootView;
	private TextView txtView1;
	private TextView txtView2;
	private TextView txtView3;
	private TextView txtView4;
	private TextView txtView5;
	private TextView txtView6;
	private TextView txtView7;
	private TextView txtView8;
	private TextView txtView9;
	private TextView txtView10;
	private Context mContext;

	public SearchTagView(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	public SearchTagView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	private void initView() {
		rootView = LayoutInflater.from(mContext).inflate(
				R.layout.search_tag_layout, this);
		txtView1 = (TextView) rootView.findViewById(R.id.search_tag_1);
		txtView2 = (TextView) rootView.findViewById(R.id.search_tag_2);
		txtView3 = (TextView) rootView.findViewById(R.id.search_tag_3);
		txtView4 = (TextView) rootView.findViewById(R.id.search_tag_4);
		txtView5 = (TextView) rootView.findViewById(R.id.search_tag_5);
		txtView6 = (TextView) rootView.findViewById(R.id.search_tag_6);
		txtView7 = (TextView) rootView.findViewById(R.id.search_tag_7);
		txtView8 = (TextView) rootView.findViewById(R.id.search_tag_8);
		txtView9 = (TextView) rootView.findViewById(R.id.search_tag_9);
		txtView10 = (TextView) rootView.findViewById(R.id.search_tag_10);
		txtView1.setOnClickListener(this);
		txtView2.setOnClickListener(this);
		txtView3.setOnClickListener(this);
		txtView4.setOnClickListener(this);
		txtView5.setOnClickListener(this);
		txtView6.setOnClickListener(this);
		txtView7.setOnClickListener(this);
		txtView8.setOnClickListener(this);
		txtView9.setOnClickListener(this);
		txtView10.setOnClickListener(this);
	}

	public void initData(List<String> tagLst) {
		if (null != tagLst && tagLst.size() > 0) {
			for (int i = 0; i < tagLst.size(); i++) {
				switch (i) {
				case 0:
					txtView1.setText(tagLst.get(i));
					txtView1.setVisibility(View.VISIBLE);
					break;
				case 1:
					txtView2.setText(tagLst.get(i));
					txtView2.setVisibility(View.VISIBLE);
					break;
				case 2:
					txtView3.setText(tagLst.get(i));
					txtView3.setVisibility(View.VISIBLE);
					break;
				case 3:
					txtView4.setText(tagLst.get(i));
					txtView4.setVisibility(View.VISIBLE);
					break;
				case 4:
					txtView5.setText(tagLst.get(i));
					txtView5.setVisibility(View.VISIBLE);
					break;
				case 5:
					txtView6.setText(tagLst.get(i));
					txtView6.setVisibility(View.VISIBLE);
					break;
				case 6:
					txtView7.setText(tagLst.get(i));
					txtView7.setVisibility(View.VISIBLE);
					break;
				case 7:
					txtView8.setText(tagLst.get(i));
					txtView8.setVisibility(View.VISIBLE);
					break;
				case 8:
					txtView9.setText(tagLst.get(i));
					txtView9.setVisibility(View.VISIBLE);
					break;
				case 9:
					txtView10.setText(tagLst.get(i));
					txtView10.setVisibility(View.VISIBLE);
					break;
				}
			}
		}
	}

	public interface OnTextCheckedListener {
		public void onTextChenked(String text);
	}

	OnTextCheckedListener mOnTextCheckedListener;

	public void setOnTextCheckedListener(
			OnTextCheckedListener onTextCheckedListener) {
		mOnTextCheckedListener = onTextCheckedListener;
	}

	@Override
	public void onClick(View v) {
		TextView txt = (TextView) v;
		if (null != mOnTextCheckedListener) {
			mOnTextCheckedListener.onTextChenked(txt.getText().toString());
		}
	}
}
