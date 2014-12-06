/*
 * Created by MadClient on 2013-1-24.
 * Copyright (c) 2006-2013 Madhouse Inc. All Rights Reserved. 
 */
package cn.optimad.trackingcode.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @ClassName: OptiMadTrackingCode
 * @Description: Main class for OptiMad tracking code
 * @author MadClient
 * @date 2013-1-24 下午10:13:23
 */
public class OptiMadTrackingCode {

	/*
	 * Member variables defined
	 */
	private static int status;
	private OptiMadTrackingCodeListener listener;
	private String sessionId = null;
	private long timestamp;
	private long mEffectiveInterval;
	
	/*
	 * Constructor Methods
	 */
	private OptiMadTrackingCode() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * Internal static class
	 */
	private static class OptiMadTrackingCodeHodler {
		private static OptiMadTrackingCode instance = new OptiMadTrackingCode();
	}

	/**
	 * Get OptiMadTrackingCode singleton instance.
	 * 
	 * @return OptiMadTrackingCode
	 */
	public static OptiMadTrackingCode getInstance() {
		return OptiMadTrackingCodeHodler.instance;
	}

	/**
	 * Register a listener for tracking code transmission status.
	 * 
	 * @param listener
	 *            OptiMadTrackingCodeListener handle
	 * @return void
	 */
	public void setTrackingListener(OptiMadTrackingCodeListener listener) {
		this.listener = listener;
	}

	/**
	 * Start tracking information.
	 * 
	 * @param context
	 *            a Context object used to access application assets
	 * @param aId
	 *            dvertisers applications promote id
	 * @return void
	 */
	public void startTracking(final Context context, final String aId) {
		
		// Check aId
		if (aId == null || aId.trim().length() == 0) {
			OptiMadUtil.LOG(OptiMadUtil.LOG_ERROR,
					"Invalid advertisers applications promote id!");
			if (listener != null)
				listener.onTrackingStatus(400);
			return;
		}
	
		// Check tracking code status
		if (OptiMadUtil.getStatus(context, OptiMadUtil.RMS_TRACKING_STATUS) == 403) {
			OptiMadUtil.LOG(OptiMadUtil.LOG_INFO,
					"Tracking features terminate service.");
			if (listener != null)
				listener.onTrackingStatus(403);
			return;
		}
		
		// Check permission
		if (!OptiMadUtil.checkPermission(context)) {
			OptiMadUtil.LOG(OptiMadUtil.LOG_ERROR,
					"No settings or missing permissions!");
			if (listener != null)
				listener.onTrackingStatus(400);
			return;
		}
		
		sessionId = null;
		// get session Id
		Intent intent = ((Activity)context).getIntent();
		if(intent != null) {
			Uri uri = intent.getData();
			if(uri != null) {
			    sessionId = uri.getQueryParameter("sid");
			    if(sessionId == null) {
			        sessionId = uri.getQueryParameter("SID");
			    }
			    if(sessionId!=null) {
			        intent.setData(null);
			        ((Activity)context).setIntent(null);
			    }
			}
		}
		
		if(sessionId!=null) {
		    timestamp = System.currentTimeMillis();
		}
		else {
		    if(timestamp == 0) {
		        timestamp = System.currentTimeMillis();
		    }
		    else {
		        if(System.currentTimeMillis() - timestamp<mEffectiveInterval) {
		            return;
		        }
		        timestamp = System.currentTimeMillis();
		    }
		}

		// Start thread
		new Handler().post(new Runnable() {

			public void run() {
				// Check network
				if (!OptiMadUtil.isNetworkAvailable(context)) {
					OptiMadUtil.LOG(OptiMadUtil.LOG_WARNING,
							"Currently no network.");
					if (listener != null)
						listener.onTrackingStatus(400);
					return;
				}

				// Generated request url and sent using webview
				WebView webview = new WebView(context);
				webview.getSettings().setJavaScriptEnabled(true);
				webview.addJavascriptInterface(getInstance(), "TrackingCode");
				
				String url = OptiMadUtil.buildRequestUrl(context, webview, aId, sessionId);
				webview.loadUrl(url);

				// Receive various notifications and requests
				webview.setWebViewClient(new WebViewClient() {

					@Override
					public void onPageFinished(WebView view, String url) {
						// Check status code when tracking information sent
						// successfully
						switch (OptiMadTrackingCode.status) {
						case 200:
							if (OptiMadUtil.getStatus(context,
									OptiMadUtil.RMS_ACTIVATE_STATUS) == 0) {
								OptiMadUtil
										.LOG(OptiMadUtil.LOG_INFO,
												"Application fist activate successfully.");
								OptiMadUtil.setStatus(context,
										OptiMadUtil.RMS_ACTIVATE_STATUS, 1);
							}
							OptiMadUtil.LOG(OptiMadUtil.LOG_INFO,
									"Tracking information sent successfully.");
							if (listener != null)
								listener.onTrackingStatus(200);
							break;
						case 400:
							OptiMadUtil.LOG(OptiMadUtil.LOG_WARNING,
									"Tracking information send failed.");
							if (listener != null)
								listener.onTrackingStatus(400);
							break;
						case 403:
							OptiMadUtil
									.LOG(OptiMadUtil.LOG_INFO,
											"Tracking features will terminate service.");
							OptiMadUtil.setStatus(context,
									OptiMadUtil.RMS_TRACKING_STATUS, 403);
							if (listener != null)
								listener.onTrackingStatus(403);
							break;
						}
						super.onPageFinished(view, url);
					}

					@Override
					public void onReceivedError(WebView view, int errorCode,
							String description, String failingUrl) {
						// Failed to send tracking information
						OptiMadUtil.LOG(OptiMadUtil.LOG_WARNING, "Tracking information send failed.");
						if (listener != null)
							listener.onTrackingStatus(400);
						super.onReceivedError(view, errorCode, description,
								failingUrl);
					}

				});
			}

		});
	}

	/**
	 * JavaScript callback methods
	 * 
	 * @param statusCode
	 *            tracking status code
	 * @return void
	 */
	public void NCTrackingStatus(int statusCode) {
		OptiMadTrackingCode.status = statusCode;
	}
	
	/**
     * JavaScript callback methods
     * 
     * @param statusCode
     *            tracking status code
     * @param effectiveInterval
     *            interval time
     * @return void
     */
	public void NCTrackingStatus(int statusCode, int effectiveInterval){
	    OptiMadTrackingCode.status = statusCode;
	    mEffectiveInterval = effectiveInterval*1000;
	}
	
}