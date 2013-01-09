package com.bbs.whu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ExpandableListView;

import com.bbs.whu.R;
import com.bbs.whu.adapter.BoardListAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.BoardBean;
import com.bbs.whu.model.board.Board;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyXMLParseUtils;

public class BoardActivity extends Activity {
	private ExpandableListView mExpandableListView;
	private BoardListAdapter mAdapter;
	// 一级列表数据
	private List<BoardBean> groups = new ArrayList<BoardBean>();
	// 二级列表数据
	private List<List<Board>> childs = new ArrayList<List<Board>>();
	// 接收请求数据的handler
	Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		// 初始化控件
		initView();
		// 初始化适配器
		initAdapter();
		// 初始化handler
		initHandler();
		// 获取列表数据
		getBoardList(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 只有捕获返回键，并返回false，才能在MainActivity中捕获返回键
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return false;
		}
		return false;
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mExpandableListView = (ExpandableListView) findViewById(R.id.board_elist);
		mExpandableListView.setGroupIndicator(null);
	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		// 创建适配器
		mAdapter = new BoardListAdapter(this, groups, childs);
		mExpandableListView.setAdapter(mAdapter);
	}

	/**
	 * 初始化handler
	 */
	private void initHandler() {
		// 初始化handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					String res = (String) msg.obj;
					// 刷新列表
					refreshBoardList(res);
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// 注册handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "BoardActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "BoardActivity");
	}

	/**
	 * 请求帖子列表数据
	 * 
	 * @param isForcingWebGet
	 *            是否强制从网络获取数据
	 */
	private void getBoardList(boolean isForcingWebGet) {
		// 添加get参数
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("app");
		values.add("boards");
		// 请求数据
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values, "BoardActivity",
				isForcingWebGet, this);
	}

	/**
	 * 刷新列表
	 * 
	 * @param res
	 *            数据
	 */
	private void refreshBoardList(String res) {
		groups.clear();
		childs.clear();

		List<BoardBean> boardBeanList = MyXMLParseUtils.readXml2BoardList(res);

		// 添加一级列表数据
		groups.addAll(boardBeanList);
		// 添加二级列表数据
		for (int i = 0; i < boardBeanList.size(); ++i) {
			childs.add(boardBeanList.get(i).getBoards());
		}
		// 刷新ListView
		mAdapter.notifyDataSetChanged();
	}
}
