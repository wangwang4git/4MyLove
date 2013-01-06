package com.bbs.whu.adapter;

import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 帖子详情列表中的帖子评论部分
 * 
 * @author double
 * 
 */
public class BulletinListCommentViewHolder {
	// 评论作者
	TextView holderCommentAuthor;
	// 评论内容
	TextView holderCommentContent;
	// 评论的“评论自”部分的框架
	LinearLayout holderCommentReplyLinearLayout;
	// 评论的“评论自”部分
	TextView holderCommentReply;
	// 评论来源
	TextView holderCommentSource;
}
