package com.bbs.whu.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.bbs.whu.R;
import com.bbs.whu.adapter.TopTenAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.TopTenBean;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyXMLParseUtils;
import com.bbs.whu.xlistview.XListView;
import com.bbs.whu.xlistview.XListView.IXListViewListener;

/**
 * “十大热帖”界面
 * 列出“十大热帖”基本情况
 * 
 * @author wwang
 * 
 */
public class TopTenActivity extends Activity implements IXListViewListener {
	private XListView mListView;
	private TopTenAdapter mAdapter;
	private ArrayList<TopTenBean> items = new ArrayList<TopTenBean>();
	// 接收请求数据的handler
	Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_ten);
		// 初始化控件
		initView();
		// 初始化适配器
		initAdapter();
		// 初始化handler
		initHandler();
		// 请求“十大”数据
		getTopTen(false);
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
		mListView = (XListView) findViewById(R.id.topten_listview);
		// 禁用“显示更多”
		mListView.setPullLoadEnable(false);
		mListView.setXListViewListener(this);
	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		// 创建适配器
		mAdapter = new TopTenAdapter(this, items, R.layout.top_ten_item);
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
					// 获取数据后停止刷新
					mListView.stopRefresh();
					// 刷新列表
					refreshTopTenList(res);
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// 注册handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "TopTenActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "TopTenActivity");
	}

	/**
	 * 请求“十大”数据
	 * 
	 * @param isForcingWebGet
	 *            是否强制从网络获取数据
	 */

	private void getTopTen(boolean isForcingWebGet) {
		// 添加get参数
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("app");
		values.add("hot");
		// 请求数据
		// 注意，因为涉及到tab的嵌套，所以直接传本Activity的context，构造对话框时会出错，
		// 需要传入父Activity的context，即使用this.getParent()而不是this
		// 参考链接：http://iandroiddev.com/post/2011-07-11/2817890
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values, "TopTenActivity",
				isForcingWebGet, this.getParent());
	}

	/**
	 * 刷新列表
	 * 
	 * @param res
	 *            数据
	 */
	private void refreshTopTenList(String res) {
		// 清空原有数据
		items.clear();
		// XML反序列化
		items.addAll(MyXMLParseUtils.readXml2TopTenList(res));
		// 刷新ListView
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		// 强制从网络请求“十大”数据
		getTopTen(true);
		// 显示刷新时间
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm",
				Locale.SIMPLIFIED_CHINESE);
		String timeStr = sdf.format(new Date());
		mListView.setRefreshTime(timeStr);
	}

	@Override
	public void onLoadMore() {
	}
}
