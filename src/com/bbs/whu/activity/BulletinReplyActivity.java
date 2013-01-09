package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;

public class BulletinReplyActivity extends Activity implements OnClickListener {
	private String board;// 版块
	private String id;// id号
	private String title;// 标题
	private String author;// 作者
	private String content;// 内容
	private String signature;// 签名

	// 回复标题
	TextView replyTitle;
	// 回复内容
	TextView replyContent;
	// 发表按钮
	Button publishButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bulletin_reply);

		// 获取传过来的数据，加载到界面上
		Intent postInfoIntent = getIntent();
		board = postInfoIntent.getStringExtra("board");// 版块
		id = postInfoIntent.getStringExtra("id");// id号
		title = postInfoIntent.getStringExtra("title");// 标题
		author = postInfoIntent.getStringExtra("author");// 作者
		content = postInfoIntent.getStringExtra("content");// 内容
		signature = postInfoIntent.getStringExtra("signature");// 签名

		// 初始化控件
		initView();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bulletin_reply_publish_button:
			// 回复帖子
			String font = "";
			replyPost(board, id, font, replyTitle.getText().toString(),
					replyContent.getText().toString(), signature);
			break;
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 初始化标题并设置初始值
		replyTitle = (TextView) findViewById(R.id.bulletin_reply_title);
		replyTitle.setText(titleFormat(title));
		// 初始化内容并设置初始值
		replyContent = (TextView) findViewById(R.id.bulletin_reply_content);
		replyContent.setText(contentFormat(author, content));
		// 初始化“发表”按钮
		publishButton = (Button) findViewById(R.id.bulletin_reply_publish_button);
		publishButton.setOnClickListener(this);
	}

	// 回复帖子的标题格式
	private String titleFormat(String originalTitle) {
		return "res:" + originalTitle;
	}

	// 回复帖子的内容格式:【 在 xioumu 的大作中提到: 】 : PPT 或 PPTX文件请上传到此贴。
	private String contentFormat(String originalAuthor, String originalContent) {
		// 内容
		return "【在" + originalAuthor + "的大作中提到: 】 : " + originalContent;
	}

	/*
	 * @mboard:版块名
	 * 
	 * @mreID:帖子ID
	 * 
	 * @mfont:字体
	 * 
	 * @msubject:标题
	 * 
	 * @mContent:内容
	 */
	private void replyPost(String mboard, String mreID, String mfont,
			String msubject, String mContent, String msignature) {
		// 添加post请求参数
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("board");
		values.add(mboard);
		keys.add("reID");
		values.add(mreID);
		keys.add("font");
		values.add(mfont);
		keys.add("subject");
		values.add(msubject);
		keys.add("Content");
		values.add(mContent);
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
