package com.bbs.whu.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.bbs.whu.model.UserPasswordBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MyBBSCache {
	// 读缓存
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

	// 写缓存
	public static void setUrlCache(String data, String url) {
		File file = new File(MyFileUtils.getSdcardDataCacheDir(MyApplication
				.getInstance().getName()) + "/" + getCacheDecodeString(url));
		try {
			// 创建缓存数据到磁盘，就是创建文件
			MyFileUtils.writeTextFile(file, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// URI编码
	public static String getCacheDecodeString(String url) {
		if (url != null) {
			return MyFileUtils.md5(url);
		}
		return null;
	}
	
	/**
	 * 读用户名、密码json文件
	 * 
	 * @param name
	 *            json文件名，约定为4MyLove，注意会对4MyLove进行MD5加密
	 * @return 读出的文本数据
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
	 * 写用户名、密码json文件
	 * 
	 * @param data
	 *            待写入的数据
	 * @param name
	 *            json文件名，约定为4MyLove，注意会对4MyLove进行MD5加密
	 */
	private static void setUserPasswordCache(String data, String name) {
		File file = new File(MyFileUtils.getSdcardDataUserPasswordDir() + "/"
				+ getCacheDecodeString(name));
		try {
			// 创建用户名、密码json文件缓存数据到磁盘，就是创建文件
			MyFileUtils.writeTextFile(file, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读用户名、密码json文件（反序列化）
	 * 
	 * @param name
	 *            json文件名，约定为4MyLove，注意会对4MyLove进行MD5加密
	 * @return List<UserPasswordBean> 反序列化对象列表
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
		List<UserPasswordBean> userPasswords = gson.fromJson(content,
				new TypeToken<List<UserPasswordBean>>() {
				}.getType());

		// 用户名，密码解密
		try {
			// 采用默认密钥
			MyEncryptionDecryptionUtils des = new MyEncryptionDecryptionUtils();
			for (int i = 0; i < userPasswords.size(); ++i) {
				String user = des.decrypt(userPasswords.get(i).getName());
				userPasswords.get(i).setName(user);
				String password = des.decrypt(userPasswords.get(i)
						.getPassword());
				userPasswords.get(i).setPassword(password);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userPasswords;
	}
	
	/**
	 * 写用户名、密码json文件（序列化）
	 * 
	 * @param userPasswords
	 *            待序列化对象列表
	 * @param name
	 *            json文件名，约定为4MyLove，注意会对4MyLove进行MD5加密
	 */
	public static void setUserPasswordList(
			List<UserPasswordBean> userPasswords, String name) {
		if (userPasswords == null) {
			return;
		}
		
		// 用户名，密码加密
		try {
			// 采用默认密钥
			MyEncryptionDecryptionUtils des = new MyEncryptionDecryptionUtils();
			for (int i = 0; i < userPasswords.size(); ++i) {
				String user = des.encrypt(userPasswords.get(i).getName());
				userPasswords.get(i).setName(user);
				String password = des.encrypt(userPasswords.get(i)
						.getPassword());
				userPasswords.get(i).setPassword(password);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		String content = gson.toJson(userPasswords);
		setUserPasswordCache(content, name);
	}
}
