package com.cyou.mrd.pengyou.adapter;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.download.DownloadService;
import com.cyou.mrd.pengyou.entity.DynamicCommentItem;
import com.cyou.mrd.pengyou.entity.DynamicCommentReplyItem;
import com.cyou.mrd.pengyou.entity.DynamicItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.ui.FriendInfoActivity;
import com.cyou.mrd.pengyou.utils.RelationCircleCache;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class RelationshipBaseAdapter extends BaseAdapter{
	public CYLog log = CYLog.getInstance();	
	public Activity mContext;
	public List<DynamicItem> mData;
	public DisplayImageOptions mCaptrueOption;
	public DisplayImageOptions mAvatarOption;
	public DisplayImageOptions mIconOption;
	public LayoutInflater mInflater;
	public Handler mHandler;
	public boolean mSupporting = false;
	public Dialog mDialog;
	public LinearLayout.LayoutParams LP_FW = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	public boolean mSending = false;
	public RelationCircleCache  relCache;
	ClipboardManager cm;

	public RelationshipBaseAdapter(Activity context,List<DynamicItem> data,Handler handler){
		this.mContext = context;
		this.mData = data;
		this.mCaptrueOption = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.capture_default)
		.showImageOnFail(R.drawable.capture_default)
		.showStubImage(R.drawable.capture_default)
		.build();
		this.mAvatarOption = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.avatar_defaul)
		.showImageOnFail(R.drawable.avatar_defaul)
		.showStubImage(R.drawable.avatar_defaul)
		.build();
		this.mIconOption = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.icon_default)
		.showImageOnFail(R.drawable.icon_default)
		.showStubImage(R.drawable.icon_default)
		.displayer(new RoundedBitmapDisplayer(Config.ROUND))
		.build();
		this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mHandler = handler;
		this.relCache = new RelationCircleCache(mContext);
		this.cm  =(ClipboardManager) context.getSystemService(DownloadService.CLIPBOARD_SERVICE);
	
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		return null;
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
    
    public void showSelectorDelete(final DynamicCommentItem commentItem, final int pos,final int type) {
        try {
        	mDialog = new AlertDialog.Builder(mContext)
                    .setItems(
                            new CharSequence[] {
                            		mContext.getString(R.string.delete),
                            		mContext.getString(R.string.cancel) },
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    switch (which) {
                                    case 0:
//                                    	deleteComment(commentItem, pos,type);
                                    	Message msg = mHandler.obtainMessage();
                        				msg.what = 2;
                        				msg.arg1 = pos;
                        				msg.arg2 = type; 
                        				msg.obj = commentItem;
                        				mHandler.sendMessageDelayed(msg,100);
                                        break;
                                    case 1:
                                    	mDialog.dismiss();
                                        break;
                                    default:
                                        break;
                                    }
                                }
                            }).create();
        	mDialog.setTitle(null);
        	mDialog.setCanceledOnTouchOutside(true);
        	mDialog.show();
        } catch (Exception e) {
            log.e(e);
        }
    }

	public boolean  mSpanNameClick = false;
	public class  ClickSpan extends ClickableSpan implements OnClickListener{
	private int uid;
	public ClickSpan(int uid){
		this.uid = uid;
	}
		@Override
		public void onClick(View v) {
			log.i("ClickSpan uid: " + uid);
			mSpanNameClick = true;
			Intent iFans = new Intent(mContext,
					FriendInfoActivity.class);
			iFans.putExtra(Params.FANS.UID, uid);
			mContext.startActivity(iFans);
			
		}
		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setUnderlineText(false);
		}
		
	}
	public  OnClickListener ClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(mSpanNameClick){
				mSpanNameClick = false;
				return;
			}
		    DynamicCommentItem dyItem = (DynamicCommentItem)v.getTag(R.id.relation_circle_message_empty);
		    int pos = ((Integer) v
					.getTag(R.id.relation_circle_dot))
					.intValue();
		    int type = ((Integer) v
					.getTag(R.id.relation_circle_message_lv))
					.intValue();
		    log.i("ClickListener  pos: " + pos);
		    log.i("ClickListener  dyItem.getUid(): " + dyItem.getUid());
			if (dyItem.getUid() == UserInfoUtil
					.getCurrentUserId()) {
				if(mDialog != null && mDialog.isShowing()){
					return;
				}
				showSelectorDelete(dyItem,pos,type);
			} else {
				Message msg = mHandler.obtainMessage();
				DynamicCommentReplyItem reItem = new DynamicCommentReplyItem();
				reItem.setNickname(dyItem.getNickname());
				reItem.setUid(dyItem.getUid());

				msg.what = 1;
				msg.arg1 = pos;
				msg.arg2 = getCount(); 
				msg.obj = reItem;
				mHandler.sendMessageDelayed(msg,100);
			}
		}

	};

	public  SpannableString getSingleUser(String nickname, int uid, String content) {		
		StringBuilder sb = new StringBuilder();
		sb.append(nickname).append(":").append(content);
		SpannableString ssb = new SpannableString(sb.toString());
		ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#265FA5")), 0, nickname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), nickname.length(), sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ssb;
	}

	public  SpannableString getReplyUser(String nickname, int uid, String rename,
			int reuid, String content) {
		StringBuilder sb = new StringBuilder();
		sb.append(nickname).append("回复").append(rename).append(":").append(content);
		SpannableString ssb = new SpannableString(sb.toString());
		ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#265FA5")), 0, nickname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), nickname.length(), nickname.length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#265FA5")), nickname.length() + 2, nickname.length() + 2 + rename.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), nickname.length() + 2 + rename.length(), sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ssb;
	}

}

