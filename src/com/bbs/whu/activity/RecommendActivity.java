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
 * �Ƽ����½���, �г��Ƽ����»�������� ����б�����ת���Ƽ����½��棬 �����б�����˵����˵���ܴ�����
 * 
 * @author wwang
 * 
 */
public class RecommendActivity extends Activity implements IXListViewListener {
	private XListView mListView;
	private RecommendListAdapter mAdapter;
	// �Ƽ������б�����Դ
	private ArrayList<RecommendBean> items = new ArrayList<RecommendBean>();
	// �����������ݵ�handler
	Handler mHandler;
	
	// get����
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();
	
	// ������Ӧһһ��Ӧ��������
	private boolean mRequestResponse = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommend);
		MyFontManager.changeFontType(this);//���õ�ǰActivity������
		
		// ��ʼ���ؼ�
		initView();
		// ��ʼ��������
		initAdapter();
		// ��ʼ��handler
		initHandler();
		// �����Ƽ���������
		getRecommend(false);
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
		mListView = (XListView) findViewById(R.id.recommend_listview);
		// ���á���ʾ���ࡱ
		mListView.setPullLoadEnable(false);
		mListView.setXListViewListener(this);
	}

	/**
	 * ��ʼ��������
	 */
	private void initAdapter() {
		// ����������
		mAdapter = new RecommendListAdapter(this, items,
				R.layout.recommend_item);
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
					refreshRecommendList(res);
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// ע��handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "RecommendActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "RecommendActivity");
	}
	
	/**
	 * �����Ƽ���������
	 * 
	 * @param isForcingWebGet
	 *            �Ƿ�ǿ�ƴ������ȡ����
	 */

	private void getRecommend(boolean isForcingWebGet) {
		keys.clear();
		values.clear();
		// ���get����
		keys.add("app");
		values.add("recomm");
		// ��������
		// ע�⣬��Ϊ�漰��tab��Ƕ�ף�����ֱ�Ӵ���Activity��context������Ի���ʱ�����
		// ��Ҫ���븸Activity��context����ʹ��this.getParent()������this
		// �ο����ӣ�http://iandroiddev.com/post/2011-07-11/2817890
		mRequestResponse = true;
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values, "RecommendActivity",
				isForcingWebGet, this.getParent());
	}
	
	/**
	 * ˢ���б�
	 * 
	 * @param res
	 *            ����
	 */
	private void refreshRecommendList(String res) {
		// ���ԭ������
		items.clear();
		// XML�����л�
		ArrayList<RecommendBean> recommends = (ArrayList<RecommendBean>) MyXMLParseUtils
				.readXml2RecommendList(res);
		// ��̳��������ȷ���ݷ��أ���ʾ������ʾ
		if (null == recommends) {
			if (mRequestResponse == true) {
				mRequestResponse = false;
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
			}
			return;
		}
		items.addAll(recommends);
		// ˢ��ListView
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onRefresh() {
		// ǿ�ƴ����������Ƽ���������
		getRecommend(true);
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
