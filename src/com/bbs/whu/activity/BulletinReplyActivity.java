package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.FaceGridAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.FaceBean;
import com.bbs.whu.progresshud.ProgressHUDTask;
import com.bbs.whu.utils.MyBBSFacesUtils;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFontManager;

public class BulletinReplyActivity extends Activity {
	private int head;// “帖子回复”还是“帖子发表”
	private String originBoard;// 原帖版块
	private String originId;// 原帖id号
	private String originTitle;// 原帖标题
	private String originAuthor;// 原帖作者
	private String originContent;// 原帖内容

	// 界面标题
	TextView headTextView;
	// 回复标题
	EditText replyTitle;
	// 回复内容
	EditText replyContent;
	// 发表按钮
	Button publishButton;
	// 接收请求数据的handler
	Handler mHandler;

	// 表情选择器切换按钮
	private ImageButton mBtnFace;
	// 软键盘切换按钮
	private ImageButton mBtnKeyboard;
	// 表情选择器
	private GridView mBBSFacesGridView;
	// 表情选择器适配器
	private FaceGridAdapter mAdapter;
	// BBS表情名称、表情资源ID映射集合
	private ArrayList<FaceBean> BBSFacesList = new ArrayList<FaceBean>();
	
	// 等待对话框
	private ProgressHUDTask mProgress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取消标题栏显示
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bulletin_reply);
		MyFontManager.changeFontType(this);//设置当前Activity的字体
		
		// 获取传过来的数据，加载到界面上
		Intent postInfoIntent = getIntent();
		head = postInfoIntent.getIntExtra("head", -1);
		originBoard = postInfoIntent.getStringExtra("board");// 版块
		originId = postInfoIntent.getStringExtra("id");// id号
		originTitle = postInfoIntent.getStringExtra("title");// 标题
		originAuthor = postInfoIntent.getStringExtra("author");// 作者
		originContent = postInfoIntent.getStringExtra("content");// 内容

		// 初始化控件
		initView();
		// 初始化适配器
		initAdapter();
		// 初始化handler
		initHandler();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 设子界面标题
		headTextView = (TextView) findViewById(R.id.bulletin_reply_head);
		if (head == MyConstants.NEW_BULLETIN)
			headTextView.setText("发表新帖");
		else if (head == MyConstants.BULLETIN_REPLY)
			headTextView.setText("帖子回复");

		// 初始化标题并设置初始值
		replyTitle = (EditText) findViewById(R.id.bulletin_reply_title);
		replyTitle.setText(titleFormat(originTitle));
		// 设置输入框焦点变更统一监听器
		replyTitle.setOnFocusChangeListener(bulletinReplyEditFocusListener);

		// 初始化内容并设置初始值
		replyContent = (EditText) findViewById(R.id.bulletin_reply_content);

		// 对执行添加的文本使用指定颜色
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(contentFormat(originAuthor, originContent));
		builder.setSpan(new ForegroundColorSpan(Color.BLUE), 0,
				contentFormat(originAuthor, originContent).length(),
				Spanned.SPAN_COMPOSING);
		replyContent.setText(builder, BufferType.EDITABLE);
		// 设置输入框焦点变更统一监听器
		replyContent.setOnFocusChangeListener(bulletinReplyEditFocusListener);

		// 初始化“发表”按钮
		publishButton = (Button) findViewById(R.id.bulletin_reply_publish_button);
		// 设置按钮统一监听器
		publishButton.setOnClickListener(bulletinReplyClickListener);

		mBtnFace = (ImageButton) findViewById(R.id.bulletin_reply_bottom_face);
		// 设置按钮统一监听器
		mBtnFace.setOnClickListener(bulletinReplyClickListener);
		mBtnFace.setEnabled(false);

		mBtnKeyboard = (ImageButton) findViewById(R.id.bulletin_reply_bottom_keyboard);
		// 设置按钮统一监听器
		mBtnKeyboard.setOnClickListener(bulletinReplyClickListener);
		mBtnKeyboard.setEnabled(false);

		mBBSFacesGridView = (GridView) findViewById(R.id.bulletin_reply_grid_face);
		// 设置监听器
		mBBSFacesGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 插入表情名称
				insertText(replyContent, BBSFacesList.get(arg2).getName());
			}
		});
	}

	/**
	 * 初始化handler
	 */
	private void initHandler() {
		// 初始化handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 取消显示等待对话框
				if (mProgress != null) {
					mProgress.dismiss();
					mProgress = null;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String toastString;
				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					if (head == MyConstants.NEW_BULLETIN)
						toastString = "发表成功！";
					else
						toastString = "评论成功！";
					Toast.makeText(BulletinReplyActivity.this, toastString,
							Toast.LENGTH_SHORT).show();
					// 退出本Activity
					finish();
					break;
				case MyConstants.REQUEST_FAIL:
					if (head == MyConstants.NEW_BULLETIN)
						toastString = "发表失败！";
					else
						toastString = "评论失败！";
					Toast.makeText(BulletinReplyActivity.this, toastString,
							Toast.LENGTH_SHORT).show();
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
		// 发表新帖
		if (head == MyConstants.NEW_BULLETIN)
			return "";
		// 格式化原帖标题
		if (!originTitle.startsWith("Re: ")) {
			originTitle = "Re: " + originTitle;
		}
		return originTitle;
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
		// 发表新帖
		if (head == MyConstants.NEW_BULLETIN)
			return "";
		// 格式化原帖内容
		// 如果原文内容太长，进行删减
		String content = originContent.length() > 20 ? originContent.substring(
				0, 20) + "......" : originContent;
		content = content.replaceAll("\n\n", "\n");
		content = content.replaceAll("\n", "\n: ");
		content += "\n:  来自Android客户端.........";
		// 内容
		return "\n【 在" + originAuthor + "的大作中提到： 】\n: " + content;
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
		// 显示等待对话框
		if (null == mProgress) {
			mProgress = new ProgressHUDTask(this);
			mProgress.execute();
		}
	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		// 获取BBS表情映射集合
		BBSFacesList = MyBBSFacesUtils.getBBSFacesList(this);
		// 创建适配器
		mAdapter = new FaceGridAdapter(this, BBSFacesList, R.layout.face_item);
		// 设置适配器
		mBBSFacesGridView.setAdapter(mAdapter);
	}

	/**
	 * 按钮单击统一监听器
	 */
	private OnClickListener bulletinReplyClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bulletin_reply_publish_button:
				// 回复帖子
				replyPost();
				break;
			case R.id.bulletin_reply_bottom_face:
				// 表情选择器显示
				facesGridViewShow(true);
				break;
			case R.id.bulletin_reply_bottom_keyboard:
				// 表情选择器隐藏
				facesGridViewShow(false);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 输入框焦点变更统一监听器
	 */
	private OnFocusChangeListener bulletinReplyEditFocusListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bulletin_reply_title:
				// 焦点切入切出标题EditText，都置表情切换按钮、键盘切换按钮处于不能点击状态
				mBtnFace.setEnabled(false);
				mBtnKeyboard.setEnabled(false);
				break;
			case R.id.bulletin_reply_content:
				if (hasFocus) {
					// 焦点切入正文EditText，置表情切换按钮、键盘切换按钮处于可以点击状态
					mBtnFace.setEnabled(true);
					mBtnKeyboard.setEnabled(true);
				} else {
					// 取消表情选择器显示
					facesGridViewShow(false);
					// 焦点切出正文EditText，置表情切换按钮、键盘切换按钮处于不能点击状态
					mBtnFace.setEnabled(false);
					mBtnKeyboard.setEnabled(false);
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 表情选择器显示与隐藏切换
	 * 
	 * @param bool
	 *            true，显示；false，隐藏
	 */
	private void facesGridViewShow(Boolean bool) {
		if (true == bool) {
			// 隐藏软键盘
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(replyContent.getWindowToken(), 0);

			mBtnFace.setVisibility(View.GONE);
			mBtnKeyboard.setVisibility(View.VISIBLE);
			mBBSFacesGridView.setVisibility(View.VISIBLE);
		} else {
			// 显示软键盘
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.showSoftInput(replyContent, 0);

			mBtnFace.setVisibility(View.VISIBLE);
			mBtnKeyboard.setVisibility(View.GONE);
			mBBSFacesGridView.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取EditText光标所在的位置
	 */
	private int getEditTextCursorIndex(EditText mEditText) {
		return mEditText.getSelectionStart();
	}

	/**
	 * 向EditText指定光标位置插入字符串
	 */
	private void insertText(EditText mEditText, String mText) {
		mEditText.getText().insert(getEditTextCursorIndex(mEditText), mText);
	}
}
