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
 * ��ʮ������������
 * �г���ʮ���������������
 * 
 * @author wwang
 * 
 */
public class TopTenActivity extends Activity implements IXListViewListener {
	private XListView mListView;
	private TopTenAdapter mAdapter;
	private ArrayList<TopTenBean> items = new ArrayList<TopTenBean>();
	// �����������ݵ�handler
	Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_ten);
		// ��ʼ���ؼ�
		initView();
		// ��ʼ��������
		initAdapter();
		// ��ʼ��handler
		initHandler();
		// ����ʮ������
		getTopTen(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ֻ�в��񷵻ؼ���������false��������MainActivity�в��񷵻ؼ�
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return false;
		}
		return false;
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		mListView = (XListView) findViewById(R.id.topten_listview);
		// ���á���ʾ���ࡱ
		mListView.setPullLoadEnable(false);
		mListView.setXListViewListener(this);
	}

	/**
	 * ��ʼ��������
	 */
	private void initAdapter() {
		// ����������
		mAdapter = new TopTenAdapter(this, items, R.layout.top_ten_item);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * ��ʼ��handler
	 */
	private void initHandler() {
		// ��ʼ��handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					String res = (String) msg.obj;
					// ��ȡ���ݺ�ֹͣˢ��
					mListView.stopRefresh();
					// ˢ���б�
					refreshTopTenList(res);
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// ע��handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "TopTenActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "TopTenActivity");
	}

	/**
	 * ����ʮ������
	 * 
	 * @param isForcingWebGet
	 *            �Ƿ�ǿ�ƴ������ȡ����
	 */

	private void getTopTen(boolean isForcingWebGet) {
		// ���get����
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("app");
		values.add("hot");
		// ��������
		// ע�⣬��Ϊ�漰��tab��Ƕ�ף�����ֱ�Ӵ���Activity��context������Ի���ʱ�����
		// ��Ҫ���븸Activity��context����ʹ��this.getParent()������this
		// �ο����ӣ�http://iandroiddev.com/post/2011-07-11/2817890
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values, "TopTenActivity",
				isForcingWebGet, this.getParent());
	}

	/**
	 * ˢ���б�
	 * 
	 * @param res
	 *            ����
	 */
	private void refreshTopTenList(String res) {
		// ���ԭ������
		items.clear();
		// XML�����л�
		items.addAll(MyXMLParseUtils.readXml2TopTenList(res));
		// ˢ��ListView
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		// ǿ�ƴ���������ʮ������
		getTopTen(true);
		// ��ʾˢ��ʱ��
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm",
				Locale.SIMPLIFIED_CHINESE);
		String timeStr = sdf.format(new Date());
		mListView.setRefreshTime(timeStr);
	}

	@Override
	public void onLoadMore() {
	}
}
