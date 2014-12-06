package com.cyou.mrd.pengyou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GameCircleViewPagerAdapter;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.log.CYLog;

/**
 * 游戏圈
 * 
 * @author chencaixing
 * 
 */
public class GameCircleActivity extends CYBaseActivity implements
        OnClickListener {

    protected static final int IS_CAN_SEND = 6;
	protected static final int SHOW_GAME_NAME = 8;
	private CYLog log = CYLog.getInstance();
    private ViewPager mViewPager;
    private ImageView mSendDynamicIV;
    private TextView mGameNameTV;
    private String mGameCode;
    private String mGameName;
    private String mGcidString;
    private String mGamePkgString;
    private boolean mIsMarked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_circle_single_layout);
        mGameCode = getIntent().getStringExtra(Params.INTRO.GAME_CODE);
        mGameName = getIntent().getStringExtra(Params.INTRO.GAME_NAME);
        if(mGameName == null){
        	mGameName = "";
        } 
        mGcidString = getIntent().getStringExtra(Params.Dynamic.GAME_CIRCLE_ID);
        mGamePkgString =  getIntent().getStringExtra(Params.INTRO.GAME_PKGE);
        mIsMarked = getIntent().getBooleanExtra(Params.INTRO.GAME_ISINSTALLED, false);
        log.d("GameCircleActivity --gamecode-" + mGameCode + "---mGcid--" + mGcidString);
        initView();
    }

    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    };

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
	protected void initView() {
        View headerbar = findViewById(R.id.gamecircle_title_bar);
        headerbar.findViewById(R.id.game_circle_back_btn).setOnClickListener(this);
        mSendDynamicIV = (ImageView) headerbar.findViewById(R.id.game_circle_senddynamic_iv);
        mGameNameTV = (TextView) headerbar.findViewById(R.id.game_circle_message_tv);
        mGameNameTV.setText(mGameName);
        mSendDynamicIV.setImageResource(R.drawable.img_pub_myact);
        mSendDynamicIV.setVisibility(View.VISIBLE);
        mSendDynamicIV.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.game_circle_viewpager);
        mViewPager.setAdapter(new GameCircleViewPagerAdapter(
                GameCircleActivity.this, false,mGameCode, mGcidString,mGamePkgString,mHandler));
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.game_circle_back_btn:
            finish();
            break;
        case R.id.game_circle_senddynamic_iv:
            // 发送动态
            Intent mIntent = new Intent();
            //行为需确定
            
            mIntent.putExtra(Params.Dynamic.GAME_CIRCLE_ID,mGcidString);
			mIntent.putExtra(Params.INTRO.GAME_ISINSTALLED, mIsMarked);
			mIntent.putExtra(Params.INTRO.GAME_CODE,mGameCode);
			mIntent.putExtra(Params.INTRO.GAME_NAME,mGameName);
            mIntent.setClass(GameCircleActivity.this, SendGameCircleDynamicActivity.class);
            startActivity(mIntent);
            break;
        default:
            break;
        }

    }
    
    Handler mHandler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case IS_CAN_SEND:
				if (msg.arg1 == 0) {
					mSendDynamicIV.setClickable(false);
				} else {
					mSendDynamicIV.setClickable(true);
				}
				break;
			case SHOW_GAME_NAME:
				mGameName = (String) msg.obj;
				mGameNameTV.setText(mGameName);
				break;
			default:
				break;
			}
    	};
    };

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}
}
