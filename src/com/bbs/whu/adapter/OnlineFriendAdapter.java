package com.bbs.whu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.activity.MailSendActivity;
import com.bbs.whu.model.FriendBean;
import com.bbs.whu.utils.MyConstants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 在线好友列表数据适配器
 * 
 * @author wwang
 * 
 */
public class OnlineFriendAdapter extends MyBaseAdapter {
	// 图片异步下载下载器
	private ImageLoader imageLoader;
	// 图片异步下载缓存设置变量
	private DisplayImageOptions options;
	
	public OnlineFriendAdapter(Context context, ArrayList<FriendBean> items,
			int rLayoutList, ImageLoader imageLoader,
			DisplayImageOptions options) {
		super(context, items, rLayoutList);
		this.imageLoader = imageLoader;
		this.options = options;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OnlineFriendViewHolder holder;
		if (convertView == null) {
			holder = new OnlineFriendViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					this.mRLayoutList, null);
			// 获取列表元素中的控件对象
			holder.holderOnlineFriendHeadPortrait = (ImageView) convertView
					.findViewById(R.id.online_friend_head);
			holder.holderOnlineFriendName = (TextView) convertView
					.findViewById(R.id.online_friend_name);
			holder.holderOnlineFriendMail = (ImageView) convertView
					.findViewById(R.id.online_friend_mail);
			// 设置控件集到convertView
			convertView.setTag(holder);
		} else {
			holder = (OnlineFriendViewHolder) convertView.getTag();
		}
		// 从数据源获取在线好友名称
		final FriendBean friend = (FriendBean) mItems.get(position);

		// 填充控件
		imageLoader.displayImage(
				MyConstants.HEAD_URL + friend.getUserFaceImg(),
				holder.holderOnlineFriendHeadPortrait, options);
		holder.holderOnlineFriendName.setText(friend.getID());
		if (friend.isOnline()) {
			holder.holderOnlineFriendName.setTextColor(context.getResources()
					.getColor(R.color.online_friend_text_color));
		}

		holder.holderOnlineFriendMail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 跳转到发信界面
				Intent mIntent = new Intent(context, MailSendActivity.class);
				mIntent.putExtra("sender", friend.getID());
				context.startActivity(mIntent);
			}
		});

		return convertView;
	}
}