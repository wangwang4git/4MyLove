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
import com.bbs.whu.adapter.PlaybillListAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.PlaybillBean;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyXMLParseUtils;
import com.bbs.whu.xlistview.XListView;
import com.bbs.whu.xlistview.XListView.IXListViewListener;

/**
 * У԰��������, �г�У԰������������� ����б�����ת��У԰����������棬 �����б�����˵����˵���ܴ�����
 * 
 * @author wwang
 * 
 */
public class PlaybillActivity extends Activity implements IXListViewListener {
	private XListView mListView;
	private PlaybillListAdapter mAdapter;
	// У԰�����б�����Դ
	private ArrayList<PlaybillBean> items = new ArrayList<PlaybillBean>();
	// �����������ݵ�handler
	Handler mHandler;
	
	// get����
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playbill);
		// ��ʼ���ؼ�
		initView();
		// ��ʼ��������
		initAdapter();
		// ��ʼ��handler
		initHandler();
		// ����У԰��������
		getPlaybill(false);
		mAdapter.notifyDataSetChanged();
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
		mListView = (XListView) findViewById(R.id.playbill_listview);
		// ���á���ʾ���ࡱ
		mListView.setPullLoadEnable(false);
		mListView.setXListViewListener(this);
	}

	/**
	 * ��ʼ��������
	 */
	private void initAdapter() {
		// ����������
		mAdapter = new PlaybillListAdapter(this, items, R.layout.playbill_item);
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
					refreshPlaybillList(res);
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// ע��handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "PlaybillActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "PlaybillActivity");
	}

	/**
	 * �����Ƽ���������
	 * 
	 * @param isForcingWebGet
	 *            �Ƿ�ǿ�ƴ������ȡ����
	 */

	private void getPlaybill(boolean isForcingWebGet) {
		keys.clear();
		values.clear();
		// ���get����
		keys.add("app");
		values.add("poster");
		// ��������
		// ע�⣬��Ϊ�漰��tab��Ƕ�ף�����ֱ�Ӵ���Activity��context������Ի���ʱ�����
		// ��Ҫ���븸Activity��context����ʹ��this.getParent()������this
		// �ο����ӣ�http://iandroiddev.com/post/2011-07-11/2817890
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values,
				"PlaybillActivity", isForcingWebGet, this.getParent());
	}

	/**
	 * ˢ���б�
	 * 
	 * @param res
	 *            ����
	 */
	private void refreshPlaybillList(String res) {
		// ���ԭ������
		items.clear();
		// XML�����л�
		ArrayList<PlaybillBean> playbills = (ArrayList<PlaybillBean>) MyXMLParseUtils
				.readXml2PlaybillList(res);
		// ��̳��������ȷ���ݷ��أ���ʾ������ʾ
		if (null == playbills) {
			// ���á�����ˢ�¡�
			mListView.setPullRefreshEnable(false);
			// ���á���ʾ���ࡱ
			mListView.setPullLoadEnable(false);
			// ɾ��ָ��Cache�ļ�
			MyBBSCache.delCacheFile(MyBBSCache.getCacheFileName(
					MyConstants.GET_URL, keys, values));
			// toast����
			Toast.makeText(this, R.string.bbs_exception_text,
					Toast.LENGTH_SHORT).show();
			return;
		}
		items.addAll(playbills);
		// ˢ��ListView
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		// ǿ�ƴ����������Ƽ���������
		getPlaybill(true);
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
