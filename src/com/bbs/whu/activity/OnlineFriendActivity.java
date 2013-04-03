package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.bbs.whu.R;
import com.bbs.whu.adapter.OnlineFriendAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.FriendBean;
import com.bbs.whu.model.FriendsAllBean;
import com.bbs.whu.model.FriendsOnlineBean;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFontManager;
import com.bbs.whu.utils.MyXMLParseUtils;
import com.bbs.whu.xlistview.XListView;
import com.bbs.whu.xlistview.XListView.IXListViewListener;

/**
 * 我的好友界面， 列表形式显示用户的好友， 点击列表项无操作， 长按列表项弹出菜单，选择向该好友发送即时消息或者邮件。
 * 
 * @author wwang
 * 
 */
public class OnlineFriendActivity extends Activity implements OnClickListener,
		IXListViewListener {
	// 我的好友列表
	private XListView mListView;
	// 我的好友列表适配器
	private OnlineFriendAdapter mAdapter;
	// 全部好友数据源
	private ArrayList<FriendsAllBean> allFriends = new ArrayList<FriendsAllBean>();
	// 在线好友数据源
	private ArrayList<FriendsOnlineBean> onlineFriends = new ArrayList<FriendsOnlineBean>();
	// 我的好友列表数据源
	private ArrayList<FriendBean> items = new ArrayList<FriendBean>();
	// 接收请求数据的handler
	Handler mHandler;
	// 返回按钮
	private ImageView backButton;
	// 刷新按钮
	private Button refreshButton;
	// 刷新动态图
	private ImageView refreshImageView;
	// 刷新动作
	private AnimationDrawable refreshAnimationDrawable;
	// 是否进行在线好友请求
	private boolean isRequestOnlineFriends = false;
	// get参数
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();

	// 进行手势动作时候的坐标
	float x_temp1 = 0, y_temp1 = 0, x_temp2, y_temp2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 取消显示title栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_online_friend);
		MyFontManager.changeFontType(this);// 设置当前Activity的字体

		// 初始化控件
		initView();
		// 初始化适配器
		initAdapter();
		// 初始化handler
		initHandler();
		// 获取好友列表
		getFriends();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.online_friend_back_icon:
			onBackPressed();
			break;
		case R.id.online_friend_refresh_button:
			// 刷新
			onRefresh();
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获得当前坐标
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x_temp1 = x;
			y_temp1 = y;
			break;

		case MotionEvent.ACTION_UP: {
			x_temp2 = x;
			y_temp2 = y;
			// 右滑
			if (x_temp1 != 0 && x_temp2 - x_temp1 > MyConstants.MIN_GAP_X
					&& Math.abs(y_temp2 - y_temp1) < MyConstants.MAX_GAP_Y) {
				onBackPressed();
			}
		}
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 最先响应触屏事件，因为ListView会屏蔽掉Activity的onTouchEvent事件，所以需要重写此方法
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		super.dispatchTouchEvent(event);
		return onTouchEvent(event);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 我的好友列表
		mListView = (XListView) findViewById(R.id.online_friend_listview);
		mListView.setXListViewListener(this);
		// 因为好友不涉及分页，所以取消“加载更多”的功能
		mListView.setPullLoadEnable(false);
		// 返回按钮
		backButton = (ImageView) findViewById(R.id.online_friend_back_icon);
		backButton.setOnClickListener(this);
		// 刷新按钮
		refreshButton = (Button) findViewById(R.id.online_friend_refresh_button);
		refreshButton.setOnClickListener(this);
		// 刷新动态图
		refreshImageView = (ImageView) findViewById(R.id.online_friend_refresh_imageView);
		// 刷新动作
		refreshAnimationDrawable = (AnimationDrawable) refreshImageView
				.getBackground();
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

	private void initHandler() {
		// 初始化handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 停止刷新动画
				refreshAnimationDrawable.stop();
				refreshImageView.setVisibility(View.GONE);
				refreshButton.setVisibility(View.VISIBLE);

				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					String res = (String) msg.obj;
					// 获取数据后停止刷新
					mListView.stopRefresh();

					if (!isRequestOnlineFriends) {
						allFriends.clear();
						allFriends.addAll(MyXMLParseUtils.readXml2FriendsAll(
								res).getFriends());
						// 请求在线好友
						isRequestOnlineFriends = true;
						getFriends();
					} else {
						isRequestOnlineFriends = false;
						onlineFriends.clear();
						onlineFriends.addAll(MyXMLParseUtils
								.readXml2FriendsOnline(res).getFriends());
						// 刷新列表
						refreshList();
					}
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// 注册handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "OnlineFriendActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "OnlineFriendActivity");
	}

	/**
	 * 获取好友列表
	 */
	private void getFriends() {
		// 显示刷新动画
		refreshButton.setVisibility(View.GONE);
		refreshImageView.setVisibility(View.VISIBLE);
		refreshAnimationDrawable.start();

		keys.clear();
		values.clear();
		// 添加get参数
		keys.add("app");
		values.add("friend");
		keys.add("list");
		if (isRequestOnlineFriends)
			values.add("online");
		else
			values.add("all");

		// 请求数据
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values,
				"OnlineFriendActivity", true, this);
	}

	/**
	 * 刷新好友列表
	 */
	private void refreshList() {
		items.clear();
		// 添加在线好友
		for (FriendsOnlineBean onlineFriend : onlineFriends) {
			items.add(new FriendBean(onlineFriend.getUserid().toString(), true));
		}

		// 添加不在线好友
		boolean isOnline;
		for (FriendsAllBean allFriend : allFriends) {
			isOnline = false;
			for (FriendsOnlineBean onlineFriend : onlineFriends) {
				if (allFriend.getID().toString()
						.equals(onlineFriend.getUserid().toString())) {
					isOnline = true;
					break;
				}
			}
			if (!isOnline)
				items.add(new FriendBean(allFriend.getID().toString(), false));
		}
		// 刷新ListView
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		// 获取好友列表
		getFriends();
	}

	@Override
	public void onLoadMore() {
	}
}
