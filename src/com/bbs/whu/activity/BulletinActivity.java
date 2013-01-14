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

import com.bbs.whu.R;
import com.bbs.whu.adapter.BulletinAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.BulletinBean;
import com.bbs.whu.model.bulletin.Page;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyRegexParseUtils;
import com.bbs.whu.utils.MyXMLParseUtils;
import com.bbs.whu.xlistview.XListView;
import com.bbs.whu.xlistview.XListView.IXListViewListener;

/**
 * 帖子详情界面， 可通过点击“刷新”，刷新帖子， 可通过点击“显示更多”加载更多，按页加载
 * 
 * @author double
 * 
 */
public class BulletinActivity extends Activity implements IXListViewListener {
	/* 帖子版块英文名与帖子ID由上一级Activity传入，用于请求帖子数据 */
	// 帖子版块英文名
	String board;
	// 帖子ID
	String groupid;
	/* 帖子的页数用于加载内容，web端数据分页传入 */
	// 帖子当前页数
	int currentPage = 1;
	// 帖子总页数
	int totalPage;
	// 是否强制从网络获取数据
	boolean isForcingWebGet = false;
	// 帖子回复列表适配器
	private BulletinAdapter mAdapter;
	// 帖子回复列表
	private ArrayList<BulletinBean> items = new ArrayList<BulletinBean>();
	// 接收请求数据的handler
	Handler mHandler;
	// 回复列表
	private XListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bulletin);
		// 获取传入的参数
		board = getIntent().getStringExtra("board");
		groupid = getIntent().getStringExtra("groupid");
		// 初始化控件
		initView();
		// 初始化适配器
		initAdapter();
		// 初始化handler
		initHandler();
		// 请求数据
		getBulletin();

		// 利用反射机制修改ListView FastScroller默认滑块
		// 参见http://www.eoeandroid.com/forum.php?mod=viewthread&tid=176342原文
		// try {
		// Field field = AbsListView.class.getDeclaredField("mFastScroller");
		// field.setAccessible(true);
		// Object obj = field.get(mListView);
		// field = field.getType().getDeclaredField("mThumbDrawable");
		// field.setAccessible(true);
		// Drawable drawable = (Drawable) field.get(obj);
		// drawable = getResources().getDrawable(R.drawable.ic_launcher);
		// field.set(obj, drawable);
		// // Toast.makeText(this, field.getType().getName(), 1000).show();
		// } catch (Exception e) {
		// throw new RuntimeException(e);
		// }
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 回复列表
		mListView = (XListView) findViewById(R.id.bulletin_listview);
		mListView.setXListViewListener(this);
	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		// 创建适配器
		mAdapter = new BulletinAdapter(this, items,
				R.layout.bulletin_comment_item);
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
					// 刷新帖子界面
					refreshBulletin(res);
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// 注册handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "BulletinActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "BulletinActivity");
	}

	/**
	 * 请求帖子详情数据
	 */
	private void getBulletin() {
		// 添加get参数
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("app");
		values.add("read");
		keys.add("board");
		values.add(board);
		keys.add("GID");
		values.add(groupid);
		keys.add("page");
		values.add(Integer.toString(currentPage));
		// 请求数据
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values,
				"BulletinActivity", isForcingWebGet, this);
	}

	/**
	 * 刷新帖子界面
	 * 
	 * @param res
	 *            数据
	 */
	private void refreshBulletin(String res) {
		// XML反序列化
		Page page = MyXMLParseUtils.readXml2Page(res);
		// 获得当前页号
		currentPage = Integer.parseInt(page.getNum().getAttributeValue());
		// 获得页总数
		totalPage = Integer.parseInt(page.getTotal().getAttributeValue());

		// 如果是最后一页，则禁用“显示更多”
		if (currentPage == totalPage)
			mListView.setPullLoadEnable(false);

		// 获取帖子内容并添加
		items.addAll(MyRegexParseUtils.getContentList(page));
		// 刷新ListView
		mAdapter.notifyDataSetChanged();
		// 当前页增加一页，便于下次申请
		currentPage++;
	}

	@Override
	public void onRefresh() {
		// 将当前页设为首页
		currentPage = 1;
		// 清空数据
		items.clear();
		// 启用强制从网络请求帖子详情数据
		isForcingWebGet = true;
		// 禁用“显示更多”
		mListView.setPullLoadEnable(false);
		// 请求数据
		getBulletin();
		// 显示刷新时间
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm",
				Locale.SIMPLIFIED_CHINESE);
		String timeStr = sdf.format(new Date());
		mListView.setRefreshTime(timeStr);
	}

	@Override
	public void onLoadMore() {
		// 请求数据
		getBulletin();
	}
}
