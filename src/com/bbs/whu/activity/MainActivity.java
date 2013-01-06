package com.bbs.whu.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;

/**
 * 主界面Activity，
 * 包含5个tab，分别是“首页”，“分类”，“我的山水”，“消息”，“更多”
 * 
 * @author double
 * 
 */
public class MainActivity extends TabActivity {
	TabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		tabHost = getTabHost();
		// 添加tab
		setTabs();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 退出登陆
		MyBBSRequest.mGet(MyConstants.LOG_OUT_URL, "MainActivity");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 捕获返回键
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			showDialog();
			return false;
		}
		return false;
	}

	/**
	 * 添加tab
	 */
	private void setTabs() {
		addTab("首页", R.drawable.tab_home, HomeActivity.class);
		addTab("分类", R.drawable.tab_search, BoardActivity.class);
		addTab("我的山水", R.drawable.tab_home, MineActivity.class);
		addTab("消息", R.drawable.tab_search, MessageActivity.class);
		addTab("更多", R.drawable.tab_home, MoreActivity.class);
	}

	/**
	 * 添加一个tab
	 * 
	 * @param labelId
	 *            tab的id
	 * @param drawableId
	 *            tab的图标
	 * @param c
	 *            tab对应的Activity
	 */
	private void addTab(String labelId, int drawableId, Class<?> c) {
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}

	/**
	 * 提示用户是否退出
	 */
	private void showDialog() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 结束程序
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}
}