package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
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
import com.bbs.whu.progresshud.ProgressHUDTask;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyXMLParseUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * �������Ͻ��棬 ��ʾһϵ�и������ϣ�����������Ʋο�������TextView�ؼ�ע�͡�
 * 
 * @author wwang
 * 
 */
public class PersonActivity extends Activity {
	// ͷ��
	private ImageView mPersonHeadPortrait;
	// �û���
	private TextView mPersonName;
	// ��ְ̳��
	private TextView mPersonPost;
	// ��������
	private TextView mPersonSect;
	// ������
	private TextView mPersonLife;
	// ����ȼ�
	private TextView mPersonLevel;
	// ����ֵ
	private TextView mPersonExperience;
	// ��������
	private TextView mPersonArticleNumber;
	// ��¼����
	private TextView mPersonLoginNumber;
	// �ϴε�¼ʱ��
	private TextView mPersonLoginTime;
	// ����ǩ��
	private TextView mPersonSign;
	// �����������ݵ�handler
	private Handler mHandler;

	// ͼƬ�첽����������
	private ImageLoader imageLoader = ImageLoader.getInstance();
	// ����ڴ滺����÷���
	// imageLoader.clearMemoryCache();
	// ����ļ�������÷���
	// imageLoader.clearDiscCache();
	// ͼƬ�첽���ػ������ñ���
	private DisplayImageOptions options;
	//
	private boolean instanceStateSaved;
	
	// ԭ������
	private String originAuthor;
	
	// get����
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();
	
	// �ȴ��Ի���
	private ProgressHUDTask mProgress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȡ����ʾtitle��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_person);
		// ��ʾ�ȴ��Ի���
		mProgress = new ProgressHUDTask(this);
		mProgress.execute();
		
		// ��ȡ�����������ݣ����ص�������
		Intent postInfoIntent = getIntent();
		// ����
		originAuthor = postInfoIntent.getStringExtra("author");
		
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.person_head_portrait)
				.showImageForEmptyUri(R.drawable.person_head_portrait)
				.cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(5)).build();

		// ��ʼ���ؼ�
		initView();
		// ��ʼ��handler
		initHandler();
		// ���������Ϣ����
		getUserInfo(false);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		instanceStateSaved = true;
	}

	@Override
	protected void onDestroy() {
		if (!instanceStateSaved) {
			// ����ڴ滺����÷���
			// imageLoader.clearMemoryCache();
			// ����ļ�������÷���
			// imageLoader.clearDiscCache();
			imageLoader.stop();
		}
		super.onDestroy();
	}

	/**
	 * ��ʼ���ؼ�
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
	 * ��ʼ��handler
	 */
	private void initHandler() {
		// ��ʼ��handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					String res = (String) msg.obj;
					// ˢ�¸�����Ϣ��ʾ
					refreshUserInfo(res);
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				// ȡ����ʾ�ȴ��Ի���
				if (mProgress != null) {
					mProgress.dismiss();
					mProgress = null;
				}
				return;
			}
		};
		// ע��handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "PersonActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "PersonActivity");
	}

	/**
	 * ���������Ϣ����
	 * 
	 * @param isForcingWebGet
	 *            �Ƿ�ǿ�ƴ������ȡ����
	 */

	private void getUserInfo(boolean isForcingWebGet) {
		keys.clear();
		values.clear();
		// ���get����
		keys.add("app");
		values.add("userInfo");
		keys.add("userId");
		values.add(originAuthor);
		// ��������
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values, "PersonActivity",
				isForcingWebGet, this);
	}

	/**
	 * ˢ�¸�����Ϣ��ʾ
	 */
	private void refreshUserInfo(String res) {
		UserInfoBean userInfo = MyXMLParseUtils.readXml2UserInfo(res);
		// ��̳��������ȷ���ݷ��أ���ʾ������ʾ
		if (null == userInfo) {
			// ɾ��ָ��Cache�ļ�
			MyBBSCache.delCacheFile(MyBBSCache.getCacheFileName(
					MyConstants.GET_URL, keys, values));
			// toast����
			Toast.makeText(this, R.string.bbs_exception_text,
					Toast.LENGTH_SHORT).show();
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
		mPersonSign.setText("");
	}
}
