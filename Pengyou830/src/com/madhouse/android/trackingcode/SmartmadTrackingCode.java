package com.madhouse.android.trackingcode;

import android.content.Context;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * @Package:      com.madhouse.android.trackingcode
 * @ClassName:    SmartmadTrackingCode  
 * @Description:  Out Interface for developer,submit tracking-code and register the SmartmadTrackingCodeListener.
 *    
 */
public class SmartmadTrackingCode {	
	private static int status;
	private boolean isError;
	private SmartmadTrackingCodeListener listener;
	private final Handler mHandler = new Handler();
	private String aid;
	private String url;
	
	//The current class allowed unauthorized construction
	private SmartmadTrackingCode() {
		
	}
	//Static inner class 
	private static class SmartmadTrackingCodeHolder {
		private static SmartmadTrackingCode instance = new SmartmadTrackingCode();
	}
	
    /**
     *  Get SmartmadTrackingCode singleton instance.
	 *  @return     SmartmadTrackingCode
     */
	public static SmartmadTrackingCode getInstance() {
		return SmartmadTrackingCodeHolder.instance;
	}
	
    /**
     *  Register a callback to be invoked when tracking successful transmission.
	 *  @return      void
	 *  @param   listener SmartmadTrackingCodeListener.
     */
	public void setTrackingListener(SmartmadTrackingCodeListener listener) {
		this.listener = listener;
	}
    /**
     *  Use the context and advertisers ID and performs initialization of internal modules.
	 *  @return void
	 *  @param  context  a context object used  to access application assets .
	 *  @param aid advertisers applications promote id.
     */
	public void startTracking(final Context context, final String aid) {		
		isError = false;
		
		//check aid 
		if(aid==null||aid.trim().length()==0){
			SmartmadUtil.LOG("aid is null or length is zero.");
			return;
		}else{
			this.aid = aid;
		}
		
		//check permission,if refuse return
		if(!SmartmadUtil.checkPermission(context)){
			SmartmadUtil.LOG("lack of permission and refuse.");
			return;
		}	
		
		//notify 403 to developer and return
		if (SmartmadUtil.getStatus(context) == 403) {
			if(listener!=null)
				listener.onTrackingStatus(403); 
			return;
		}	
		
		//if statusCode equal 200 return
 		if (status == 200) {
			SmartmadUtil.LOG("in a life cycle,can't upload tracking code again.");
			return;
		}	 
		
		
		mHandler.post(new Runnable(){
			public void run() {

				if (!SmartmadUtil.isNetworkAvailable(context)) {
					if(listener!=null)
					listener.onTrackingStatus(400);
					return;
				}		
				WebView webview = new WebView(context);
				url = SmartmadUtil.getTrackingCodeUrl(context,webview,SmartmadTrackingCode.this.aid);
				
				webview.setWebViewClient(new WebViewClient(){							
					public void onPageFinished(WebView view, String url) {	
						switch(status){
						case 200:
							if(listener!=null){
								listener.onTrackingStatus(200); 
								SmartmadUtil.saveStatus(context, 1);
							}
							break;
						case 400:
							if(listener!=null){
								listener.onTrackingStatus(400); 
							}
							break;
						case 403:
							SmartmadUtil.saveStatus(context, 403);
							if(listener!=null){
								listener.onTrackingStatus(403); 
							}
							break;	
						default:
							if(!isError){
								isError = true;
								if(listener!=null){
									listener.onTrackingStatus(400); 
								} 
							}
						}								 		
					};
					
					@Override
					public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {	
						if(!isError){
							isError = true;
							if(listener!=null){
								listener.onTrackingStatus(400); 
							} 
						}
					}
				});
				webview.getSettings().setJavaScriptEnabled(true);
				webview.addJavascriptInterface(getInstance(), "TrackingCode"); 
				webview.loadUrl(url); 
			};
		}); 
	} 

	/*
     *  
     *  Event callback tracking status code.
	 *  @return      void
	 *  @param statusCode Tracking status code .
     */
 	 public void NCTrackingStatus(int statusCode) {
		this.status = statusCode;		
	 }  
}
