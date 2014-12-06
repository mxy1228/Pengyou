package com.cyou.mrd.pengyou.utils;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.SNSBean;
import com.cyou.mrd.pengyou.entity.UserInfo;
import com.cyou.mrd.pengyou.log.CYLog;

public class UserInfoUtil {

	private static CYLog log = CYLog.getInstance();
	private static String uSpToken;
	private static String uMyToken;
	/**
	 * 获取当前用户头像
	 * @return
	 */
	public static String getCurrentUserPicture(){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		return sp.getString(Params.SP_PARAMS.KEY_PICORIG, null);
	}
	
	public static String getCurrentAreaId(){
		log.d("getCurrentAreaId");
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		return sp.getString(Params.SP_PARAMS.KEY_LOCAL, null);
	}
	
	/**
	 * 获取当前用户名
	 * @return
	 */
	public static String getCurrentUserNickname(){
		log.d("getCurrentUserNickname");
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		return sp.getString(Params.SP_PARAMS.KEY_NICKNAME, null);
	}
	
	/**
	 * 获取当前用户大头像
	 * @return
	 */
	public static String getCurrentPicOrig(){
		log.d("getCurrentPicOrig");
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		return sp.getString(Params.SP_PARAMS.KEY_PICORIG, null);
	}
	
	/**
	 * 获取当前用户性别
	 * @return
	 */
	public static int getCurrentUserGender(){
		log.d("getCurrentUserGender");
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		return sp.getInt(Params.SP_PARAMS.KEY_GENDER, 0);
	}
	
	/**
	 * 获取当前用户生日
	 * @return
	 */
	public static long getCurrentUserBirthday(){
		log.d("getCurrentUserBirthday");
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		return sp.getLong(Params.SP_PARAMS.KEY_BIRTHDAY, 0);
	}
	
	/**
	 * 获取当前粉丝数
	 * @return
	 */
	public static int getCurrentFansCount(){
		log.d("getCurrentFansCount");
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_DATA, Context.MODE_PRIVATE);
		return sp.getInt(Params.SP_PARAMS.KEY_FANS, 0);
	}
	
	/**
	 * 获取当前用户关注数
	 * @return
	 */
	public static int getCurrentFocusCount(){
		log.d("getCurrentFocusCount");
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_DATA, Context.MODE_PRIVATE);
		return sp.getInt(Params.SP_PARAMS.KEY_FOCUS, 0);
	}
	
	/**
	 * 获取当前用户动态数
	 * @return
	 */
	public static int getCurrentDynamicCount(){
		log.d("getCurrentDynamicCount");
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_DATA, Context.MODE_PRIVATE);
		return sp.getInt(Params.SP_PARAMS.KEY_ACTS, 0);
	}
	
	/**
	 * 用户关注数+1/-1
	 */
	public static void changeFocusCount(int i){
		log.d("changeFocusCount");
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_DATA, Context.MODE_PRIVATE);
		int pre = sp.getInt(Params.SP_PARAMS.KEY_FOCUS, 0);
		int cur = pre+=i;
		cur = cur <= 0 ? 0 : cur;
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.KEY_FOCUS, cur);
		e.commit();
	}
	
	/**
	 * 已发布动态数+1
	 */
	public static void changeDyanmicCount(int i){
		log.d("changeDyanmicCount");
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_DATA, Context.MODE_PRIVATE);
		int pre = sp.getInt(Params.SP_PARAMS.KEY_ACTS, 0);
		int cur = pre += i;
		cur = cur <= 0 ? 0 : cur;
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.KEY_ACTS, cur);
		e.commit();
	}
	
	/**
	 * 用户粉丝数+1/-1
	 * @param i
	 */
	public static void changeFansCount(int i){
		log.d("changeFansCount");
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_DATA, Context.MODE_PRIVATE);
		int pre = sp.getInt(Params.SP_PARAMS.KEY_FANS, 0);
		int cur = pre+=i;
		cur = cur <= 0 ? 0 : cur;
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.KEY_FANS, cur);
		e.commit();
	}
	
	/**
	 * 保存用户头像/大头像
	 * @param context
	 * @param picture
	 * @param pictureorig
	 */
	public static void saveUserPicture(Context context, String picture,
			String pictureorig) {
		SharedPreferences sp = context.getSharedPreferences(
				Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putString(Params.SP_PARAMS.KEY_AVATAR, picture);
		e.putString(Params.SP_PARAMS.KEY_PICORIG, pictureorig);
		e.commit();
	}
	
	/**
	 * 是否为游客
	 * 
	 * @return
	 */
	public static boolean isGuestUser() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		boolean isGuest = sp.getBoolean(Params.SP_PARAMS.KEY_ISGUEST, false);
		return isGuest;
	}
	
	/**
	 * 保存用户相关信息
	 * 
	 * @param userInfo
	 */
	public static void saveUserInfo(UserInfo userInfo) {
		log.d("saveUserInfo");
		if (userInfo == null) {
			return;
		}
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		SharedPreferences dataSP = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_DATA,
						Context.MODE_PRIVATE);
		Editor dataE = dataSP.edit();
		Editor e = sp.edit();
		e.putString(Params.SP_PARAMS.KEY_NICKNAME, userInfo.getNickname());// 昵称
		e.putLong(Params.SP_PARAMS.KEY_BIRTHDAY, userInfo.getBirthday());// 生日
		e.putInt(Params.SP_PARAMS.KEY_GENDER, userInfo.getGender());// 性别
		if (!TextUtils.isEmpty(userInfo.getPicture())) {// 头像
			e.putString(Params.SP_PARAMS.KEY_AVATAR, userInfo.getPicture());
		} else {
			e.putString(Params.SP_PARAMS.KEY_AVATAR, "");
		}
		if (!TextUtils.isEmpty(userInfo.getPictureorig())) {
			e.putString(Params.SP_PARAMS.KEY_PICORIG, userInfo.getPictureorig());
		} else {
			e.putString(Params.SP_PARAMS.KEY_PICORIG, "");
		}
		e.putString(Params.SP_PARAMS.KEY_FAVGAME, userInfo.getFavgame());// 最喜欢游戏类型
		e.putString(Params.SP_PARAMS.KEY_LOCAL, userInfo.getAreaid());// 地区
		e.putString(Params.SP_PARAMS.KEY_TAG, userInfo.getSignature());// 签名
		e.putInt(Params.SP_PARAMS.KEY_UID, userInfo.getUid());// 编号
		e.putInt(Params.SP_PARAMS.KEY_LEVEL, userInfo.getLevel());// 等级
		e.putInt(Params.SP_PARAMS.KEY_EXP, userInfo.getExp());// 经验值
		e.putInt(Params.SP_PARAMS.KEY_MAXLEVEL, userInfo.getExplimit());// 经验上限
		//----------------------------
		dataE.putInt(Params.SP_PARAMS.KEY_FOCUS, userInfo.getFocus());// 关注数
		dataE.putInt(Params.SP_PARAMS.KEY_FANS, userInfo.getFans());// 粉丝数
		dataE.putInt(Params.SP_PARAMS.KEY_ACTS, userInfo.getActs());// 动态数
		//----------------------------
		e.putString(Params.SP_PARAMS.KEY_PHONE, userInfo.getPhone());// 手机号
		e.putInt(Params.SP_PARAMS.KEY_GAMES, userInfo.getGames());// 手机号
		e.putInt(Params.SP_PARAMS.KEY_UNREAD_LETTER,
				userInfo.getUnreadChatLetter());
		e.putInt(Params.SP_PARAMS.KEY_UNREAD_RELATION_CRICLE_LETTER,
				userInfo.getUnreadRelationCircleLetter());
		e.putInt(Params.SP_PARAMS.KEY_AVAILABLE_SCORE, userInfo.getAvailablescore());
		e.putBoolean(Params.SP_PARAMS.KEY_IS_CAN_EXCHANGE_SCORE, userInfo.isCanexchangescore());
		e.commit();
		dataE.commit();
//		SharedPreferences isBindPhoneSP = CyouApplication.mAppContext
//				.getSharedPreferences(Contants.SP_NAME.BIND_PHONE,
//						Context.MODE_PRIVATE);
//		Editor bindPhoneE = isBindPhoneSP.edit();
//		bindPhoneE.putString(Params.SP_PARAMS.KEY_PHONE, userInfo.getPhone());
//		bindPhoneE.commit();
		// 保存信息到系统消息
		SharedPreferences systemMsgSP = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG,
						Context.MODE_PRIVATE);
		Editor sysE = systemMsgSP.edit();
		sysE.putInt(Params.SP_PARAMS.CURRENT_FANS_COUNT, userInfo.getFans());
		sysE.putInt(Params.SP_PARAMS.CURRENT_FOCUS_COUNT, userInfo.getFocus());
		sysE.commit();
		List<SNSBean> snsLst = userInfo.getSnsbindlist();
		if (null != snsLst && snsLst.size() > 0) {
			for (int i = 0; i < snsLst.size(); i++) {
				SNSBean temp = snsLst.get(i);
				String snstoken = temp.getSnstoken();// 暂时sns默认为新浪微博
				String snsUserid = temp.getSnsusrid();
				if (!TextUtils.isEmpty(snstoken)
						&& !TextUtils.isEmpty(snsUserid)
						&& "sina".equals(temp.getSnsname())) {
					SharedPreferenceUtil.saveSinaWeiboInfo(snsUserid, snstoken,
							String.valueOf(WeiboApi.EXPIRES_IN_DEFAULT));
				} else {
					SharedPreferenceUtil.clearSinaWeiboInfo();
				}
			}
		}
	}
	
	/**
	 * 保存用户相关信息
	 * 
	 * @param userInfo
	 */
	public static void saveUserID(int userID) {
		log.d("saveUserID");
		if (userID == 0) {
			return;
		}
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.KEY_UID, userID);
		e.commit();
	}
	
	/**
	 * 获取当前用户信息
	 * 
	 * @return
	 */
	public static synchronized UserInfo getCurrentUserInfo() {
		log.d("getCurrentUserInfo");
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		SharedPreferences dataSp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_DATA,
						Context.MODE_PRIVATE);
		UserInfo userInfo = new UserInfo();
//		String tempValue = "";
		userInfo.setUid(sp.getInt(Params.SP_PARAMS.KEY_UID, 0));
		userInfo.setPicture(sp.getString(Params.SP_PARAMS.KEY_AVATAR, null));
		userInfo.setBirthday(sp.getLong(Params.SP_PARAMS.KEY_BIRTHDAY, 0));
//		if (!TextUtils.isEmpty(tempValue)) {
			userInfo.setPhone(sp.getString(Params.SP_PARAMS.KEY_PHONE, null));
//		}
		userInfo.setNickname(sp.getString(
				Params.SP_PARAMS.KEY_NICKNAME,
				CyouApplication.mAppContext.getResources().getString(
						R.string.default_nickname)));
		// userInfo.setSchool(sp.getString(Params.SP_PARAMS.KEY_SCHOOL, ""));
		userInfo.setGender(sp.getInt(Params.SP_PARAMS.KEY_GENDER, 0));
		userInfo.setSignature(sp.getString(Params.SP_PARAMS.KEY_TAG, null));
		userInfo.setFavgame(sp.getString(Params.SP_PARAMS.KEY_FAVGAME, ""));
		userInfo.setAreaid(sp.getString(Params.SP_PARAMS.KEY_LOCAL, ""));
		userInfo.setLevel(sp.getInt(Params.SP_PARAMS.KEY_LEVEL, 0));
		userInfo.setExp(sp.getInt(Params.SP_PARAMS.KEY_EXP, 0));
		userInfo.setExplimit(sp.getInt(Params.SP_PARAMS.KEY_MAXLEVEL, 0));
		userInfo.setFocus(dataSp.getInt(Params.SP_PARAMS.KEY_FOCUS, 0));
		userInfo.setFans(dataSp.getInt(Params.SP_PARAMS.KEY_FANS, 0));
		userInfo.setActs(dataSp.getInt(Params.SP_PARAMS.KEY_ACTS, 0));
		// userInfo.setMsgs(sp.getInt(Params.SP_PARAMS.KEY_MSG, 0));
		userInfo.setGames(sp.getInt(Params.SP_PARAMS.KEY_GAMES, 0));
		userInfo.setPictureorig(sp.getString(Params.SP_PARAMS.KEY_PICORIG, ""));
		userInfo.setUnreadChatLetter(sp.getInt(
				Params.SP_PARAMS.KEY_UNREAD_LETTER, 0));
		userInfo.setUnreadRelationCircleLetter(sp.getInt(
				Params.SP_PARAMS.KEY_UNREAD_RELATION_CRICLE_LETTER, 0));
		userInfo.setAvailablescore(sp.getInt(
				Params.SP_PARAMS.KEY_AVAILABLE_SCORE, 0));
		userInfo.setCanexchangescore(sp.getBoolean(Params.SP_PARAMS.KEY_IS_CAN_EXCHANGE_SCORE, true));
		return userInfo;
	}
	
	/**
	 * 获取当前用户编号
	 * 
	 * @return
	 */
	public static int getCurrentUserId() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		int uid = sp.getInt(Params.SP_PARAMS.KEY_UID, 0);
		if (uid == 0) {
			return 0;
		}
		return uid;
	}

	/**
	 * 获取绑定手机号
	 * 
	 * @return
	 */
	public static String getPhoneNumber() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		String tempValue = sp.getString(Params.SP_PARAMS.KEY_PHONE, null);
		return tempValue;
	}

	public static void setPhoneNumber(String phoneNum) {
		log.d("setPhoneNumber");
		if (TextUtils.isEmpty(phoneNum)) {
			return;
		}
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(Params.SP_PARAMS.KEY_PHONE, phoneNum);
		editor.commit();
	}
	
	/**
	 * 获取当前用户头像的本地存储路径
	 * 
	 * @return 具体文件地址
	 */
	public static String getUserIconPath() {
		String userIconPath = SharedPreferenceUtil
				.getRootPath(CyouApplication.mAppContext)
				+ "/"
				+ getCurrentUserId() + Config.IMGTYPE;
		return userIconPath;
	}

	/**
	 * 保存用户验证字符串
	 * 
	 * @param uauth
	 */
	public static void setUauth(String uauth, boolean isGuest) {
		log.d("setUauth");
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(Params.SP_PARAMS.KEY_UAUTH, uauth);
		editor.putBoolean(Params.SP_PARAMS.KEY_ISGUEST, isGuest);
		editor.commit();
	}
	
	public static boolean setUToken(String utoken) {
		uMyToken = utoken;
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(Params.SP_PARAMS.KEY_UTOKEN, utoken);
		return editor.commit();
	}

	public static String getUToken() {

		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		if (("").equals(sp.getString(Params.SP_PARAMS.KEY_UTOKEN, null))) {
			return uMyToken ;
		} else {
			return sp.getString(Params.SP_PARAMS.KEY_UTOKEN, null);
		}

	}
	
	/**
	 * 得到验证串
	 * 
	 * @return
	 */
	public static String getUauth() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		String tempValue = sp.getString(Params.SP_PARAMS.KEY_UAUTH, null);
		log.d("getUauth"+tempValue);
		return tempValue;
	}
	
	/**
	 * 计算个人信息完善度 完成度满分为100，各个项目占比为：
	 * 绑定手机号：30%，头像：20%，昵称：20%，性别：20%，生日：5%，地区：5%，绑定第三方账号：0
	 * 
	 * @return
	 */
	public static float getPersonalInfoProgress() {
		log.d("getPersonalInfoProgress");
		float progress = 0;
//		UserInfo currentUserinfo = getCurrentUserInfo();
		if (!TextUtils.isEmpty(getPhoneNumber())) {
			progress += 3;
		}
		if (!TextUtils.isEmpty(getCurrentUserPicture())) {
			progress += 2;
		}
		if (!TextUtils.isEmpty(getCurrentUserNickname())) {
			progress += 2;
		}
		if (Contants.GENDER_TYPE.BOY == getCurrentUserGender()
				|| Contants.GENDER_TYPE.GIRL == getCurrentUserGender()) {
			progress += 2;
		}
		if (getCurrentUserBirthday() > 0) {
			progress += 0.5;
		}
		if (!TextUtils.isEmpty(getCurrentAreaId())) {
			progress += 0.5;
		}
		return progress;
	}
	
	/**
	 * 获取当前用户的个性签名
	 * @return
	 */
	public static String getCurrentUserTag(){
		log.d("getCurrentUserTag");
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		return sp.getString(Params.SP_PARAMS.KEY_TAG, null);
	}
	
	/**
	 * 是否绑定了手机
	 * @return
	 */
	public static boolean isBindPhone(){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		String phone = sp.getString(Params.SP_PARAMS.KEY_PHONE, null);
		log.i("phone = "+phone);
		return !TextUtils.isEmpty(phone);
	}
	
	/**
	 * 修改用户性别
	 */
	public static void setGender(int gender){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.KEY_GENDER, gender);
		e.commit();
	}
	
	/**
	 * 修改用户昵称
	 * @param nickname
	 */
	public static void setNickName(String nickname){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putString(Params.SP_PARAMS.KEY_NICKNAME, nickname);
		e.commit();
	}
	
	/**
	 * 获取游戏用户评分
	 * 
	 * @return
	 */
	public static int getGameScore(String gamecode) {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.GAME_STAR,
						Context.MODE_PRIVATE);
		int scoreValue = sp.getInt(gamecode, 0);
		return scoreValue;
	}
	
	/**
	 * 保存游戏用户评分
	 */
	public static void setGameScore(String gamecode, int isMarked){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.GAME_STAR, Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(gamecode, isMarked);
		e.commit();
	}
	
	/**
	 * 更新个人信息完成进度
	 * xumengyang
	 */
	public static void updateUserInfoProgress(TextView mProgressTV){
		float progress = UserInfoUtil.getPersonalInfoProgress();
		if (progress == 10) {
			if (getIsCanExchangeScore()) {
				mProgressTV
						.setBackgroundResource(R.drawable.avatar_coins);
				mProgressTV.setText("");
			} else {
				mProgressTV.setVisibility(View.GONE);
			}
		} else {
			mProgressTV.setVisibility(View.VISIBLE);
			mProgressTV.setText((int)(UserInfoUtil
					.getPersonalInfoProgress() * 10) + "%");
			if (progress < 3) {
				mProgressTV
						.setBackgroundResource(R.drawable.img_progress_10);
			} else if (progress < 5) {
				mProgressTV
						.setBackgroundResource(R.drawable.img_progress_30);
			} else if (progress < 7) {
				mProgressTV
						.setBackgroundResource(R.drawable.img_progress_50);
			} else if (progress < 9) {
				mProgressTV
						.setBackgroundResource(R.drawable.img_progress_70);
			} else if (progress < 10) {
				mProgressTV
						.setBackgroundResource(R.drawable.img_progress_90);
			}
		}
	}
	
	public static void updatePersonalCenterUserInfoProgress(TextView mProgressTV) {
		float progress = UserInfoUtil.getPersonalInfoProgress();
		if (progress == 10) {
			mProgressTV.setVisibility(View.GONE);
		} else {
			mProgressTV.setVisibility(View.VISIBLE);
			mProgressTV
					.setText((int) (UserInfoUtil.getPersonalInfoProgress() * 10)
							+ "%");
			if (progress < 3) {
				mProgressTV.setBackgroundResource(R.drawable.img_progress_10);
			} else if (progress < 5) {
				mProgressTV.setBackgroundResource(R.drawable.img_progress_30);
			} else if (progress < 7) {
				mProgressTV.setBackgroundResource(R.drawable.img_progress_50);
			} else if (progress < 9) {
				mProgressTV.setBackgroundResource(R.drawable.img_progress_70);
			} else if (progress < 10) {
				mProgressTV.setBackgroundResource(R.drawable.img_progress_90);
			}
		}
	}
	
	/**
	 * 获取电话号码
	 * @return
	 */
	public static String getCurrentUserPhoneNum(){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		return sp.getString(Params.SP_PARAMS.KEY_PHONE, null);
	}
	/**
	 * 获取手机绑定密码
	 * @return
	 */
	public static String getPassword(){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		return sp.getString(Params.SP_PARAMS.KEY_PASSWORD, "");
	}
	
	/**
	 * 获取当前用户积分
	 * @return
	 */
	public static int getAvailableScore(){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		log.i("Count 当前用户总积分 = " + sp.getInt(Params.SP_PARAMS.KEY_AVAILABLE_SCORE, 0));
		return sp.getInt(Params.SP_PARAMS.KEY_AVAILABLE_SCORE, 0);
	}

    //更新当前用户积分	
	public static void sumAvailableScore(int coins, int addOrSub) {
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		if(addOrSub == 1) {
			coins += sp.getInt(Params.SP_PARAMS.KEY_AVAILABLE_SCORE, 0);
		} else if(addOrSub == 0){
			coins -= sp.getInt(Params.SP_PARAMS.KEY_AVAILABLE_SCORE, 0);
		}
		Editor editor = sp.edit();
		editor.putInt(Params.SP_PARAMS.KEY_AVAILABLE_SCORE, coins);
		editor.commit();
	}
	
    //是否显示填写用户信息到100%可领100积分的信息
	public static void setIsCanExchangeScore(boolean ifshow) {
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(Params.SP_PARAMS.KEY_IS_CAN_EXCHANGE_SCORE, ifshow);
		editor.commit();
	}
    //是否显示填写用户信息到100%可领100积分的信息
	public static boolean getIsCanExchangeScore() {
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		return sp.getBoolean(Params.SP_PARAMS.KEY_IS_CAN_EXCHANGE_SCORE, true);
	}
	
	/**
	 * 获取当前用户积分
	 * @return
	 */
	public static int getDownloadGames(){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		log.i("Count 当前用户总积分 = " + sp.getInt(Params.SP_PARAMS.KEY_DOWNLOAD_GAMES, 0));
		return sp.getInt(Params.SP_PARAMS.KEY_DOWNLOAD_GAMES, 0);
	}

    //更新当前用户积分	
	public static void sumDownloadGames(int times) {
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		times += sp.getInt(Params.SP_PARAMS.KEY_DOWNLOAD_GAMES, 0);
		Editor editor = sp.edit();
		editor.putInt(Params.SP_PARAMS.KEY_DOWNLOAD_GAMES, times);
		editor.commit();
	}
    public static void setCMSPersonalInfoScore(int score) {        
        SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(Params.SP_PARAMS.KEY_CMS_PERSONAL_INFO_SCORE, score);
        editor.commit();
    }
    
    public static int getCMSPersonalInfoScore() {  
        SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
        return sp.getInt(Params.SP_PARAMS.KEY_CMS_PERSONAL_INFO_SCORE, 100);//如果CMS的配置获取失败，默认给100分
    }
    
    public static void setCMSShareSinaScore(int score) {        
        SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(Params.SP_PARAMS.KEY_CMS_SHARE_SINA_SCORE, score);
        editor.commit();
    }
    
    public static int getCMSShareSinaScore() {  
        SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
        return sp.getInt(Params.SP_PARAMS.KEY_CMS_SHARE_SINA_SCORE, 10);//如果CMS的配置获取失败，默认给10分
    }

}
