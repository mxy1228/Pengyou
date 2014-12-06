package com.cyou.mrd.pengyou.log;

import java.util.Locale;

import android.text.TextUtils;

public class BehaviorInfo {
	/**
	 * 
	 * @param mBehaviorId
	 *            行为编号
	 * @param mBehaviorArgs
	 *            行为参数
	 */
	public BehaviorInfo(String mBehaviorId, String mBehaviorArgs) {
		behaviorId = mBehaviorId;
		behaviorArgs = mBehaviorArgs;
		getCountyId();
		getCounty();
	}

	private void getCountyId() {
//		countryId = Locale.getDefault().getCountry();
//		if (null == countryId || TextUtils.isEmpty(countryId)) {
//			countryId = Locale.getDefault().getLanguage();
//		}
//		if (null == countryId || TextUtils.isEmpty(countryId)) {
			countryId = "0";
//		}
	}

	private void getCounty() {
		country = Locale.getDefault().getDisplayCountry();
		if (null == country || TextUtils.isEmpty(country)) {
			country = Locale.getDefault().getDisplayLanguage();
		}
		if (null == country || TextUtils.isEmpty(country)) {
			country = "";
		}
	}

	// public String behavior;
	// public String gameId;
	// public String game;
	// public String userId;
	// public String clientId;
	// public String account;
	public String behaviorId;
	public String behaviorArgs;
	public String countryId;
	public String country;
}
