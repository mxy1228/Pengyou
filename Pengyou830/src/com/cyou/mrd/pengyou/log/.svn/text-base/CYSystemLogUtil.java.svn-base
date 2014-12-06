package com.cyou.mrd.pengyou.log;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.TextUtils;

import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.StringUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;

public class CYSystemLogUtil {
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String DAY_FORMAT = "yyyy-MM-dd";
	public static final String ARGEENMENT_BUTTON = "button";
	public static final String ARGEENMENT_USER = "behavior";
	public static final String GAMEID = "6";
	public static final String GAMENAME = "pengyou_android";
	public static final String BEHAVIOR_OPENAPP = "应用打开时间";
	public static final String BEHAVIOR_CLOSEAPP = "应用关闭时间";
	public static final String BEHAVIOR_TIMES = "开启次数";
	public static final String BEHAVIOR_SYSTEMINFO = "系统信息";
	public static final int FILE_MAXNUM = 30;// 读取文件最大数
	public static final String GAME_DOWNLOAD = "游戏下载";
	public static final String TIMES = "1";
	boolean isInit = false;

	public static class ME {
		public static final String ID = "我";
		public static final String NAME = "我";// 模块名
		// 我-个人中心的模块按钮统计
		public static final String BTN_PERSONCENTER_ID = "我";
		public static final String BTN_PERSONCENTER_NAME = "个人中心";
		public static final String BTN_PERSONINFO_ID = "我";
		public static final String BTN_PERSONINFO_NAME = "个人信息";
		public static final String BTN_MY_ATTENTION_ID = "我";
		public static final String BTN_MY_ATTENTION_NAME = "关注列表";
		public static final String BTN_MY_FANS_ID = "我";
		public static final String BTN_MY_FANS_NAME = "粉丝列表";
		public static final String BTN_MY_DYNAMIC_ID = "我";
		public static final String BTN_MY_DYNAMIC_NAME = "我的动态";
		public static final String BTN_PRI_MSG_ID = "我";
		public static final String BTN_PRI_MSG_NAME = "消息";
		public static final String BTN_GAME_ITEM_ID = "我";
		public static final String BTN_GAME_ITEM_NAME = "游戏item";
		public static final String BTN_PLAY_GAME_ID = "我";
		public static final String BTN_PLAY_GAME_NAME = "启动";
		public static final String BTN_GAME_DETAIL_ID = "我";
		public static final String BTN_GAME_DETAIL_NAME = "详情";
		public static final String BTN_GAME_SHARE_ID = "我";
		public static final String BTN_GAME_SHARE_NAME = "分享";
		public static final String BTN_GAME_UPDATE_ID = "我";
		public static final String BTN_GAME_UPDATE_NAME = "更新";
		public static final String BTN_GAME_ISPUBLIC_ID = "我";
		public static final String BTN_GAME_ISPUBLIC_NAME = "隐私";// 我的游戏-隐私设置
		public static final String BTN_GAME_GUESS_REFRESH_ID = "我";
		public static final String BTN_GAME_GUESS_REFRESH_NAME = "猜你喜欢刷新";//
		public static final String BTN_GAME_GUESS_ID = "我";
		public static final String BTN_GAME_GUESS_NAME = "猜你喜欢游戏icon";//
		public static final String BTN_GAME_GUESS_DETAIL_ID = "我";
		public static final String BTN_GAME_GUESS_DETAIL_NAME = "猜你喜欢详情";//
		public static final String BTN_GAME_GUESS_DOWNLOAD_ID = "我";
		public static final String BTN_GAME_GUESS_DOWNLOAD_NAME = "猜你喜欢下载";//

		// 我-个人中心-个人信息编辑按钮
		public static final String BTN_USERINFO_ICON_ID = "我";
		public static final String BTN_USERINFO_ICON_NAME = "头像";
		public static final String BTN_USERINFO_ICON_EDIT_ID = "我";
		public static final String BTN_USERINFO_ICON_EDIT_NAME = "头像编辑";// 我的游戏-隐私设置
		public static final String BTN_USERINFO_NICKNAME_ID = "我";
		public static final String BTN_USERINFO_NICKNAME_NAME = "昵称";//
		public static final String BTN_USERINFO_GENDER_ID = "我";
		public static final String BTN_USERINFO_GENDER_NAME = "性别";//
		public static final String BTN_USERINFO_BIRTHDAY_ID = "我";
		public static final String BTN_USERINFO_BIRTHDAY_NAME = "生日";//
		public static final String BTN_USERINFO_DCODE_ID = "我";
		public static final String BTN_USERINFO_DCODE_NAME = "二维码名片";//
		public static final String BTN_USERINFO_AREAID_ID = "我";
		public static final String BTN_USERINFO_AREAID_NAME = "地区";//
		public static final String BTN_USERINFO_GAMELIKE_ID = "我";
		public static final String BTN_USERINFO_GAMELIKE_NAME = "游戏偏好";//
		public static final String BTN_USERINFO_SIGN_ID = "我";
		public static final String BTN_USERINFO_SIGN_NAME = "个性签名";//

		// 我-个人中心-关注列表按钮
		public static final String BTN_MYATTENTION_SEARCH_ID = "我";
		public static final String BTN_MYATTENTION_SEARCH_NAME = "搜索";//
		public static final String BTN_MYATTENTION_PRIMSG_ID = "我";
		public static final String BTN_MYATTENTION_PRIMSG_NAME = "发私信";//
		public static final String BTN_MYATTENTION_DETAIL_ID = "我";
		public static final String BTN_MYATTENTION_DETAIL_NAME = "好友详情";//

		// 我-个人中心-粉丝列表
		public static final String BTN_MYFANS_SEARCH_ID = "我";
		public static final String BTN_MYFANS_SEARCH_NAME = "搜索";//
		public static final String BTN_MYFANS_PRIMSG_ID = "我";
		public static final String BTN_MYFANS_PRIMSG_NAME = "发私信";//
		public static final String BTN_MYFANS_ATT_ID = "我";
		public static final String BTN_MYFANS_ATT_NAME = "关注";//
		public static final String BTN_MYFANS_DETAIL_ID = "我";
		public static final String BTN_MYFANS_DETAIL_NAME = "好友详情";//

		// 我-个人中心-我的动态
		public static final String BTN_MYDYNAMIC_ASSIST_ID = "我";
		public static final String BTN_MYDYNAMIC_ASSIST_NAME = "赞";//
		public static final String BTN_MYDYNAMIC_GAMEDETIAL_ID = "我";
		public static final String BTN_MYDYNAMIC_GAMEDETIAL_NAME = "游戏详情";//
		public static final String BTN_MYDYNAMIC_COMMENT_ID = "我";
		public static final String BTN_MYDYNAMIC_COMMENT_NAME = "评论";//
		public static final String BTN_MYDYNAMIC_COMMENT_ALL_ID = "我";
		public static final String BTN_MYDYNAMIC_COMMENT_ALL_NAME = "更多评论";//
		public static final String BTN_MYDYNAMIC_COMMENT_PUBLISH_ID = "我";
		public static final String BTN_MYDYNAMIC_COMMENT_PUBLISH_NAME = "发表评论";//
		public static final String BTN_MYDYNAMIC_DELETE_ID = "我";
		public static final String BTN_MYDYNAMIC_DELETE_NAME = "删除";//

		// 我-个人中心-消息
		public static final String BTN_MYMSG_PRI_MSG_ID = "我";
		public static final String BTN_MYMSG_PRI_MSG_NAME = "私信";
		public static final String BTN_MYMSG_RELATION_ID = "我";
		public static final String BTN_MYMSG_RELATION_NAME = "关系圈消息";
		public static final String BTN_MYMSG_PUB_MSG_ID = "我";
		public static final String BTN_MYMSG_PUB_MSG_NAME = "官方消息";
		public static final String BTN_MYMSG_RE_MSG_ID = "我";
		public static final String BTN_MYMSG_RE_MSG_NAME = "回复私信";
		public static final String BTN_MYMSG_DYNAMIC_COMMENT_ID = "我";
		public static final String BTN_MYMSG_DYNAMIC_COMMENT_NAME = "查看动态评论";
		public static final String BTN_MYMSG_PUBMSG_DETAIL_ID = "我";
		public static final String BTN_MYMSG_PUBMSG_DETAIL_NAME = "官方消息详情";
		public static final String BTN_MYMSG_DELETE_ID = "我";
		public static final String BTN_MYMSG_DELETE_NAME = "删除消息";

		// 我-我的好友
		public static final String BTN_MYFRIEND_ID = "我";
		public static final String BTN_MYFRIEND_NAME = "我的好友";
		public static final String BTN_MYFRIEND_SEARCH_ID = "我";
		public static final String BTN_MYFRIEND_SEARCH_NAME = "搜索";
		public static final String BTN_MYFRIEND_ADD_ID = "我";
		public static final String BTN_MYFRIEND_ADD_NAME = "新的好友";
		public static final String BTN_MYFRIEND_LOSE_ID = "我";
		public static final String BTN_MYFRIEND_LOSE_NAME = "忽略";
		public static final String BTN_MYFRIEND_ATTENTION_ID = "我";
		public static final String BTN_MYFRIEND_ATTENTION_NAME = "关注";
		public static final String BTN_MYFRIEND_PRI_MSG_ID = "我";
		public static final String BTN_MYFRIEND_PRI_MSG_NAME = "发私信";
		public static final String BTN_MYFRIEND_DETAIL_ID = "我";
		public static final String BTN_MYFRIEND_DETAIL_NAME = "好友详情";
		public static final String BTN_MYFRIEND_RECOM_DETAIL_ID = "我";
		public static final String BTN_MYFRIEND_RECOM_DETAIL_NAME = "推荐好友详情";

		// 我-我的好友-新的好友
		public static final String BTN_MYFRIEND_NEW_ID = "我";
		public static final String BTN_MYFRIEND_NEW_NAME = "猜你认识";
		public static final String BTN_MYFRIEND_NEW_ATTENTION_ID = "我";
		public static final String BTN_MYFRIEND_NEW_ATTENTION_NAME = "猜你认识-关注";
		public static final String BTN_MYFRIEND_NEW_BIND_ID = "我";
		public static final String BTN_MYFRIEND_NEW_BIND_NAME = "猜你认识-绑定账号";
		public static final String BTN_MYFRIEND_NEW_FRD_DETAIL_ID = "我";
		public static final String BTN_MYFRIEND_NEW_FRD_DETAIL_NAME = "猜你认识-好友详情";
		public static final String BTN_MYFRIEND_NEW_TELE_ID = "我";
		public static final String BTN_MYFRIEND_NEW_TELE_NAME = "通讯录";
		public static final String BTN_MYFRIEND_NEW_TELE_SEARCH_ID = "我";
		public static final String BTN_MYFRIEND_NEW_TELE_SEARCH_NAME = "通讯录-搜索";
		public static final String BTN_MYFRIEND_NEW_TELE_ADDFRD_ID = "我";
		public static final String BTN_MYFRIEND_NEW_TELE_ADDFRD_NAME = "通讯录-邀请好友";
		public static final String BTN_MYFRIEND_NEW_SEARCH_ID = "我";
		public static final String BTN_MYFRIEND_NEW_SEARCH_NAME = "寻找好友";
		public static final String BTN_MYFRIEND_NEW_SEARCH_FRD_ID = "我";
		public static final String BTN_MYFRIEND_NEW_SEARCH_FRD_NAME = "寻找好友-搜好友";
		public static final String BTN_MYFRIEND_NEW_SEARCH_SFRD_ID = "我";
		public static final String BTN_MYFRIEND_NEW_SEARCH_SFRD_NAME = "寻找好友-搜索";
		public static final String BTN_MYFRIEND_NEW_SEARCH_SFRD_ATT_ID = "我";
		public static final String BTN_MYFRIEND_NEW_SEARCH_SFRD_ATT_NAME = "寻找好友-搜索关注";
		public static final String BTN_MYFRIEND_NEW_SEARCH_IMPORT_FRD_ID = "我";
		public static final String BTN_MYFRIEND_NEW_SEARCH_IMPORT_FRD_NAME = "寻找好友-导入好友";
		public static final String BTN_MYFRIEND_NEW_SEARCH_IMPORT_FRD_ATT_ID = "我";
		public static final String BTN_MYFRIEND_NEW_SEARCH_IMPORT_FRD_ATT_NAME = "寻找好友-导入好友关注";
		public static final String BTN_MYFRIEND_NEW_SEARCH_IMPORT_FRD_ATT_ALL_ID = "我";
		public static final String BTN_MYFRIEND_NEW_SEARCH_IMPORT_FRD_ATT_ALL_NAME = "寻找好友-导入好友全部关注";
		public static final String BTN_MYFRIEND_NEW_IMPORT_ID = "我";
		public static final String BTN_MYFRIEND_NEW_IMPORT_NAME = "寻找好友-导入好友邀请";
		public static final String BTN_MYFRIEND_NEW_DCODE_ID = "我";
		public static final String BTN_MYFRIEND_NEW_DCODE_NAME = "二维码";
		public static final String BTN_MYFRIEND_NEW_DCODE_SCAN_ID = "我";
		public static final String BTN_MYFRIEND_NEW_DCODE_SCAN_NAME = "扫描二维码";
		public static final String BTN_MYFRIEND_NEW_DCODE_ATT_ID = "我";
		public static final String BTN_MYFRIEND_NEW_DCODE_ATT_NAME = "二维码-关注";

		// 我-我的附近
		public static final String BTN_MYNEAR_ID = "我";
		public static final String BTN_MYNEAR_NAME = "我的附近";
		public static final String BTN_MYNEAR_ATT_ID = "我";
		public static final String BTN_MYNEAR_ATT_NAME = "关注";
		public static final String BTN_MYNEAR_DETAIL_ID = "我";
		public static final String BTN_MYNEAR_DETAIL_NAME = "好友详情";
	}

	public static class GAMESTORE {
		// 游戏库-推荐
	//	public static final String BTN_RECOMMEND_ID = "游戏库";
	//	public static final String BTN_RECOMMEND_NAME = "推荐";
		public static final String BTN_RECOMMEND_ADS_ID = "游戏库";
		public static final String BTN_RECOMMEND_ADS_NAME = "轮播图";
		public static final String BTN_RECOMMEND_GUESS_REFRESH_ID = "游戏库";
		public static final String BTN_RECOMMEND_GUESS_REFRESH_NAME = "猜你喜欢刷新";
		public static final String BTN_RECOMMEND_GUESS_ICON_ID = "游戏库";
		public static final String BTN_RECOMMEND_GUESS_ICON_NAME = "猜你喜欢-游戏icon";
		public static final String BTN_RECOMMEND_GUESS_DETAIL_ID = "游戏库";
		public static final String BTN_RECOMMEND_GUESS_DETAIL_NAME = "猜你喜欢游戏详情";
		public static final String BTN_RECOMMEND_GUESS_DOWNLOAD_ID = "游戏库";
		public static final String BTN_RECOMMEND_GUESS_DOWNLOAD_NAME = "猜你喜欢下载";
		public static final String BTN_RECOMMEND_GAME_DETAIL_ID = "游戏库";
		public static final String BTN_RECOMMEND_GAME_DETAIL_NAME = "游戏详情";
		public static final String BTN_RECOMMEND_GAME_DOWNLOAD_ID = "游戏库";
		public static final String BTN_RECOMMEND_GAME_DOWNLOAD_NAME = "下载";
		// 游戏库-分类
//		public static final String BTN_RECOMMEND_CLASSIFY_ID = "游戏库";
//		public static final String BTN_RECOMMEND_CLASSIFY_NAME = "分类大全";
//		public static final String BTN_RECOMMEND_CLASSIFY_ITEM_ID = "游戏库";
//		public static final String BTN_RECOMMEND_CLASSIFY_ITEM_NAME = "分类_分类名称"; // 需传值
//		public static final String BTN_RECOMMEND_CLASSIFY_HOT_ID = "游戏库";
//		public static final String BTN_RECOMMEND_CLASSIFY_HOT_NAME = "分类列表-热门";
//		public static final String BTN_RECOMMEND_CLASSIFY_NEW_ID = "游戏库";
//		public static final String BTN_RECOMMEND_CLASSIFY_NEW_NAME = "分类列表-最新";
//		public static final String BTN_RECOMMEND_CLASSIFY_GOOD_ID = "游戏库";
//		public static final String BTN_RECOMMEND_CLASSIFY_GOOD_NAME = "分类列表-好评";
//		public static final String BTN_RECOMMEND_CLASSIFY_DOWNLOAD_ID = "游戏库";
//		public static final String BTN_RECOMMEND_CLASSIFY_DOWNLOAD_NAME = "下载";
//		public static final String BTN_RECOMMEND_CLASSIFY_GAME_DETAIL_ID = "游戏库";
//		public static final String BTN_RECOMMEND_CLASSIFY_GAME_DETAIL_NAME = "游戏详情";
		// 游戏库-榜单
		public static final String BTN_RECOMMEND_RANK_ID = "游戏库";
		public static final String BTN_RECOMMEND_RANK_NAME = "榜单";
		public static final String BTN_RECOMMEND_RANK_FRIEND_ID = "游戏库";
		public static final String BTN_RECOMMEND_RANK_FRIEND_NAME = "朋友榜";
		public static final String BTN_RECOMMEND_RANK_WORLD_ID = "游戏库";
		public static final String BTN_RECOMMEND_RANK_WORLD_NAME = "世界榜";
		public static final String BTN_RECOMMEND_RANK_NEAR_ID = "游戏库";
		public static final String BTN_RECOMMEND_RANK_NEAR_NAME = "附近榜";
		public static final String BTN_RECOMMEND_RANK_FRIEND_DETAIL_ID = "游戏库";
		public static final String BTN_RECOMMEND_RANK_FRIEND_DETAIL_NAME = "朋友榜详细";
		public static final String BTN_RECOMMEND_RANK_FRIEND_DOWNLOAD_ID = "游戏库";
		public static final String BTN_RECOMMEND_RANK_FRIEND_DOWNLOAD_NAME = "朋友榜下载";
		public static final String BTN_RECOMMEND_RANK_WORLD_DETAIL_ID = "游戏库";
		public static final String BTN_RECOMMEND_RANK_WORLD_DETAIL_NAME = "世界榜详细";
		public static final String BTN_RECOMMEND_RANK_WORLD_DOWNLOAD_ID = "游戏库";
		public static final String BTN_RECOMMEND_RANK_WORLD_DOWNLOAD_NAME = "世界榜下载";
		public static final String BTN_RECOMMEND_RANK_NEAR_DETAIL_ID = "游戏库";
		public static final String BTN_RECOMMEND_RANK_NEAR_DETAIL_NAME = "附近榜详细";
		public static final String BTN_RECOMMEND_RANK_NEAR_DOWNLOAD_ID = "游戏库";
		public static final String BTN_RECOMMEND_RANK_NEAR_DOWNLOAD_NAME = "附近榜下载";
		//找游戏-推荐
		public static final String BTN_RECOMMEND_ID = "找游戏";
		public static final String BTN_RECOMMEND_NAME = "推荐";
		//找游戏-推荐-好友在玩
		public static final String BTN_RECOMMEND_FRIENDPLAYING_ID="找游戏-推荐";
	    public static final String BTN_RECOMMEND_FRIENDPLAYING_NAME="好友在玩";
	    public static final String BTN_RECOMMEND_FRIENDPLAYING_DOWNLOAD_ID="找游戏-推荐";
	    public static final String BTN_RECOMMEND_FRIENDPLAYING_DOWNLOAD_NAME="好友在玩下载";
	    public static final String BTN_RECOMMEND_FRIENDPLAYING_GAMEDETAIL_ID="找游戏-推荐";
	    public static final String BTN_RECOMMEND_FRIENDPLAYING_GAMEDETAIL_NAME="好友在玩-游戏详情";
	  //找游戏-推荐-附近在玩
	    public static final String BTN_RECOMMEND_NEARPLAYING_ID="找游戏-推荐";
	    public static final String BTN_RECOMMEND_NEARPLAYING_NAME="附近在玩";
	    public static final String BTN_RECOMMEND_NEARPLAYING_DOWNLOAD_ID="找游戏-推荐";
	    public static final String BTN_RECOMMEND_NEARPLAYING_DOWNLOAD_NAME="附近在玩下载";
	    public static final String BTN_RECOMMEND_NEARPLAYING_GAMEDETAIL_ID="找游戏-推荐";
	    public static final String BTN_RECOMMEND_NEARPLAYING_GAMEDETAIL_NAME="附近在玩-游戏详情";
	    //找游戏-推荐-分类
	    public static final String BTN_RECOMMEND_CLASSIFY_ID = "找游戏-推荐";
		public static final String BTN_RECOMMEND_CLASSIFY_NAME = "分类大全";
		public static final String BTN_RECOMMEND_CLASSIFY_ITEM_ID = "找游戏-推荐";
		public static final String BTN_RECOMMEND_CLASSIFY_ITEM_NAME = "分类_分类名称"; // 需传值
		public static final String BTN_RECOMMEND_CLASSIFY_HOT_ID = "找游戏-推荐";
		public static final String BTN_RECOMMEND_CLASSIFY_HOT_NAME = "分类列表-热门";
		public static final String BTN_RECOMMEND_CLASSIFY_NEW_ID = "找游戏-推荐";
		public static final String BTN_RECOMMEND_CLASSIFY_NEW_NAME = "分类列表-最新";
		public static final String BTN_RECOMMEND_CLASSIFY_GOOD_ID = "找游戏-推荐";
		public static final String BTN_RECOMMEND_CLASSIFY_GOOD_NAME = "分类列表-好评";
		public static final String BTN_RECOMMEND_CLASSIFY_DOWNLOAD_ID = "找游戏-推荐";
		public static final String BTN_RECOMMEND_CLASSIFY_DOWNLOAD_NAME = "分类-下载";
		public static final String BTN_RECOMMEND_CLASSIFY_GAME_DETAIL_ID = "找游戏-推荐";
		public static final String BTN_RECOMMEND_CLASSIFY_GAME_DETAIL_NAME = "分类-游戏详情";
		//找游戏-推荐-猜你喜欢
	    public static final String BTN_RECOMMEND_GUESSYOULIKE_ID="找游戏-推荐";
	    public static final String BTN_RECOMMEND_GUESSYOULIKE_NAME="猜你喜欢";
	    public static final String BTN_RECOMMEND_GUESSYOULIKE_DOWNLOAD_ID="找游戏-推荐";
	    public static final String BTN_RECOMMEND_GUESSYOULIKE_DOWNLOAD_NAME="猜你喜欢-下载";
	    public static final String BTN_RECOMMEND_GUESSYOULIKE_GAMEDETAIL_ID="找游戏-推荐";
	    public static final String BTN_RECOMMEND_GUESSYOULIKE_GAMEDETAIL_NAME="猜你喜欢-游戏详情";
	  //找游戏-精品推荐
//	    public static final String BTN_NICERECOMMEND_ID="找游戏";
//	    public static final String BTN_NICERECOMMEND_NAME="精品推荐";
	    //找游戏-精品推荐-更多精品
	    public static final String BTN_NICERECOMMEND_MOREGOODGAME_ID="找游戏";
	    public static final String BTN_NICERECOMMEND_MOREGOODGAME_NAME="精品推荐-更多精品";
	    public static final String BTN_NICERECOMMEND_MOREGOODGAME_GAMEDETAIL_ID="找游戏";
	    public static final String BTN_NICERECOMMEND_MOREGOODGAME_GAMEDETAIL_NAME="精品推荐-游戏详情";
	    
	  //找游戏-最新排行-更多精品	    
	    public static final String BTN_RECOMMEND_NEWRANK_MOREGAME_ID="找游戏";
	    public static final String BTN_RECOMMEND_NEWRANK_MOREGAME_NAME="最新排行-更多精品";
	    public static final String BTN_RECOMMEND_NEWRANK_MOREGAME_GAMEDETAIL_DOWNLOAD_ID="找游戏";
	    public static final String BTN_RECOMMEND_NEWRANK_MOREGAME_GAMEDETAIL_DOWNLOAD_NAME="最新排行-下载";
	    public static final String BTN_RECOMMEND_NEWRANK_MOREGAME_GAMEDETAIL_ID="找游戏";
	    public static final String BTN_RECOMMEND_NEWRANK_MOREGAME_GAMEDETAIL_NAME="最新排行-游戏详情";
	    //找游戏-最热游戏-更多精品
	    public static final String BTN_RECOMMEND_HOTRANK_MOREGAME_ID="找游戏";
	    public static final String BTN_RECOMMEND_HOTRANK_MOREGAME_NAME="最热游戏-更多精品";
	    public static final String BTN_RECOMMEND_HOTRANK_MOREGAME_GAMEDETAIL_DOWNLOAD_ID="找游戏";
	    public static final String BTN_RECOMMEND_HOTRANK_MOREGAME_GAMEDETAIL_DOWNLOAD_NAME="最热游戏-下载";
	    public static final String BTN_RECOMMEND_HOTRANK_MOREGAME_GAMEDETAIL_ID="找游戏";
	    public static final String BTN_RECOMMEND_HOTRANK_MOREGAME_GAMEDETAIL_NAME="最热游戏-游戏详情";
	    
	    
	
	}
	/**
	 * 
	 * 玩游戏
	 * 
	 */
	public static class PLAYGAME{
		//玩游戏-编辑个人信息
	    public static final String BTN_PLAYGAME_PERSONAL_ID="玩游戏";
	    public static final String BTN_PLAYGAME_PERSONAL_NAME="编辑个人信息";
	    
	    //玩游戏-启动下载管理
	    public static final String BTN_PLAYGAME_DOWNLOADMANAGER_ID="玩游戏";
	    public static final String BTN_PLAYGAME_DOWNLOADMANAGER_NAME="启动下载管理";
	}
	/**
	 * 
	 * 关系圈
	 * 
	 */
	public static class RELATION {

		// public static final String BTN_RELATION_ID = "76";
		// public static final String BTN_RELATION_NAME = "推荐";
		// 游戏库-推荐
		public static final String BTN_RELATION_REFRESH_ID = "关系圈";
		public static final String BTN_RELATION_REFRESH_NAME = "下拉刷新";
		public static final String BTN_RELATION_GAME_DETAIL_ID = "关系圈";
		public static final String BTN_RELATION_GAME_DETAIL_NAME = "游戏详情";
		public static final String BTN_RELATION_PIC_DETAIL_ID = "关系圈";
		public static final String BTN_RELATION_PIC_DETAIL_NAME = "图片详情";
		public static final String BTN_RELATION_COMMENT_ID = "关系圈";
		public static final String BTN_RELATION_COMMENT_NAME = "评论";
		public static final String BTN_RELATION_COMMENT_MORE_ID = "关系圈";
		public static final String BTN_RELATION_COMMENT_MORE_NAME = "更多评论";
		public static final String BTN_RELATION_STOP_ID = "关系圈";
		public static final String BTN_RELATION_STOP_NAME = "收起";
		public static final String BTN_RELATION_COMMENT_PUBLISH_ID = "关系圈";
		public static final String BTN_RELATION_COMMENT_PUBLISH_NAME = "发布评论";
		public static final String BTN_RELATION_TOP_ID = "关系圈";
		public static final String BTN_RELATION_TOP_NAME = "赞";
		public static final String BTN_RELATION_PIC_ID = "关系圈";
		public static final String BTN_RELATION_PIC_NAME = "用户头像";
	}

	// 更多
	public static class MORE {

		public static final String BTN_MORE_ID = "更多";
		public static final String BTN_MORE_NAME = "更多";

		public static final String BTN_MORE_FEED_ID = "更多";
		public static final String BTN_MORE_FEED_NAME = "意见反馈";
		public static final String BTN_MORE_NET_ID = "更多";
		public static final String BTN_MORE_NET_NAME = "2G/3G网络下禁止下载图片";
		public static final String BTN_MORE_SHARE_ID = "更多";
		public static final String BTN_MORE_SHARE_NAME = "应用分享";
		public static final String BTN_MORE_PUSH_ID = "更多";
		public static final String BTN_MORE_PUSH_NAME = "推送设置";
		public static final String BTN_MORE_CLEAR_ID = "更多";
		public static final String BTN_MORE_CLEAR_NAME = "清理缓存";
		public static final String BTN_MORE_CHECK_UPDATE_ID = "更多";
		public static final String BTN_MORE_CHECK_UPDATE_NAME = "查验更新";
		public static final String BTN_MORE_EXIT_ID = "更多";
		public static final String BTN_MORE_EXIT_NAME = "退出登录";

	}

	// 游戏详细
	public static class GAMEDETAIL {
		public static final String BTN_SLIDE_ID = "游戏详情";
		public static final String BTN_SLIDE_NAME = "滑动好友在玩";
		public static final String BTN_FRDDETAIL_ID = "游戏详情";
		public static final String BTN_FRDDETAIL_NAME = "好友详情";
		public static final String BTN_PIC_ID = "游戏详情";
		public static final String BTN_PIC_NAME = "游戏截图";
		public static final String BTN_DETAIL_ID = "游戏详情";
		public static final String BTN_DETAIL_NAME = "游戏介绍";
		public static final String BTN_RATE_ID = "游戏详情";
		public static final String BTN_RATE_NAME = "评分";
		public static final String BTN_COMMENT_ID = "游戏详情";
		public static final String BTN_COMMENT_NAME = "评论";
		public static final String BTN_COMMENT_MORE_ID = "游戏详情";
		public static final String BTN_COMMENT_MORE_NAME = "更多评论";
		public static final String BTN_COMMENT_GONE_ID = "游戏详情";
		public static final String BTN_COMMENT_GONE_NAME = "收起评论";
		public static final String BTN_COMMENT_RE_ID = "游戏详情";
		public static final String BTN_COMMENT_RE_NAME = "回复评论";
		public static final String BTN_COMMENT_RE_MORE_ID = "游戏详情";
		public static final String BTN_COMMENT_RE_MORE_NAME = "更多回复评论";
		public static final String BTN_COMMENT_RE_MORE_GONE_ID = "游戏详情";
		public static final String BTN_COMMENT_RE_MORE_GONE_NAME = "收起更多回复评论";
		public static final String BTN_COMMENT_RE_PUBLISH_ID = "游戏详情";
		public static final String BTN_COMMENT_RE_PUBLISH_NAME = "发布回复";
		public static final String BTN_YOULIKE_REFRESH_ID = "游戏详情";
		public static final String BTN_YOULIKE_REFRESH_NAME = "猜你喜欢刷新";
		public static final String BTN_YOULIKE_ICON_ID = "游戏详情";
		public static final String BTN_YOULIKE_ICON_NAME = "猜你喜欢游戏icon";
		public static final String BTN_YOULIKE_DETAIL_ID = "游戏详情";
		public static final String BTN_YOULIKE_DETAIL_NAME = "猜你喜欢详情";
		public static final String BTN_YOULIKE_DOWNLOAD_ID = "游戏详情";
		public static final String BTN_YOULIKE_DOWNLOAD_NAME = "猜你喜欢下载";
		public static final String BTN_DOWNLOAD_ID = "游戏详情";
		public static final String BTN_DOWNLOAD_NAME = "下载";
		public static final String BTN_SHARE_ID = "游戏详情";
		public static final String BTN_SHARE_NAME = "分享";
		public static final String BTN_FAV_ID = "游戏详情";
		public static final String BTN_FAV_NAME = "收藏";

	}

	// 好友详细
	public static class FRIENDDETAIL {
		public static final String BTN_ATT_ID = "用户详情";
		public static final String BTN_ATT_NAME = "关注";
		public static final String BTN_PRIMSG_ID = "用户详情";
		public static final String BTN_PRIMSG_NAME = "发私信";
		public static final String BTN_ATT_LIST_ID = "用户详情";
		public static final String BTN_ATT_LIST_NAME = "关注列表";
		public static final String BTN_FANS_LIST_ID = "用户详情";
		public static final String BTN_FANS_LIST_NAME = "粉丝列表";
		public static final String BTN_SLIDE_ID = "用户详情";
		public static final String BTN_SLIDE_NAME = "玩过的游戏翻页滑动";
		public static final String BTN_ID = "用户详情";
		public static final String BTN_NAME = "玩过游戏详情";
		public static final String BTN_DYNAMIC_DETAIL_ID = "用户详情";
		public static final String BTN_DYNAMIC_DETAIL_NAME = "他的动态-游戏详情";
		public static final String BTN_DYNAMIC_PIC_ID = "用户详情";
		public static final String BTN_DYNAMIC_PIC_NAME = "他的动态-图片浏览";
		public static final String BTN_DYNAMIC_TOP_ID = "用户详情";
		public static final String BTN_DYNAMIC_TOP_NAME = "他的动态-赞";
		public static final String BTN_DYNAMIC_COMMENT_ID = "用户详情";
		public static final String BTN_DYNAMIC_COMMENT_NAME = "他的动态-评论";
		public static final String BTN_DYNAMIC_COMMENT_MORE_ID = "用户详情";
		public static final String BTN_DYNAMIC_COMMENT_MORE_NAME = "他的动态-更多评论";
		public static final String BTN_DYNAMIC_COMMENT_PUBLISH_ID = "用户详情";
		public static final String BTN_DYNAMIC_COMMENT_PUBLISH_NAME = "他的动态-发布评论";
		public static final String BTN_DYNAMIC_GONE_ID = "用户详情";
		public static final String BTN_DYNAMIC_GONE_NAME = "他的动态-收起";
		public static final String BTN_GONE_RELATION_ID = "用户详情";
		public static final String BTN_GONE_RELATION_NAME = "屏蔽关系圈动态";
		public static final String BTN_SHOW_RELATION_ID = "用户详情";
		public static final String BTN_SHOW_RELATION_NAME = "启动关系圈动态";
		public static final String BTN_GONE_PRIMSG_ID = "用户详情";
		public static final String BTN_GONE_PRIMSG_NAME = "屏蔽私信";
		public static final String BTN_SHOW_PRIMSG_ID = "用户详情";
		public static final String BTN_SHOW_PRIMSG_NAME = "启动私信";
		public static final String BTN_MAINPAGE_ID = "用户详情";
		public static final String BTN_MAINPAGE_NAME = "回到首页";

	}

	// 搜索
	public static class SEARCHGAME {

		public static final String BTN_SEARCH_ID = "搜索";
		public static final String BTN_SEARCH_NAME = "搜索游戏次数";

		public static final String BTN_DETAIL_ID = "搜索";
		public static final String BTN_DETAIL_NAME = "搜索结果游戏详情";
		public static final String BTN_DOWNLOAD_ID = "搜索";
		public static final String BTN_DOWNLOAD_NAME = "搜索结果游戏下载";

		public static final String BTN_RECOMMEND_ID = "搜索";
		public static final String BTN_RECOMMEND_NAME = "点击推荐";

	}

	// 下载管理
	public static class DOWNLOAD {

		public static final String BTN_DOWNLOAD_ID = "下载管理";
		public static final String BTN_DOWNLOAD_NAME = "下载管理按钮";

		public static final String BTN_PAUSE_ID = "下载管理";
		public static final String BTN_PAUSE_NAME = "暂停";
		public static final String BTN_BEGIN_ID = "下载管理";
		public static final String BTN_BEGIN_NAME = "开始";

		public static final String BTN_DELETE_ID = "下载管理";
		public static final String BTN_DELETE_NAME = "删除";

	}

	private static void init() {
		File filePath = new File(SharedPreferenceUtil.getBehaviorPath());
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

	}

	/**
	 * 用户行为信息记录
	 * 
	 * @param registerInfo
	 */
	public synchronized static void behaviorLog(final BehaviorInfo behaviorInfo) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// init();
					String fileName = getTimerFileName(ARGEENMENT_USER);
					File path = new File(SharedPreferenceUtil.getBehaviorPath());
					if (null != path && !path.exists()) {
						path.mkdirs();
					}
					File file = new File(
							SharedPreferenceUtil.getBehaviorPath(), fileName);
					boolean isNewFile = true;
					if (!file.exists()) {
						isNewFile = true;
						file.createNewFile();
					} else {
						isNewFile = false;
					}
					FileWriter filewriter = new FileWriter(file, true);
					String currentTime = getCurrentTimeByFormat(TIME_FORMAT);
//					if (!isNewFile) {
						filewriter.write("\n");
//					}
					String phoneNum = UserInfoUtil.getPhoneNumber();
					if (TextUtils.isEmpty(phoneNum)) {
						phoneNum = "0";
					}
					int userId = UserInfoUtil.getCurrentUserId();
					filewriter.write(currentTime + "," + ARGEENMENT_USER + ","
							+ GAMEID + "," + GAMENAME + "," + userId + ","
							+ Util.getPhoneImei() + "," + phoneNum + ","
							+ StringUtil.getString(behaviorInfo.behaviorId)
							+ ","
							+ StringUtil.getString(behaviorInfo.behaviorArgs)
							+ ",0" + ","
							+ StringUtil.getString(behaviorInfo.country));

					filewriter.close();
				} catch (Exception e) {
					e.fillInStackTrace();
				}
			}
		}).start();
	}

	public static String getCurrentTimeByFormat(String format) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
		String currentTime = simpledateformat.format(new Date());
		return currentTime;
	}

	private static String getTimerFileName(String logType) {
		StringBuffer stringbuffer = new StringBuffer();
		stringbuffer.append(logType + ".log.").append(
				getCurrentTimeByFormat(DAY_FORMAT));
		stringbuffer.append("." + Util.getPhoneImei());
		return stringbuffer.toString();
	}
}
