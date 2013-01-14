package com.bbs.whu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbs.whu.R;

/**
 * 在线好友列表数据适配器
 * 
 * @author wwang
 * 
 */
public class OnlineFriendAdapter extends MyBaseAdapter {

	public OnlineFriendAdapter(Context context, ArrayList<String> items,
			int rLayoutList) {
		super(context, items, rLayoutList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OnlineFriendViewHolder holder;
		if (convertView == null) {
			holder = new OnlineFriendViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					this.mRLayoutList, null);
			// 获取列表元素中的控件对象
			holder.holderOnlineFriendNumber = (TextView) convertView
					.findViewById(R.id.online_frined_number);
			holder.holderOnlineFriendHeadPortrait = (ImageView) convertView
					.findViewById(R.id.online_frined_head_portrait);
			holder.holderOnlineFriendName = (TextView) convertView
					.findViewById(R.id.online_frined_name);
			// 设置控件集到convertView
			convertView.setTag(holder);
		} else {
			holder = (OnlineFriendViewHolder) convertView.getTag();
		}
		// 从数据源获取在线好友名称
		String name = (String) mItems.get(position);

		// 填充控件
		holder.holderOnlineFriendName.setText(name);

		return convertView;
	}
}