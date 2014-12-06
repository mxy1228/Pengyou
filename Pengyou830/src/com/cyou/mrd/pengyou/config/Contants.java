package com.cyou.mrd.pengyou.config;

public class Contants {
	public static final String SYSTEM_NAME = "ANDROID";

	/**
	 * SharedPreference名称
	 * 
	 * @author xumengyang
	 * 
	 */
	public static class SP_NAME {
		public static final String SETTING = "setting";// 设置
		public static final String USER_INFO = "user_info";// 用户属性
		public static final String BIND_PHONE = "bind_phone";//是否已绑定手机
		public static final String ISFIRST_OPENAPP = "isFirst";// 是否第一次打开应用
//		public static final String WEIBO_SINA = "weibo_sina";// 保存微博相关信息
		public static final String SYSTEM_MSG="system_msg";//服务器推送过来的消息
		public static final String SETTING_TELEPHONE="setting_telephone";//最后使用的手机号
		public static final String PERSONAL_CENTER="personal_center";//个人中心页面数据
		public static final String GAMESTORE_RECOMMEND="gamestore_recommend";//游戏库-推荐
		public static final String GAMESTORE_SPECIAL="gamestore_special";//游戏库-游戏专题
		public static final String GAMESTORE_CLASSIFY="gamestore_classify";//游戏库-分类
		public static final String GAMESTORE_RANK="gamestore_rank";//游戏库-榜单
		public static final String GAMESTORE_GUESS_YOU_LIKE="gamestore_guess_you_like";//游戏库-猜你喜欢随机显示
		public static final String LETTER_ABOUT = "letter_about";//私信相关
		public static final String NEED_WAIT_FILTERGAME_DOWN="need_wait_filtergame_down";//扫描手机游戏是否完成
		public static final String USER_DATA = "user_data";//存储用户相关数字数据
		public static final String GAME_STAR = "gamestar_info";//存储用户安装游戏评分数据
		public static final String DOWNLOAD_INFO = "download_info";//存储下载时相关数据
	}

	public static class PATH {
		public static final String LOG_PATH = "/log/";
		public static final String APK_PATH = "/apk/";
		public static final String ROOT_PATH = "/pengyou/";
		public static final String DATA_PATH = "/data/com.cyou.mrd.pengyou";
		public static final String IMG_PATH = "/pubimg/";
		// 统计日志的存放路径
		public static String BEHAVIOR_PATH="/behaviorlog/";
		public static final String ERRORLOG_PATH="/errorlog/";//错误日志
	}

	public static class TABLE_NAME {
		public static final String DOWNLOAD = "download";
		public static final String FRIEND_LIST = "friend";
		// 更新任务表
		public static final String UPDATE_TASK = "update_task";
		// 私信
		public static final String LETTER = "letter";
		//最近联系人
		public static final String RECENT_USER = "recentUser";
		//关系圈消息
		public static final String RELATIONCIRCLE_MSG = "relationCircleMsg";
		//我的游戏
		public static final String MY_GAME = "myGame";
		//我的游戏玩耍时间片段
		public static final String MY_GAME_PLAY_RECORD = "myGamePlayRecord";
		//游戏从朋游下载安装记录
		public static final String GAME_DOWNLOAD_RECORD = "gameDownloadRecord";
		//关系圈消息
//		public static final String RELATIONSHIP_MSG = "relationshipMsg";
		//动态发布信息
		public static final String DYNAMIC_INFO = "dynamicInfo";
		//游戏圈动态消息
		public static final String GAME_DYNAMIC_INFO = "gamedynamicInfo";
		//关系圈动态表
		public static final String DYNAMIC_RELATION_CIRCLE = "dynamicRelation";
		public static final String DYNAMIC_RELATION_CIRCLE_SUPPORTER = "dynamicRelationSupporter";
		public static final String DYNAMIC_RELATION_CIRCLE_GAME = "dynamicRelationGame";
		public static final String DYNAMIC_RELATION_CIRCLE_COMMENT = "dynamicRelationComment";
		
	}

	public static class ACTION {
		public static final String DOWNLOADING_COUNT = "com.cyou.mrd.pengyou.download.count";// DownloadService广播正在下载的任务数量
		public static final String GAME_DOWNLOAD_AND_INSTALL = "com.cyou.mrd.pengyou.game.download.and.install";// DownloadService广播正在下载的任务数量
//		public static final String LOGIN_ROOT_ACTION = "com.cyou.mrd.pengyou.login";
		public static final String FILTER_GAME_FINISHED = "com.cyou.mrd.pengyou.filter_game_finished";
		public static final String PENGYOU_UPDATE = "com.cyou.mrd.pengyou.update";//朋游的更新
		public static final String INSTALL = "com.cyou.mrd.pengyou.install";//安装了某应用
		public static final String GAME_INSTALL = "com.cyou.mrd.pengyou.install";//安装了某应用,通知游戏详情
		public static final String UNINSTALL = "com.cyou.mrd.pengyou.uninstall";//卸载了某应用
		public static final String FAV="com.cyou.mrd.pengyou.fav";//收藏某应用
		public static final String UNFAV="com.cyou.mrd.pengyou.unfav";//取消收藏某应用
		public static final String FAVCHANGE="com.cyou.mrd.pengyou.favchange";//取消收藏某应用
		public static final String DOWNLOADED = "com.cyou.mrd.pengyou.downloaded";//游戏下载完成
		public static final String REORDER_MY_GAME_LIST="com.cyou.mrd.pengyou.reorder_mygame_list";//启动某应用
		public static final String SYSTEM_MSG_ACTION="com.cyou.mrd.pengyou.sysmsg";
		public static final String ATTENTION_NOTIFY_CHANGED="com.cyou.mrd.pengyou.notify";//我一系列页面关注时刷新页面
		public static final String UPDATE_RELATION_MSG_DOT_ACTION="com.cyou.mrd.pengyou.refresh_relation_msg_dot";
		public static final String REFRESH_RELATION_CIRCLE = "com.cyou.mrd.pengyou.refresh_relationcircle";//刷新关系圈消息
		public static final String TOP_RELATION_CIRCLE = "com.cyou.mrd.pengyou.top_relationcircle";//置顶关系圈消息
		public static final String REFRESH_SQUARE_CIRCLE = "com.cyou.mrd.pengyou.refresh_squarecircle";//刷新关系圈消息
		public static final String REFRESH_GAME_CIRCLE = "com.cyou.mrd.pengyou.refresh_gamecircle";//刷新游戏圈消息
		public static final String UPDATE_RELATION_SUPPORT_INFO = "com.cyou.mrd.pengyou.update_relation_support_info";
		public static final String UPDATE_RELATION_COMMENT_INFO = "com.cyou.mrd.pengyou.update_relation_comment_info";
		public static final String UPDATE_PERSONAL_INFO = "com.cyou.mrd.pengyou.update_persional_info";
		public static final String NEW_CHAT = "com.cyou.mrd.pengyou.newchat";
		public static final String NEW_CONVERSATION = "com.cyou.mrd.pengyou.newconversion";//新对话
		public static final String UPDATE_MY_DOT = "com.cyou.mrd.pengyou.update.my_dot";//更新“我”页面的红点提示
		
		public static final String SEND_DYNAMIC_SENDING = "com.cyou.mrd.pengyou.dynamic_sending";//正在发送消息
		public static final String SEND_DYNAMIC_SUCCESS = "com.cyou.mrd.pengyou.dynamic_success";//动态发布成功消息
		public static final String SEND_DYNAMIC_FAIL = "com.cyou.mrd.pengyou.dynamic_fail";//动态发布失败消息
		//游戏圈消息
		public static final String SEND_GAMEDYNAMIC_SUCCESS = "com.cyou.mrd.pengyou.gamedynamic_success"; //游戏圈动态发布成功消息
		public static final String SEND_GAMEDYNAMIC_STAR_SUCCESS="com.cyou.mrd.pengyou.gamedynamic_success_star";//游戏圈发布动态成功返回平均分
		public static final String SEND_GAMEDYNAMIC_FAIL = "com.cyou.mrd.pengyou.gamedynamic_fail";//游戏圈动态发布失败消息
		public static final String SEND_GAMEDYNAMIC_SENDING = "com.cyou.mrd.pengyou.gamedynamic_sending";//正在发送消息
		public static final String SEND_GAMEDYNAMIC_REPUBLISH = "com.cyou.mrd.pengyou.gamedynamic_republish";//重新发布
		public static final String SEND_GAMEDYNAMIC_DELETE = "com.cyou.mrd.pengyou.gamedynamic_delete";//删除动态
		public static final String UPDATE_GAME_DYNAMIC_COUNT = "com.cyou.mrd.pengyou.update_gamedynamic_count";


		public static final String SEND_DYNAMIC_REPUBLISH = "com.cyou.mrd.pengyou.dynamic_republish";//动态发布重新发布
		public static final String SEND_DYNAMIC_DELETE = "com.cyou.mrd.pengyou.dynamic_delete";//删除动态
		
		public static final String SQUARE_SEND_FAILED_COMMENT = "com.cyou.mrd.pengyou.comment_republish_square";//评论重新发布
		public static final String RELATION_SEND_FAILED_COMMENT = "com.cyou.mrd.pengyou.comment_republish_relation";//评论重新发布
		public static final String GIFT = "com.cyou.mrd.pengyou.gift";//玩游戏页面是否显示红点
		public static final String REMOTE_LETTER = "com.cyou.mrd.pengyou.remote_letter";//CoreService通知主进程更新Letter_about SP
		//game circle
		public static final String SEND_GAME_CIRCLE_SWITCH_TAB="com.cyou.mrd.pengyou.game_circle_switch";
		public static final String SEND_GAME_CIRCLE_GCID="com.cyou.mrd.pengyou.game_circle_gcid";
		public static final String FORCE_LOGIN_OUT = "com.cyou.mrd.pengyou.foce_login_out";//发生登录互斥
		public static final String UPDATE_GAME_CIRCLE_MSG = "com.cyou.mrd.pengyou.update_game_circle";//更新玩游戏页面的游戏圈消息数字
		
		
	}

	/**
	 * 标记好友来源
	 * 
	 * @author xumengyang
	 * 
	 */
	public static class FRIEND_TAG {
		public static final int SAME_GAME = 1;// 玩同一个游戏
		public static final int WEIBO = 2;// 微博
		public static final int QQ = 3;// 扣扣
	}
	public static class RETURNHOME{
		public static int RETURN_HOME=1;//通过home切换到后台
		public static int RETURN_HOME_PLAY=2;//通过"玩游戏" 切换后台
		public static int RETURN_HOME_SEARCHGAME=3;//通过"找游戏"切换后台
		public static int RETURN_HOME_RELATIONS=4;//通过"关系圈"切换
		public static int RETURN_HOME_MINE=5;//通过"我"切换后台
		public static int RETURN_HOME_MORE=6;
	}
	/**
	 * 动态类别
	 * 
	 * @author xumengyang
	 * 
	 */
	public static class DYNAMIC_TYPE {
		public static final int SHARE_GAME = 0;// 分享了游戏
		public static final int PUB_TEXT = 1;//发布文字
		public static final int PUB_PIC = 2;//发布图片
		public static final int PLAY_GAME = 3;//正在玩游戏
		public static final int CAPTURE = 4;//截图
		public static final int GAME_CIRCLE = 5;
		public static final int MY_DYNAMIC = 6;//自己刚发的动态
	}

	/**
	 * 消息类型
	 * 
	 * @author xumengyang
	 * 
	 */
	public static class PERSONAL_MSG_TYPE {
		public static final String MSG = "privatemsg";// 私信
		public static final String SHARE_GAME = "sharegamemsg";// 邀请玩游戏
	}

	/**
	 * 性别
	 */
	public static class GENDER_TYPE {
		public static final int BOY = 1;// 男
		public static final int GIRL = 2;// 女
	}

	public static class GAME_ISPUBLIC {
		public static final int GAME_PUBLIC = 1;// 公开
		public static final int GAME_UNPUBLIC = 0;// 隐藏
	}
	public static class MSG_TYPE{
		public static final int MSG_SYSTEM=1;//系统消息
		public static final int MSG_IM=2;//私信
	}
	public static class GAME_HAS_SCORE{
		public static final int HAS=1;//有分
		public static final int NOTHAVE=0;//无分
	}
	/**
	 * IM信息归属者
	 * 
	 * @author xumengyang
	 * 
	 */
	public static class CHAT_FROM {
		public static final int ME = 1;
		public static final int YOU = 2;
	}
	
	public static class ERROR_NO{
		public static final int ERROR_1=101;//注册时 该手机号已经被注册过
		public static final int ERROR_2=108;//注册验证码过期
		public static final int ERROR_3=109;//验证码不正确
		public static final int ERROR_4=105;//修改个人信息时 昵称重复
		public static final int ERROR_5=106;//不能重复发验证码 
		public static final int ERROR_UNREGIST=100;//未注册
		public static final int ERROR_USERNAME=103;//用户名或密码错误
		public static final int ERROR_SNS_BINDED=112;//微博账号已经被绑定
		public static final String ERROR_MASK_WORD_STRING = "004"; //敏感词
		public static final String ERROR_OPERATION_STRING = "005"; //操作频繁
		public static final String ERROR_NOT_EXIST_STRING = "110"; //动态不存在
	}

	public static class WEIBO_SHARE_RESULT{
		public static final int SUCCESS = 0;//分享成功
		public static final int FAILED = -1;//分享失败
	}
	public static class WEIBO_EXCHANGE_SCORE {
		public static final int RETURN = -100;//新浪微博兑换积分返回
	}
	public static class WEIBO_ERROR_CODE{
		public static final int USER_DOES_NOT_EXISTS = 20003;//用户不存在
		public static final int OUT_OF_LIMIT = 20016;//发微博的次数超过限制
		public static final int REPEAT_SIMINAL_CONTENT = 20017;//发相似的内容
		public static final int REPEAT_SAME_CONTENT = 20019;//发重复的内容
		public static final int TOKEN_EXPIRED = 21315;//Token 过期
		public static final int EXPIRED_TOKEN = 21327;//过期Token
		public static final int INVALID_ACCESS_TOKEN = 21332;
	}
	
	public static class WEIBO_INVITE_RESULT{
		public static final int SUCCESS = 0;//分享成功
		public static final int FAILED = -1;//分享失败
	}

	/**
	 * 关系圈消息格式
	 * @author xumengyang
	 *
	 */
	public static class RELATION_CIRCLE_MESSAGE_TYPE{
		public static final int IMAGE = 1;
		public static final int TEXT = 2;
	}
	/**
	 * 网络状态
	 * 
	 * @author xumengyang
	 * 
	 */
	public static class NET_STATE {
		public static final int MOBILE = 2;// 当前网络不可用
		public static final int UNAVAILABLE = 3;// 当前为移动网络
		public static final int WIFI = 1;// 当前为wifi网络
		public static final String NETWORK_ERROR = "network_error";// 网路异常时Task返回的string结果
	}

	/**
	 * 关注
	 * @author xumengyang
	 *
	 */
	public static class FOCUS{
		public static final int YES = 1;//已关注
		public static final int NO = 0;
	}
	
	/**
	 * 相互关注
	 * @author xumengyang
	 *
	 */
	public static class EACH_FOCUS{
		public static final int YES = 1;
		public static final int NO = 0;
	}
	
	/**
	 * 屏蔽动态和私信
	 * @author xumengyang
	 *
	 */
	public static class SHIELD{
		public static final String YES = "1";//已屏蔽
		public static final String NO = "0";
	}
	
	/**
	 * 是否已顶
	 * @author xumengyang
	 *
	 */
	public static class SUPPORT{
		public static final int YES = 1;//已顶
		public static final int NO = 0;//已顶
	}
	public static class GAME_FAV{
		public static final int FAV=1;//已收藏
		public static final int UNFAV=0;//未收藏
	}
	/**
	 * 消息类型
	 * @author xumengyang
	 *
	 */
	public static class MESSAGE_TYPE{
		public static final int SIXIN = 0;//私信
		public static final int RELATION_CIRCLE = 1;//关系圈消息
		public static final int OFFICAL = 2;//官方消息
	}
	
	/**
	 * 我的私信页面ListView存储tag以方便从服务器获取信息后快速找到控件
	 * @author xumengyang
	 *
	 */
	public static class MESSAGE_LISTVIEW_TAG{
		public static final String NICKNAME_TV = "nickname_tv";
		public static final String AVATAR_IV = "avatar_iv";
		public static final String GAME_ICON_IV = "game_icon_iv";
		public static final String GAME_RATINGBAR = "game_rating_bar";
		public static final String GAME_NAME_TV = "game_name_tv";
	}
	
	/**
	 * 接收到私信消息的类型
	 * @author xumengyang
	 *
	 */
	public static class CHAT_TYPE{
		public static final int TEXT = 1;//文本
		public static final int GAME = 2;//分享游戏
		public static final int TO_BE_FRIEND = 3;//"我们成为好友了"
		public static final int GIFT = 4;//礼包
		public static final int OPERATE = 5;//运营推送
	}
	
	/**
	 * 向消息服务器发送关系改变消息
	 * @author xumengyang
	 *
	 */
	public static class RELATION_ACTION{
		public static final String CANCEL_FOCUS = "cancelfocuson";
		public static final String FOCUS = "focuson";
	}
	
	/**
	 * 向CoreService发送的Action type
	 * @author xumengyang
	 *
	 */
	public static class PUBLISH_ACTION{
		public static final int CHAT = 1;//IM
		public static final int RELATION = 2;//关系
		public static final int JOIN = 3;
		public static final int CHANGE_BIND_ING = 4;//正在更换绑定
	}
	
	/**
	 * SNS标示
	 * @author xumengyang
	 *
	 */
	public static class SNS_TAG{
		public static final int NULL = 0;
		public static final int SINA = 1;
		public static final int PENGYOU = 8;
	}
	
	/**
	 * 未读消息的种类
	 * @author xumengyang
	 *
	 */
	public static class UNREAD_MSG_TYPE{
		public static final String TEXT = "immsg";
		public static final String GAME = "sharegamemsg";
		public static final String ADD_FRIEND = "friendshipaddmsg";
		public static final String DEL_FRIEND = "friendshipdelmsg ";
	}
	
	/**
	 * 私信是否已读
	 * @author xumengyang
	 *
	 */
	public static class LETTER_IS_READ{
		public static final int YES = 1;//已读
		public static final int NO = 2;//未读
	}
	
	/**
	 * 关系
	 * @author xumengyang
	 *
	 */
	public static class RELATION{
		public static final int NO_RELATION = 0;//没有任何关系
		public static final int HAD_FOCUS = 3;//我已关注对方
		public static final int EACH_FOCUS = 4;//互相关注
	}
	
	/**
	 * 动态发送的状态
	 * @author tuozhonghua_zk
	 *
	 */
	public static class SEND_DYNAMIC_STATUS{
		public static final int FAIL = 0;//发送失败
		public static final int SUCCESS = 1;//发送成功
		public static final int SENDING = 2;//发送中
	}
	/**
	 * 
	 * @author tuozhonghua_zk
	 *
	 */
	public static class SEND_DYNAMIC_DATA{
		public static final String DYNAMIC_TYPE = "dynamic_type";
		public static final String DYNAMIC_TEXT = "dynamic_text";//发送文字
		public static final String DYNAMIC_PICTURE = "dynamic_picture";//发送图片
		public static final String DYNAMIC_DATA_PID = "dynamic_item_pid";//pid
		public static final String DYNAMIC_DATA_AID = "dynamic_item_aid";//aid
		public static final String DYNAMIC_DATA_FAIL_TYPE = "dynamic_item_failed";//失败类型
		public static final String DYNAMIC_DATA_SUPPORT = "dynamic_item_support";
		public static final String DYNAMIC_DATA_STAR = "dynamic_item_star"; //发送动态后服务器返回的游戏平均分
		public static final String DYNAMIC_DATA_STARDISTR = "dynamic_item_stardistr"; 
	}
	/**
	 * 私信是否发送成功
	 * @author xumengyang
	 *
	 */
	public static class LETTER_SEND_SUCCESS{
		public static final String YES = "1";
		public static final String NO = "2";
	}
	

	/*
	 * 游戏圈人数，游戏ID
	 * */
	public static class RELATIONSHIP_DATA{
		public static final  String  RELATION = "relation";
		public static final  String  GAME = "game";
		public static final  String  GAME_CIRCLE_COUNT = "game_circle_count";
	}
	/**
	 * 游戏库顶部广告跳转参数
	 * @author wangkang
	 *
	 */
	public static class SCROLL_ADS{
		public static final String GAME="game";//游戏详细
		public static final String TOPIC="topic";//专题
		public static final String CIRCLE="circle";//游戏圈
		public static final String WEB="web";//网页
		public static final String GIFT="gift";//礼包
		public static final String GLOBALACT="globalact";//广场
	}
	//积分动作exchange rules
	public static class SCORE_ACTION{
		public static final int QUERY_EXCHANGE_RULES = 0;
		public static final int EXCHANGE_PLAY_GAME_SCORE = 1;
		public static final int EXCHANGE_DOWNLOAD_GAME_SCORE = 2;
		public static final int EXCHANGE_PERSON_INFO_SCORE = 3;
		public static final int EXCHANGE_SHARE_SINA_SCORE = 5;
	}

	 //积分兑换返回content中的data中包含的error_no
	public static class EXCHANGE_ERROR_NO {	
		public static final int SUCCESS = 0;
		public static final int NOT_CONFORM_RULES = 402;
		public static final int TIMESTAMP_INVALID = 404;
	}
	
	//可用积分加减act
	public static class SCORE_ADD_OR_SUB {
		public static final int ADD = 1;//加
		public static final int SUB = 0;//减
	}
	//关系圈常量
	public static class RELATION_CONTANTS{
		//RelationShipCircleFragment
		public static String REL_CIRCLE_STARTCOMMENT_SRC = "RelationShipCircleFragment";
		public static int REL_CIRCLE_REQUEST_COMMENT_CODE = 1;
		
		//RelationShipSquareFragment
		public static String REL_SQUARE_STARTCOMMENT_SRC = "RelationShipSquareFragment";
		public static int REL_SQUARE_REQUEST_COMMENT_CODE = 2;
		//RelationCommentActivity
		public static int REL_CIRCLE_BACK_COMMENT_CODE = 3;
		public static int REL_SQUARE_BACK_COMMENT_CODE = 4;
	}
}
