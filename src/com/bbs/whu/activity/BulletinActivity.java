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
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.BulletinAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.BulletinBean;
import com.bbs.whu.model.bulletin.Page;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSCache;
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
public class BulletinActivity extends Activity implements IXListViewListener,
		OnClickListener {
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
	// 帖子回复按钮
	private Button replyButton;
	// 接收请求数据的handler
	Handler mHandler;
	// 回复列表
	private XListView mListView;
	// get参数
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();

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
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bulletin_reply_button:
			// 回复本帖
			goToBulletinReply();
			break;
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 回复列表
		mListView = (XListView) findViewById(R.id.bulletin_listview);
		mListView.setXListViewListener(this);
		// 回复按钮
		replyButton = (Button) findViewById(R.id.bulletin_reply_button);
		replyButton.setOnClickListener(this);
		// 如果是匿名用户，不显示回复按钮
		if (MyApplication.getInstance().getName().equals("4MyLove"))
			replyButton.setVisibility(View.GONE);
		else
			replyButton.setVisibility(View.VISIBLE);
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
		keys.clear();
		values.clear();
		// 添加get参数
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
		// 论坛错误，无正确数据返回，显示错误提示
		if (null == page) {
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
			return;
		}

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
	
	/**
	 * 回复本帖
	 */
	private void goToBulletinReply() {
		BulletinBean item = items.get(0);
		String itemBoard = item.getBoard();
		String itemId = item.getId();
		String itemTitle = item.getTitle();
		String itemAuthor = item.getAuthor();
		String itemBody = item.getText();
		String itemSign = item.getSign();

		// 跳转到帖子回复界面
		Intent intent = new Intent(this, BulletinReplyActivity.class);

		// 添加参数
		intent.putExtra("head", "帖子回复");
		intent.putExtra("board", itemBoard);
		intent.putExtra("id", itemId);
		intent.putExtra("title", itemTitle);
		intent.putExtra("author", itemAuthor);
		intent.putExtra("content", itemBody);
		intent.putExtra("signature", itemSign);

		// 启动Activity。并传递一个intend对象
		this.startActivity(intent);
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
