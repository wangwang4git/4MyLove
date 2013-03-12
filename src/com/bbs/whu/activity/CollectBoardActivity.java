package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.CollectBoardAdapter;
import com.bbs.whu.utils.MyFontManager;
import com.bbs.whu.xlistview.XListView;

/**
 * �ҵ��ղذ�����棬 �б���ʽ��ʾ�û��ղصİ��棬 ����б�����ת�����������б���棬 �����б�����˵����˵���ܴ�����
 * 
 * @author wwang
 * 
 */
public class CollectBoardActivity extends Activity {
	// �ղذ���б�
	private XListView mListView;
	// �ղذ���б�������
	private CollectBoardAdapter mAdapter;
	// �ղذ���б�����Դ
	final private ArrayList<String> items = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ȡ����ʾtitle��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_collect_board);
		MyFontManager.changeFontType(this);//���õ�ǰActivity������
		
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
		// �ղذ���б�
		mListView = (XListView) findViewById(R.id.collectboard_listview);
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
			}
		});
	}

	/**
	 * ��ʼ��������
	 */
	private void initAdapter() {
		// ����������
		mAdapter = new CollectBoardAdapter(this, items,
				R.layout.collect_board_item);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * �������
	 */
	private void addData() {
		items.add("����ֱͨ��");
		items.add("�о���֮��");
		items.add("Ե�ֵ����");
		items.add("����ؿ�");
		items.add("������Ϣ");
		items.add("����ֱͨ��");
		items.add("�о���֮��");
		items.add("Ե�ֵ����");
		items.add("����ؿ�");
		items.add("������Ϣ");
		items.add("tail");
		mAdapter.notifyDataSetChanged();
	}
}
