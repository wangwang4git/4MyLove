package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.UserInfoBean;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
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
	// 个性签名
	private TextView mPersonSign;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取消显示title栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_person);

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.person_head_portrait)
				.showImageForEmptyUri(R.drawable.person_head_portrait)
				.cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(5)).build();

		// 初始化控件
		initView();
		// 初始化handler
		initHandler();

		if (null == ((MyApplication) getApplicationContext()).getName()) {
			Toast.makeText(this,
					"亲，你是匿名用户登录，看毛线的个人资料啊！\n这些是测试资料，退出切换你滴账号登录再回来吧~~~",
					Toast.LENGTH_LONG).show();
		} else {
			// 请求个人信息数据
			getUserInfo(false);
		}
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
		super.onDestroy();
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
		mPersonSign = (TextView) findViewById(R.id.person_sign);
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
		// 添加get参数
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("app");
		values.add("userInfo");
		keys.add("userId");
		values.add(((MyApplication) getApplicationContext()).getName());
		// 请求数据
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values, "PersonActivity",
				isForcingWebGet, this);
	}

	/**
	 * 刷新个人信息显示
	 */
	private void refreshUserInfo(String res) {
		UserInfoBean userInfo = MyXMLParseUtils.readXml2UserInfo(res);
		//
		imageLoader.displayImage(
				MyConstants.WHU_BBS + userInfo.getUserface_img(),
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
		mPersonSign.setText("null");
	}
}
