package com.bbs.whu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.activity.BulletinActivity;
import com.bbs.whu.model.TopTenBean;

/**
 * “十大”列表数据适配器
 * 
 * @author wwang
 * 
 */
public class TopTenAdapter extends MyBaseAdapter {

	public TopTenAdapter(Context context, ArrayList<TopTenBean> items,
			int rLayoutList) {
		super(context, items, rLayoutList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TopTenViewHolder holder;
		if (convertView == null) {
			holder = new TopTenViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					this.mRLayoutList, null);
			// 获取列表元素中的控件对象
			holder.holderTopTenTitle = (TextView) convertView
					.findViewById(R.id.top_ten_title);
			holder.holderTopTenNumber = (TextView) convertView
					.findViewById(R.id.top_ten_number);
			holder.holderTopTenAuthor = (TextView) convertView
					.findViewById(R.id.top_ten_author);
			holder.holderTopTenBoardName = (TextView) convertView
					.findViewById(R.id.top_ten_boardname);
			// 设置控件集到convertView
			convertView.setTag(holder);
		} else {
			holder = (TopTenViewHolder) convertView.getTag();
		}
		// 帖子标题
		String title = ((TopTenBean) mItems.get(position)).getTilte();
		// 帖子回复数
		Long number = ((TopTenBean) mItems.get(position)).getNumber();
		// 帖子作者
		String author = ((TopTenBean) mItems.get(position)).getAuthor();
		// 帖子版面中文名
		String boardName = ((TopTenBean) mItems.get(position)).getBoardname();
		// 帖子版面英文名
		final String board = ((TopTenBean) mItems.get(position)).getBoard();
		// 帖子ID
		final Long groupid = ((TopTenBean) mItems.get(position)).getGroupid();
		
		// 填充控件
		holder.holderTopTenTitle.setText(title);
		holder.holderTopTenNumber.setText(number.toString());
		holder.holderTopTenAuthor.setText(author);
		holder.holderTopTenBoardName.setText(boardName);
		
		// 添加点击响应事件
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 跳转到帖子详情界面
				Intent mIntent = new Intent(context, BulletinActivity.class);
				// 添加参数
				mIntent.putExtra("board", board);
				mIntent.putExtra("groupid", groupid.toString());
				context.startActivity(mIntent);
			}
		});
		return convertView;
	}
}