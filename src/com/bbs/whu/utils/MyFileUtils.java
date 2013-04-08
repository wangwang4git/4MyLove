package com.bbs.whu.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.os.Environment;

public class MyFileUtils {
	private static final int BUFFER = 8192;
	// �û���������json�ļ���
	public static final String USERPASSWORDNAME = "4MyLove";
	// �Ѷ����json�ļ���
	public static final String READEDTAGNAME = "ReadedTag";
	
	// �����ļ�SD��·��
	private static String mSdcardDataCacheDir;
	// �û���������json�ļ�SD��·��
	private static String mSdcardDataUserPasswordDir;

	public static String getSdcardDataCacheDir(String userName) {
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/whubbs/data/cache/" + userName);
			if (!file.exists()) {
				if (file.mkdirs()) {
					mSdcardDataCacheDir = file.getAbsolutePath();
				}
			} else {
				mSdcardDataCacheDir = file.getAbsolutePath();
			}
		}
		return mSdcardDataCacheDir;
	}
	
	public static String getSdcardDataUserPasswordDir() {
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/whubbs/data/cache");
			if (!file.exists()) {
				if (file.mkdirs()) {
					mSdcardDataUserPasswordDir = file.getAbsolutePath();
				}
			} else {
				mSdcardDataUserPasswordDir = file.getAbsolutePath();
			}
		}
		return mSdcardDataUserPasswordDir;
	}

	// ��ȡ�ļ�
	public static String readTextFile(File file) throws IOException {
		String text = null;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			text = readTextInputStream(is);
			;
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return text;
	}

	// �����ж�ȡ�ļ�
	private static String readTextInputStream(InputStream is) throws IOException {
		StringBuffer strbuffer = new StringBuffer();
		String line;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			while ((line = reader.readLine()) != null) {
				// �س����з��Ƿ���Ҫ��
				// strbuffer.append(line).append("\r\n");
				strbuffer.append(line);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return strbuffer.toString();
	}

	// ���ı�����д���ļ�
	public static void writeTextFile(File file, String str) throws IOException {
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(new FileOutputStream(file));
			out.write(str.getBytes());
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	// �����ļ�
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
			byte[] buffer = new byte[BUFFER];
			int length;
			while ((length = inBuff.read(buffer)) != -1) {
				outBuff.write(buffer, 0, length);
			}
			outBuff.flush();
		} finally {
			if (inBuff != null) {
				inBuff.close();
			}
			if (outBuff != null) {
				outBuff.close();
			}
		}
	}

	// ����MD5ժҪ
	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	// ɾ���ļ���
	public static void delFolder(String folderPath) {
		try {
			// ɾ����������������
			delAllFile(folderPath);
			// ɾ�����ļ���
			new File(folderPath).delete();
		} catch (Exception e) {
			System.out.println("ɾ���ļ��в�������");
			e.printStackTrace();
		}
	}

	// ɾ���ļ�������������ļ�
	private static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				// ��ɾ���ļ���������ļ�
				delAllFile(path + "/" + tempList[i]);
				// ��ɾ�����ļ���
				delFolder(path + "/" + tempList[i]);
			}
		}
	}
}
