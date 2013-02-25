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
import com.bbs.whu.model.RecommendBean;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyFileUtils;

/**
 * 推荐文章数据适配器
 * 
 * @author wwang
 * 
 */
public class RecommendListAdapter extends MyBaseAdapter {

	public RecommendListAdapter(Context context,
			ArrayList<RecommendBean> items, int rLayoutList) {
		super(context, items, rLayoutList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final RecommendListViewHolder holder;
		if (convertView == null) {
			holder = new RecommendListViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					this.mRLayoutList, null);
			// 获取列表元素中的控件对象
			holder.holderRecommendTitle = (TextView) convertView
					.findViewById(R.id.recommend_title);
			holder.holderRecommendAuthor = (TextView) convertView
					.findViewById(R.id.recommend_author);
			holder.holderRecommendRecommendPeople = (TextView) convertView
					.findViewById(R.id.recommend_recommend_people);
			holder.holderRecommendBoard = (TextView) convertView
					.findViewById(R.id.recommend_board);
			holder.holderRecommendTime = (TextView) convertView
					.findViewById(R.id.recommend_time);
			holder.holderRecommendReplyNumber = (TextView) convertView
					.findViewById(R.id.recommend_reply_number);
			// 设置控件集到convertView
			convertView.setTag(holder);
		} else {
			holder = (RecommendListViewHolder) convertView.getTag();
		}
		// 推荐文章标题
		String title = ((RecommendBean) mItems.get(position)).getTitle();
		// 原文作者
		String author = ((RecommendBean) mItems.get(position)).getAuthor();
		// 推荐人
		String recommby = ((RecommendBean) mItems.get(position)).getRecommby();
		// 原文所属版面中文名称
		String originBoardName = ((RecommendBean) mItems.get(position))
				.getOriginBoardName();
		// 推荐时间
		String recommTime = ((RecommendBean) mItems.get(position))
				.getRecommTime();
		// 文章所在原始版块的英文名
		final String board = ((RecommendBean) mItems.get(position))
				.getOriginBoard();
		// 文章所在原始版块的编号
		final String recommGID = ((RecommendBean) mItems.get(position))
				.getOriginGID();

		// 读取已读标记
		HashMap<String, Byte> readedTagMap = MyApplication.getInstance()
				.getReadedTag();
		String key = recommGID;
		if (readedTagMap.containsKey(key)) {
			Byte tag = (Byte) readedTagMap.get(key);
			if ((byte) 0 == tag) {
				// 未读，设置为黑色
				holder.holderRecommendTitle.setTextColor(Color.BLACK);
			} else {
				// 已读，设置为灰色
				holder.holderRecommendTitle.setTextColor(Color.GRAY);
			}
		} else {
			// 未读，设置为黑色
			holder.holderRecommendTitle.setTextColor(Color.BLACK);
			readedTagMap.put(key, (byte) 0);
		}
		
		// 填充控件
		holder.holderRecommendTitle.setText(title);
		holder.holderRecommendAuthor.setText(author);
		holder.holderRecommendRecommendPeople.setText(recommby);
		holder.holderRecommendBoard.setText(originBoardName);
		// 提取年月日部分，时间格式：2012-11-25 18:02:08
		holder.holderRecommendTime.setText(recommTime.split(" ")[0]);

		// 添加点击响应事件
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 已读，设置为灰色
				holder.holderRecommendTitle.setTextColor(Color.GRAY);
				// 更新已读标记
				HashMap<String, Byte> readedTagMap = MyApplication
						.getInstance().getReadedTag();
				String key = recommGID;
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
				mIntent.putExtra("groupid", recommGID);
				context.startActivity(mIntent);
			}
		});

		return convertView;
	}
}