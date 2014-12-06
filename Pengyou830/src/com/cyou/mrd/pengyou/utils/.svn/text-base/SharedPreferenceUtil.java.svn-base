package com.cyou.mrd.pengyou.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.log.CYLog;

public class SharedPreferenceUtil {

	private static CYLog log = CYLog.getInstance();

	/**
	 * 保存通知开关
	 * 
	 * @param isopen
	 */
	public static void saveNotificationSwitch(boolean isopen) {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.SETTING,
						Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putBoolean(Params.SP_PARAMS.NOTIFICATION_SWITCH, isopen);
		e.commit();
	}

	/**
	 * 获取通知开关
	 * 
	 * @return
	 */
	public static boolean getNotificationSwitch() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.SETTING,
						Context.MODE_PRIVATE);
		return sp.getBoolean(Params.SP_PARAMS.NOTIFICATION_SWITCH, true);
	}

	// /**
	// * 保存用户忽略好友推荐的时间
	// */
	// public static void saveIgnoreRecommendFriendTime() {
	// SharedPreferences sp = CyouApplication.mAppContext
	// .getSharedPreferences(Contants.SP_NAME.SETTING,
	// Context.MODE_PRIVATE);
	// Editor e = sp.edit();
	// e.putLong(Params.SP_PARAMS.IGNORE_TIME, System.currentTimeMillis());
	// e.commit();
	// }
	//
	// /**
	// * 获取用户忽略好友推荐的时间
	// *
	// * @return
	// */
	// public static long getIgnoreRecommendFriendTime() {
	// SharedPreferences sp = CyouApplication.mAppContext
	// .getSharedPreferences(Contants.SP_NAME.SETTING,
	// Context.MODE_PRIVATE);
	// return sp.getLong(Params.SP_PARAMS.IGNORE_TIME, 0);
	// }

	/**
	 * 保存该设备最后一次登陆的手机号
	 * 
	 * @param telephoneNum
	 */
	public static void saveLastTelephone(String telephoneNum) {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.SETTING_TELEPHONE,
						Context.MODE_PRIVATE);
		Editor e = sp.edit();
		if (!TextUtils.isEmpty(telephoneNum)) {
			e.putString(Params.SP_PARAMS.KEY_TELEPHONE, telephoneNum);
		}
		e.commit();
	}

	/**
	 * 获取最后一次登陆的手机号
	 * 
	 * @return
	 */
	public static String getLastTelephone() {
		String telephoneNum = "";
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.SETTING_TELEPHONE,
						Context.MODE_PRIVATE);
		telephoneNum = sp.getString(Params.SP_PARAMS.KEY_TELEPHONE, "");
		return telephoneNum;
	}

	/**
	 * 保存个人中心的数据 若无数据 通过|进行替代 通过_进行分隔
	 * 
	 * @param myGame
	 * @param guessGame
	 */
	public static void savePersonalCenterData(String myGame, String guessGame) {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.PERSONAL_CENTER,
						Context.MODE_PRIVATE);
		Editor e = sp.edit();
		if (!TextUtils.isEmpty(myGame)) {
			e.putString(Params.SP_PARAMS.PERSONAL_CENTER_MYGAME, myGame);
		}
		if (!TextUtils.isEmpty(guessGame)) {
			e.putString(Params.SP_PARAMS.PERSONAL_CENTER_GUESSGAME, guessGame);
		}
		e.commit();
	}

	/**
	 * 获取个人信息数据-我的游戏
	 * 
	 * @return
	 */
	public static String getMyGameData() {
		String myGame = "";
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.PERSONAL_CENTER,
						Context.MODE_PRIVATE);
		myGame = sp.getString(Params.SP_PARAMS.PERSONAL_CENTER_MYGAME, "");
		return myGame;
	}

	/**
	 * 获取个人信息数据-猜你喜欢
	 * 
	 * @return
	 */
	public static String getGuessGameData() {
		String guessGame = "";
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.PERSONAL_CENTER,
						Context.MODE_PRIVATE);
		guessGame = sp
				.getString(Params.SP_PARAMS.PERSONAL_CENTER_GUESSGAME, "");
		return guessGame;
	}

	/**
	 * 更新用户签名已被编辑-计算个人信息完善度
	 */
	public static void upadateUserSign() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(Params.SP_PARAMS.KEY_HASUPDATE_SIGN, true);
		editor.commit();
	}

	/**
	 * 更新用户签名已被编辑-计算个人信息完善度
	 */
	public static void initUserSign() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(Params.SP_PARAMS.KEY_HASUPDATE_SIGN, false);
		editor.commit();
	}

	/**
	 * 判断是否更新过
	 * 
	 * @return
	 */
	public static boolean hasUserSignUpdate() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		return sp.getBoolean(Params.SP_PARAMS.KEY_HASUPDATE_SIGN, false);
	}

	/**
	 * 
	 * @param topData
	 *            顶部轮播图
	 * @param specialGame
	 *            游戏专题
	 * @param recommandData
	 *            精品推荐
	 * @param newData
	 *            最新
	 * @param hotData
	 *            最热
	 */
	public static void saveRecommendData(String topData, String specialGame,
			String recommandData, String newData, String hotData) {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.GAMESTORE_RECOMMEND,
						Context.MODE_PRIVATE);
		Editor e = sp.edit();
		if (!TextUtils.isEmpty(topData)) {
			e.putString(Params.SP_PARAMS.GAMESTORE_RECOMMAND_TOPADS, topData);
		}
		if (!TextUtils.isEmpty(specialGame)) {
			e.putString(Params.SP_PARAMS.GAMESTORE_SPECIAL, specialGame);
		}
		if (!TextUtils.isEmpty(recommandData)) {
			e.putString(Params.SP_PARAMS.GAMESTORE_RECOMMEND_LIST,
					recommandData);
		}
		if (!TextUtils.isEmpty(newData)) {
			e.putString(Params.SP_PARAMS.GAMESTORE_NEW_DATA, newData);
		}
		if (!TextUtils.isEmpty(hotData)) {
			e.putString(Params.SP_PARAMS.GAMESTORE_HOT_DATA, hotData);
		}

		e.commit();
	}

	/**
	 * 游戏库 推荐-顶部Ads数据
	 * 
	 * @return
	 */
	public static String getRecommendTopData() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.GAMESTORE_RECOMMEND,
						Context.MODE_PRIVATE);
		String topData = sp.getString(
				Params.SP_PARAMS.GAMESTORE_RECOMMAND_TOPADS, "");
		return topData;
	}

	/**
	 * 游戏库精品 推荐
	 * 
	 * @return
	 */
	public static String getRecommendData() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.GAMESTORE_RECOMMEND,
						Context.MODE_PRIVATE);
		String topData = sp.getString(
				Params.SP_PARAMS.GAMESTORE_RECOMMEND_LIST, "");
		return topData;
	}

	/**
	 * 游戏库 -最热
	 * 
	 * @return
	 */
	public static String getHotGameData() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.GAMESTORE_RECOMMEND,
						Context.MODE_PRIVATE);
		String guessGame = sp
				.getString(Params.SP_PARAMS.GAMESTORE_HOT_DATA, "");
		return guessGame;
	}

	/**
	 * 游戏库 最新
	 * 
	 * @return
	 */
	public static String getNewGameData() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.GAMESTORE_RECOMMEND,
						Context.MODE_PRIVATE);
		String guessGame = sp
				.getString(Params.SP_PARAMS.GAMESTORE_NEW_DATA, "");
		return guessGame;
	}

	/**
	 * 游戏库 最新
	 * 
	 * @return
	 */
	public static String getSpecialData() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.GAMESTORE_RECOMMEND,
						Context.MODE_PRIVATE);
		String guessGame = sp.getString(Params.SP_PARAMS.GAMESTORE_SPECIAL, "");
		return guessGame;
	}

	/**
	 * 游戏库 -分类
	 * 
	 * @param topData
	 * @param guessGame
	 * @param recommandData
	 */
	public static void saveClassifyData(String classify) {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.GAMESTORE_CLASSIFY,
						Context.MODE_PRIVATE);
		Editor e = sp.edit();
		if (!TextUtils.isEmpty(classify)) {
			e.putString(Params.SP_PARAMS.GAMESTORE_CLASSIFY, classify);
		}
		e.commit();
	}

	/**
	 * 游戏库 分类
	 * 
	 * @return
	 */
	public static String getClassifyData() {
		String classifyData = "";
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.GAMESTORE_CLASSIFY,
						Context.MODE_PRIVATE);
		classifyData = sp.getString(Params.SP_PARAMS.GAMESTORE_CLASSIFY, "");
		return classifyData;
	}

	/**
	 * 游戏库 -排行
	 * 
	 * @param topData
	 * @param guessGame
	 * @param recommandData
	 */
	public static void saveGameRankData(String friend, String world, String near) {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.GAMESTORE_RANK,
						Context.MODE_PRIVATE);
		Editor e = sp.edit();
		if (!TextUtils.isEmpty(friend)) {
			e.putString(Params.SP_PARAMS.GAMESTORE_RANK_FRIEND, friend);
		}
		if (!TextUtils.isEmpty(world)) {
			e.putString(Params.SP_PARAMS.GAMESTORE_RANK_WORLD, world);
		}
		if (!TextUtils.isEmpty(near)) {
			e.putString(Params.SP_PARAMS.GAMESTORE_RANK_NEAR, near);
		}
		e.commit();
	}

	/**
	 * 游戏库 排行榜-朋友榜
	 * 
	 * @return
	 */
	public static String getGameRankFriendData() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.GAMESTORE_RANK,
						Context.MODE_PRIVATE);
		String friend = sp
				.getString(Params.SP_PARAMS.GAMESTORE_RANK_FRIEND, "");
		return friend;
	}

	/**
	 * 游戏库 排行榜-世界榜
	 * 
	 * @return
	 */
	public static String getGameRankWorldData() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.GAMESTORE_RANK,
						Context.MODE_PRIVATE);
		String world = sp.getString(Params.SP_PARAMS.GAMESTORE_RANK_WORLD, "");
		return world;
	}

	/**
	 * 游戏库 排行榜-朋友榜
	 * 
	 * @return
	 */
	public static String getGameRankNearData() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.GAMESTORE_RANK,
						Context.MODE_PRIVATE);
		String near = sp.getString(Params.SP_PARAMS.GAMESTORE_RANK_NEAR, "");
		return near;
	}

	/**
	 * 游戏库 -猜你喜欢页面随机显示
	 * 
	 * @param guess
	 */
	public static void saveGuessYouLikeGame(String guess) {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(
						Contants.SP_NAME.GAMESTORE_GUESS_YOU_LIKE,
						Context.MODE_PRIVATE);
		Editor e = sp.edit();
		if (!TextUtils.isEmpty(guess)) {
			e.putString(Params.SP_PARAMS.GAMESTORE_GUESSYOULIKE_GAME, guess);
		}
		e.commit();
	}

	/**
	 * 游戏库 -猜你喜欢页面随机显示
	 * 
	 * @return
	 */
	public static String getGuessYouLikeGame() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(
						Contants.SP_NAME.GAMESTORE_GUESS_YOU_LIKE,
						Context.MODE_PRIVATE);
		String guess = sp.getString(
				Params.SP_PARAMS.GAMESTORE_GUESSYOULIKE_GAME, "");
		return guess;
	}

	/**
	 * 保存根路径
	 * 
	 * @param path
	 */
	public static void saveRootPath(Context context, String path) {
		if (!TextUtils.isEmpty(path)) {
			SharedPreferences sp = context.getSharedPreferences(
					Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
			Editor e = sp.edit();
			e.putString(Params.SP_PARAMS.ROOT_PATH, path);
			e.commit();
		}
	}

	/**
	 * 获取根路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getRootPath(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
		return sp.getString(Params.SP_PARAMS.ROOT_PATH, null);
	}

	/**
	 * 判断是否是第一次打开应用
	 * 
	 * @return
	 */
	public static boolean isFirstOpenApp() {
		boolean isFirstOpenApp = true;
		SharedPreferences sharedpreferences = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.ISFIRST_OPENAPP,
						Context.MODE_WORLD_READABLE);
		isFirstOpenApp = sharedpreferences.getBoolean("isFirst", true);
		return isFirstOpenApp;
	}

	public static void setHasOpenApp() {
		SharedPreferences sharedpreferences = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.ISFIRST_OPENAPP,
						Context.MODE_WORLD_READABLE);
		sharedpreferences.edit().putBoolean("isFirst", false).commit();
	}

	/**
	 * 保存系统推送消息
	 * 
	 * @param path
	 */
	// public static void saveSystemMsgInfo(SystemCountMsgItem item) {
	// if (item == null) {
	// return;
	// }
	// SharedPreferences sp = CyouApplication.mAppContext
	// .getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG,
	// Context.MODE_PRIVATE);
	// Editor e = sp.edit();
	// e.putInt(Params.SP_PARAMS.COUNTFOCUS, item.getCountfocus());
	// if (item.getNewprivates() > 0) {
	// e.putInt(Params.SP_PARAMS.NEWPRIVATES, item.getNewprivates());
	// }
	// e.putInt(Params.SP_PARAMS.NEWACTID, item.getNewactid());
	// e.putInt(Params.SP_PARAMS.NEWCOMMENTS, item.getNewcomments());
	// e.putInt(Params.SP_PARAMS.LASTUID, item.getLastuid());
	// e.putString(Params.SP_PARAMS.AVATAR, item.getAvatar());
	// // //获取关注,粉丝数 更新当前用户
	// if (item.getCountfans()>-1) {// 存储增加了多少粉丝,可能为负
	// e.putInt(Params.SP_PARAMS.COUNTFANS,item.getCountfans());
	// } else {
	// e.putInt(Params.SP_PARAMS.COUNTFANS, -1);
	// }
	// e.commit();
	// }

	/**
	 * 获取系统推送消息
	 * 
	 * @param context
	 * @return
	 */
	// public static SystemCountMsgItem getSystemMsgInfo() {
	// SharedPreferences sp = CyouApplication.mAppContext
	// .getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG,
	// Context.MODE_PRIVATE);
	// SystemCountMsgItem item = new SystemCountMsgItem();
	// item.setCountfocus(sp.getInt(Params.SP_PARAMS.COUNTFOCUS, 0));
	// item.setCountfans(sp.getInt(Params.SP_PARAMS.COUNTFANS, 0));
	// item.setNewactid(sp.getInt(Params.SP_PARAMS.NEWACTID, 0));
	// item.setNewprivates(sp.getInt(Params.SP_PARAMS.NEWPRIVATES, 0));
	// item.setNewcomments(sp.getInt(Params.SP_PARAMS.NEWCOMMENTS, 0));
	// item.setLastuid(sp.getInt(Params.SP_PARAMS.LASTUID, 0));
	// item.setAvatar(sp.getString(Params.SP_PARAMS.AVATAR, ""));
	// return item;
	// }

	/**
	 * 保存日志路径
	 * 
	 * @param path
	 */
	public static void saveLogPath(Context context, String path) {
		if (!TextUtils.isEmpty(path)) {
			SharedPreferences sp = context.getSharedPreferences(
					Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
			Editor e = sp.edit();
			e.putString(Params.SP_PARAMS.LOG_PATH, path);
			e.commit();
		}
	}

	/**
	 * 获取日志路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getLogPath(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
		return sp.getString(Params.SP_PARAMS.LOG_PATH, null);
	}

	/**
	 * 保存统计日志路径
	 * 
	 * @param path
	 */
	public static void saveBehaviorPath(String path) {
		if (!TextUtils.isEmpty(path)) {
			SharedPreferences sp = CyouApplication.mAppContext
					.getSharedPreferences(Contants.SP_NAME.SETTING,
							Context.MODE_PRIVATE);
			Editor e = sp.edit();
			e.putString(Params.SP_PARAMS.BEHAVIOR_PATH, path);
			e.commit();
		}
	}

	/**
	 * 统计日志路径
	 * 
	 * @return
	 */
	public static String getBehaviorPath() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.SETTING,
						Context.MODE_PRIVATE);
		return sp.getString(Params.SP_PARAMS.BEHAVIOR_PATH, null);
	}

	/**
	 * 保存APK路径
	 * 
	 * @param path
	 */
	public static void saveAPKPath(Context context, String path) {
		if (!TextUtils.isEmpty(path)) {
			SharedPreferences sp = context.getSharedPreferences(
					Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
			Editor e = sp.edit();
			e.putString(Params.SP_PARAMS.APK_PATH, path);
			e.commit();
		}
	}

	/**
	 * 获取日志路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getAPKPath(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
		return sp.getString(Params.SP_PARAMS.APK_PATH, null);
	}

	/**
	 * 保存IMG路径
	 * 
	 * @param path
	 */
	public static void saveImgPath(Context context, String path) {
		if (!TextUtils.isEmpty(path)) {
			SharedPreferences sp = context.getSharedPreferences(
					Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
			Editor e = sp.edit();
			e.putString(Params.SP_PARAMS.IMG_PATH, path);
			e.commit();
		}
	}

	/**
	 * 获取IMG路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getImgPath(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
		return sp.getString(Params.SP_PARAMS.IMG_PATH, null);
	}


	// /**
	// * 获取密码
	// *
	// * @return
	// */
	// public static String getUserPassword() {
	// log.d("getUserPassword");
	// SharedPreferences sp = CyouApplication.mAppContext
	// .getSharedPreferences(Contants.SP_NAME.USER_INFO,
	// Context.MODE_PRIVATE);
	// String tempValue = sp.getString(Params.SP_PARAMS.KEY_PASSWORD, null);
	// return tempValue;
	// }

	/**
	 * 清除新浪微博
	 */
	public static void clearSinaWeiboInfo() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(Params.SP_PARAMS.TOKEN, "");
		editor.putString(Params.SP_PARAMS.UID, "");
		editor.putString(Params.SP_PARAMS.EXPIRES_IN, "");
		editor.commit();
	}

	/**
	 * 保存新浪微博相关信息
	 * 
	 * @param uid
	 * @param token
	 * @param expires
	 */
	public static void saveSinaWeiboInfo(String uid, String token,
			String expires) {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(Params.SP_PARAMS.TOKEN, token);
		editor.putString(Params.SP_PARAMS.UID, uid);
		editor.putString(Params.SP_PARAMS.EXPIRES_IN, expires);
		editor.commit();
	}

	public static String getSinaWeiboToken() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		String tempValue = sp.getString(Params.SP_PARAMS.TOKEN, null);
		return tempValue;
	}

	public static String getSinaWeiboUid() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.USER_INFO,
						Context.MODE_PRIVATE);
		String tempValue = sp.getString(Params.SP_PARAMS.UID, null);
		return tempValue;
	}

	/**
	 * 安装后第一次打开存储的标志位
	 */
	public static void saveInstallFirstOpen() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.SETTING,
						Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putBoolean(Params.SP_PARAMS.INSTALL_FIRST_OPEN, false);
		e.commit();
	}

	public static boolean isInstallFirstOpen() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.SETTING,
						Context.MODE_PRIVATE);
		return sp.getBoolean(Params.SP_PARAMS.INSTALL_FIRST_OPEN, true);
	}

	/**
	 * 保存ChatActivity当前状态
	 * 
	 * @param shown
	 * @param uid
	 */
	public static void saveChatActivityState(boolean shown, int uid) {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.LETTER_ABOUT,
						Context.MODE_APPEND);
		Editor e = sp.edit();
		e.putBoolean(Params.LETTER_SP_PARAMS.CHAT_ACTIVITY_SHOWN, shown);
		e.putInt(Params.LETTER_SP_PARAMS.CHAT_ACTIVITY_UID, uid);
		e.commit();
	}

	/**
	 * ChatActivity当前是否在显示
	 */
	public static boolean isChatActivityShown() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.LETTER_ABOUT,
						Context.MODE_APPEND);
		return sp
				.getBoolean(Params.LETTER_SP_PARAMS.CHAT_ACTIVITY_SHOWN, false);
	}

	/**
	 * 获取当前ChatActivity的UID
	 * 
	 * @return
	 */
	public static int getChatActivityUID() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.LETTER_ABOUT,
						Context.MODE_APPEND);
		return sp.getInt(Params.LETTER_SP_PARAMS.CHAT_ACTIVITY_UID, 0);
	}

	// 游戏圈 关系圈SP
	public static void saveGameID(Context context, int id) {
		SharedPreferences sp = context.getSharedPreferences(
				Contants.RELATIONSHIP_DATA.GAME, Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.GAME_ID, id);
		e.commit();
	}

	public static int getGameID() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.RELATIONSHIP_DATA.GAME,
						Context.MODE_PRIVATE);
		return sp.getInt(Params.SP_PARAMS.GAME_ID, 0);
	}

	public static void saveGameCount(Context context, int id) {
		SharedPreferences sp = context.getSharedPreferences(
				Contants.RELATIONSHIP_DATA.GAME, Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.GAME_COUNTS, id);
		e.commit();
	}

	public static int getGameCount() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.RELATIONSHIP_DATA.GAME,
						Context.MODE_PRIVATE);
		return sp.getInt(Params.SP_PARAMS.GAME_COUNTS, 0);
	}

	public static void saveRelationData(Context context, String data) {
		SharedPreferences sp = context.getSharedPreferences(
				Contants.RELATIONSHIP_DATA.RELATION, Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putString(Params.SP_PARAMS.REL_SHIP_DATA, data);
		e.commit();
	}

	public static String getRelationData() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.RELATIONSHIP_DATA.RELATION,
						Context.MODE_PRIVATE);
		return sp.getString(Params.SP_PARAMS.REL_SHIP_DATA, "");
	}

	/**
	 * 是否已经创建快捷方式
	 * 
	 * @return
	 */
	public static boolean hadCreateShortCut() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.SETTING,
						Context.MODE_PRIVATE);
		return sp.getBoolean(Params.SP_PARAMS.HAD_CREATE_SHORT_CUT, false);
	}

	/**
	 * 标记已经创建了快捷方式
	 * 
	 * @return
	 */
	public static void createShortCut() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.SETTING,
						Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putBoolean(Params.SP_PARAMS.HAD_CREATE_SHORT_CUT, true);
		e.commit();
	}

	// 判断是否需要与扫描游戏做同步
	public static boolean ifNeedWaitFilterGameDown() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(
						Contants.SP_NAME.NEED_WAIT_FILTERGAME_DOWN,
						Context.MODE_WORLD_READABLE);
		return sp
				.getBoolean(Params.SP_PARAMS.NEED_WAIT_FILTER_GAME_DOWN, false);
	}

	// 标记需要做与扫描游戏的同步
	public static void mustWaitFilterGameDown() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(
						Contants.SP_NAME.NEED_WAIT_FILTERGAME_DOWN,
						Context.MODE_WORLD_READABLE);
		sp.edit().putBoolean(Params.SP_PARAMS.NEED_WAIT_FILTER_GAME_DOWN, true)
				.commit();
	}

	// 标记已做与扫描游戏的同步
	public static void noNeedWaitFilterGameDown() {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(
						Contants.SP_NAME.NEED_WAIT_FILTERGAME_DOWN,
						Context.MODE_WORLD_READABLE);
		sp.edit()
				.putBoolean(Params.SP_PARAMS.NEED_WAIT_FILTER_GAME_DOWN, false)
				.commit();
	}

	/**
	 * 记录下载完成后签名冲突的文件的地址 tuozhonghua_zk 2013-11-14下午6:38:41
	 * 
	 * @param filePath
	 */
	public static void setDownloadedInstallFile(Context context, String filePath) {
		SharedPreferences sp = context.getSharedPreferences(
				Contants.SP_NAME.DOWNLOAD_INFO, Context.MODE_WORLD_READABLE);
		sp.edit()
				.putString(Params.SP_PARAMS.DOWNLOAD_INSTALL_FILE_PATH,
						filePath).commit();
	}

	/**
	 * 
	 * tuozhonghua_zk 2013-11-14下午6:45:52
	 * 
	 * @param context
	 * @return
	 */
	public static String getDownloadedInstallFile(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				Contants.SP_NAME.DOWNLOAD_INFO, Context.MODE_WORLD_READABLE);
		return sp.getString(Params.SP_PARAMS.DOWNLOAD_INSTALL_FILE_PATH, "");
	}
	/**
	 * 判断游戏库数据是否显示
	 * @return
	 */
	public static boolean checkIsShowDot(long currentTime) {
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.SETTING,
						Context.MODE_PRIVATE);
		long lastDate = sp
				.getLong(Params.SP_PARAMS.GAMESTORE_ISTODDY_DATA, 0);
		if (currentTime>lastDate) {
			return true;
		}
		return false;

	}
	/**
	 * 设置当前日期
	 */
	public static void setGamestoreDataTime(long time){
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.SETTING,
						Context.MODE_PRIVATE);
		Editor mEditor=sp.edit();
		mEditor.putLong(Params.SP_PARAMS.GAMESTORE_ISTODDY_DATA, time);
		mEditor.commit();
	}
}
