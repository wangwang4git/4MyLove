package com.bbs.whu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.FavBoardAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.FavBrdBean;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFontManager;
import com.bbs.whu.utils.MyXMLParseUtils;
import com.bbs.whu.xlistview.XListView;
import com.bbs.whu.xlistview.XListView.IXListViewListener;

/**
 * 我的收藏版面界面， 列表形式显示用户收藏的版面， 点击列表项跳转到版面文章列表界面， 长按列表项弹出菜单，菜单项功能待定。
 * 
 * @author wwang
 * 
 */
public class FavBoardActivity extends Activity implements OnClickListener,
		IXListViewListener {
	// 收藏版块列表
	private XListView mListView;
	// 收藏版块列表适配器
	private FavBoardAdapter mAdapter;

	// 收藏版块列表数据源
	private ArrayList<FavBrdBean> items = new ArrayList<FavBrdBean>();
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

	private String select = "0";

	// 进行手势动作时候的坐标
	float x_temp1 = 0, y_temp1 = 0, x_temp2, y_temp2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 取消显示title栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fav_board);
		MyFontManager.changeFontType(this);// 设置当前Activity的字体

		// 获取传入参数
		Intent mIntent = getIntent();
		select = mIntent.getStringExtra("select");

		// 初始化控件
		initView();
		// 初始化适配器
		initAdapter();
		// 初始化handler
		initHandler();
		// 获取收藏版面列表
		getFavBoards();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fav_board_back_icon:
			onBackPressed();
			break;
		case R.id.fav_board_add_icon:
			// 显示新增好友对话框
			// showAddFriendDialog();
			break;
		case R.id.fav_board_refresh_button:
			// 刷新
			onRefresh();
			break;
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 收藏版块列表
		mListView = (XListView) findViewById(R.id.fav_board_listview);
		mListView.setXListViewListener(this);
		// 因为不涉及分页，所以取消“加载更多”的功能
		mListView.setPullLoadEnable(false);
		// 返回按钮
		backButton = (ImageView) findViewById(R.id.fav_board_back_icon);
		backButton.setOnClickListener(this);
		// 新增按钮
		addButton = (ImageView) findViewById(R.id.fav_board_add_icon);
		addButton.setOnClickListener(this);
		// 刷新按钮
		refreshButton = (Button) findViewById(R.id.fav_board_refresh_button);
		refreshButton.setOnClickListener(this);
		// 刷新动态图
		refreshImageView = (ImageView) findViewById(R.id.fav_board_refresh_imageView);
		// 刷新动作
		refreshAnimationDrawable = (AnimationDrawable) refreshImageView
				.getBackground();
	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		// 创建适配器
		mAdapter = new FavBoardAdapter(this, items, R.layout.fav_board_item);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 初始化handler
	 */
	private void initHandler() {
		// 初始化handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 停止刷新动画
				refreshAnimationDrawable.stop();
				refreshImageView.setVisibility(View.GONE);
				refreshButton.setVisibility(View.VISIBLE);

				// 注销handler
				MessageHandlerManager.getInstance().unregister(
						MyConstants.REQUEST_SUCCESS, "FavBoardActivity");
				MessageHandlerManager.getInstance().unregister(
						MyConstants.REQUEST_FAIL, "FavBoardActivity");

				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					// 获取数据后停止刷新
					mListView.stopRefresh();

					String res = (String) msg.obj;
					// 刷新收藏版面列表
					refreshList(res);
				case MyConstants.REQUEST_FAIL:
					break;
				}
			}
		};
		// 注册handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "FavBoardActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "FavBoardActivity");
	}

	/**
	 * 获取收藏版块列表
	 */
	private void getFavBoards() {
		// 显示刷新动画
		refreshButton.setVisibility(View.GONE);
		refreshImageView.setVisibility(View.VISIBLE);
		refreshAnimationDrawable.start();

		// get参数
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.clear();
		values.clear();
		// 添加get参数
		keys.add("app");
		values.add("favor");
		keys.add("select");
		values.add(select);

		// 请求数据
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values,
				"FavBoardActivity", true, this);
	}

	/**
	 * 刷新收藏版面列表
	 */
	private void refreshList(String res) {
		items.clear();
		List<FavBrdBean> favBoardList = MyXMLParseUtils.readXml2FavBrdList(res);
		if (favBoardList == null) {
			// 提醒用户
			Toast.makeText(this, "目录为空", Toast.LENGTH_SHORT).show();
		} else {
			items.addAll(favBoardList);
		}
		// 刷新ListView
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		// 获取收藏版块列表
		getFavBoards();
	}

	@Override
	public void onLoadMore() {
	}
}
