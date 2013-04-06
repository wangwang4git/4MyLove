package com.bbs.whu.utils;

/**
 * 存储全局变量的工具类
 * 
 * @author double
 * 
 */
public class MyConstants {
	// 用户名
	// public static final String MY_USER_NAME = "double0291";
	// // 密码
	// public static final String MY_PASSWORD = "chenshuang";
	// 用户名
	// public static final String MY_USER_NAME = "wwang";
	// // 密码
	// public static final String MY_PASSWORD = "w196988";
	// 请求成功
	public static final int REQUEST_SUCCESS = 0;
	// 请求失败
	public static final int REQUEST_FAIL = 1;
	// 登录下拉框选中某项
	public static final int LOGIN_SELECT_USER = 7;
	// 登录下拉框删除某项
	public static final int LOGIN_DELETE_USER = 8;
	// 登陆URL
	public static final String LOGIN_URL = "http://bbs.whu.edu.cn/bbslogin.php";
	// 登录URL 2.0
	public static final String LOGIN_URL_2 = "http://bbs.whu.edu.cn/mobile.php";
	// 退出登录URL
	public static final String LOG_OUT_URL = "http://bbs.whu.edu.cn/bbslogout.php";
	// get请求URL
	public static final String GET_URL = "http://bbs.whu.edu.cn/mobile.php";
	// post提交回复帖子的数据
	public static final String POST_BULLETIN_REPLY_URL = "http://bbs.whu.edu.cn/wForum/dopostarticle.php";
	// post提交发信
	public static final String POST_NEW_MAIL_URL = "http://bbs.whu.edu.cn/wForum/dosendmail.php";
	// 用户头像URL前缀
	public static final String HEAD_URL = "http://bbs.whu.edu.cn/";
	// 帖子内容中图片URL前缀
	public static final String IMAGE_URL = "http://bbs.whu.edu.cn/wForum/bbscon.php";
	// 发表新帖
	public static final int NEW_BULLETIN = 0;
	// 帖子回复
	public static final int BULLETIN_REPLY = 1;
	// 滑动退出的x轴最小像素间距
	public static final int MIN_GAP_X = 150;
	// 滑动退出的y轴最小像素间距
	public static final int MAX_GAP_Y = 100;
	// 版块列表加载的页数上限
	public static final int TOPIC_MAX_PAGES = 30;

	// 客户端更新URL
	public static final String UPDATE_CLIENT_URL = "http://bbs.whu.edu.cn/mobileUpdate/update.xml";

	// 发信
	public static final int NEW_MAIL = 1;
	// 读信
	public static final int READ_MAIL = 2;
}
