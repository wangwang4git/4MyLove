package com.bbs.whu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
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
import com.bbs.whu.utils.MyFontManager;

public class HomeActivity extends ActivityGroup {

	// ViewPager��google SDk���Դ���һ�����Ӱ���һ���࣬��������ʵ����Ļ����л���
	// android-support-v4.jar
	private ViewPager mViewPager; // ҳ������
	private List<View> mListView; // Tabҳ���б�
	private ImageView mCursor; // ����ͼƬ
	private TextView mTopten, mRecommend, mPlaybill; // ҳ��ͷ��
	private int mOffset = 0; // ����ͼƬƫ����
	private int mCurrIndex = 0; // ��ǰҳ�����
	private int mBmpW; // ����ͼƬ���

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);
		MyFontManager.changeFontType(this);//���õ�ǰActivity������
		
		initImageView();
		initTextView();
		initViewPager();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ֻ�в��񷵻ؼ���������false��������MainActivity�в��񷵻ؼ�
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return false;
		}
		return false;
	}

	/**
	 * ��ʼ��ͷ��
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
	 * ��ʼ��ViewPager
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
	 * ��ʼ������
	 */
	private void initImageView() {
		mCursor = (ImageView) findViewById(R.id.cursor);
		mBmpW = BitmapFactory.decodeResource(getResources(),
				R.drawable.home_cursor).getWidth(); // ��ȡͼƬ���
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels; // ��ȡ�ֱ��ʿ��
		mOffset = (screenW / 3 - mBmpW) / 2;// ����ƫ����
		Matrix matrix = new Matrix();
		matrix.postTranslate(mOffset, 0);
		mCursor.setImageMatrix(matrix); // ���ö�����ʼλ��
	}

	/**
	 * ViewPager������
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
	 * ͷ��������
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
	 * ҳ���л�����
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = mOffset * 2 + mBmpW; // ҳ��1 -> ҳ��2 ƫ����
		int two = one * 2; // ҳ��1 -> ҳ��3 ƫ����

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
			animation.setFillAfter(true); // True:ͼƬͣ�ڶ�������λ��
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
