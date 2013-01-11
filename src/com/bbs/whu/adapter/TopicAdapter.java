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
import com.bbs.whu.model.TopicBean;

/**
 * 分类中的板块下的帖子列表数据适配器
 * 
 * @author ljp
 * 
 */
public class TopicAdapter extends MyBaseAdapter {
	// 帖子英文名
	private String board;

	public TopicAdapter(Context context, ArrayList<TopicBean> items,
			int rLayoutList, String board) {
		super(context, items, rLayoutList);
		this.board = board;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TopicViewHolder holder;
		if (convertView == null) {
			holder = new TopicViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					this.mRLayoutList, null);
			// 获取列表元素中的控件对象
			holder.holderTopicTitle = (TextView) convertView
					.findViewById(R.id.topic_title);
			holder.holderTopicAuthor = (TextView) convertView
					.findViewById(R.id.topic_author);
			holder.holderTopicDatetime = (TextView) convertView
					.findViewById(R.id.topic_datetime);
			holder.holderTopicNumber = (TextView) convertView
					.findViewById(R.id.topic_number);
			// 设置控件集到convertView
			convertView.setTag(holder);
		} else {
			holder = (TopicViewHolder) convertView.getTag();
		}
		// 帖子标题
		String title = ((TopicBean) mItems.get(position)).getTitle();
		// 帖子作者
		String author = ((TopicBean) mItems.get(position)).getAuthor();
		// 帖子发表时间
		String datetime = ((TopicBean) mItems.get(position)).getPosttime();
		// 帖子回复数
		String number = ((TopicBean) mItems.get(position)).getReplyNum();
		// 帖子ID
		final String groupid = ((TopicBean) mItems.get(position)).getGID();

		// 填充控件
		holder.holderTopicTitle.setText(title);
		holder.holderTopicAuthor.setText(author);
		holder.holderTopicDatetime.setText(datetime);
		holder.holderTopicNumber.setText(number);

		// 添加点击响应事件
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
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