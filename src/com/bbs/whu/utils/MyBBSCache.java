package com.bbs.whu.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.bbs.whu.model.UserPasswordBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MyBBSCache {
	// ������
	public static String getUrlCache(String url) {
		if (url == null) {
			return null;
		}

		String result = null;
		File file = new File(MyFileUtils.getSdcardDataCacheDir(MyApplication
				.getInstance().getName()) + "/" + getCacheDecodeString(url));
		if (file.exists() && file.isFile()) {
			try {
				result = MyFileUtils.readTextFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// д����
	public static void setUrlCache(String data, String url) {
		File file = new File(MyFileUtils.getSdcardDataCacheDir(MyApplication
				.getInstance().getName()) + "/" + getCacheDecodeString(url));
		try {
			// �����������ݵ����̣����Ǵ����ļ�
			MyFileUtils.writeTextFile(file, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// URI����
	public static String getCacheDecodeString(String url) {
		if (url != null) {
			return MyFileUtils.md5(url);
		}
		return null;
	}
	
	/**
	 * ���û���������json�ļ�
	 * 
	 * @param name
	 *            json�ļ�����Լ��Ϊ4MyLove��ע����4MyLove����MD5����
	 * @return �������ı�����
	 */
	private static String getUserPasswordCache(String name) {
		if (name == null) {
			return null;
		}

		String result = null;
		File file = new File(MyFileUtils.getSdcardDataUserPasswordDir() + "/"
				+ getCacheDecodeString(name));
		if (file.exists() && file.isFile()) {
			try {
				result = MyFileUtils.readTextFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * д�û���������json�ļ�
	 * 
	 * @param data
	 *            ��д�������
	 * @param name
	 *            json�ļ�����Լ��Ϊ4MyLove��ע����4MyLove����MD5����
	 */
	private static void setUserPasswordCache(String data, String name) {
		File file = new File(MyFileUtils.getSdcardDataUserPasswordDir() + "/"
				+ getCacheDecodeString(name));
		try {
			// �����û���������json�ļ��������ݵ����̣����Ǵ����ļ�
			MyFileUtils.writeTextFile(file, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ���û���������json�ļ��������л���
	 * 
	 * @param name
	 *            json�ļ�����Լ��Ϊ4MyLove��ע����4MyLove����MD5����
	 * @return List<UserPasswordBean> �����л������б�
	 */
	public static List<UserPasswordBean> getUserPasswordList(String name) {
		if (name == null) {
			return null;
		}
		String content = getUserPasswordCache(name);
		if (content == null) {
			return null;
		}

		Gson gson = new Gson();
		return gson.fromJson(content, new TypeToken<List<UserPasswordBean>>() {
		}.getType());
	}
	
	/**
	 * д�û���������json�ļ������л���
	 * 
	 * @param userPasswords
	 *            �����л������б�
	 * @param name
	 *            json�ļ�����Լ��Ϊ4MyLove��ע����4MyLove����MD5����
	 */
	public static void setUserPasswordList(
			List<UserPasswordBean> userPasswords, String name) {
		if (userPasswords == null) {
			return;
		}

		Gson gson = new Gson();
		String content = gson.toJson(userPasswords);
		setUserPasswordCache(content, name);
	}
}
