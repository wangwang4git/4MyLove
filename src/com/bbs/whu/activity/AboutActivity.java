package com.bbs.whu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.bbs.whu.R;

public class AboutActivity extends Activity implements OnClickListener {
	// 返回按钮
	private ImageView backButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);
		// 初始化控件
		initView();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.about_back_icon:
			finish();
			break;
		}
	}

	private void initView() {
		// 退出按钮
		backButton = (ImageView) findViewById(R.id.about_back_icon);
		backButton.setOnClickListener(this);
	}
}
