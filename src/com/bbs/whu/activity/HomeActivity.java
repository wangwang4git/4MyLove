package com.bbs.whu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.progresshud.ProgressHUDTask;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFontManager;

public class HomeActivity extends ActivityGroup {

	// ViewPager是google SDk中自带的一个附加包的一个类，可以用来实现屏幕间的切换。
	// android-support-v4.jar
	private ViewPager mViewPager; // 页卡内容
	private List<View> mListView; // Tab页面列表
	private ImageView mCursor; // 动画图片
	private TextView mTopten, mRecommend, mPlaybill; // 页卡头标
	private int mOffset = 0; // 动画图片偏移量
	private int mCurrIndex = 0; // 当前页卡编号
	private int mBmpW; // 动画图片宽度
	
	// 接收请求数据的handler
	private Handler mHandler;
	// 等待对话框
	private ProgressHUDTask mProgress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);
		MyFontManager.changeFontType(this);//设置当前Activity的字体
		
		initImageView();
		initTextView();
		initViewPager();
		
		// 初始化handler
		initHandler();
		
		// 显示等待对话框
		mProgress = new ProgressHUDTask(this);
		mProgress.execute();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 注销handler
		MessageHandlerManager.getInstance().unregister(
				MyConstants.LOADING_DISAPPEAR, "HomeActivity");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 只有捕获返回键，并返回false，才能在MainActivity中捕获返回键
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return false;
		}
		return false;
	}

	/**
	 * 初始化头标
	 */
	private void initTextView() {
		mTopten = (TextView) findViewById(R.id.topten);
		mRecommend = (TextView) findViewById(R.id.recommend);
		mPlaybill = (TextView) findViewById(R.id.playbill);

		mTopten.setOnClickListener(new MyOnClickListener(0));
		mRecommend.setOnClickListener(new MyOnClickListener(1));
		mPlaybill.setOnClickListener(new MyOnClickListener(2));
	}

	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mListView = new ArrayList<View>();
		mListView.add(getLocalActivityManager().startActivity("TopTenActivity",
				new Intent(this, TopTenActivity.class)).getDecorView());
		mListView.add(getLocalActivityManager().startActivity("RecommendActivity",
				new Intent(this, RecommendActivity.class)).getDecorView());
		mListView.add(getLocalActivityManager().startActivity("PlaybillActivity",
				new Intent(this, PlaybillActivity.class)).getDecorView());
		mViewPager.setAdapter(new MyPagerAdapter(mListView));
		mViewPager.setCurrentItem(0);
		mTopten.setTextColor(0xFF000000);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 初始化动画
	 */
	private void initImageView() {
		mCursor = (ImageView) findViewById(R.id.cursor);
		mBmpW = BitmapFactory.decodeResource(getResources(),
				R.drawable.home_cursor).getWidth(); // 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels; // 获取分辨率宽度
		mOffset = (screenW / 3 - mBmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(mOffset, 0);
		mCursor.setImageMatrix(matrix); // 设置动画初始位置
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
				case MyConstants.LOADING_DISAPPEAR:
					// 取消显示等待对话框
					if (mProgress != null) {
						mProgress.dismiss();
						mProgress = null;
					}
					break;
				}
				return;
			}
		};
		// 注册handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.LOADING_DISAPPEAR, "HomeActivity");
	}
	
	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mTopten.setTextColor(0xFFA0A0A0);
			mRecommend.setTextColor(0xFFA0A0A0);
			mPlaybill.setTextColor(0xFFA0A0A0);
			((TextView) v).setTextColor(0xFF000000);
			mViewPager.setCurrentItem(index);
		}
	};

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = mOffset * 2 + mBmpW; // 页卡1 -> 页卡2 偏移量
		int two = one * 2; // 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (mCurrIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (mCurrIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				mRecommend.setTextColor(0xFFA0A0A0);
				mPlaybill.setTextColor(0xFFA0A0A0);
				mTopten.setTextColor(0xFF000000);
				break;
			case 1:
				if (mCurrIndex == 0) {
					animation = new TranslateAnimation(mOffset, one, 0, 0);
				} else if (mCurrIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				mTopten.setTextColor(0xFFA0A0A0);
				mPlaybill.setTextColor(0xFFA0A0A0);
				mRecommend.setTextColor(0xFF000000);
				break;
			case 2:
				if (mCurrIndex == 0) {
					animation = new TranslateAnimation(mOffset, two, 0, 0);
				} else if (mCurrIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				mTopten.setTextColor(0xFFA0A0A0);
				mRecommend.setTextColor(0xFFA0A0A0);
				mPlaybill.setTextColor(0xFF000000);
				break;
			}
			mCurrIndex = arg0;
			animation.setFillAfter(true); // True:图片停在动画结束位置
			animation.setDuration(300);
			mCursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
}
