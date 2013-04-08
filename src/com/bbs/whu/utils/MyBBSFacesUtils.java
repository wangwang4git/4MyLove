package com.bbs.whu.utils;

import java.util.ArrayList;

import android.content.Context;

import com.bbs.whu.R;
import com.bbs.whu.model.FaceBean;

/**
 * BBS�����ȡ������
 * 
 * @author WWang
 * 
 */
public class MyBBSFacesUtils {
	// BBS�������ơ�������ԴIDӳ�伯��
	private static ArrayList<FaceBean> BBSFacesList;

	public static ArrayList<FaceBean> getBBSFacesList() {
		return BBSFacesList;
	}

	public static void setBBSFacesList(ArrayList<FaceBean> bBSFacesList) {
		BBSFacesList = bBSFacesList;
	}

	// ��ȡBBS�������ơ�������BBS������ԴID����
	public static ArrayList<FaceBean> getBBSFacesList(Context context) {
		if (BBSFacesList != null) {
			return BBSFacesList;
		}
		BBSFacesList = new ArrayList<FaceBean>();

		// ����em01.gif��em69.gif
		// ��ǰBBSֻ�ṩ���ϱ��飬�������������Ҫ�ͻ������������ģ����Ӧ����
		for (int i = 1; i <= 69; i++) {
			// �����������
			String faceName = "";
			if (i < 10) {
				faceName = "em0" + i;
			} else {
				faceName = "em" + i;
			}
			// ���ұ�����ԴID
			try {
				int id = R.drawable.class.getDeclaredField(faceName).getInt(
						context);
				// ���BBS�������ơ�������ԴIDӳ���
				FaceBean face = new FaceBean();
				face.setName("[" + faceName + "]");
				face.setId(id);
				BBSFacesList.add(face);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}

		return BBSFacesList;
	}
}
