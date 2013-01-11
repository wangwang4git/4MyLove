package com.bbs.whu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbs.whu.R;

/**
 * �ղذ���б�����������
 * 
 * @author wwang
 * 
 */
public class CollectBoardListAdapter extends MyBaseAdapter {

	public CollectBoardListAdapter(Context context, ArrayList<String> items,
			int rLayoutList) {
		super(context, items, rLayoutList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CollectBoardListViewHolder holder;
		if (convertView == null) {
			holder = new CollectBoardListViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					this.mRLayoutList, null);
			// ��ȡ�б�Ԫ���еĿؼ�����
			holder.holderCollectBoardName = (TextView) convertView
					.findViewById(R.id.collect_board_name);
			holder.holderCollectBoardNewArticleNumber = (TextView) convertView
					.findViewById(R.id.collect_board_new_article_number);
			// ���ÿؼ�����convertView
			convertView.setTag(holder);
		} else {
			holder = (CollectBoardListViewHolder) convertView.getTag();
		}
		// ������Դ��ȡ�ղذ�����
		String name = (String) mItems.get(position);

		// ���ؼ�
		holder.holderCollectBoardName.setText(name);
		
		return convertView;
	}
}