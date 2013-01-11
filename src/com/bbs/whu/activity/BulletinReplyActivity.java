package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;

public class BulletinReplyActivity extends Activity implements OnClickListener {
	private String originBoard;// 原帖版块
	private String originId;// 原帖id号
	private String originTitle;// 原帖标题
	private String originAuthor;// 原帖作者
	private String originContent;// 原帖内容

	// 回复标题
	TextView replyTitle;
	// 回复内容
	TextView replyContent;
	// 发表按钮
	Button publishButton;
	// 接收请求数据的handler
	Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bulletin_reply);

		// 获取传过来的数据，加载到界面上
		Intent postInfoIntent = getIntent();
		originBoard = postInfoIntent.getStringExtra("board");// 版块
		originId = postInfoIntent.getStringExtra("id");// id号
		originTitle = postInfoIntent.getStringExtra("title");// 标题
		originAuthor = postInfoIntent.getStringExtra("author");// 作者
		originContent = postInfoIntent.getStringExtra("content");// 内容

		// 初始化控件
		initView();
		// 初始化handler
		initHandler();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bulletin_reply_publish_button:
			// 回复帖子
			replyPost();
			break;
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 初始化标题并设置初始值
		replyTitle = (TextView) findViewById(R.id.bulletin_reply_title);
		replyTitle.setText(titleFormat(originTitle));
		// 初始化内容并设置初始值
		replyContent = (TextView) findViewById(R.id.bulletin_reply_content);
		replyContent.setText(contentFormat(originAuthor, originContent));
		// 初始化“发表”按钮
		publishButton = (Button) findViewById(R.id.bulletin_reply_publish_button);
		publishButton.setOnClickListener(this);
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
					// 评论成功
					Toast.makeText(BulletinReplyActivity.this, "评论成功！",
							Toast.LENGTH_SHORT).show();
					// 退出本Activity
					finish();
					break;
				case MyConstants.REQUEST_FAIL:
					// 评论失败
					Toast.makeText(BulletinReplyActivity.this, "评论失败！",
							Toast.LENGTH_SHORT).show();
					// 退出本Activity
					finish();
					break;
				}
				return;
			}
		};
		// 注册handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "BulletinReplyActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "BulletinReplyActivity");
	}

	/**
	 * 回复帖子的标题格式
	 * 
	 * @param originTitle
	 *            原帖标题
	 * @return
	 */
	private String titleFormat(String originTitle) {
		return "res:" + originTitle;
	}

	/**
	 * 回复帖子的内容格式:
	 * 【 在 xioumu 的大作中提到: 】 : PPT 或 PPTX文件请上传到此贴。
	 * 
	 * @param originAuthor
	 *            原帖作者
	 * @param originContent
	 *            原帖内容
	 * @return
	 */
	private String contentFormat(String originAuthor, String originContent) {
		// 内容
		return "\n【在" + originAuthor + "的大作中提到: 】 : " + originContent;
	}

	/**
	 * 发表评论
	 */
	private void replyPost() {
		// 添加post请求参数
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("board");
		values.add(originBoard);
		keys.add("reID");
		values.add(originId);
		keys.add("font");
		values.add("");
		keys.add("subject");
		values.add(replyTitle.getText().toString());
		keys.add("Content");
		values.add(replyContent.getText().toString());
		keys.add("signature");
		// if (msignature.equals("--"))
		values.add("0");
		// else
		// values.add(msignature);

		// post请求
		MyBBSRequest.mPost(MyConstants.POST_BULLETIN_REPLY_URL, keys, values,
				"BulletinReplyActivity", this);

	}
}
