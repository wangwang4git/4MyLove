package com.bbs.whu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbs.whu.R;

/**
 * 收藏版块列表数据适配器
 * 
 * @author wwang
 * 
 */
public class CollectBoardAdapter extends MyBaseAdapter {

	public CollectBoardAdapter(Context context, ArrayList<String> items,
			int rLayoutList) {
		super(context, items, rLayoutList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CollectBoardViewHolder holder;
		if (convertView == null) {
			holder = new CollectBoardViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					this.mRLayoutList, null);
			// 获取列表元素中的控件对象
			holder.holderCollectBoardName = (TextView) convertView
					.findViewById(R.id.collect_board_name);
			holder.holderCollectBoardNewArticleNumber = (TextView) convertView
					.findViewById(R.id.collect_board_new_article_number);
			// 设置控件集到convertView
			convertView.setTag(holder);
		} else {
			holder = (CollectBoardViewHolder) convertView.getTag();
		}
		// 从数据源获取收藏版块标题
		String name = (String) mItems.get(position);

		// 填充控件
		holder.holderCollectBoardName.setText(name);
		
		return convertView;
	}
}