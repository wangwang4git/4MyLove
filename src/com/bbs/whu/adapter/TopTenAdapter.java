package com.bbs.whu.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.activity.BulletinActivity;
import com.bbs.whu.model.TopTenBean;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyFileUtils;

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
		final TopTenViewHolder holder;
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
		TopTenBean mItem = (TopTenBean) mItems.get(position);
		// 帖子标题
		String title = mItem.getTitle();
		// 帖子回复数
		String number = mItem.getNumber();
		// 帖子作者
		String author = mItem.getAuthor();
		// 帖子版面中文名
		String boardName = mItem.getBoardname();
		// 帖子版面英文名
		final String board = mItem.getBoard();
		// 帖子ID
		final String groupid = mItem.getGroupid();

		// 读取已读标记
		HashMap<String, Byte> readedTagMap = MyApplication.getInstance()
				.getReadedTag();
		String key = groupid;
		if (readedTagMap.containsKey(key)) {
			Byte tag = (Byte) readedTagMap.get(key);
			if ((byte) 0 == tag) {
				// 未读，设置为黑色
				holder.holderTopTenTitle.setTextColor(Color.BLACK);
			} else {
				// 已读，设置为灰色
				holder.holderTopTenTitle.setTextColor(Color.GRAY);
			}
		} else {
			// 未读，设置为黑色
			holder.holderTopTenTitle.setTextColor(Color.BLACK);
			readedTagMap.put(key, (byte) 0);
		}
		
		// 填充控件
		holder.holderTopTenTitle.setText(title);
		holder.holderTopTenNumber.setText(number);
		holder.holderTopTenAuthor.setText(author);
		holder.holderTopTenBoardName.setText(boardName);

		// 添加点击响应事件
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 已读，设置为灰色
				holder.holderTopTenTitle.setTextColor(Color.GRAY);
				// 更新已读标记
				HashMap<String, Byte> readedTagMap = MyApplication
						.getInstance().getReadedTag();
				String key = groupid;
				// 如果该条目未读，需要标记为已读
				readedTagMap.put(key, (byte) 1);
				MyApplication.getInstance().setReadedTag(readedTagMap);
				// 序列化到已读标记json文件
				MyBBSCache.setReadedTagMap(MyApplication.getInstance()
						.getReadedTag(), MyFileUtils.READEDTAGNAME);

				// 跳转到帖子详情界面
				Intent mIntent = new Intent(context, BulletinActivity.class);
				// 添加参数
				mIntent.putExtra("board", board);
				mIntent.putExtra("groupid", groupid);
				context.startActivity(mIntent);
			}
		});
		return convertView;
	}
}