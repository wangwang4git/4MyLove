package com.bbs.whu.utils;

import android.content.Context;

/**
 * �������ñ������������
 * 
 * @author double
 * 
 */
public class MySharedPreference {
	private static final String PREFERENCE_NAME = "4MyLove";
	// Ĭ���û���
	public static final String DEFAULT_USER_NAME = "defaultUserName";
	// Ĭ������
	public static final String DEFAULT_USER_PASSWORD = "defaultPassword";

	/** ���沼��ֵ */
	public static void save(Context context, String key, boolean value) {
		context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
				.edit().putBoolean(key, value).commit();
	}

	/** ��ȡ����ֵ */
	public static boolean get(Context context, String key, boolean defaultValue) {
		return context.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE).getBoolean(key, defaultValue);
	}

	/** ��������ֵ */
	public static void save(Context context, String key, int value) {
		context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
				.edit().putInt(key, value).commit();
	}

	/** ��ȡ����ֵ */
	public static int get(Context context, String key, int defaultValue) {
		return context.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE).getInt(key, defaultValue);
	}

	/** ���泤����ֵ */
	public static void save(Context context, String key, long value) {
		context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
				.edit().putLong(key, value).commit();
	}

	/** ��ȡ������ֵ */
	public static long get(Context context, String key, long defaultValue) {
		return context.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE).getLong(key, defaultValue);
	}

	/** ���渡���� */
	public static void save(Context context, String key, float value) {
		context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
				.edit().putFloat(key, value).commit();
	}

	/** ��ȡ������ */
	public static float get(Context context, String key, float defaultValue) {
		return context.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE).getFloat(key, defaultValue);
	}

	/** �����ַ���ֵ */
	public static void save(Context context, String key, String value) {
		context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
				.edit().putString(key, value).commit();
	}

	/** ��ȡ�ַ���ֵ */
	public static String get(Context context, String key, String defaultValue) {
		return context.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE).getString(key, defaultValue);
	}
}
