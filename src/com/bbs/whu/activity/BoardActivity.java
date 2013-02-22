package com.bbs.whu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.BoardAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.BoardBean;
import com.bbs.whu.model.board.Board;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyXMLParseUtils;

public class BoardActivity extends Activity {
	private ExpandableListView mExpandableListView;
	private BoardAdapter mAdapter;
	// 一级列表数据
	private List<BoardBean> groups = new ArrayList<BoardBean>();
	// 二级列表数据
	private List<List<Board>> childs = new ArrayList<List<Board>>();
	// 接收请求数据的handler
	Handler mHandler;

	// get参数
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();

	// 搜索匹配数据源
	private List<String> mSearchBoardsList = new ArrayList<String>();
	// 搜索匹配适配器
	private ArrayAdapter<String> mSearchAdapter;
	// 搜索布局
	private LinearLayout mSearchLinearLayout;
	// 搜索输入框
	private AutoCompleteTextView mSearchEditText;
	// 搜索确定按钮
	private ImageView mSearchConfirmButton;
	// 触发搜索布局显示按钮
	private Button mShowSearch;
	// 刷新按钮
	private Button refreshButton;
	// 刷新动态图
	private ImageView refreshImageView;
	// 刷新动作
	private AnimationDrawable refreshAnimationDrawable;

	// 请求响应一一对应布尔变量
	private boolean mRequestResponse = false;

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
			// 如果版块搜索正在显示，则隐藏
			if (mSearchLinearLayout.getVisibility() == View.VISIBLE) {
				searchLayoutShow(false);
				return true;
			}
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

		// 获取AutoCompleteTextView对象，输入框
		mSearchEditText = (AutoCompleteTextView) findViewById(R.id.board_search_autocompletetextview);
		// 绑定搜索匹配数据源
		mSearchAdapter = new ArrayAdapter<String>(this,
				R.layout.board_search_match_item, mSearchBoardsList);
		// 给AutoCompleteTextView对象指定ArrayAdapter对象
		mSearchEditText.setAdapter(mSearchAdapter);

		// 获取确定按钮
		mSearchConfirmButton = (ImageView) findViewById(R.id.board_search_confirm);
		// 获取搜索布局
		mSearchLinearLayout = (LinearLayout) findViewById(R.id.board_search_linearlayout);
		// 获取触发搜索布局显示按钮
		mShowSearch = (Button) findViewById(R.id.board_search_show_linearlayout_button);

		mSearchConfirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchLayoutShow(false);
				searchTheBoard(mSearchEditText.getText().toString());
			}
		});

		mShowSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchLayoutShow(true);
			}
		});

		// 刷新按钮
		refreshButton = (Button) findViewById(R.id.board_refresh_button);
		refreshButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 刷新
				getBoardList(true);
			}
		});
		// 刷新动态图
		refreshImageView = (ImageView) findViewById(R.id.board_refresh_imageView);
		// 刷新动作
		refreshAnimationDrawable = (AnimationDrawable) refreshImageView
				.getBackground();
	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		// 创建适配器
		mAdapter = new BoardAdapter(this, groups, childs);
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
				// 停止刷新动画
				refreshAnimationDrawable.stop();
				refreshImageView.setVisibility(View.GONE);
				refreshButton.setVisibility(View.VISIBLE);

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
		// 显示刷新动画
		refreshButton.setVisibility(View.GONE);
		refreshImageView.setVisibility(View.VISIBLE);
		refreshAnimationDrawable.start();
		keys.clear();
		values.clear();
		// 添加get参数
		keys.add("app");
		values.add("boards");
		// 请求数据
		mRequestResponse = true;
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
		// 论坛错误，无正确数据返回，显示错误提示
		if (null == boardBeanList) {
			if (mRequestResponse == true) {
				mRequestResponse = false;
				// 删除指定Cache文件
				MyBBSCache.delCacheFile(MyBBSCache.getCacheFileName(
						MyConstants.GET_URL, keys, values));
				// toast提醒
				Toast.makeText(this, R.string.bbs_exception_text,
						Toast.LENGTH_SHORT).show();
			}
			return;
		}

		// 添加一级列表数据
		groups.addAll(boardBeanList);
		// 添加二级列表数据
		for (int i = 0; i < boardBeanList.size(); ++i) {
			childs.add(boardBeanList.get(i).getBoards());
		}
		// 刷新ListView
		mAdapter.notifyDataSetChanged();

		mSearchBoardsList.clear();
		// 构造搜索匹配数据源
		for (int i = 0; i < boardBeanList.size(); ++i) {
			for (int j = 0; j < boardBeanList.get(i).getBoards().size(); ++j) {
				mSearchBoardsList.add(boardBeanList.get(i).getBoards().get(j)
						.getName().getAttributeValue());
				mSearchBoardsList.add(boardBeanList.get(i).getBoards().get(j)
						.getId().getAttributeValue());
			}
		}
		mSearchAdapter.notifyDataSetChanged();
	}

	/**
	 * 搜索布局显示与隐藏切换
	 * 
	 * @param bool
	 *            true，显示；false，隐藏
	 */
	void searchLayoutShow(boolean bool) {
		if (true == bool) {
			// 清空搜索输入框
			mSearchEditText.setText("");
			// 显示
			mSearchLinearLayout.setVisibility(View.VISIBLE);
			mShowSearch.setVisibility(View.INVISIBLE);
		} else {
			// 隐藏软键盘
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
			mSearchLinearLayout.setVisibility(View.INVISIBLE);
			mShowSearch.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 搜索输入的版块名，并进行界面跳转
	 * 
	 * @param keyword
	 *            输入的版块名
	 */
	void searchTheBoard(String keyword) {
		// 遍历查找
		for (int i = 0; i < childs.size(); ++i) {
			for (int j = 0; j < childs.get(i).size(); ++j) {
				// 查询该版块名存在
				if (keyword.equals(childs.get(i).get(j).getName()
						.getAttributeValue())
						|| keyword.equals(childs.get(i).get(j).getId()
								.getAttributeValue())) {
					// 跳转到帖子列表界面
					Intent mIntent = new Intent(this, TopicActivity.class);
					// 添加参数 app=topics&board=PieFriends&page=1
					mIntent.putExtra("board", childs.get(i).get(j).getId()
							.getAttributeValue());
					mIntent.putExtra("name", childs.get(i).get(j).getName()
							.getAttributeValue());
					this.startActivity(mIntent);
					return;
				}
			}
		}
		// 查询该版块名不存在
		Toast.makeText(this, "版面不存在", Toast.LENGTH_SHORT).show();
		return;
	}
}
