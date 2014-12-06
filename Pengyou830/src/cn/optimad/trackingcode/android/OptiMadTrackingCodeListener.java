/*
 * Created by MadClient on 2013-1-24.
 * Copyright (c) 2006-2013 Madhouse Inc. All Rights Reserved. 
 */
package cn.optimad.trackingcode.android;

/**
 * @ClassName: OptiMadTrackingCodeListener
 * @Description: Listener interface for OptiMad tracking events.
 * @author Madclient
 * @date 2013-1-24 下午10:17:22
 */
public interface OptiMadTrackingCodeListener {
	/**
	 * Callback tracking status code to listener.
	 * 
	 * @param statusCode
	 *            Tracking status code.
	 * @return void
	 */
	public void onTrackingStatus(int statusCode);
}