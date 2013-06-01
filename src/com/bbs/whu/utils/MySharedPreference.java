package com.bbs.whu.utils;

import android.content.Context;

/**
 * 程序永久保存变量控制类
 * 
 * @author double
 * 
 */
public class MySharedPreference {
	private static final String PREFERENCE_NAME = "4MyLove";
	// 默认用户名
	public static final String DEFAULT_USER_NAME = "defaultUserName";
	// 默认密码
	public static final String DEFAULT_USER_PASSWORD = "defaultPassword";

	/** 保存布尔值 */
	public static void save(Context context, String key, boolean value) {
		context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
				.edit().putBoolean(key, value).commit();
	}

	/** 获取布尔值 */
	public static boolean get(Context context, String key, boolean defaultValue) {
		return context.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE).getBoolean(key, defaultValue);
	}

	/** 保存整型值 */
	public static void save(Context context, String key, int value) {
		context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
				.edit().putInt(key, value).commit();
	}

	/** 获取整型值 */
	public static int get(Context context, String key, int defaultValue) {
		return context.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE).getInt(key, defaultValue);
	}

	/** 保存长整型值 */
	public static void save(Context context, String key, long value) {
		context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
				.edit().putLong(key, value).commit();
	}

	/** 获取长整型值 */
	public static long get(Context context, String key, long defaultValue) {
		return context.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE).getLong(key, defaultValue);
	}

	/** 保存浮点数 */
	public static void save(Context context, String key, float value) {
		context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
				.edit().putFloat(key, value).commit();
	}

	/** 获取浮点数 */
	public static float get(Context context, String key, float defaultValue) {
		return context.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE).getFloat(key, defaultValue);
	}

	/** 保存字符串值 */
	public static void save(Context context, String key, String value) {
		context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
				.edit().putString(key, value).commit();
	}

	/** 获取字符串值 */
	public static String get(Context context, String key, String defaultValue) {
		return context.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE).getString(key, defaultValue);
	}
}
