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
import com.bbs.whu.model.PlaybillBean;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyFileUtils;

/**
 * 校园海报数据适配器
 * 
 * @author wwang
 * 
 */
public class PlaybillListAdapter extends MyBaseAdapter {

	public PlaybillListAdapter(Context context, ArrayList<PlaybillBean> items,
			int rLayoutList) {
		super(context, items, rLayoutList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final PlaybillListViewHolder holder;
		if (convertView == null) {
			holder = new PlaybillListViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					this.mRLayoutList, null);
			// 获取列表元素中的控件对象
			holder.holderPlaybillTitle = (TextView) convertView
					.findViewById(R.id.playbill_title);
			// 设置控件集到convertView
			convertView.setTag(holder);
		} else {
			holder = (PlaybillListViewHolder) convertView.getTag();
		}
		// 校园海报标题
		String title = ((PlaybillBean) mItems.get(position)).getTitle();
		// 校园海报所在版块的英文名
		final String board = ((PlaybillBean) mItems.get(position)).getBoard();
		;
		// 校园海报所在版块的编号
		final String groupid = ((PlaybillBean) mItems.get(position))
				.getGroupid();

		// 读取已读标记
		HashMap<String, Byte> readedTagMap = MyApplication.getInstance()
				.getReadedTag();
		String key = groupid;
		if (readedTagMap.containsKey(key)) {
			Byte tag = (Byte) readedTagMap.get(key);
			if ((byte) 0 == tag) {
				// 未读，设置为黑色
				holder.holderPlaybillTitle.setTextColor(Color.BLACK);
			} else {
				// 已读，设置为灰色
				holder.holderPlaybillTitle.setTextColor(Color.GRAY);
			}
		} else {
			// 未读，设置为黑色
			holder.holderPlaybillTitle.setTextColor(Color.BLACK);
			readedTagMap.put(key, (byte) 0);
		}
		
		// 填充控件
		holder.holderPlaybillTitle.setText(title);

		// 添加点击响应事件
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 已读，设置为灰色
				holder.holderPlaybillTitle.setTextColor(Color.GRAY);
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