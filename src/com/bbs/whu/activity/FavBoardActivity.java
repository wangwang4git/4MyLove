package com.bbs.whu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.FavBoardAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.FavBrdBean;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFontManager;
import com.bbs.whu.utils.MyXMLParseUtils;
import com.bbs.whu.xlistview.XListView;
import com.bbs.whu.xlistview.XListView.IXListViewListener;

/**
 * �ҵ��ղذ�����棬 �б���ʽ��ʾ�û��ղصİ��棬 ����б�����ת�����������б���棬 �����б�����˵����˵���ܴ�����
 * 
 * @author wwang
 * 
 */
public class FavBoardActivity extends Activity implements OnClickListener,
		IXListViewListener {
	// �ղذ���б�
	private XListView mListView;
	// �ղذ���б�������
	private FavBoardAdapter mAdapter;

	// �ղذ���б�����Դ
	private ArrayList<FavBrdBean> items = new ArrayList<FavBrdBean>();
	// �����������ݵ�handler
	Handler mHandler;
	// ���ذ�ť
	private ImageView backButton;
	// �������Ѱ�ť
	private ImageView addButton;
	// ˢ�°�ť
	private Button refreshButton;
	// ˢ�¶�̬ͼ
	private ImageView refreshImageView;
	// ˢ�¶���
	private AnimationDrawable refreshAnimationDrawable;

	private String select = "0";

	// �������ƶ���ʱ�������
	float x_temp1 = 0, y_temp1 = 0, x_temp2, y_temp2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ȡ����ʾtitle��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fav_board);
		MyFontManager.changeFontType(this);// ���õ�ǰActivity������

		// ��ȡ�������
		Intent mIntent = getIntent();
		select = mIntent.getStringExtra("select");

		// ��ʼ���ؼ�
		initView();
		// ��ʼ��������
		initAdapter();
		// ��ʼ��handler
		initHandler();
		// ��ȡ�ղذ����б�
		getFavBoards();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// ��õ�ǰ����
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x_temp1 = x;
			y_temp1 = y;
			break;

		case MotionEvent.ACTION_UP: {
			x_temp2 = x;
			y_temp2 = y;
			// �һ�
			if (x_temp1 != 0 && x_temp2 - x_temp1 > MyConstants.MIN_GAP_X
					&& Math.abs(y_temp2 - y_temp1) < MyConstants.MAX_GAP_Y) {
				onBackPressed();
			}
		}
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * ������Ӧ�����¼�����ΪListView�����ε�Activity��onTouchEvent�¼���������Ҫ��д�˷���
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		super.dispatchTouchEvent(event);
		return onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fav_board_back_icon:
			onBackPressed();
			break;
		case R.id.fav_board_add_icon:
			// ��ʾ�������ѶԻ���
			// showAddFriendDialog();
			break;
		case R.id.fav_board_refresh_button:
			// ˢ��
			onRefresh();
			break;
		}
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		// �ղذ���б�
		mListView = (XListView) findViewById(R.id.fav_board_listview);
		mListView.setXListViewListener(this);
		// ��Ϊ���漰��ҳ������ȡ�������ظ��ࡱ�Ĺ���
		mListView.setPullLoadEnable(false);
		// ���ذ�ť
		backButton = (ImageView) findViewById(R.id.fav_board_back_icon);
		backButton.setOnClickListener(this);
		// ������ť
		addButton = (ImageView) findViewById(R.id.fav_board_add_icon);
		addButton.setOnClickListener(this);
		// ˢ�°�ť
		refreshButton = (Button) findViewById(R.id.fav_board_refresh_button);
		refreshButton.setOnClickListener(this);
		// ˢ�¶�̬ͼ
		refreshImageView = (ImageView) findViewById(R.id.fav_board_refresh_imageView);
		// ˢ�¶���
		refreshAnimationDrawable = (AnimationDrawable) refreshImageView
				.getBackground();
	}

	/**
	 * ��ʼ��������
	 */
	private void initAdapter() {
		// ����������
		mAdapter = new FavBoardAdapter(this, items, R.layout.fav_board_item);
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
				// ֹͣˢ�¶���
				refreshAnimationDrawable.stop();
				refreshImageView.setVisibility(View.GONE);
				refreshButton.setVisibility(View.VISIBLE);

				// ע��handler
				MessageHandlerManager.getInstance().unregister(
						MyConstants.REQUEST_SUCCESS, "FavBoardActivity");
				MessageHandlerManager.getInstance().unregister(
						MyConstants.REQUEST_FAIL, "FavBoardActivity");

				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					// ��ȡ���ݺ�ֹͣˢ��
					mListView.stopRefresh();

					String res = (String) msg.obj;
					// ˢ���ղذ����б�
					refreshList(res);
				case MyConstants.REQUEST_FAIL:
					break;
				}
			}
		};
		// ע��handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "FavBoardActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "FavBoardActivity");
	}

	/**
	 * ��ȡ�ղذ���б�
	 */
	private void getFavBoards() {
		// ��ʾˢ�¶���
		refreshButton.setVisibility(View.GONE);
		refreshImageView.setVisibility(View.VISIBLE);
		refreshAnimationDrawable.start();

		// get����
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.clear();
		values.clear();
		// ���get����
		keys.add("app");
		values.add("favor");
		keys.add("select");
		values.add(select);

		// ��������
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values,
				"FavBoardActivity", true, this);
	}

	/**
	 * ˢ���ղذ����б�
	 */
	private void refreshList(String res) {
		items.clear();
		List<FavBrdBean> favBoardList = MyXMLParseUtils.readXml2FavBrdList(res);
		if (favBoardList == null) {
			// �����û�
			Toast.makeText(this, "Ŀ¼Ϊ��", Toast.LENGTH_SHORT).show();
		} else {
			items.addAll(favBoardList);
		}
		// ˢ��ListView
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		// ��ȡ�ղذ���б�
		getFavBoards();
	}

	@Override
	public void onLoadMore() {
	}
}
