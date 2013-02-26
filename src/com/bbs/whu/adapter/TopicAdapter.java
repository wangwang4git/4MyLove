package com.bbs.whu.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.activity.BulletinActivity;
import com.bbs.whu.model.TopicBean;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyFileUtils;

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
		final TopicViewHolder holder;
		if (convertView == null) {
			holder = new TopicViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					this.mRLayoutList, null);
			// 获取列表元素中的控件对象
			holder.holderTopicTag = (ImageView) convertView
					.findViewById(R.id.topic_tag);
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

		// 读取已读标记
		HashMap<String, Byte> readedTagMap = MyApplication.getInstance()
				.getReadedTag();
		String key = groupid;
		if (readedTagMap.containsKey(key)) {
			Byte tag = (Byte) readedTagMap.get(key);
			if ((byte) 0 == tag) {
				// 未读，设置为黑色
				holder.holderTopicTitle.setTextColor(Color.BLACK);
			} else {
				// 已读，设置为灰色
				holder.holderTopicTitle.setTextColor(Color.GRAY);
			}
		} else {
			// 未读，设置为黑色
			holder.holderTopicTitle.setTextColor(Color.BLACK);
			readedTagMap.put(key, (byte) 0);
		}
		
		// 填充控件
		if (isNewTopic(datetime)) {
			holder.holderTopicTag.setVisibility(View.VISIBLE);
			holder.holderTopicTag.setImageResource(R.drawable.new_topic);
		} else {
			holder.holderTopicTag.setVisibility(View.INVISIBLE);
		}
		holder.holderTopicTitle.setText(title);
		holder.holderTopicAuthor.setText(author);
		holder.holderTopicDatetime.setText(datetime);
		holder.holderTopicNumber.setText(number);

		// 添加点击响应事件
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 已读，设置为灰色
				holder.holderTopicTitle.setTextColor(Color.GRAY);
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
	
	private boolean isNewTopic(String topicDateString) {
		// 定义时间格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取当前时间
		Date nowDate = new Date();
		// 由字符串构造发帖时间
		Date topicDate = null;
		try {
			topicDate = sdf.parse(topicDateString);
			// 与当前时间间隔一天内的帖子，约定为新帖
			if ((nowDate.getTime() - topicDate.getTime()) / (1000 * 60 * 60) <= 24) {
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}