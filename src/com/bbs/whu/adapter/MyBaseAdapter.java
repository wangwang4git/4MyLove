package com.bbs.whu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.widget.BaseAdapter;

/**
 * 基本适配器，列表界面都需要适配器，本类可减少冗余代码
 * 
 * @author DoubleChen
 * 
 */
public abstract class MyBaseAdapter extends BaseAdapter {
	// 应用上下文
	protected Context context;
	// ListView元素数据
	protected ArrayList<?> mItems;
	// ListView元素的资源标识
	protected int mRLayoutList;

	public MyBaseAdapter(Context context, ArrayList<?> items, int rLayoutList) {
		this.context = context;
		this.mItems = items;
		this.mRLayoutList = rLayoutList;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);

	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
