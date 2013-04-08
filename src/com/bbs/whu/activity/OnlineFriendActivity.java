package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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
	// 新增好友按钮
	private ImageView addButton;
	// 刷新按钮
	private Button refreshButton;
	// 刷新动态图
	private ImageView refreshImageView;
	// 刷新动作
	private AnimationDrawable refreshAnimationDrawable;
	// 请求码
	private static int requestCode = 0;
	private static final int SERVER_REQUEST_ALL_FRIEND = 1;
	private static final int SERVER_REQUEST_ONLINE_FRIEND = 2;
	private static final int SERVER_REQUEST_ADD_FRIEND = 3;
	public static final int SERVER_REQUEST_DELETE_FRIEND = 4;

	// get参数
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();

	// 图片异步下载下载器
	private ImageLoader imageLoader = ImageLoader.getInstance();
	// 清空内存缓存调用方法
	// imageLoader.clearMemoryCache();
	// 清空文件缓存调用方法
	// imageLoader.clearDiscCache();
	// 图片异步下载缓存设置变量
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 取消显示title栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_online_friend);
		MyFontManager.changeFontType(this);// 设置当前Activity的字体

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.person_head_portrait)
				.showImageForEmptyUri(R.drawable.person_head_portrait)
				.cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(5)).build();

		// 初始化控件
		initView();
		// 初始化适配器
		initAdapter();
		// 初始化handler
		initHandler();
		// 获取好友列表
		requestCode = SERVER_REQUEST_ALL_FRIEND;
		getFriends();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 注销handler
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_SUCCESS, "OnlineFriendActivity");
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_FAIL, "OnlineFriendActivity");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.online_friend_back_icon:
			onBackPressed();
			break;
		case R.id.online_friend_add_icon:
			// 显示新增好友对话框
			showAddFriendDialog();
			break;
		case R.id.online_friend_refresh_button:
			// 刷新
			onRefresh();
			break;
		}
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
		// 新增好友按钮
		addButton = (ImageView) findViewById(R.id.online_friend_add_icon);
		addButton.setOnClickListener(this);
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
				R.layout.online_friend_item, imageLoader, options);
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
					switch (requestCode) {
					case SERVER_REQUEST_ALL_FRIEND:
						if (res == null) {
							// 请求码归零，防止垃圾数据
							requestCode = 0;
							Toast.makeText(OnlineFriendActivity.this,
									"获取信息失败！", Toast.LENGTH_SHORT).show();
						} else {
							// 添加数据
							allFriends.clear();
							allFriends.addAll(MyXMLParseUtils
									.readXml2FriendsAll(res).getFriends());
							// 请求在线好友
							requestCode = SERVER_REQUEST_ONLINE_FRIEND;
							getFriends();
						}
						break;

					case SERVER_REQUEST_ONLINE_FRIEND:
						// 请求码归零，防止垃圾数据
						requestCode = 0;
						if (res == null)
							Toast.makeText(OnlineFriendActivity.this,
									"获取信息失败！", Toast.LENGTH_SHORT).show();
						else {
							// 添加数据
							onlineFriends.clear();
							onlineFriends.addAll(MyXMLParseUtils
									.readXml2FriendsOnline(res).getFriends());
							// 刷新列表
							refreshList();
						}
						break;

					case SERVER_REQUEST_DELETE_FRIEND:
						// 请求码归零，防止垃圾数据
						requestCode = 0;
						// 刷新列表
						onRefresh();
						break;
					case SERVER_REQUEST_ADD_FRIEND:
						// 请求码归零，防止垃圾数据
						requestCode = 0;
						// 刷新列表
						onRefresh();
						// 提示用户
						Toast.makeText(OnlineFriendActivity.this, res,
								Toast.LENGTH_SHORT).show();

					}
				case MyConstants.REQUEST_FAIL:
					break;
				}
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
		if (requestCode == SERVER_REQUEST_ALL_FRIEND)
			values.add("all");
		else if (requestCode == SERVER_REQUEST_ONLINE_FRIEND)
			values.add("online");

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
			if (onlineFriend.getUserid() != null)
				items.add(new FriendBean(onlineFriend.getUserid().toString(),
						onlineFriend.getUserfaceimg().toString(), true));
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
			if (!isOnline) {
				items.add(new FriendBean(allFriend.getID().toString(),
						allFriend.getUserfaceimg().toString(), false));
			}
		}
		// 刷新ListView
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 显示新增好友对话框
	 */
	private void showAddFriendDialog() {
		View dialog = getLayoutInflater().inflate(R.layout.add_friend_dialog,
				(ViewGroup) findViewById(R.id.add_friend_dialog));
		final EditText addFriendName = (EditText) dialog
				.findViewById(R.id.add_friend_name_editText);
		// 弹出回复对话框
		new AlertDialog.Builder(this)
				.setTitle("新增好友")
				.setView(dialog)
				.setPositiveButton("确定",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String name = addFriendName.getText()
										.toString();

								keys.clear();
								values.clear();
								// 添加get参数
								keys.add("app");
								values.add("friend");
								keys.add("add");
								values.add(name);

								// 设置请求码
								requestCode = SERVER_REQUEST_ADD_FRIEND;
								// 请求数据
								MyBBSRequest.mGet(MyConstants.GET_URL, keys,
										values, "OnlineFriendActivity", true,
										OnlineFriendActivity.this);

							}
						}).setNegativeButton("取消", null).show();
	}

	public static void setRequestCode(int requestCode) {
		OnlineFriendActivity.requestCode = requestCode;
	}

	@Override
	public void onRefresh() {
		requestCode = SERVER_REQUEST_ALL_FRIEND;
		// 获取好友列表
		getFriends();
	}

	@Override
	public void onLoadMore() {
	}
}
