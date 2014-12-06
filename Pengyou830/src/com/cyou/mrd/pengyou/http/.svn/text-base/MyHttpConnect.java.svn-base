package com.cyou.mrd.pengyou.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MyHttpConnect extends AsyncHttpClient {
	private CYLog log = CYLog.getInstance();
	private static MyHttpConnect mConn;

	private MyHttpConnect() {
		// TODO
	}

	public static MyHttpConnect getInstance() {
		if (mConn == null) {
			mConn = new MyHttpConnect();
			mConn.setTimeout(20 * 1000);
		}
		return mConn;
	}

	/**
	 * 添加公共参数
	 * 
	 * @param params
	 */
	private void addDefaultParams(RequestParams params) {
		if (params == null) {
			return;
		}
		params.put(Params.HttpParams.UAUTH, UserInfoUtil.getUauth());
		params.put(Params.HttpParams.PLATFORM, Params.HttpParams.PLATFORM_VALUE);
		params.put(Params.HttpParams.UDID, Util.getUDIDNum());
		if(UserInfoUtil.getUToken() != null||("").equals(UserInfoUtil.getUToken())){
			params.put(Params.HttpParams.UTOKEN, UserInfoUtil.getUToken());
		}
	}

	public void addHeader(String key, String value) {
		super.addHeader(key, value);
	}

	public void get(String url, RequestParams params,
			AsyncHttpResponseHandler handler) {
		this.addDefaultParams(params);
		log.i("url = " + url);
		log.i("params = " + params.toString());
		super.get(url, params, handler);
	}

	public void post(String url, RequestParams params,
			AsyncHttpResponseHandler handler) {
		this.addDefaultParams(params);
		log.i("url = " + url);
		log.i("params = " + params.toString());
		super.post(url, params, handler);
	}

	public void post2Json(String url, RequestParams params,
			JSONAsyncHttpResponseHandler handler) {
		this.addDefaultParams(params);
		log.i("url = " + url);
		log.i("params = " + params.toString());
		super.post(url, params, handler);
	}

	/**
	 * 上传文件处理
	 * 
	 * @param actionUrl
	 * @param params
	 * @param photoLst
	 * @param audioLst
	 * @param videoLst
	 * @return
	 * @throws IOException
	 */
	public static String postAsMultipart(String actionUrl,
			Map<String, String> params, List<FormFile> fileLst)
			throws IOException {
		final String BOUNDARY = java.util.UUID.randomUUID().toString();
		final String PREFIX = "--";
		final String LINEND = "\r\n";
		final String MULTIPART_FROM_DATA = "multipart/form-data";
		final String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setConnectTimeout(60 * 1000 * 10);
		conn.setChunkedStreamingMode(5555);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		OutputStream outStream = null;
		BufferedInputStream resultInStream = null;
		ByteArrayOutputStream resultOutStream = null;
		try {
			outStream = conn.getOutputStream();
			StringBuilder textParam = new StringBuilder();

			for (Map.Entry<String, String> entry : params.entrySet()) {
				textParam.append(PREFIX);
				textParam.append(BOUNDARY);
				textParam.append(LINEND);
				textParam.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"" + LINEND);
				textParam.append("Content-Type: text/plain; charset=" + CHARSET
						+ LINEND);
				textParam.append("Content-Transfer-Encoding: 8bit" + LINEND);
				textParam.append(LINEND);
				textParam.append(entry.getValue());
				textParam.append(LINEND);
			}

			outStream.write(textParam.toString().getBytes());
			outStream.flush();

			if (null != fileLst && fileLst.size() > 0) {

				for (FormFile file : fileLst) {
					StringBuilder sb1 = new StringBuilder();
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINEND);
					sb1.append("Content-Disposition: form-data; name=\""
							+ file.getName() + "\"; filename=\""
							+ file.getFile().getName() + "\"" + LINEND);
					sb1.append("Content-Type: application/octet-stream; charset="
							+ CHARSET + LINEND);
					sb1.append(LINEND);
					outStream.write(sb1.toString().getBytes());
					outStream.flush();

					FileInputStream inStream = new FileInputStream(
							file.getFile());
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = inStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
						outStream.flush();
					}

					inStream.close();
					outStream.write(LINEND.getBytes());
					outStream.flush();
				}
			}

			byte[] endData = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(endData);
			outStream.flush();

			resultInStream = new BufferedInputStream(conn.getInputStream());
			resultOutStream = new ByteArrayOutputStream();

			byte[] resultBuf = new byte[1024];
			int resutReadLen = 0;
			while ((resutReadLen = resultInStream.read(resultBuf)) != -1) {
				resultOutStream.write(resultBuf, 0, resutReadLen);
				resultOutStream.flush();
			}

			byte[] result = resultOutStream.toByteArray();
			if (null == result || result.length == 0) {
				return null;
			}
			return new String(result, "UTF-8");
		} finally {
			if (outStream != null)
				try {
					outStream.close();
				} catch (Exception e) {
				}
			if (resultInStream != null)
				try {
					resultInStream.close();
				} catch (Exception e) {
				}
			if (resultOutStream != null)
				try {
					resultOutStream.close();
				} catch (Exception e) {
				}
			if (conn != null)
				try {
					conn.disconnect();
				} catch (Exception e) {
					e.fillInStackTrace();
				}
		}
	}

	public static class FormFile {
		private File file;
		private String name;

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
