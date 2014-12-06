package com.cyou.mrd.pengyou.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.LetterDBHelper;
import com.cyou.mrd.pengyou.db.LetterDao;
import com.cyou.mrd.pengyou.download.DownloadService;
import com.cyou.mrd.pengyou.entity.GameSimpleInfo;
import com.cyou.mrd.pengyou.entity.MyMessageItem;
import com.cyou.mrd.pengyou.entity.base.GameSimpleInfoBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.service.CoreService;
import com.cyou.mrd.pengyou.ui.ChatActivity;
import com.cyou.mrd.pengyou.ui.FriendInfoActivity;
import com.cyou.mrd.pengyou.ui.GameCircleDetailActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.viewcache.ChatViewCache;
import com.cyou.mrd.pengyou.widget.LetterPB;
import com.cyou.mrd.pengyou.widget.LetterPB.ResultListener;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ChatAdapter extends BaseAdapter implements OnClickListener{

	private CYLog log = CYLog.getInstance();
	
	private static final long THREE_MINUTE = 3 * 60 * 1000;
	private static final long ONE_DAY = 24 * 60 * 60 * 1000;
	
	private Context mContext;
	private List<MyMessageItem> mData;
	private LayoutInflater mInflater;
	private DisplayImageOptions mIconOptions;
	private DisplayImageOptions mAvatarOptions;
	private LetterDao mDao;
	private SimpleDateFormat mAFormat;
	private SimpleDateFormat mBFormat;
	private SimpleDateFormat mTodayFormat;
	private Handler mHandler;
	ClipboardManager cm;
	public ChatAdapter(Context context,List<MyMessageItem> data,Handler handler){
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater)mContext.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		this.mIconOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.icon_default)
		.showImageOnFail(R.drawable.icon_default)
		.showStubImage(R.drawable.icon_default)
		.build();
		this.mAvatarOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.avatar_defaul)
		.showImageOnFail(R.drawable.avatar_defaul)
		.showStubImage(R.drawable.avatar_defaul)
		.displayer(new RoundedBitmapDisplayer(Config.ROUND))
		.build();
		this.mDao = new LetterDao(mContext);
		this.mAFormat = new SimpleDateFormat("HH:mm");
		this.mBFormat = new SimpleDateFormat("MM-dd HH:mm");
		this.mTodayFormat = new SimpleDateFormat("yyyy-MM-dd");
		this.mHandler = handler;
		cm  =(ClipboardManager) context.getSystemService(DownloadService.CLIPBOARD_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChatViewCache viewCache = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.chat_item, null);
			viewCache = new ChatViewCache(convertView);
			convertView.setTag(viewCache);
		}else{
			viewCache = (ChatViewCache)convertView.getTag();
		}
		MyMessageItem item = mData.get(position);
		if(item != null){
			int myUid = UserInfoUtil.getCurrentUserId();
			if(position != 0){
				setDate(mData.get(position - 1),item, viewCache);
			}else{
				viewCache.getmDateTV().setVisibility(View.GONE);
			}
			if(item.getFrom() == myUid){
				viewCache.getmLetterPB().reset();
				viewCache.getmLetterPB().setTag(item);
				viewCache.getmLetterPB().setOnClickListener(this);
				viewCache.getmRightLL().setVisibility(View.VISIBLE);
				viewCache.getmLeftLL().setVisibility(View.GONE);
				viewCache.getmRightContentTV().setText(item.getContent());
				CYImageLoader.displayImg(UserInfoUtil.getCurrentUserPicture()
						, viewCache.getmRightAvatar().getImageView()
						, mAvatarOptions);
				viewCache.getmLetterPB().setResultListener(new ResultListener() {
					
					@Override
					public void onFailed(MyMessageItem i) {
						for(MyMessageItem temp : mData){
							if(temp.getCreatetime() == i.getCreatetime()){
//								temp.setSendSuccess(Contants.LETTER_SEND_SUCCESS.NO);
								temp.setSendState(LetterDBHelper.SEND_STATE.SEND_FAILED);
								mDao.updateSendResult(temp.getCreatetime(), LetterDBHelper.SEND_STATE.SEND_FAILED, temp.getTauid(), temp.getMsgseq());
								ChatAdapter.this.notifyDataSetChanged();
								break;
							}
						}
					}
				}, item);
				switch (item.getSendState()) {
				case LetterDBHelper.SEND_STATE.SENDING:
					viewCache.getmLetterPB().start();
					break;
				case LetterDBHelper.SEND_STATE.SEND_FAILED:
					viewCache.getmLetterPB().sendFailed();
					break;
				default:
					viewCache.getmLetterPB().sendSuccess();
					break;
				}
//				if(item.getSendSuccess() != null){
//					if(item.getSendSuccess().equals(Contants.LETTER_SEND_SUCCESS.YES)){
//						viewCache.getmLetterPB().sendSuccess();
//					}else if(item.getSendSuccess().equals(Contants.LETTER_SEND_SUCCESS.NO)){
//						viewCache.getmLetterPB().sendFailed();
//					}
//				}else{
//					viewCache.getmLetterPB().start();
//				}
				switch (item.getType()) {
				case Contants.CHAT_TYPE.GAME:
					viewCache.getmRightGameRL().setVisibility(View.VISIBLE);
					viewCache.getmRightGameRL().setTag(item);
					viewCache.getmRightGameRL().setOnClickListener(this);
					if(item.getGamename() == null){
						getGameInfo(item);
					}else{
						viewCache.getmRightGameNameTV().setText(item.getGamename());
						CYImageLoader.displayIconImage(item.getIcon(), 
								viewCache.getmRightIconIV()
								, mIconOptions);
						viewCache.getmRightRB().setRating(item.getRating());
					}
					break;
				case Contants.CHAT_TYPE.TEXT:
					viewCache.getmRightGameRL().setVisibility(View.GONE);
					break;
				default:
					break;
				}
			}else{
				viewCache.getmRightLL().setVisibility(View.GONE);
				viewCache.getmLeftLL().setVisibility(View.VISIBLE);
				viewCache.getmLeftContentTV().setText(item.getContent());
				CYImageLoader.displayImg(item.getAvatar()
						, viewCache.getmLeftAvatar().getImageView()
						, mAvatarOptions);
				viewCache.getmLeftAvatar().setOnClickListener(this);
				viewCache.getmLeftAvatar().setTag(item);
				switch (item.getType()) {
				case Contants.CHAT_TYPE.GAME:
					viewCache.getmLeftGameRL().setVisibility(View.VISIBLE);
					viewCache.getmLeftGameRL().setTag(item);
					viewCache.getmLeftGameRL().setOnClickListener(this);
					if(item.getGamename() == null){
						getGameInfo(item);
					}else{
						viewCache.getmLeftGameNameTV().setText(item.getGamename());
						CYImageLoader.displayIconImage(item.getIcon(), 
								viewCache.getmLeftIconIV()
								, mIconOptions);
						viewCache.getmLeftRB().setRating(item.getRating());
					}
					break;
				case Contants.CHAT_TYPE.TEXT:
					viewCache.getmLeftGameRL().setVisibility(View.GONE);
					break;
				default:
					break;
				}
			}
		}
		viewCache.getmLeftContentTV().setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				String text=((TextView)v).getText().toString();
                 cm.setText(text);
                 Toast.makeText(mContext,mContext.getString(R.string.copy_text, text), 0).show();
                 return true;
				
			}
		});
		viewCache.getmRightContentTV().setOnLongClickListener(new OnLongClickListener() {
	
			@Override
			public boolean onLongClick(View v) {
				String text=((TextView)v).getText().toString();
		         cm.setText(text);
		         Toast.makeText(mContext,mContext.getString(R.string.copy_text, text), 0).show();
		         return true;
				
			}
		});
		return convertView;
	}
	
	private void getGameInfo(final MyMessageItem item){
		RequestParams params = new RequestParams();
		String str = new JSONArray().put(item.getGamecode()).toString();
		params.put("gidlist", str);
		MyHttpConnect.getInstance().post(HttpContants.NET.GET_GAME_SIMPLE_INFO, params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if(TextUtils.isEmpty(content)){
					return;
				}
				log.i("get game simple info result = "+content);
				try {
					GameSimpleInfoBase base = new ObjectMapper()
					.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.readValue(content, new TypeReference<GameSimpleInfoBase>() {
					});
					if(base != null){
						GameSimpleInfo info = base.getData().get(0);
						item.setIcon(info.getIcon());
						item.setGamename(info.getName());
						item.setRating(info.getStar());
						mDao.updateGameInfo(item);
						ChatAdapter.this.notifyDataSetChanged();
					}
				} catch (Exception e) {
					log.e(e);
				}
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(mContext);
				dialog.create().show();
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		final MyMessageItem item = (MyMessageItem)v.getTag();
		switch (v.getId()) {
		case R.id.chat_item_right_game_rl:
			intent.setClass(mContext, GameCircleDetailActivity.class);
			intent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
			intent.putExtra(Params.INTRO.GAME_NAME, item.getGamename());
			intent.putExtra(Params.INTRO.GAME_CIRCLE, false);
			mContext.startActivity(intent);
			break;
		case R.id.chat_item_left_game_rl:
			intent.setClass(mContext, GameCircleDetailActivity.class);
			intent.putExtra(Params.INTRO.GAME_NAME, item.getGamename());
			intent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
			intent.putExtra(Params.INTRO.GAME_CIRCLE, false);
			mContext.startActivity(intent);
			break;
		case R.id.chat_item_left_avatar_iv:
			intent.setClass(mContext, FriendInfoActivity.class);
			intent.putExtra(Params.FRIEND_INFO.UID, item.getFrom());
			intent.putExtra(Params.FRIEND_INFO.NICKNAME, item.getNickname());
			mContext.startActivity(intent);
			break;
		case R.id.chat_item_letter_pb:
			LetterPB pb = ((LetterPB)v);
			if(pb.isLoading()){
				return;
			}
			new AlertDialog.Builder(mContext)
			.setTitle(R.string.re_send_the_message)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Message msg = new Message();
					msg.obj = item;
					msg.what = ChatActivity.RE_SEND;
					mHandler.sendMessage(msg);
//					mData.remove(item);
//					mDao.deleteSingleLetter(item);
//					item.setCreatetime(System.currentTimeMillis());
//					item.setSendSuccess(null);
//					mData.add(item);
//					mDao.insert(item);
//					Message msg = new Message();
//					msg.what = ChatActivity.CHANGE_CURSOR;
//					msg.obj = item.getCreatetime();
//					mHandler.sendMessage(msg);
//					Intent intent = new Intent(mContext, CoreService.class);
//					intent.putExtra(Params.PUBLISH.ACTION, Contants.PUBLISH_ACTION.CHAT);
//					intent.putExtra(Params.CHAT.FROM, item.getTo());
//					intent.putExtra(Params.CHAT.MSG, item.getContent());
//					intent.putExtra(Params.CHAT.TAG, item.getType());
//					intent.putExtra(Params.CHAT.GAMECODE, item.getGamecode());
//					intent.putExtra(Params.CHAT.TIME, item.getCreatetime());
//					mContext.startService(intent);
//					//将该会话中所有消息标记为已读
//					mDao.markRead(item.getTo());
//					ChatAdapter.this.notifyDataSetChanged();
				}
			}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).create().show();
			break;
		default:
			break;
		}
	}
	
	private void setDate(MyMessageItem preItem,MyMessageItem item,ChatViewCache viewCache){
		long cur = item.getCreatetime();
		long pre = preItem.getCreatetime();
		long temp = cur - pre;
		if(temp <= THREE_MINUTE){
			viewCache.getmDateTV().setVisibility(View.GONE);
		}else{
			viewCache.getmDateTV().setVisibility(View.VISIBLE);
			Date now = new Date(System.currentTimeMillis());
			if(mTodayFormat.format(now).equals(mTodayFormat.format(new Date(cur)))){
				viewCache.getmDateTV().setText(mAFormat.format(new Date(cur)));
			}else{
				Calendar canlendar = Calendar.getInstance();
				long today = canlendar.getTime().getTime();
				long t = today - cur;
				if(t <= ONE_DAY && t > THREE_MINUTE){
					viewCache.getmDateTV().setText(mContext.getString(R.string.yesterday,mAFormat.format(new Date(cur))));
				}else{
					viewCache.getmDateTV().setText(mBFormat.format(new Date(cur)));
				}
			}
		}
	}
}
