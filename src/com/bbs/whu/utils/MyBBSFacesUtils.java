package com.bbs.whu.utils;

import java.util.ArrayList;

import android.content.Context;

import com.bbs.whu.R;
import com.bbs.whu.model.FaceBean;

/**
 * BBS表情获取工具类
 * 
 * @author WWang
 * 
 */
public class MyBBSFacesUtils {
	// BBS表情名称、表情资源ID映射集合
	private static ArrayList<FaceBean> BBSFacesList;

	public static ArrayList<FaceBean> getBBSFacesList() {
		return BBSFacesList;
	}

	public static void setBBSFacesList(ArrayList<FaceBean> bBSFacesList) {
		BBSFacesList = bBSFacesList;
	}

	// 获取BBS表情名称、工程中BBS表情资源ID集合
	public static ArrayList<FaceBean> getBBSFacesList(Context context) {
		if (BBSFacesList != null) {
			return BBSFacesList;
		}
		BBSFacesList = new ArrayList<FaceBean>();

		// 遍历em01.gif至em69.gif
		// 当前BBS只提供以上表情，如果表情增加需要客户端软件本功能模块相应升级
		for (int i = 1; i <= 69; i++) {
			// 构造表情名称
			String faceName = "";
			if (i < 10) {
				faceName = "em0" + i;
			} else {
				faceName = "em" + i;
			}
			// 查找表情资源ID
			try {
				int id = R.drawable.class.getDeclaredField(faceName).getInt(
						context);
				// 添加BBS表情名称、表情资源ID映射对
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
