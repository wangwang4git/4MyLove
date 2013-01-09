package com.bbs.whu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.model.BulletinBean;

/**
 * 帖子详情列表数据适配器，
 * 注意，列表中有两种显示形式，分别是“帖子作者”和“评论”
 * 
 * @author double
 * 
 */

public class BulletinAdapter extends MyBaseAdapter {

	// 定义不同Item视图的标志
	public static final int AUTHOR_ITEM = 0;
	public static final int COMMENT_ITEM = 1;

	public BulletinAdapter(Context context, ArrayList<BulletinBean> items,
			int rLayoutList) {
		super(context, items, rLayoutList);
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0)
			return AUTHOR_ITEM;
		else
			return COMMENT_ITEM;
	}

	@Override
	public int getViewTypeCount() {
		// 两种Item视图
		return 2;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 标题
		String title = ((BulletinBean) mItems.get(position)).getTitle();
		// 作者
		String author = ((BulletinBean) mItems.get(position)).getAuthor();
		// 时间
		String datetime = ((BulletinBean) mItems.get(position)).getTime();
		// 内容
		String text = ((BulletinBean) mItems.get(position)).getText();
		// 来源
		String source = ((BulletinBean) mItems.get(position)).getFrom();
		// 回复
		String reply = ((BulletinBean) mItems.get(position)).getReply();

		if (getItemViewType(position) == AUTHOR_ITEM) {
			// 获取“帖子作者”部分的控件
			BulletinAuthorViewHolder holder = new BulletinAuthorViewHolder();
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.bulletin_author_item, null);
				holder.holderBulletinContentTitle = (TextView) convertView
						.findViewById(R.id.bulletin_content_title);
				holder.holderBulletinContentAuthor = (TextView) convertView
						.findViewById(R.id.bulletin_content_author);
				holder.holderBulletinContentDateTime = (TextView) convertView
						.findViewById(R.id.bulletin_content_datetime);
				holder.holderBulletinContentText = (TextView) convertView
						.findViewById(R.id.bulletin_content_text);
				holder.holderBulletinContentSource = (TextView) convertView
						.findViewById(R.id.bulletin_content_source);
				// 设置控件集到convertView
				convertView.setTag(holder);
			} else {
				holder = (BulletinAuthorViewHolder) convertView.getTag();
			}
			// 填充控件
			holder.holderBulletinContentTitle.setText(title);
			holder.holderBulletinContentAuthor.setText(author);
			holder.holderBulletinContentDateTime.setText(datetime);
			holder.holderBulletinContentText.setText(text);
			holder.holderBulletinContentSource.setText(source);

		} else if (getItemViewType(position) == COMMENT_ITEM) {
			// 获取“回复”部分的控件
			BulletinCommentViewHolder holder = new BulletinCommentViewHolder();
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.bulletin_comment_item, null);
				holder.holderCommentAuthor = (TextView) convertView
						.findViewById(R.id.bulletin_comment_author);
				holder.holderCommentContent = (TextView) convertView
						.findViewById(R.id.bulletin_comment_content);
				holder.holderCommentReplyLinearLayout = (LinearLayout) convertView
						.findViewById(R.id.bulletin_comment_reply_linearLayout);
				holder.holderCommentReply = (TextView) convertView
						.findViewById(R.id.bulletin_comment_reply);
				holder.holderCommentSource = (TextView) convertView
						.findViewById(R.id.bulletin_comment_source);
				// 设置控件集到convertView
				convertView.setTag(holder);
			} else {
				holder = (BulletinCommentViewHolder) convertView.getTag();
			}
			// 填充控件
			holder.holderCommentAuthor.setText(author);
			// if (reply.equals("null"))
			// holder.holderCommentReplyLinearLayout.setVisibility(View.GONE);
			// else
			holder.holderCommentReply.setText(reply);
			holder.holderCommentContent.setText(text);
			holder.holderCommentSource.setText(source);
		}
		return convertView;
	}
}
