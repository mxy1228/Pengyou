package com.cyou.mrd.pengyou.utils;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.log.CYLog;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.net.RequestListener;

public class WeiboApi {
	private final static String APPKEY = "3443142555";
	private final static String URL = "http://123.126.49.182/weibocallback.html";
	private static Weibo mWeibo;
	private final String TOKEN = "access_token";
	private final String EXPIRES_IN = "expires_in";
	private final String UID = "uid";
	private CYLog log = CYLog.getInstance();
	private static WeiboApi weiboApi;
	public static final String MID = "mid";// 消息ID
	public static final int EXPIRES_IN_DEFAULT = 1000;

	private WeiboApi() {

	}

	public static WeiboApi getInstance() {
		if (weiboApi == null) {
			weiboApi = new WeiboApi();
		}
		return weiboApi;
	}

	/**
	 * 
	 * @param text
	 *            要发布的微博文本内容，内容不超过140个汉字 若picUrl==null 或者 picFile==null 则默认为纯文字
	 * @param picFile
	 *            若为网络照片 则传网络地址 图片的URL地址，必须以http开头 若有本地和网络图片 优先网络图片
	 * @param picPath
	 *            若为本地照片 则传本地文件 仅支持JPEG、GIF、PNG格式，图片大小小于5M。
	 * @param weiboApiListener
	 */
	public void share2Weibo(Context mContext, String text, String pic,
			File picFile, WeiboApiListener weiboApiListener) {
		if (!isBindSina()) {// 证明未登陆
			Toast.makeText(mContext,
					mContext.getString(R.string.unbind_sina_text), 1).show();
			mWeibo = Weibo.getInstance(APPKEY, URL);
			mWeibo.authorize(mContext, new AuthorizeListener(text, pic,
					picFile, weiboApiListener));
		} else {
			shareWeibo(text, pic, picFile, weiboApiListener);
		}
	}

	/**
	 * 解除绑定微博
	 * 
	 * @return
	 */
	public void unBindSina(Context mContext) {
		 CookieSyncManager.createInstance(mContext);
		 CookieManager cookieManager = CookieManager.getInstance();
		 cookieManager.removeAllCookie();
		 SharedPreferenceUtil.clearSinaWeiboInfo();
	}

	/**
	 * 是否绑定了微博
	 * 
	 * @return
	 */
	public boolean isBindSina() {
		String token = SharedPreferenceUtil.getSinaWeiboToken();
		String userid = SharedPreferenceUtil.getSinaWeiboUid();
		if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(userid)) {
			return true;
		}
		return false;
	}

	public void bindSina(Context mContext, WeiboBindListener weiboBindListener) {
		mWeibo = Weibo.getInstance(APPKEY, URL);
		mWeibo.authorize(mContext, new AuthorizeListener(weiboBindListener));
	}

	class WeiboRequestListener implements RequestListener {
		WeiboApiListener weiboApiListener;

		public WeiboRequestListener(WeiboApiListener mweiboApiListener) {
			weiboApiListener = mweiboApiListener;
		}

		@Override
		public void onIOException(IOException arg0) {
			if (null != weiboApiListener) {
				//Log.e("weiboapi onIOException-------"," onIOException");
				weiboApiListener.onFaild(arg0);
				weiboApiListener.onSuccess(false);
			}
			if (null != arg0) {
				CYLog.getInstance().e(arg0);
			}
		}

		@Override
		public void onError(WeiboException arg0) {
			if (null != weiboApiListener) {
			//	Log.e("weiboapi onError-------"," onError");
				weiboApiListener.onFaild(arg0);
				weiboApiListener.onSuccess(false);
			}
			if (null != arg0) {
				CYLog.getInstance().e(arg0);
			}

		}

		@Override
		public void onComplete(String arg0) {
			if (null != weiboApiListener) {
				String midString = JsonUtils.getJsonValue(arg0, MID);// 根据返回json的
				if (!TextUtils.isEmpty(midString)) {
					try {
						long mid = Long.parseLong(midString);
						if (mid > 0) {
							weiboApiListener.onFinish(arg0);
							weiboApiListener.onSuccess(true);
						}
					} catch (Exception e) {
						CYLog.getInstance().e(e);
						weiboApiListener.onSuccess(false);
					}
				}
			}
		}

	}

	public void shareWeibo(String text, String picUrl, File picPath,
			final WeiboApiListener weiboApiListener) {
		StatusesAPI api = new StatusesAPI(new Oauth2AccessToken(
				SharedPreferenceUtil.getSinaWeiboToken(),
				String.valueOf(EXPIRES_IN_DEFAULT)));
		if ((TextUtils.isEmpty(picUrl) || !picUrl.contains("http://"))
				&& (picPath == null || !picPath.exists())) {
			api.update(text, null, null, new WeiboRequestListener(
					weiboApiListener));
		} else if (!TextUtils.isEmpty(picUrl) && picUrl.contains("http://")) {// 上传网络图片和文字
			api.uploadUrlText(text, picUrl, null, null,
					new WeiboRequestListener(weiboApiListener));
		} else {
			if (null != picPath && picPath.exists()) {
				api.upload(text, picPath.getPath(), null, null,
						new WeiboRequestListener(weiboApiListener));
			}
		}
	}

	public interface WeiboBindListener {
		public void onBindSuccess();

		public void onBindFaild(Object exp);
	}

	public interface WeiboApiListener {
		public void onFinish(String arg0);// 返回微博的详细数据 json格式

		public void onFaild(Exception exp);

		public void onSuccess(boolean success);// 是否成功
	}

	class AuthorizeListener implements WeiboAuthListener {
		private String mContent;
		private String mPicPath;
		WeiboApiListener mWeiboApiListener;
		File mPicFile;
		WeiboBindListener weiboBindListener;

		public AuthorizeListener(WeiboBindListener mWeiboBindListener) {
			weiboBindListener = mWeiboBindListener;
		}

		public AuthorizeListener(String text, String pic, File picFile,
				final WeiboApiListener weiboApiListener) {
			mContent = text;
			mPicPath = pic;
			mWeiboApiListener = weiboApiListener;
			mPicFile = picFile;
		}

		@Override
		public void onCancel() {
			log.d("weibo->onCancel");
		}

		@Override
		public void onComplete(Bundle arg0) {
			try {
				String token = arg0.getString(TOKEN);
				String expires_in = arg0.getString(EXPIRES_IN);
				String uid = arg0.getString(UID);
				saveToken(token, expires_in, uid);
				if (!TextUtils.isEmpty(mContent) && null != mWeiboApiListener) {
					shareWeibo(mContent, mPicPath, mPicFile, mWeiboApiListener);
				}
				if (null != weiboBindListener) {
					weiboBindListener.onBindSuccess();
				}
			} catch (Exception e) {
				log.e(e);
			}

		}

		@Override
		public void onError(WeiboDialogError arg0) {
			log.d("weibo->onError");
			if (null != weiboBindListener) {
				weiboBindListener.onBindFaild(arg0);
			}
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			log.d("weibo->onWeiboException");
			if (null != weiboBindListener) {
				weiboBindListener.onBindFaild(arg0);
			}
		}

	}

	private void saveToken(String token, String expires_in, String uid) {
		try {
			log.d("weibo->save info " + token + "____" + expires_in + "__"
					+ uid);
			SharedPreferenceUtil.saveSinaWeiboInfo(uid, token, expires_in);
		} catch (Exception e) {
			log.e(e);
		}
	}

}
