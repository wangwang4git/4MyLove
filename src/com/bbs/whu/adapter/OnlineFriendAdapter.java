package com.bbs.whu.adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.activity.MailSendActivity;
import com.bbs.whu.activity.OnlineFriendActivity;
import com.bbs.whu.activity.PersonActivity;
import com.bbs.whu.model.FriendBean;
import com.bbs.whu.utils.MyBBSRequest;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// 长按事件监听
		OnLongClickListener mOnLongClickListener = new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				String[] choices = new String[] { "删除好友" };
				// 弹出对话框
				new AlertDialog.Builder(context)
						.setTitle("列表框")
						.setItems(choices,
								new DialogInterface.OnClickListener() {
									// 响应列表的点击事件
									public void onClick(DialogInterface dialog,
											int which) {
										// 处理
										processLongClick(which, position);
									}
								}).show();
				return false;
			}
		};

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
		// 设置长按动作
		convertView.setOnLongClickListener(mOnLongClickListener);
		// 设置点击动作
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 跳转到发信界面
				Intent mIntent = new Intent(context, PersonActivity.class);
				mIntent.putExtra("author", friend.getID());
				context.startActivity(mIntent);
			}
		});
		return convertView;
	}

	/**
	 * 处理长按的响应事件
	 * 
	 * @param which
	 *            对话框中按下的选项序号
	 * @param position
	 *            长按的item在列表中的序号
	 */

	private void processLongClick(int which, int position) {
		switch (which) {
		case 0:
			// get参数
			ArrayList<String> keys = new ArrayList<String>();
			ArrayList<String> values = new ArrayList<String>();
			// 添加get参数
			keys.add("app");
			values.add("friend");
			keys.add("delete");
			values.add(((FriendBean) mItems.get(position)).getID());

			// 更改请求码
			OnlineFriendActivity
					.setRequestCode(OnlineFriendActivity.SERVER_REQUEST_DELETE_FRIEND);
			// 请求数据
			MyBBSRequest.mGet(MyConstants.GET_URL, keys, values,
					"OnlineFriendActivity", true, context);

			break;
		default:
			break;
		}
	}
}