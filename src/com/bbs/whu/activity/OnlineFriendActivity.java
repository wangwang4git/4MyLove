package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.OnlineFriendAdapter;
import com.bbs.whu.xlistview.XListView;

/**
 * �ҵĺ��ѽ��棬 �б���ʽ��ʾ�û��ĺ��ѣ� ����б����޲����� �����б�����˵���ѡ����ú��ѷ��ͼ�ʱ��Ϣ�����ʼ���
 * 
 * @author wwang
 * 
 */
public class OnlineFriendActivity extends Activity {
	// �ҵĺ����б�
	private XListView mListView;
	// �ҵĺ����б�������
	private OnlineFriendAdapter mAdapter;
	// �ҵĺ����б�����Դ
	final private ArrayList<String> items = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ȡ����ʾtitle��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_online_friend);

		// ��ʼ���ؼ�
		initView();
		// ��ʼ��������
		initAdapter();
		// �������
		addData();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		// �ҵĺ����б�
		mListView = (XListView) findViewById(R.id.onlinefriend_listview);
		// ���á���ʾ���ࡱ
		mListView.setPullLoadEnable(false);
		// ���á�����ˢ�¡�
		mListView.setPullRefreshEnable(false);
		// ȡ��ListView�ָ���
		// mListView.setDividerHeight(0);
		// ���õ��������
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(arg0.getContext(),
						"������� " + items.get((int) arg3), Toast.LENGTH_SHORT)
						.show();
				// ��ת���ʼ����ͽ���
				arg0.getContext().startActivity(
						new Intent(arg0.getContext(), MailSendActivity.class));
			}
		});
	}
	
	/**
	 * ��ʼ��������
	 */
	private void initAdapter() {
		// ����������
		mAdapter = new OnlineFriendAdapter(this, items,
				R.layout.online_friend_item);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * �������
	 */
	private void addData() {
		items.add("zhl2007");
		items.add("whuuser");
		items.add("zhl2007");
		items.add("whuuser");
		items.add("zhl2007");
		items.add("whuuser");
		items.add("zhl2007");
		items.add("whuuser");		
		items.add("tail");
		mAdapter.notifyDataSetChanged();
	}
}
