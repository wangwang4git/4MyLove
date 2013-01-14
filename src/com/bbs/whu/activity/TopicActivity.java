package com.bbs.whu.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.adapter.TopicAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.TopicBean;
import com.bbs.whu.model.topic.Topics;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyXMLParseUtils;
import com.bbs.whu.xlistview.XListView;
import com.bbs.whu.xlistview.XListView.IXListViewListener;

/**
 * 分类中的版块帖子列表界面
 * 
 * @author ljp
 * 
 */

public class TopicActivity extends Activity implements IXListViewListener {
	/* 帖子版块英文名由上一级Activity传入，用于请求列表数据 */
	private String board;// 版块英文名字
	private String name;// 版块名称
	/* 帖子的页数用于加载内容，web端数据分页传入 */
	int currentPage = 1;// 当前页号
	// 是否强制从网络获取数据
	boolean isForcingWebGet = false;
	// 标题
	TextView title;
	// 帖子列表
	private XListView mListView;
	// 帖子列表适配器
	private TopicAdapter mAdapter;
	// 帖子列表数据
	private ArrayList<TopicBean> items = new ArrayList<TopicBean>();
	// 接收请求数据的handler
	Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_topic);
		// 获取传入的参数
		board = getIntent().getStringExtra("board");
		name = getIntent().getStringExtra("name");
		// 初始化控件
		initView();
		// 初始化适配器
		initAdapter();
		// 初始化handler
		initHandler();
		// 请求帖子列表数据
		getTopic();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 标题
		title = (TextView) findViewById(R.id.topic_title);
		title.setText(name);
		// 帖子列表
		mListView = (XListView) findViewById(R.id.topic_listview);
		mListView.setXListViewListener(this);
	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		// 创建适配器
		mAdapter = new TopicAdapter(this, items, R.layout.topic_item, board);
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
				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					String res = (String) msg.obj;
					// 启用“显示更多”
					// 注意，默认的“显示更多”功能屏蔽了点击事件，所以要启用
					mListView.setPullLoadEnable(true);
					// 获取数据后停止刷新
					mListView.stopRefresh();
					mListView.stopLoadMore();
					// 刷新列表
					refreshBulletinList(res);
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// 注册handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "TopicActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "TopicActivity");
	}

	/**
	 * 请求帖子列表数据
	 * 
	 */

	private void getTopic() {
		// 添加get参数
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("app");
		values.add("topics");
		keys.add("board");
		values.add(board);
		keys.add("page");
		values.add(Integer.toString(currentPage));

		// 请求数据
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values, "TopicActivity",
				isForcingWebGet, this);
	}

	/**
	 * 刷新列表
	 * 
	 * @param res
	 *            数据
	 */
	private void refreshBulletinList(String res) {
		// XML反序列化
		Topics topics = MyXMLParseUtils.readXml2Topics(res);
		// 获得当前页号
		currentPage = Integer.parseInt(topics.getPage().toString());
		// 最多加载10页
		if (currentPage == 10) {
			// 禁用“显示更多”
			mListView.setPullLoadEnable(false);
		}
		// 获取帖子列表并添加
		items.addAll(topics.getTopics());
		// 刷新ListView
		mAdapter.notifyDataSetChanged();
		// 当前页增加一页，便于下次申请
		currentPage++;
	}

	@Override
	public void onRefresh() {
		// 将当前页设为首页
		currentPage = 1;
		// 清空原有数据
		items.clear();
		// 强制从网络请求帖子列表的数据
		isForcingWebGet = true;
		// 禁用“显示更多”
		mListView.setPullLoadEnable(false);
		// 请求数据
		getTopic();
		// 显示刷新时间
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm",
				Locale.SIMPLIFIED_CHINESE);
		String timeStr = sdf.format(new Date());
		mListView.setRefreshTime(timeStr);
	}

	@Override
	public void onLoadMore() {
		// 请求数据
		getTopic();
	}
}