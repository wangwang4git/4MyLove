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
 * 我的山水界面， 可通过点击“个人资料”进入个人资料界面， 可通过点击“站内信息”进入站内信息界面， 可通过点击“收藏版面”进入收藏版面界面，
 * 可通过点击“在线好友”进入在线好友界面。
 * 
 * @author wwang
 * 
 */
public class MineActivity extends Activity implements View.OnClickListener {
	// 个人资料点击控件（非按钮）
	LinearLayout mPerson;
	// 站内信件点击控件
	LinearLayout mMail;
	// 收藏版面点击控件
	LinearLayout mBoard;
	// 在线好友点击控件
	LinearLayout mFriend;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine);
		
		// 初始化控件
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mPerson = (LinearLayout) findViewById(R.id.mine_person);
		// 设置监听器
		mPerson.setOnClickListener(this);
		// 设置tag，用于onClick来源判断
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
			// 跳转到个人资料页面
			Intent intent = new Intent(this, PersonActivity.class);
			// 添加参数
			intent.putExtra("author",
					(Boolean) ((MyApplication) getApplication()).getName()
							.equals("4MyLove") ? "wwang"
							: ((MyApplication) getApplication()).getName());
			this.startActivity(intent);
			break;
		case 2:
			// 跳转到站内信件页面
			break;
		case 3:
			// 跳转到收藏版面页面
			this.startActivity(new Intent(this, CollectBoardActivity.class));
			break;
		case 4:
			// 跳转到在线好友页面
			this.startActivity(new Intent(this, OnlineFriendActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 只有捕获返回键，并返回false，才能在MainActivity中捕获返回键
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return false;
		}
		return false;
	}
}
