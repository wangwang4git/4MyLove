package com.bbs.whu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.cookie.Cookie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.AdvancedAutoCompleteAdapter;
import com.bbs.whu.adapter.LoginOptionsAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.UserPasswordBean;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFileUtils;
import com.bbs.whu.utils.MyHttpClient;
import com.bbs.whu.utils.MyWaitDialog;
import com.loopj.android.http.PersistentCookieStore;

/**
 * 登陆的Activity， 如果通过用户名密码登陆，进行post请求，获取Cookie， 如果匿名登陆，直接进入主页
 * 
 * @author double
 * 
 */

public class LoginActivity extends Activity implements OnClickListener {
	// 用户名输入框
	private AutoCompleteTextView userNameEditText;
	private AdvancedAutoCompleteAdapter mAdapter;
	// 密码输入框
	private EditText passwordEditText;
	// 确定按钮
	private Button loginButton;
	// 匿名按钮
	private Button anonymousButton;
	// 记住密码checkbox
	private CheckBox rememberUserCheckBox;
	// 记住密码标志
	private boolean rememberUserFlag = true;
	// 接收请求数据的handler
	Handler mHandler;

	// 登录时的等待对话框
	private MyWaitDialog loginWaitDialog;
	// PopupWindow对象
	private PopupWindow selectPopupWindow = null;
	// 自定义Adapter
	private LoginOptionsAdapter optionsAdapter = null;
	// 下拉框选项数据源
	private ArrayList<String> datas = new ArrayList<String>();
	// 下拉框依附组件
	private LinearLayout parent;
	// 下拉框依附组件宽度，也将作为下拉框的宽度
	private int pwidth;
	// 下拉箭头图片组件
	private ImageView selectImg;
	// 展示所有下拉选项的ListView
	private ListView listView = null;
	// 是否初始化完成标志
	private boolean flag = false;

	// 用户名、密码对列表
	private ArrayList<UserPasswordBean> userPasswords;
	// 删除账号缓存文件的确认对话框
	private AlertDialog.Builder deleteConfirmDlg;
	// 选中的要删除的项
	private int delIndex;

	// 记录应用是否为第一次使用
	private SharedPreferences firstUseSP;
	private SharedPreferences.Editor firstUseEditor;
	private String firstUseSPKey  = "isUsed";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取出Activity的title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		// 判断是否为第一次使用
		firstUseSP = this.getSharedPreferences("firstUse", MODE_WORLD_READABLE); 
		
		//如果为第一次使用则生成SharedPreferences变量并跳转至新手导航页
		if (!firstUseSP.contains(firstUseSPKey)) { 
			firstUseEditor = firstUseSP.edit(); 
			firstUseEditor.putInt(firstUseSPKey, 1);// 1为使用过 
			firstUseEditor.commit(); // 提交 
			
			//跳转至新手导航页
			Intent intent = new Intent(LoginActivity.this,BeginnerNavigationActivity.class);
			LoginActivity.this.startActivity(intent); 
		}

		// 登录前操作
		loginBefore();
		// 初始化控件
		init();
		// 初始化handler
		initHandler();

		// 设定loginWaitDialog的属性
		// loginWaitDialog = new WaitDialog(LoginActivity.this, "提示",
		// "正在登录...");
		loginWaitDialog = new MyWaitDialog(LoginActivity.this, "登录",
				"正在登录，稍安勿躁......");
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login_button:
			// 登录时的等待对话框
			loginWaitDialog.show();
			// 登陆
			login();
			break;
		case R.id.anonymous_button:
			// 跳转的主页
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			// 登陆后操作
			loginAfter();
			// 关闭登陆页面
			finish();
			break;
		}
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		// 用户名、密码输入框
		userNameEditText = (AutoCompleteTextView) findViewById(R.id.user_name_editText);
		passwordEditText = (EditText) findViewById(R.id.password_editText);
		// 设置用户名、密码初始值
		// userNameEditText.setText(MyConstants.MY_USER_NAME);
		// passwordEditText.setText(MyConstants.MY_PASSWORD);

		// 设置userNameEditText适配器
		mAdapter = new AdvancedAutoCompleteAdapter(this, userPasswords, 10);
		userNameEditText.setAdapter(mAdapter);

		// 设置userNameEditText监听器
		userNameEditText.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				passwordEditText.setText(userPasswords.get(arg2).getPassword());
			}
		});

		// 确定按钮
		loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(this);
		// 匿名按钮
		anonymousButton = (Button) findViewById(R.id.anonymous_button);
		anonymousButton.setOnClickListener(this);
		
		// 记住密码checkbox
		rememberUserCheckBox = (CheckBox) findViewById(R.id.remember_user_checkBox);
		rememberUserCheckBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							// 选中
							rememberUserFlag = true;
						} else {
							// 未选中
							rememberUserFlag = false;
						}
					}
				});

		// 删除账号缓存文件的确认对话框
		deleteConfirmDlg = new Builder(LoginActivity.this);
		deleteConfirmDlg.setMessage("确认删除该账户的缓存文件吗？");
		deleteConfirmDlg.setTitle("提示");
		deleteConfirmDlg.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						datas.remove(delIndex);

						// 删除缓存文件
						MyFileUtils.delFolder(MyFileUtils
								.getSdcardDataCacheDir(userPasswords.get(
										delIndex).getName()));

						// 删除userPasswords中的账户
						userPasswords.remove(delIndex);

						// 深拷贝tempRefreshJson
						ArrayList<UserPasswordBean> tempRefreshJson = new ArrayList<UserPasswordBean>();
						for (UserPasswordBean tempAdd : userPasswords) {
							UserPasswordBean tempUser = new UserPasswordBean();
							tempUser.setName(tempAdd.getName());
							tempUser.setPassword(tempAdd.getPassword());
							tempRefreshJson.add(tempUser);
						}

						// 序列化到用户名、密码json文件
						MyBBSCache.setUserPasswordList(tempRefreshJson,
								MyFileUtils.USERPASSWORDNAME);

						// 刷新下拉列表
						optionsAdapter.notifyDataSetChanged();

						dialog.dismiss();
					}
				});

		deleteConfirmDlg.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
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
				Bundle data = msg.getData();
				switch (msg.what) {
				case MyConstants.LOGIN_SELECT_USER:
					// 选中下拉项，下拉框消失
					int selIndex = data.getInt("selIndex");
					userNameEditText.setText(datas.get(selIndex));
					passwordEditText.setText(userPasswords.get(selIndex)
							.getPassword());
					dismiss();
					break;
				case MyConstants.LOGIN_DELETE_USER:
					// 移除下拉项数据
					delIndex = data.getInt("delIndex");

					// 弹出删除账号缓存文件的确认对话框
					deleteConfirmDlg.create().show();
					break;
				case MyConstants.REQUEST_SUCCESS:
					if (loginWaitDialog.mStatus == MyWaitDialog.SHOWING) {

						// 如果Cookie为空，说明用户名密码错误
						List<Cookie> cookies = MyApplication.getInstance()
								.getCookieStore().getCookies();
						if (cookies.size() == 0) {
							// 关闭等待对话框
							loginWaitDialog.cancel();
							// 提醒用户
							Toast.makeText(LoginActivity.this, "用户名、密码错误！",
									Toast.LENGTH_SHORT).show();
							return;
						}

						// 登录成功后，将用户名密码设为全局变量
						((MyApplication) getApplicationContext())
								.setName(userNameEditText.getText().toString());
						((MyApplication) getApplicationContext())
								.setPassword(passwordEditText.getText()
										.toString());

						// 跳转的主页
						startActivity(new Intent(LoginActivity.this,
								MainActivity.class));
						// 关闭等待对话框
						loginWaitDialog.cancel();
						// 登陆后操作
						loginAfter();
						// 关闭登陆页面
						finish();
					} else if (loginWaitDialog.mStatus == MyWaitDialog.CANCELLED) {
						// 登录成功但是取消了等待对话框则发送登出请求
						MyBBSRequest.mGet(MyConstants.LOG_OUT_URL,
								"MainActivity");// 设置为MainActivity是为了不接收退出的响应事件
					}
					break;
				case MyConstants.REQUEST_FAIL:
					// 关闭等待对话框
					loginWaitDialog.cancel();
					// 提示失败
					break;
				}
			}
		};
		// 注册handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "LoginActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "LoginActivity");
	}

	/**
	 * 登陆
	 */
	private void login() {
		// 新建CookieStore对象
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
		// 将CookieStore设为全局变量
		((MyApplication) getApplicationContext()).setCookieStore(myCookieStore);
		// 添加CookieStore
		MyHttpClient.setCookieStore(myCookieStore);
		// 添加post请求参数
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("id");
		values.add(userNameEditText.getText().toString());
		keys.add("passwd");
		values.add(passwordEditText.getText().toString());
		keys.add("webtype");
		values.add("wforum");
		// 添加支持多次登录参数
		keys.add("kick_multi");
		values.add("1");
		// post请求
		MyBBSRequest.mPost(MyConstants.LOGIN_URL, keys, values,
				"LoginActivity", this);
	}

	/**
	 * 登录后操作，包括用户名、密码列表序列化
	 */
	private void loginAfter() {
		// 遍历当前用户名、密码是否存在于列表中
		// 但是如果是匿名，不遍历也不加入
		String loginName = ((MyApplication) getApplicationContext()).getName();
		String loginPassword = ((MyApplication) getApplicationContext())
				.getPassword();
		if (!loginName.equals(MyFileUtils.USERPASSWORDNAME)) {
			Boolean isExist = false;
			for (int i = 0; i < userPasswords.size(); ++i) {
				// 如果用户名不同，则添加
				// 如果用户名相同，密码不同，则更新密码
				if (userPasswords.get(i).getName().equals(loginName)) {
					if (!userPasswords.get(i).getPassword()
							.equals(loginPassword)) {
						// 更新密码
						userPasswords.get(i).setPassword(loginPassword);
					}
					isExist = true;
					break;
				}
			}
			// 不存在，就添加
			if (!isExist) {
				UserPasswordBean bean = new UserPasswordBean();
				bean.setName(loginName);
				bean.setPassword(loginPassword);
				userPasswords.add(bean);
			}
		}

		// 序列化到用户名、密码json文件
		// 如果未选中记住密码，则不保存密码
		if (!rememberUserFlag) {
			for (int i = 0; i < userPasswords.size(); ++i) {
				if (userPasswords.get(i).getName().equals(loginName)) {
					userPasswords.get(i).setPassword("");
					break;
				}
			}
		}
		MyBBSCache.setUserPasswordList(userPasswords,
				MyFileUtils.USERPASSWORDNAME);
		
		// 读取已读标记json文件，并反序列化
		HashMap<String, Byte> readedTagMap = MyBBSCache
				.getReadedTagMap(MyFileUtils.READEDTAGNAME);
		// 如果readedTagMap为空，当前还不存在已读标记json文件
		if (null == readedTagMap) {
			readedTagMap = new HashMap<String, Byte>();
		}
		// 设置全部变量
		((MyApplication) getApplicationContext()).setReadedTag(readedTagMap);
	}

	/**
	 * 登录前操作，包括反序列化用户名、密码json文件；已读标记json文件
	 */
	private void loginBefore() {
		// 读取用户名、密码json文件，并反序列化
		userPasswords = (ArrayList<UserPasswordBean>) MyBBSCache
				.getUserPasswordList(MyFileUtils.USERPASSWORDNAME);
		// 如果userPasswords为空，当前还不存在用户名、密码json文件
		if (null == userPasswords) {
			userPasswords = new ArrayList<UserPasswordBean>();
		}
	}

	/**
	 * 没有在onCreate方法中调用initWedget()，而是在onWindowFocusChanged方法中调用，
	 * 是因为initWedget()中需要获取PopupWindow浮动下拉框依附的组件宽度，在onCreate方法中是无法获取到该宽度的
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		while (!flag) {
			initWedget();
			flag = true;
		}

	}

	/**
	 * 初始化下拉列表控件
	 */
	private void initWedget() {
		// 初始化界面组件
		parent = (LinearLayout) findViewById(R.id.login_user_parent);
		userNameEditText = (AutoCompleteTextView) findViewById(R.id.user_name_editText);
		passwordEditText = (EditText) findViewById(R.id.password_editText);
		selectImg = (ImageView) findViewById(R.id.btn__multiple_user_select);

		// 获取下拉框依附的组件宽度
		int width = parent.getWidth();
		pwidth = width;

		// 设置点击下拉箭头图片事件，点击弹出PopupWindow浮动下拉框
		selectImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag) {
					// 显示PopupWindow窗口
					popupWindwShowing();
				}
			}
		});

		// 初始化PopupWindow
		initPopuWindow();
	}

	/**
	 * 初始化填充LoginOptionsAdapter所用List数据
	 */
	private void initDatas() {
		// 清空datas
		datas.clear();

		for (int i = 0; i < userPasswords.size(); i++) {
			UserPasswordBean temp = userPasswords.get(i);
			datas.add(temp.getName());
		}
	}

	/**
	 * 初始化PopupWindow
	 */
	private void initPopuWindow() {

		initDatas();

		// PopupWindow浮动下拉框布局
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.login_options, null);
		listView = (ListView) loginwindow.findViewById(R.id.user_list);

		// 设置自定义Adapter
		optionsAdapter = new LoginOptionsAdapter(this, mHandler, datas);
		listView.setAdapter(optionsAdapter);

		selectPopupWindow = new PopupWindow(loginwindow, pwidth,
				LayoutParams.WRAP_CONTENT, true);

		selectPopupWindow.setOutsideTouchable(true);

		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		// 本人能力极其有限，不明白其原因，还望高手、知情者指点一下
		selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * 显示PopupWindow窗口
	 * 
	 * @param popupwindow
	 */
	public void popupWindwShowing() {
		// 将selectPopupWindow作为parent的下拉框显示，并指定selectPopupWindow在Y方向上向上偏移3pix，
		// 这是为了防止下拉框与文本框之间产生缝隙，影响界面美化
		// （是否会产生缝隙，及产生缝隙的大小，可能会根据机型、Android系统版本不同而异吧，不太清楚）
		selectPopupWindow.showAsDropDown(parent, 0, -3);
	}

	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d("Login", "Activity OnKeyDown called");
		}
		return super.onKeyDown(keyCode, event);
	}
}
