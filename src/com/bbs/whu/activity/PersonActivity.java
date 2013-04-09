package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.UserInfoBean;
import com.bbs.whu.progresshud.ProgressHUDTask;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFontManager;
import com.bbs.whu.utils.MyRegexParseUtils;
import com.bbs.whu.utils.MyXMLParseUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 个人资料界面， 显示一系列个人资料，相关资料名称参看声明的TextView控件注释。
 * 
 * @author wwang
 * 
 */
public class PersonActivity extends Activity {
	// 头像
	private ImageView mPersonHeadPortrait;
	// 用户名
	private TextView mPersonName;
	// 论坛职务
	private TextView mPersonPost;
	// 所属门派
	private TextView mPersonSect;
	// 生命力
	private TextView mPersonLife;
	// 经验等级
	private TextView mPersonLevel;
	// 经验值
	private TextView mPersonExperience;
	// 帖子总数
	private TextView mPersonArticleNumber;
	// 登录次数
	private TextView mPersonLoginNumber;
	// 上次登录时间
	private TextView mPersonLoginTime;
	// 用户状态
	private TextView mPersonUserMode;
	// 个性签名
	private TextView mPersonSign;
	// 个性签名图片（部分bbs用户，个性签名为图片）
	private ImageView mPersonSignView;
	
	// 返回按钮
	private ImageView backButton;
	
	// 接收请求数据的handler
	private Handler mHandler;

	// 图片异步下载下载器
	private ImageLoader imageLoader = ImageLoader.getInstance();
	// 清空内存缓存调用方法
	// imageLoader.clearMemoryCache();
	// 清空文件缓存调用方法
	// imageLoader.clearDiscCache();
	// 图片异步下载缓存设置变量
	private DisplayImageOptions options;
	//
	private boolean instanceStateSaved;

	// 原帖作者
	private String originAuthor;

	// get参数
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();

	// 进行手势动作时候的坐标
	float x_temp1 = 0, y_temp1 = 0, x_temp2, y_temp2;

	// 等待对话框
	private ProgressHUDTask mProgress;
	// 请求响应一一对应布尔变量
	private boolean mRequestResponse = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取消显示title栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_person);
		MyFontManager.changeFontType(this);// 设置当前Activity的字体

		// 显示等待对话框
		mProgress = new ProgressHUDTask(this);
		mProgress.execute();

		// 获取传过来的数据，加载到界面上
		Intent postInfoIntent = getIntent();
		// 作者
		originAuthor = postInfoIntent.getStringExtra("author");

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.person_head_portrait)
				.showImageForEmptyUri(R.drawable.person_head_portrait)
				.cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(5)).build();

		// 初始化控件
		initView();
		// 初始化handler
		initHandler();
		// 请求个人信息数据
		getUserInfo(true);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		instanceStateSaved = true;
	}

	@Override
	protected void onDestroy() {
		if (!instanceStateSaved) {
			// 清空内存缓存调用方法
			// imageLoader.clearMemoryCache();
			// 清空文件缓存调用方法
			// imageLoader.clearDiscCache();
			imageLoader.stop();
		}

		// 注销handler
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_SUCCESS, "PersonActivity");
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_FAIL, "PersonActivity");

		super.onDestroy();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获得当前坐标
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
			// 右滑
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
	 * 最先响应触屏事件，因为ListView会屏蔽掉Activity的onTouchEvent事件，所以需要重写此方法
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		super.dispatchTouchEvent(event);
		return onTouchEvent(event);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mPersonHeadPortrait = (ImageView) findViewById(R.id.person_head_portrait);
		mPersonName = (TextView) findViewById(R.id.person_name);
		mPersonPost = (TextView) findViewById(R.id.person_post);
		mPersonSect = (TextView) findViewById(R.id.person_sect);
		mPersonLife = (TextView) findViewById(R.id.person_life);
		mPersonLevel = (TextView) findViewById(R.id.person_level);
		mPersonExperience = (TextView) findViewById(R.id.person_experience);
		mPersonArticleNumber = (TextView) findViewById(R.id.person_article_number);
		mPersonLoginNumber = (TextView) findViewById(R.id.person_login_number);
		mPersonLoginTime = (TextView) findViewById(R.id.person_login_time);
		mPersonUserMode = (TextView) findViewById(R.id.person_user_mode);
		mPersonSign = (TextView) findViewById(R.id.person_sign);
		mPersonSignView = (ImageView) findViewById(R.id.person_sign_image);
		// 返回按钮
		backButton = (ImageView) findViewById(R.id.person_back_icon);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
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
				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					String res = (String) msg.obj;
					// 刷新个人信息显示
					refreshUserInfo(res);
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// 注册handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "PersonActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "PersonActivity");
	}

	/**
	 * 请求个人信息数据
	 * 
	 * @param isForcingWebGet
	 *            是否强制从网络获取数据
	 */

	private void getUserInfo(boolean isForcingWebGet) {
		keys.clear();
		values.clear();
		// 添加get参数
		keys.add("app");
		values.add("userInfo");
		keys.add("userId");
		values.add(originAuthor);
		// 请求数据
		mRequestResponse = true;
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values, "PersonActivity",
				isForcingWebGet, this);
	}

	/**
	 * 刷新个人信息显示
	 */
	private void refreshUserInfo(String res) {
		UserInfoBean userInfo = MyXMLParseUtils.readXml2UserInfo(res);
		// 论坛错误，无正确数据返回，显示错误提示
		if (null == userInfo) {
			if (mRequestResponse == true) {
				mRequestResponse = false;
				// 删除指定Cache文件
				MyBBSCache.delCacheFile(MyBBSCache.getCacheFileName(
						MyConstants.GET_URL, keys, values));
				// toast提醒
				Toast.makeText(this, R.string.bbs_exception_text,
						Toast.LENGTH_SHORT).show();
				System.out.println(this.getString(R.string.bbs_exception_text));
			}
			return;
		}

		imageLoader.displayImage(
				MyConstants.HEAD_URL + userInfo.getUserface_img(),
				mPersonHeadPortrait, options);
		mPersonName.setText(userInfo.getNickname());
		mPersonPost.setText(userInfo.getUserlevel());
		mPersonSect.setText(userInfo.getMenpai());
		mPersonLife.setText(userInfo.getUservalue());
		mPersonLevel.setText(userInfo.getExplevel());
		mPersonExperience.setText(userInfo.getUserexp());
		mPersonArticleNumber.setText(userInfo.getNumposts());
		mPersonLoginNumber.setText(userInfo.getNumlogins());
		mPersonLoginTime.setText(userInfo.getLastlogin());
		mPersonUserMode.setText(userInfo.getUsermode());
		String signText = Html.fromHtml(
				Html.fromHtml(userInfo.getSigcontent()).toString()).toString();
		mPersonSign.setText(signText.replaceAll("\\[IMG\\](.*?)\\[/IMG\\]",
				"$1"));
		String signImageUrl = MyRegexParseUtils.getSignView(signText);
		if (!signImageUrl.equals("null")) {
			mPersonSignView.setVisibility(View.VISIBLE);
			imageLoader.displayImage(signImageUrl, mPersonSignView, options);
		}
	}
}
