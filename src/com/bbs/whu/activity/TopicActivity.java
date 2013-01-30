package com.bbs.whu.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.TopicAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.TopicBean;
import com.bbs.whu.model.topic.Topics;
import com.bbs.whu.progresshud.ProgressHUDTask;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSCache;
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

public class TopicActivity extends Activity implements IXListViewListener,
		OnClickListener {
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
	// 帖子发表按钮
	private Button publishButton;
	// 接收请求数据的handler
	Handler mHandler;

	// get参数
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();
	
	// 等待对话框
	private ProgressHUDTask mProgress;
	
	// 请求响应一一对应布尔变量
	private boolean mRequestResponse = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_topic);
		// 显示等待对话框
		mProgress = new ProgressHUDTask(this);
		mProgress.execute();
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

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.topic_publish_button:
			// 发表新帖
			publishBulletin();
			break;
		}
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
		// 发表按钮
		publishButton = (Button) findViewById(R.id.topic_publish_button);
		publishButton.setOnClickListener(this);
		// 如果是匿名用户，不显示发表按钮
		if (MyApplication.getInstance().getName().equals("4MyLove"))
			publishButton.setVisibility(View.GONE);
		else
			publishButton.setVisibility(View.VISIBLE);
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
				// 取消显示等待对话框
				if (mProgress != null) {
					mProgress.dismiss();
					mProgress = null;
				}
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
		// 显示等待对话框
		if (null == mProgress) {
			mProgress = new ProgressHUDTask(this);
			mProgress.execute();
		}
		keys.clear();
		values.clear();
		// 添加get参数
		keys.add("app");
		values.add("topics");
		keys.add("board");
		values.add(board);
		keys.add("page");
		values.add(Integer.toString(currentPage));

		// 请求数据
		mRequestResponse = true;
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
		// 论坛错误，无正确数据返回，显示错误提示
		if (null == topics) {
			if (mRequestResponse == true) {
				mRequestResponse = false;
				// 禁用“下拉刷新”
				mListView.setPullRefreshEnable(false);
				// 禁用“显示更多”
				mListView.setPullLoadEnable(false);
				// 删除指定Cache文件
				MyBBSCache.delCacheFile(MyBBSCache.getCacheFileName(
						MyConstants.GET_URL, keys, values));
				// toast提醒
				Toast.makeText(this, R.string.bbs_exception_text,
						Toast.LENGTH_SHORT).show();
			}
			return;
		}
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

	/**
	 * 发表新帖
	 */
	private void publishBulletin() {
		// 跳转到帖子回复界面
		Intent intent = new Intent(this, BulletinReplyActivity.class);

		// 添加参数
		intent.putExtra("head", MyConstants.NEW_BULLETIN);
		intent.putExtra("board", board);
		intent.putExtra("id", "0");

		// 启动Activity。并传递一个intend对象
		this.startActivity(intent);
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