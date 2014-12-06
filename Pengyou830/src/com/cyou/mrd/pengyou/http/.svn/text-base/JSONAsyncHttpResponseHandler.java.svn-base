package com.cyou.mrd.pengyou.http;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.cyou.mrd.pengyou.entity.RootPojo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

public class JSONAsyncHttpResponseHandler<T> extends JsonHttpResponseHandler {
	public static String success = "successful";
	public static String errorNo = "pageNo";
	public static String errorException = "errorException";
	public static String data = "data";
	private CYLog log = CYLog.getInstance();
	Class<T> t;
	public static final int RESULT_BOOEALN = 1;
	public static final int RESULT_LIST = 2;
	public static final int RESULT_BEAN = 3;
	private int resultType = 0;

	public JSONAsyncHttpResponseHandler() {
	}

	/**
	 * 传入分类数据的格式类型
	 * 
	 * @param resultType
	 * @param mt
	 */
	public JSONAsyncHttpResponseHandler(int mResultType, final Class<T> cls) {
		t = cls;
		resultType = mResultType;
	}

	public void onSuccessForList(List list) {
	}

	public void onSuccessForBoolean(boolean isSuccess) {

	}

	public void onSuccessRootPojo(RootPojo rootPojo) {

	}

	@Override
	protected void onLoginOut() {
		// TODO Auto-generated method stub
		super.onLoginOut();
	}

	public <T> void onSuccessForPojo(T o) {

	}

	public void onSuccessForString(String content) {

	}

	@Override
	public void onSuccess(int statusCode, JSONObject response) {
		log.d("result data is:" + response.toString());
		if (statusCode != 200 || response == null) {
			onSuccessRootPojo(null);
			return;
		}
		try {
			if (response.has("errorNo")) {
				// 发生了互斥登录
				if (response.getInt("errorNo") == 120) {
					onLoginOut();
					return;
				}
			}
		} catch (Exception e) {
			log.e(e);
			onFailure(e);
		}
		if (resultType == 0) {
			onSuccessForString(response.toString());
			return;
		}
		super.onSuccess(statusCode, response);
		switch (resultType) {
		case RESULT_BOOEALN:
			try {
				if (response.has(success)) {
					if (!TextUtils.isEmpty(response.getString(success))
							&& "1".equals(response.getString(success))) {
						onSuccessForBoolean(true);
					} else {
						onSuccessForBoolean(false);
					}
				} else {
					onSuccessForPojo(null);
				}
			} catch (Exception e) {
				onSuccessForBoolean(false);
				log.e(e);
			}
			break;
		case RESULT_BEAN:
			if (response.has(data)) {
				try {
					String dataValue = response.getString(data);
					T obj = (T) JsonUtils.fromJson(dataValue, t.getClass());
					onSuccessForPojo(obj);
				} catch (JSONException e) {
					log.e(e);
					onSuccessForPojo(null);
					onFailure(e);
				}
			} else {
				onSuccessForPojo(null);
			}
			break;
		case RESULT_LIST:
			onSuccessForString(response.toString());
			;
			if (response.has(data)) {
				try {
					// String dataValue = response.getString(data);
					List<T> list = new ArrayList<T>();
					JSONArray jsonArray = response.getJSONArray(data);
					for (int index = 0; index < jsonArray.length(); index++) {
						JSONObject jsonItemObject = jsonArray
								.getJSONObject(index);
						T stockBuyInfo = (T) JsonUtils.fromJson(
								jsonItemObject.toString(), t);
						list.add(stockBuyInfo);
					}
					onSuccessForList(list);
				} catch (Exception e) {
					onFailure(e);
					onSuccessForList(null);
					log.e(e);
				}
			} else {
				onSuccessForList(null);
			}
			break;
		default:
			onSuccessForString(response.toString());
			break;
		}
	}
}
