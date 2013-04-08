package com.bbs.whu.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bbs.whu.model.ReadedTagBean;
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
		List<UserPasswordBean> userPasswords = gson.fromJson(content,
				new TypeToken<List<UserPasswordBean>>() {
				}.getType());

		// �û������������
		try {
			// ����Ĭ����Կ
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
		
		// �û������������
		try {
			// ����Ĭ����Կ
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

	/**
	 * ���Ѷ����json�ļ�
	 * 
	 * @param name
	 *            json�ļ�����Լ��ΪReadedTag��ע����ReadedTag����MD5����
	 * @return �������ı�����
	 */
	private static String getReadedTagCache(String name) {
		if (name == null) {
			return null;
		}

		String result = null;
		File file = new File(MyFileUtils.getSdcardDataCacheDir(MyApplication
				.getInstance().getName()) + "/" + getCacheDecodeString(name));
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
	 * д�Ѷ����json�ļ�
	 * 
	 * @param data
	 *            ��д�������
	 * @param name
	 *            json�ļ�����Լ��ΪReadedTag��ע����ReadedTag����MD5����
	 */
	private static void setReadedTagCache(String data, String name) {
		File file = new File(MyFileUtils.getSdcardDataCacheDir(MyApplication
				.getInstance().getName()) + "/" + getCacheDecodeString(name));
		try {
			// �����Ѷ����json�ļ��������ݵ����̣����Ǵ����ļ�
			MyFileUtils.writeTextFile(file, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���Ѷ����json�ļ��������л���
	 * 
	 * @param name
	 *            json�ļ�����Լ��ΪReadedTag��ע����ReadedTag����MD5����
	 * @return HashMap<String, Byte> �����л�����װ��Map����
	 */
	public static HashMap<String, Byte> getReadedTagMap(String name) {
		if (name == null) {
			return null;
		}
		String content = getReadedTagCache(name);
		if (content == null) {
			return null;
		}

		Gson gson = new Gson();
		List<ReadedTagBean> readedTagList = gson.fromJson(content,
				new TypeToken<List<ReadedTagBean>>() {
				}.getType());

		HashMap<String, Byte> readedTagMap = new HashMap<String, Byte>();
		for (int i = 0; i < readedTagList.size(); ++i) {
			readedTagMap.put(readedTagList.get(i).getId(), readedTagList.get(i)
					.getTag());
		}
		return readedTagMap;
	}

	/**
	 * д�Ѷ����json�ļ������л���
	 * 
	 * @param readedTagMap
	 *            �����л�Map����
	 * @param name
	 *            json�ļ�����Լ��ΪReadedTag��ע����ReadedTag����MD5����
	 */
	@SuppressWarnings("rawtypes")
	public static void setReadedTagMap(HashMap<String, Byte> readedTagMap,
			String name) {
		if (readedTagMap == null) {
			return;
		}

		List<ReadedTagBean> readedTagList = new ArrayList<ReadedTagBean>();
		// ����HashMap
		Iterator iter = readedTagMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String id = (String) entry.getKey();
			Byte tag = (Byte) entry.getValue();
			readedTagList.add(new ReadedTagBean(id, tag));
		}

		Gson gson = new Gson();
		String content = gson.toJson(readedTagList);
		setReadedTagCache(content, name);
	}
	
	// ɾ��ָ��Cache�ļ�
	public static void delCacheFile(String fileName) {
		File file = new File(MyFileUtils.getSdcardDataCacheDir(MyApplication
				.getInstance().getName())
				+ "/"
				+ getCacheDecodeString(fileName));
		if (file.isFile()) {
			file.delete();
		}
	}
	
	/**
	 * ���컺���ļ���
	 * 
	 * @param url
	 *            �����URL��ַ
	 * @param keys
	 *            get����ʱ����key��
	 * @param values
	 *            get����ʱֵ��value��
	 * @return �����ļ�������ʽΪ��URL+get����+Cookie+�û���
	 */
	public static String getCacheFileName(String url, ArrayList<String> keys,
			ArrayList<String> values) {
		// �����ļ����еĲ�������
		StringBuilder paramsString = new StringBuilder();
		/* ���� get����Ĳ���String */
		for (int i = 0; i < keys.size(); ++i) {
			paramsString.append(keys.get(i));
			paramsString.append(values.get(i));
		}
		// ���ع���Ļ����ļ���
		return url + paramsString.toString()
				+ MyApplication.getInstance().getName();
	}
}
