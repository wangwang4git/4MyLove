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
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.RecommendListAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.RecommendBean;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFontManager;
import com.bbs.whu.utils.MyXMLParseUtils;
import com.bbs.whu.xlistview.XListView;
import com.bbs.whu.xlistview.XListView.IXListViewListener;

/**
 * 推荐文章界面, 列出推荐文章基本情况， 点击列表项跳转到推荐文章界面， 长按列表项弹出菜单，菜单项功能待定。
 * 
 * @author wwang
 * 
 */
public class RecommendActivity extends Activity implements IXListViewListener {
	private XListView mListView;
	private RecommendListAdapter mAdapter;
	// 推荐文章列表数据源
	private ArrayList<RecommendBean> items = new ArrayList<RecommendBean>();
	// 接收请求数据的handler
	Handler mHandler;
	
	// get参数
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();
	
	// 请求响应一一对应布尔变量
	private boolean mRequestResponse = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend);
		MyFontManager.changeFontType(this);//设置当前Activity的字体
		
		// 初始化控件
		initView();
		// 初始化适配器
		initAdapter();
		// 初始化handler
		initHandler();
		// 请求推荐文章数据
		getRecommend(false);
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
		mListView = (XListView) findViewById(R.id.recommend_listview);
		// 禁用“显示更多”
		mListView.setPullLoadEnable(false);
		mListView.setXListViewListener(this);
	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		// 创建适配器
		mAdapter = new RecommendListAdapter(this, items,
				R.layout.recommend_item);
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
					refreshRecommendList(res);
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// 注册handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "RecommendActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "RecommendActivity");
	}
	
	/**
	 * 请求推荐文章数据
	 * 
	 * @param isForcingWebGet
	 *            是否强制从网络获取数据
	 */

	private void getRecommend(boolean isForcingWebGet) {
		keys.clear();
		values.clear();
		// 添加get参数
		keys.add("app");
		values.add("recomm");
		// 请求数据
		// 注意，因为涉及到tab的嵌套，所以直接传本Activity的context，构造对话框时会出错，
		// 需要传入父Activity的context，即使用this.getParent()而不是this
		// 参考链接：http://iandroiddev.com/post/2011-07-11/2817890
		mRequestResponse = true;
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values, "RecommendActivity",
				isForcingWebGet, this.getParent());
	}
	
	/**
	 * 刷新列表
	 * 
	 * @param res
	 *            数据
	 */
	private void refreshRecommendList(String res) {
		// 清空原有数据
		items.clear();
		// XML反序列化
		ArrayList<RecommendBean> recommends = (ArrayList<RecommendBean>) MyXMLParseUtils
				.readXml2RecommendList(res);
		// 论坛错误，无正确数据返回，显示错误提示
		if (null == recommends) {
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
		items.addAll(recommends);
		// 刷新ListView
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onRefresh() {
		// 强制从网络请求推荐文章数据
		getRecommend(true);
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
