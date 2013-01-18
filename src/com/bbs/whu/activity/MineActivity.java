package com.bbs.whu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.bbs.whu.R;
import com.bbs.whu.utils.MyApplication;

/**
 * �ҵ�ɽˮ���棬 ��ͨ��������������ϡ�����������Ͻ��棬 ��ͨ�������վ����Ϣ������վ����Ϣ���棬 ��ͨ��������ղذ��桱�����ղذ�����棬
 * ��ͨ����������ߺ��ѡ��������ߺ��ѽ��档
 * 
 * @author wwang
 * 
 */
public class MineActivity extends Activity implements View.OnClickListener {
	// �������ϵ���ؼ����ǰ�ť��
	LinearLayout mPerson;
	// վ���ż�����ؼ�
	LinearLayout mMail;
	// �ղذ������ؼ�
	LinearLayout mBoard;
	// ���ߺ��ѵ���ؼ�
	LinearLayout mFriend;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine);
		
		// ��ʼ���ؼ�
		initView();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		mPerson = (LinearLayout) findViewById(R.id.mine_person);
		// ���ü�����
		mPerson.setOnClickListener(this);
		// ����tag������onClick��Դ�ж�
		mPerson.setTag(1);

		mMail = (LinearLayout) findViewById(R.id.mine_mail);
		mMail.setOnClickListener(this);
		mMail.setTag(2);

		mBoard = (LinearLayout) findViewById(R.id.mine_board);
		mBoard.setOnClickListener(this);
		mBoard.setTag(3);

		mFriend = (LinearLayout) findViewById(R.id.mine_friend);
		mFriend.setOnClickListener(this);
		mFriend.setTag(4);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = (Integer) v.getTag();
		switch (tag) {
		case 1:
			// ��ת����������ҳ��
			Intent intent = new Intent(this, PersonActivity.class);
			// ��Ӳ���
			intent.putExtra("author",
					(Boolean) ((MyApplication) getApplication()).getName()
							.equals("4MyLove") ? "wwang"
							: ((MyApplication) getApplication()).getName());
			this.startActivity(intent);
			break;
		case 2:
			// ��ת��վ���ż�ҳ��
			break;
		case 3:
			// ��ת���ղذ���ҳ��
			this.startActivity(new Intent(this, CollectBoardActivity.class));
			break;
		case 4:
			// ��ת�����ߺ���ҳ��
			this.startActivity(new Intent(this, OnlineFriendActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ֻ�в��񷵻ؼ���������false��������MainActivity�в��񷵻ؼ�
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return false;
		}
		return false;
	}
}
