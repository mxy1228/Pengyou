package com.cyou.mrd.pengyou.config;
public class Params {
	public static class HttpParams {
		public static final String UAUTH = "uauth";// 用户验证串
		public static final String CHECK_KEY_VALUE = "app-cyou-key";
		public static final String UID = "uid";
		public static final String PLATFORM = "platform";
		public static final String UTOKEN = "utoken";
		public static final String PLATFORM_VALUE = "2";
		public static final String PHONE = "phone";
		public static final String PASSWD = "passwd";
		public static final String UDID = "udid";
		public static final String SUCCESSFUL = "successful";
		public static final int SUCCESS = 1;
	}

	public static class SP_PARAMS {
		public static final String ROOT_PATH = "root_path";// 根路径
		public static final String LOG_PATH = "log_path";// 日志路径
		public static final String APK_PATH = "apk_path";// APK路径
		public static final String IMG_PATH = "img_path";// APK路径
		public static final String BEHAVIOR_PATH = "behavior_path";// APK路径
		public static final String MORE = "more";
		public static final String IGNORE_TIME = "ginore_time";// 用户忽略好友推荐时间
		public static final String NOTIFICATION_SWITCH = "notification_switch";//通知开关
		public static final String INSTALL_FIRST_OPEN = "install_first_open";//安装后第一次打开
		/**
		 * 个人信息
		 */
		public static final String KEY_AGE = "age";
		public static final String KEY_APPKEY = "appkey";
		public static final String KEY_BIRTHDAY = "birthday";
		public static final String KEY_CONSTELLATION = "constellation";
		public static final String KEY_GENDER = "gender";
		// public static final String KEY_LOCATE = "locate";
		public static final String KEY_NICKNAME = "nickname";
		public static final String KEY_PHONE = "phone_num";
		public static final String KEY_PLAYER_STYLE = "player_style";
		public static final String KEY_UAUTH = "uauth";// 用户验证串
		public static final String KEY_UTOKEN = "utoken";
		public static final String KEY_UID = "uid";
		public static final String KEY_AVATAR = "avatar";// 用户头像
		public static final String KEY_TAG = "tag";// 个性签名
		public static final String KEY_PASSWORD = "password";// 密码
		public static final String KEY_PLATFORM = "platform";
		public static final String KEY_UDID = "udid";// 用户唯一标识
		public static final String KEY_FAVGAME = "favgame";// 最喜欢游戏类型
		public static final String KEY_LEVEL = "level";// 等级
		public static final String KEY_EXP = "exp";// 经验
		public static final String KEY_MAXLEVEL = "maxexp";// 等级
		public static final String KEY_FOCUS = "focus";// 等级
		public static final String KEY_FANS = "fans";// 等级
		public static final String KEY_ACTS = "acts";// 等级
//		public static final String KEY_MSG = "msg";// 私信数
		public static final String KEY_SCHOOL = "school";// 学校
		public static final String KEY_LOCAL = "local";// 地区
		public static final String KEY_ISGUEST = "isguest";// 是否是游客
		public static final String KEY_GAMES = "games";// 我的游戏个数
		public static final String KEY_PICORIG = "picorig";// 头像大图
		public static final String KEY_UNREAD_LETTER = "unreadChatLetter";//未读的私信数目
		public static final String KEY_UNREAD_RELATION_CRICLE_LETTER = "unreadRelationCircleLetter";//未读的关系圈消息数目
		public static final String KEY_HASUPDATE_SIGN="user_sign_update";//是否更新过签名-统计个人信息完善度
		public static final String KEY_AVAILABLE_SCORE = "availablescore";//我的可用积分
		public static final String KEY_IS_CAN_EXCHANGE_SCORE = "iscanexchangescore";//用户是否可以领取完成个人信息的积分
		public static final String KEY_DOWNLOAD_GAMES = "downloadgames";//我的可用积分
		public static final String KEY_CMS_PERSONAL_INFO_SCORE = "personalinfoscore";//个人信息完成度所能兑换的积分值-CMS所配
		public static final String KEY_CMS_SHARE_SINA_SCORE = "sharesinascore";//分享新浪微博能兑换的积分值-CMS所配
		/**
		 * 微博相关参数
		 */
		public static final String TOKEN = "access_token";
		public static final String EXPIRES_IN = "expires_in";
		public static final String UID = "sina_uid";

		// 系统推送相关参数
		// public static final String SYSTEM_MSG="system_msg";
		public static final String CURRENT_FANS_COUNT = "current_fans_count";//当前粉丝数
		public static final String NEW_FANS_COUNT = "new_fans_count";//新粉丝数
		public static final String LAST_EACH_FOCUS_DYNAMIC_ID = "last_each_focus_dynamic_id";// 关系圈最新关系圈动态id
		public static final String LAST_DYNAMIC_READ = "last_dynamic_read";// 关系圈上次最新关系圈动态id
		public static final String LAST_MAX_DYNAMIC_ID = "last_max_dynamic_id";// 关系圈上次保存的关系圈动态id
		public static final String NEW_COMMENTS_COUNT = "new_comments_count";// 未读关系圈消息数
		public static final String LASTUID = "lastuid";// 最后一次评论用户id
		public static final String AVATAR = "avatar";// 最后一次评论用户icon
		public static final String CURRENT_FOCUS_COUNT = "current_focus_count";//当前关注数
		public static final String UNREAD_LETTER_COUNT = "unread_letter_count";//未读私信数
//		public static final String RECOMMEND_FRIEND = "recommend_friend";//好友推荐数
		public static final String RECOMMEND_FRIEND_COUNT = "recommend_friend_count";//猜你认识数目

		// 保存手机号
		public static final String KEY_TELEPHONE = "telephone";
		// 一级页面缓存字段
		public static final String PERSONAL_CENTER_MYGAME = "personal_center_mygame";
		public static final String PERSONAL_CENTER_GUESSGAME = "personal_center_guessgame";
		public static final String GAMESTORE_RECOMMAND_TOPADS = "gamestore_recommand_topads";
		public static final String GAMESTORE_SPECIAL = "gamestore_special";
		public static final String GAMESTORE_NEW_DATA = "gamestore_new";
		public static final String GAMESTORE_HOT_DATA = "gamestore_hot";
		public static final String GAMESTORE_RECOMMEND_LIST = "gamestore_recommand_list";
		public static final String GAMESTORE_CLASSIFY = "gamestore_classify";
		public static final String GAMESTORE_RANK_FRIEND = "gamestore_rank_friend";
		public static final String GAMESTORE_RANK_WORLD = "gamestore_rank_world";
		public static final String GAMESTORE_RANK_NEAR = "gamestore_rank_near";
		// 猜你喜欢缓存字段
		public static final String GAMESTORE_GUESSYOULIKE_GAME = "gamestore_guessyoulike_game";
		
		//游戏圈、关系圈
		public static final String REL_SHIP_DATA = "rel_data";
		public static final String SQL_SHIP_DATA = "sql_data";
		public static final String GAME_ID = "game_id";
		public static final String GAME_COUNTS = "game_counts";
		//是否已经创建快捷方式
		public static final String HAD_CREATE_SHORT_CUT = "had_create_short_cut";
		//是否已经扫描完手机游戏
		public static final String NEED_WAIT_FILTER_GAME_DOWN = "need_wait_filter_game_down";
		//保存的拍照后的图片路径
		public static final String CAMERA_IMAGE_PATH = "camera_imgage_path";
		public static final String CAMERA_IMAGE_WIDTH = "camera_imgage_width";
		public static final String CAMERA_IMAGE_HEIGHT = "camera_imgage_height";
		
		public static final String FEED_BACK_HINT = "feed_back_hint";//意见反馈hint
		public static final String FEED_BACK_FINISH = "feed_back_finish";//意见反馈结束语\
		public static final String GIFT_SHOWN = "gift_shown";//礼包是否显示
		
		public static final String DOWNLOAD_INSTALL_FILE_PATH = "download_install_file_path";//安装时产生签名冲突时，记录的apk文件路径
		public static final String FEED_BACK_SHOWN = "feed_back_shown";//意见反馈是否显示
		public static final String GAMESTORE_ISTODDY_DATA = "gamestore_istoddy_data";//游戏底部导航红点是否显示
	}
	
	/**
	 * 私信相关的SP参数
	 * @author xumengyang
	 *
	 */
	public static class LETTER_SP_PARAMS{
		public static final String CHAT_ACTIVITY_SHOWN = "chat_activity_shown";//ChatActivity是否在显示
		public static final String CHAT_ACTIVITY_UID = "chat_activity_uid";//当前ChatActivity的UID
	}
	
	public static class SHOW_PHOTO{
		public static final String PHOTO_TYPE="photo_type";
		public static final int PHOTO_USER=1;//用户头像
		public static final int PHOTO_PIC=2;//其他图片
	}
	public static final String FROM = "form";// 跳转前页面
	public static final String FROM_EDIT = "form_edit";
	public static final String PHOTO_URL = "photo_url";// 放大图片跳转参数
	public static final String PHOTO_MIDDLE_URL = "photo_middle_url";// 小图片跳转参数
	public static final String GCID = "gcid";
	public static final String FOLLOW  = "follow "; //成功为一。不成功为0，默认值为3
	public static class FRIEND_INFO {
		public static final String UID = "uid";
		public static final String NICKNAME = "nickname";
		public static final String GENDER = "gender";
	}

	// 游戏相关参数
	public static class GAME_INFO {
		public static final String GAME_NAME = "game_name";
		public static final String GAME_CODE = "game_code";
		public static final String GAME_ICON = "game_icon";
		public static final String GAME_IDENTIFY = "game_identify";
		public static final String GAME_TIMESTAMP = "game_timestamp";
	}

	// 跳转到IM页面参数
	public static class CHAT {
		public static final String FROM = "uid";
		public static final String NICK_NAME = "nickname";
		public static final String MSG = "msg";// 发送的内容
		public static final String TAG = "tag";// 类型标示。1：聊天 2：分享 gamecode
		public static final String GAMECODE = "gamecode";
		public static final String ITEM = "item";
		public static final String TIME = "time";//消息创建时间
		public static final String TO = "to";
		public static final String MSG_SEQ = "msg_seq";
	}

	// 系统推送消息
	public static class SYSTEM {
		public static final String MSG_TYPE = "msg_type";// 消息类型 IM or 系统消息
		public static final String SYSTEMINFO_ITEM = "system_item";

	}

	// 跳转到粉丝页面参数
	public static class FANS {
		public static final String UID = "uid";
	}

	// 跳转到关注页面
	public static class FOCUS {
		public static final String UID = "uid";
	}

	// 跳转到分享给好友页面
	public static class SHARE_TO_FRIEND {
		public static final String GAME_ITEM = "game_item";
	}

	public static final String CLASSIFY_NAME = "classify_name";// 游戏分类ID
	public static final String CLASSIFY_ID = "classify_id";// 游戏分类ID
	public static final String GAMESTORE_HTML="gamestore_html";//游戏库顶部跳转至网页
	public static final String GAMESTORE_HTML_NAME="gamestore_html_name";//游戏库顶部跳转至网页

	// 跳转到动态详情页面
	public static class DYNAMIC_DETAIL {
		public static final String AID = "a_id";// 动态id
		public static final String UID = "uid";
		public static final String POSITION = "position";
		public static final String DELETE_BUTTON_SHOW = "delete_button_show";
		public static final String GAMEGIFTID = "game_gift_id";// 礼包id
		public static final String GAMEGIFTCODE = "game_gift_code";// 礼包
		public static final String GAMEGIFTNAME = "game_gift_name";// 礼包名
		public static final String GAMENAME = "game_name";// 游戏名
		public static final String GAMECODE = "game_code";// gamecode
		public static final String GAMEGIFTIDENTIFIER = "game_gift_identifier";// 礼包名
	}

	// 跳转到游戏详情页
	public static class INTRO {
		public static final String GAME_CODE = "game_code";
		public static final String GAME_NAME = "game_name";
		public static final String GAME_SCORE = "game_score";
		public static final String GAME_PKGE = "game_pkge";
		public static final String GAME_ISINSTALLED = "game_isinstalled";
		public static final String GAME_DISPFLAG="dispflag";
		public static final String GAME_ISDISPLAYSTAR = "displaystar";
		public static final String GAME_CIRCLE =  "game_circle";
	}
    //跳转到游戏圈发布页面
	public static class Dynamic {
		public static final String GAME_CIRCLE_ID = "game_circle_id";
	}
	// 跳转到发短信页面
	public static class SEND_SMS {
		public static final String ITEM = "item";
	}
	// 跳转到获取手机验证码页面
	public static class RESET_PASSWORD {
		public static final String RESET_PASSWORD = "reset_password";
	}

	public static class PENGYOU_UPDATE {
		public static final String CONTENT = "content";
		public static final String VERSION = "version";
		public static final String URL = "url";
		public static final String TYPE = "type";
		public static final String CHECK = "check";
		public static final String CANCEL_UPDATE = "cancel_update";
		public static final String START_UPDATE = "start_update";
	}

	/**
	 * 请求CoreService的动作
	 * 
	 * @author xumengyang
	 * 
	 */
	public static class PUBLISH {
		public static final String ACTION = "action";
		public static final String UTOKEN = "utoken";
		public static final String STATE = "state";
		public static final String LOGIN = "login";
	}

	public static class REGIST {
		public static final String TELEPHONE = "telephone";
		public static final String VALIDATE_CODE = "vcode";
	}

	public static class RELATION {
		public static final String FROM = "sponsor";
		public static final String TO = "receiver";
		public static final String ACTION = "actiontype";
	}

	// 跳转到Captrue
	public static class CAPTRUE {
		public static final String BIGCAPTRUE = "bigcaptrue";
		public static final String SMALLCAPTRUE = "smallcaptrue";
		public static final String CURRENT_PAGE = "current_page";
		public static final String ANIM = "amin";// 打开和退出动画
	}
	
	/**
	 * 更新个人中心的广播携带的参数
	 * @author xumengyang
	 *
	 */
	public static class UPDATE_MY_DOT{
		public static final String NEW_FANS = "new_fans";//新粉丝数
		public static final String TOTAL_FANS_COUNT = "total_fans_count";//粉丝总数
		public static final String NEW_CHAT_COUNT = "new_chat_count";//新私信数
		public static final String TOTAL_MESSAGE_COUNT = "total_chat_count";//私信总数
		public static final String RECOMMEND_FRIEND_COUNT = "recommend_friend_count";//猜你认识数目
	}
	/**
	 * 
	 * @author tuozhonghua_zk
	 * 游戏专题参数
	 */
	public static class GAME_SPECIAL {
		public static final String SPECIAL_ID = "special_id";//专题id
		public static final String SPECIAL_NAME = "special_name";//专题名称
		public static final String SPECIAL_DATE = "special_date";//专题时间
		public static final String SPECIAL_IMAGE = "special_image";//专题图片
		public static final String SPECIAL_DESC = "special_desc";//专题描述
	}
	
}
