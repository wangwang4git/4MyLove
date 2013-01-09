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
				holder.holderBulletinAuthorTitle = (TextView) convertView
						.findViewById(R.id.bulletin_author_title);
				holder.holderBulletinAuthorAuthor = (TextView) convertView
						.findViewById(R.id.bulletin_author_author);
				holder.holderBulletinAuthorDateTime = (TextView) convertView
						.findViewById(R.id.bulletin_author_datetime);
				holder.holderBulletinAuthorText = (TextView) convertView
						.findViewById(R.id.bulletin_author_text);
				holder.holderBulletinAuthorSource = (TextView) convertView
						.findViewById(R.id.bulletin_author_source);
				// 设置控件集到convertView
				convertView.setTag(holder);
			} else {
				holder = (BulletinAuthorViewHolder) convertView.getTag();
			}
			// 填充控件
			holder.holderBulletinAuthorTitle.setText(title);
			holder.holderBulletinAuthorAuthor.setText(author);
			holder.holderBulletinAuthorDateTime.setText(datetime);
			holder.holderBulletinAuthorText.setText(text);
			holder.holderBulletinAuthorSource.setText(source);

		} else if (getItemViewType(position) == COMMENT_ITEM) {
			// 获取“回复”部分的控件
			BulletinCommentViewHolder holder = new BulletinCommentViewHolder();
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.bulletin_comment_item, null);
				holder.holderBulletinCommentAuthor = (TextView) convertView
						.findViewById(R.id.bulletin_comment_author);
				holder.holderBulletinCommentContent = (TextView) convertView
						.findViewById(R.id.bulletin_comment_content);
				holder.holderBulletinCommentReplyLinearLayout = (LinearLayout) convertView
						.findViewById(R.id.bulletin_comment_reply_linearLayout);
				holder.holderBulletinCommentReply = (TextView) convertView
						.findViewById(R.id.bulletin_comment_reply);
				holder.holderBulletinCommentSource = (TextView) convertView
						.findViewById(R.id.bulletin_comment_source);
				// 设置控件集到convertView
				convertView.setTag(holder);
			} else {
				holder = (BulletinCommentViewHolder) convertView.getTag();
			}
			// 填充控件
			holder.holderBulletinCommentAuthor.setText(author);
			// if (reply.equals("null"))
			// holder.holderCommentReplyLinearLayout.setVisibility(View.GONE);
			// else
			holder.holderBulletinCommentReply.setText(reply);
			holder.holderBulletinCommentContent.setText(text);
			holder.holderBulletinCommentSource.setText(source);
		}
		return convertView;
	}
}
