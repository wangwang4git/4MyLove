package com.bbs.whu.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 帖子详情列表中的帖子评论部分
 * 
 * @author double
 * 
 */
public class BulletinCommentViewHolder {
	// 评论作者
	TextView holderBulletinCommentAuthor;
	// 评论作者头像
	ImageView holderBulletinCommentAuthorIcon;
	// 评论时间
	TextView holderBulletinCommentTime;
	// 评论内容
	TextView holderBulletinCommentContent;
	// 评论的“评论自”部分的框架
	LinearLayout holderBulletinCommentReplyLinearLayout;
	// 评论的“评论自”部分
	TextView holderBulletinCommentReply;
	// 评论来源
	TextView holderBulletinCommentSource;
}
