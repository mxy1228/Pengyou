package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.SearchHistoryAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.SearchBar;
import com.cyou.mrd.pengyou.widget.SearchBar.TextAndActionListsner;
import com.cyou.mrd.pengyou.widget.SearchTagView;
import com.cyou.mrd.pengyou.widget.SearchTagView.OnTextCheckedListener;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SearchActivity extends CYBaseActivity implements OnSharedPreferenceChangeListener {

	private ImageButton mBackBtn;
	private PullToRefreshListView mHistoryListView;
	private SearchHistoryAdapter mHistoryAdapter;
	private List<Map<String,Object>> mHistoryData;
	private String mKey = null;
	private SearchBar mSearchBar;
	private EditText mSearchEditText;
	private SearchTagView searchTagView;
	private String tid;
	private static String TAG="SearchActivity";
	private static final  int historyMaxCount=10;
	private ImageButton mDownloadImg;
	private TextView mDownloadCountTV;
	private boolean isTagchange=false;
	private boolean mHadRegistDownloadReceiver = false;
	private DownloadCountReceiver mDownloadReceiver;
	private String mBeforeKey="";
	private SharedPreferences mHistoryPreference;
	
	private WaitingDialog waitingDialog;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.search_game);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		//tid = getIntent().getStringExtra(Params.CLASSIFY_ID);
		mHistoryPreference = getSharedPreferences("game_search_history", 0);
		mHistoryPreference.registerOnSharedPreferenceChangeListener(this);
		initView();
		initEvent();
		initData();
		registerDownloads();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (mHadRegistDownloadReceiver && mDownloadReceiver != null) {
			unregisterReceiver(mDownloadReceiver);
		}
		mHistoryPreference.unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();
	}

	List<String> tagLst = new ArrayList<String>();

	protected void initData() {
		RequestParams params = new RequestParams();
		params.put("count", "10");
		MyHttpConnect.getInstance().post(HttpContants.NET.SEARCH_KEY, params,
				new AsyncHttpResponseHandler() {
				@Override
				public void onFailure(Throwable error, String content) {
					try {
						showNetErrorDialog(SearchActivity.this,new ReConnectListener() {							
							@Override
							public void onReconnect() {
								initData();
							}
						});
					} catch (Exception e) {
						// TODO: handle exception
					}
				    log.e(content);
				    log.e(error.getMessage());
			   }
				
				@Override
				public void onLoginOut() {
					LoginOutDialog dialog = new LoginOutDialog(SearchActivity.this);
					dialog.create().show();
				}
			
				@Override
				public void onSuccess(String content) {
					try {
						super.onSuccess( content);
						JSONArray jsonArray = new JSONArray(JsonUtils
								.getJsonValue(content, "data"));
						if (jsonArray != null && jsonArray.length() != 0) {
							for (int i = 0; i < jsonArray.length(); i++) {
								String value = jsonArray.getString(i);
								tagLst.add(value);
							}
						}
						searchTagView.initData(tagLst);
						mHistoryListView.setEmptyView(searchTagView);
					} catch (JSONException e) {
						CYLog.getInstance().e(e);
					}
					super.onSuccess(content);
				}
				
				});
					
	}

	protected void initView() {
		View headerBar = findViewById(R.id.search_game_headerbar);
		
		((TextView) headerBar.findViewById(R.id.sub_header_bar_tv)).setText(R.string.search_game_title);
		mBackBtn = (ImageButton) headerBar.findViewById(R.id.sub_header_bar_left_ibtn);	
		mDownloadImg = (ImageButton) findViewById(R.id.sub_header_download_btn);
		mDownloadCountTV = (TextView) findViewById(R.id.header_download_count_tv);
		searchTagView = (SearchTagView) findViewById(R.id.search_tagview);
		mHistoryListView = (PullToRefreshListView) findViewById(R.id.search_history);
	
		mHistoryListView.setVisibility(View.GONE);
		mHistoryData = new ArrayList<Map<String,Object>>();
			
		if(mHistoryAdapter == null){
			mHistoryAdapter= new SearchHistoryAdapter(this,mHistoryData,false);
		}
		mHistoryListView.setAdapter(mHistoryAdapter);
		mHistoryListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if (mHistoryData != null && mHistoryData.size() > position) {
					String text=(String)mHistoryData.get(position).get("title");
					search(text);
				}
			}
		});

		this.mSearchBar = (SearchBar) findViewById(R.id.game_searchbar);
		mSearchEditText = mSearchBar.getEditText();
		mSearchEditText.setHint(R.string.game_name_default);
		mSearchBar.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*String buttonText = ((Button)v).getText().toString();
				if(buttonText.equals(getString(R.string.cancel))){
					mSearchEditText.setText("");
					mSearchEditText.clearFocus();
					hideInputMethod();
					mSearchBar.addCancelButton(false);
					mHistoryData.clear();
					mHistoryAdapter.updateData(mHistoryData,false,true);
					mHistoryAdapter.notifyDataSetChanged();	
				}else 
				if(buttonText.equals(getString(R.string.search))){*/
					String searchKey =mSearchEditText.getText().toString();
					if (!TextUtils.isEmpty(searchKey)) {
						search(searchKey);
					}else{
						showLongToast(R.string.game_search_nokey);
					}					
				///}
			}
		});
		
		//点击输入框
		mSearchEditText.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {  
		    	log.d("SearchBar--onFocusChange=="+hasFocus);
		    	if(hasFocus){ 
		    		mSearchBar.addCancelButton(true);
		    		if(mHistoryAdapter.mShowHistory){
		    			List<Map<String,Object>> mTempData = getSerchHistory();
			    		if(mTempData.size()==0){
			    			mSearchBar.getCancelButton().setText(R.string.search);
			    		}
			    		mHistoryAdapter.updateData(mTempData,false,true);
						mHistoryAdapter.notifyDataSetChanged();
						mHistoryListView.loadComplete();
		    		}    		
		    	}
		    }
		});
		
		this.mSearchBar.setTextAndActionListener(new TextAndActionListsner() {

			@Override
			public void onText() {
				String searchKey = mSearchEditText.getText().toString();
				if(mBeforeKey.equalsIgnoreCase(searchKey)){
					return;
				}
				mBeforeKey=searchKey;
				matchKeySearch(searchKey);
				showButtonText();		
			}

			@Override
			public void onEmpty() {
				log.i("onEmpty---"+mSearchEditText.isFocused());			
				if(!mSearchBar.getCancelButton().isShown()){
					mSearchEditText.setFocusable(true);
					mSearchEditText.setFocusableInTouchMode(true); 
					mSearchEditText.requestFocus();
					showInputMethod();
					mSearchBar.addCancelButton(true);
				}
				mHistoryAdapter.updateData(getSerchHistory(),false,true);
				mHistoryAdapter.notifyDataSetChanged();
				mHistoryListView.loadComplete();
				showButtonText();
			}

			@Override
			public void onAction(String key) {
				hideInputMethod();
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.SEARCHGAME.BTN_SEARCH_ID,
						CYSystemLogUtil.SEARCHGAME.BTN_SEARCH_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				if (TextUtils.isEmpty(key)) {
					showLongToast(R.string.game_search_nokey);
					return;
				}
				mKey = key;
				new Handler().postDelayed(new Runnable(){    
					public void run() {    
						search(mKey);   
					}    
				}, 200); 	
			}
		});		
	}
	
	@Override
	protected void initEvent() {
		mBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mDownloadImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.DOWNLOAD.BTN_DOWNLOAD_ID,
						CYSystemLogUtil.DOWNLOAD.BTN_DOWNLOAD_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				mIntent.setClass(SearchActivity.this, DownloadActivity.class);
				startActivity(mIntent);
			}
		});
		searchTagView.setOnTextCheckedListener(new OnTextCheckedListener() {
			@Override
			public void onTextChenked(String text) {
				isTagchange=true;
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.SEARCHGAME.BTN_RECOMMEND_ID,
						CYSystemLogUtil.SEARCHGAME.BTN_RECOMMEND_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				search(text);
			}
		});
		
	}
	
	//保存搜索记录
	private void saveSerchHistory(String keyword){
		if (TextUtils.isEmpty(keyword)) {
			return;
		}	
		int count = mHistoryPreference.getInt("count",0);
		
		SharedPreferences.Editor editor = mHistoryPreference.edit();
		for(int i=1;i<count+1;i++){
			if(mHistoryPreference.getString("history_"+i,"").equals(keyword)){
				for(int j=i;j<count;j++){
					editor.putString("history_"+j,mHistoryPreference.getString("history_"+(j+1),""));
				}
				editor.putString("history_"+count,keyword);
				editor.commit();
				return;
			}
		}
		if(count<historyMaxCount){	
			editor.putInt("count",count+1);
			editor.putString("history_"+(count+1),keyword);
		}
		else{
			for(int i=1;i<historyMaxCount;i++){	
				editor.putString("history_"+i,mHistoryPreference.getString("history_"+(i+1),""));
			}
			editor.putString("history_"+historyMaxCount,keyword);
		}
		editor.commit();
		
	}

	//获取搜索历史记录
	 private List<Map<String,Object>> getSerchHistory(){
		///SharedPreferences settings = getSharedPreferences("game_search_history", 0);
		int count = mHistoryPreference.getInt("count",0);
		mHistoryData.clear();
		for(int i=count;i>0;i--){
			Map<String,Object> map = new HashMap<String,Object>();
    		map.put("title", mHistoryPreference.getString("history_"+i,""));
    		mHistoryData.add(map);			
		}
		return mHistoryData;
	}
	 
	private void matchKeySearch(String keyword){
		
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(1));//
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		params.put("schkey", keyword);
	
		MyHttpConnect.getInstance().post(HttpContants.NET.SEARCH_MATCH_GAME, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						Log.d(TAG,"requestMyNearBy----"+"mLoading=onStart");
					}

					@Override
					public void onFailure(Throwable error, String content) {
						Log.d(TAG,"requestMyNearBy----"+"mLoading=onFailure");
					}

					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(SearchActivity.this);
						dialog.create().show();
					}
					
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						log.i("result = " + content);
						try {
							JSONArray jsonArray = new JSONArray(JsonUtils
									.getJsonValue(content, "data"));
							if (jsonArray != null && jsonArray.length() != 0) {
								mHistoryData.clear();
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject obj = jsonArray.getJSONObject(i);
									Map<String,Object> map = new HashMap<String,Object>();
									map.put("title", obj.getString("name"));
									map.put("icon", obj.getString("icon"));
						    		mHistoryData.add(map);
								}
								String searchKeyString=mSearchEditText.getText().toString();
								if(!TextUtils.isEmpty(searchKeyString)){ 
									mHistoryAdapter.updateData(mHistoryData,wifiConnected(),false);
									mHistoryAdapter.notifyDataSetChanged();
								}
								showButtonText();
							}

						} catch (JSONException e) {
							CYLog.getInstance().e(e);
						}
						super.onSuccess(content);
					} 	
			});						
	}
	
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if(mHistoryPreference.getInt("count",0) == 0){
        	mSearchBar.getCancelButton().setText(R.string.search);
        }
	}
	
	private void showButtonText() {
		/*if(mHistoryData.size()> 0){
			mSearchBar.getCancelButton().setText(R.string.cancel);
		}else {*/
			mSearchBar.getCancelButton().setText(R.string.search);
		///}
	}
	

	/**
	 * 搜索游戏
	 * 
	 * @param keyword
	 * @param currentPage
	 */
	private void search(String keyword) {
		mSearchEditText.clearFocus();
		saveSerchHistory(keyword);
		Intent mIntent = new Intent();
		mIntent.setClass(SearchActivity.this,GameSearchResultActivity.class);
		mIntent.putExtra("gamedetail_gamename", keyword);
		startActivity(mIntent);	
	}
	
	private void registerDownloads() {
		updateDownloadCount();
		if (mDownloadReceiver == null) {
			mDownloadReceiver = new DownloadCountReceiver();
		}
		if (mDownloadReceiver != null && !mHadRegistDownloadReceiver) {
			registerReceiver(mDownloadReceiver, new IntentFilter(
					Contants.ACTION.DOWNLOADING_COUNT));
			mHadRegistDownloadReceiver = true;
		}
	}
	
	private boolean wifiConnected(){
		ConnectivityManager connMgr =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);  
		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();  
		if (activeInfo != null && activeInfo.isConnected()) {  
			if(activeInfo.getType() == ConnectivityManager.TYPE_WIFI){
				return true;
			} 
		} 
		return false;  
	}
	
	public void updateDownloadCount() {
		Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.download_count);
		int count = DownloadDao.getInstance(this).getDownloadingTaskSize();
		if (count > 0) {
			mDownloadCountTV.setVisibility(View.VISIBLE);
			if (count <= 99) {
				mDownloadCountTV.setText(String.valueOf(count));
			} else {
				mDownloadCountTV.setText("N");
			}
			mDownloadCountTV.startAnimation(anim);
		} else {
			mDownloadCountTV.setVisibility(View.GONE);
			mDownloadCountTV.setText("");
		}
	}
	
	private class DownloadCountReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Animation anim = AnimationUtils.loadAnimation(context,
					R.anim.download_count);
			int count = DownloadDao.getInstance(SearchActivity.this).getDownloadingTaskSize();
			if (count > 0) {
				mDownloadCountTV.setVisibility(View.VISIBLE);
				if (count <= 99) {
					mDownloadCountTV.setText(String.valueOf(count));
				} else {
					mDownloadCountTV.setText("N");
				}
				mDownloadCountTV.startAnimation(anim);
			} else {
				mDownloadCountTV.setVisibility(View.GONE);
				mDownloadCountTV.setText("");
			}
		}
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	log.i("onActivityResult");
        /*if(!TextUtils.isEmpty(mSearchEditText.getText())) {
        	mSearchEditText.setFocusable(true);
			mSearchEditText.setFocusableInTouchMode(true); 
			mSearchEditText.requestFocus();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
			///imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			imm.showSoftInput(mSearchEditText, 0);

			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
        }*/
    }
    
	public void showLoadListProgressDialog() {
		waitingDialog = new WaitingDialog(this);
		waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		waitingDialog.setMessage(getResources().getString(R.string.list_loading));
		waitingDialog.setIndeterminate(false);
		waitingDialog.setCancelable(true);
		waitingDialog.show();
	}
	/**
	 * 隐藏滚动的对话框
	 */
	public void dismissProgressDialog() {
		if (waitingDialog != null && waitingDialog.isShowing()) {
			waitingDialog.dismiss();
		}
	}
	
    private InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void showInputMethod() {
        InputMethodManager imm = getInputMethodManager();
        if (imm != null) {
            imm.showSoftInput(mSearchBar.getEditText(), 0);
        }
    }

    public void hideInputMethod() {
        InputMethodManager imm = getInputMethodManager();
        if (imm != null) {
            imm.hideSoftInputFromWindow(mSearchBar.getEditText().getWindowToken(), 0);
        }
    }

}
