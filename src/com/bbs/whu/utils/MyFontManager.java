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
 * ��������֧࣬��APP��������á�
 * @author ljp
 *
 */
public class MyFontManager {
	//�޸�Activity����
	public static void changeFontType(Activity activity) {
		changeFontsByListView(getAllChildViews(activity), activity);
	}

	//��ȡActivity�����пؼ�
	private static List<View> getAllChildViews(Activity activity) {
		return getAllChildViews(activity.getWindow().getDecorView());
	}

	// ��ȡָ��View�����пؼ�
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

	//�޸�Activity������ΪMicrosoft Sans Serif���͵�
	private static void changeFontsByListView(List<View> children, Activity act) {
		changeFontsByListView(children, act, "Microsoft Sans Serif");
	}

	//�޸�Activity������
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
