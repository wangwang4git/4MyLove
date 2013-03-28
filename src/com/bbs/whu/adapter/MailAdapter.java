package com.bbs.whu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.model.MailBean;

public class MailAdapter extends MyBaseAdapter {
	public MailAdapter(Context context, ArrayList<MailBean> items,
			int rLayoutList) {
		super(context, items, rLayoutList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final MailViewHolder holder;
		if (convertView == null) {
			holder = new MailViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					this.mRLayoutList, null);
			// 获取列表元素中的控件对象
			holder.holderMailTag = (ImageView) convertView
					.findViewById(R.id.mail_tag);
			holder.holderMailTitle = (TextView) convertView
					.findViewById(R.id.mail_title);
			holder.holderMailAuthor = (TextView) convertView
					.findViewById(R.id.mail_author);
			holder.holderMailDatetime = (TextView) convertView
					.findViewById(R.id.mail_datetime);
			// 设置控件集到convertView
			convertView.setTag(holder);
		} else {
			holder = (MailViewHolder) convertView.getTag();
		}
		// 邮件标题
		String title = ((MailBean) mItems.get(position)).getTitle();
		// 邮件作者
		String author = ((MailBean) mItems.get(position)).getSender()
				.toString();
		// 邮件发表时间
		String datetime = ((MailBean) mItems.get(position)).getTime()
				.toString();
		// 邮件属性
		// String flag = ((TopicBean) mItems.get(position)).getFlag();
		// 帖子ID
		final String size = ((MailBean) mItems.get(position)).getSize()
				.toString();

		// 填充控件
		holder.holderMailTitle.setText(title);
		holder.holderMailAuthor.setText(author);
		holder.holderMailDatetime.setText(datetime);

		// 添加点击响应事件
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// // 已读，设置为灰色
				// holder.holderTopicTitle.setTextColor(Color.GRAY);
				// // 更新已读标记
				// HashMap<String, Byte> readedTagMap = MyApplication
				// .getInstance().getReadedTag();
				// String key = groupid;
				// // 如果该条目未读，需要标记为已读
				// readedTagMap.put(key, (byte) 1);
				// MyApplication.getInstance().setReadedTag(readedTagMap);
				// // 序列化到已读标记json文件
				// MyBBSCache.setReadedTagMap(MyApplication.getInstance()
				// .getReadedTag(), MyFileUtils.READEDTAGNAME);
				//
				// // 跳转到帖子详情界面
				// Intent mIntent = new Intent(context, BulletinActivity.class);
				// // 添加参数
				// mIntent.putExtra("board", board);
				// mIntent.putExtra("groupid", groupid);
				// context.startActivity(mIntent);
			}
		});
		return convertView;
	}
}
