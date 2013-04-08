package com.bbs.whu.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 字体管理类，支持APP字体的设置。
 * @author ljp
 *
 */
public class MyFontManager {
	//修改Activity字体
	public static void changeFontType(Activity activity) {
		changeFontsByListView(getAllChildViews(activity), activity);
	}

	//获取Activity里所有控件
	private static List<View> getAllChildViews(Activity activity) {
		return getAllChildViews(activity.getWindow().getDecorView());
	}

	// 获取指定View里所有控件
	private static List<View> getAllChildViews(View view) {
		List<View> allchildren = new ArrayList<View>();
		if (view instanceof ViewGroup) {
			ViewGroup vp = (ViewGroup) view;
			for (int i = 0; i < vp.getChildCount(); i++) {
				View viewchild = vp.getChildAt(i);
				allchildren.add(viewchild);
				allchildren.addAll(getAllChildViews(viewchild));
			}
		}
		return allchildren;
	}

	//修改Activity的字体为Microsoft Sans Serif类型的
	private static void changeFontsByListView(List<View> children, Activity act) {
		changeFontsByListView(children, act, "Microsoft Sans Serif");
	}

	//修改Activity的字体
	private static void changeFontsByListView(List<View> children, Activity act,
			String fontType) {
		String fontTypePath = "fonts/" + fontType + ".ttf";
		Typeface tf = Typeface.createFromAsset(act.getAssets(), fontTypePath);

		for (View v : children) {
			if (v instanceof TextView) {
				((TextView) v).setTypeface(tf);
			} else if (v instanceof Button) {
				((Button) v).setTypeface(tf);
			} else if (v instanceof EditText) {
				((EditText) v).setTypeface(tf);
			}
		}
	}
}
