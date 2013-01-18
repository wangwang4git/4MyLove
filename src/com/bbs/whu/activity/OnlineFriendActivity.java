package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.OnlineFriendAdapter;
import com.bbs.whu.xlistview.XListView;

/**
 * 我的好友界面， 列表形式显示用户的好友， 点击列表项无操作， 长按列表项弹出菜单，选择向该好友发送即时消息或者邮件。
 * 
 * @author wwang
 * 
 */
public class OnlineFriendActivity extends Activity {
	// 我的好友列表
	private XListView mListView;
	// 我的好友列表适配器
	private OnlineFriendAdapter mAdapter;
	// 我的好友列表数据源
	final private ArrayList<String> items = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 取消显示title栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_online_friend);

		// 初始化控件
		initView();
		// 初始化适配器
		initAdapter();
		// 添加数据
		addData();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 我的好友列表
		mListView = (XListView) findViewById(R.id.onlinefriend_listview);
		// 禁用“显示更多”
		mListView.setPullLoadEnable(false);
		// 禁用“下拉刷新”
		mListView.setPullRefreshEnable(false);
		// 取消ListView分割线
		// mListView.setDividerHeight(0);
		// 设置点击监听器
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(arg0.getContext(),
						"点击的是 " + items.get((int) arg3), Toast.LENGTH_SHORT)
						.show();
				// 跳转到邮件发送界面
				arg0.getContext().startActivity(
						new Intent(arg0.getContext(), MailSendActivity.class));
			}
		});
	}
	
	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		// 创建适配器
		mAdapter = new OnlineFriendAdapter(this, items,
				R.layout.online_friend_item);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 添加数据
	 */
	private void addData() {
		items.add("zhl2007");
		items.add("whuuser");
		items.add("zhl2007");
		items.add("whuuser");
		items.add("zhl2007");
		items.add("whuuser");
		items.add("zhl2007");
		items.add("whuuser");		
		items.add("tail");
		mAdapter.notifyDataSetChanged();
	}
}
