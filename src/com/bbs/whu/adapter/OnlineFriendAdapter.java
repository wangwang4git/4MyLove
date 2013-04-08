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
 * ���ߺ����б�����������
 * 
 * @author wwang
 * 
 */
public class OnlineFriendAdapter extends MyBaseAdapter {
	// ͼƬ�첽����������
	private ImageLoader imageLoader;
	// ͼƬ�첽���ػ������ñ���
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
		// �����¼�����
		OnLongClickListener mOnLongClickListener = new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				String[] choices = new String[] { "ɾ������" };
				// �����Ի���
				new AlertDialog.Builder(context)
						.setTitle("�б��")
						.setItems(choices,
								new DialogInterface.OnClickListener() {
									// ��Ӧ�б�ĵ���¼�
									public void onClick(DialogInterface dialog,
											int which) {
										// ����
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
			// ��ȡ�б�Ԫ���еĿؼ�����
			holder.holderOnlineFriendHeadPortrait = (ImageView) convertView
					.findViewById(R.id.online_friend_head);
			holder.holderOnlineFriendName = (TextView) convertView
					.findViewById(R.id.online_friend_name);
			holder.holderOnlineFriendMail = (ImageView) convertView
					.findViewById(R.id.online_friend_mail);
			// ���ÿؼ�����convertView
			convertView.setTag(holder);
		} else {
			holder = (OnlineFriendViewHolder) convertView.getTag();
		}
		// ������Դ��ȡ���ߺ�������
		final FriendBean friend = (FriendBean) mItems.get(position);

		// ���ؼ�
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
				// ��ת�����Ž���
				Intent mIntent = new Intent(context, MailSendActivity.class);
				mIntent.putExtra("sender", friend.getID());
				context.startActivity(mIntent);
			}
		});
		// ���ó�������
		convertView.setOnLongClickListener(mOnLongClickListener);
		// ���õ������
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ��ת�����Ž���
				Intent mIntent = new Intent(context, PersonActivity.class);
				mIntent.putExtra("author", friend.getID());
				context.startActivity(mIntent);
			}
		});
		return convertView;
	}

	/**
	 * ����������Ӧ�¼�
	 * 
	 * @param which
	 *            �Ի����а��µ�ѡ�����
	 * @param position
	 *            ������item���б��е����
	 */

	private void processLongClick(int which, int position) {
		switch (which) {
		case 0:
			// get����
			ArrayList<String> keys = new ArrayList<String>();
			ArrayList<String> values = new ArrayList<String>();
			// ���get����
			keys.add("app");
			values.add("friend");
			keys.add("delete");
			values.add(((FriendBean) mItems.get(position)).getID());

			// ����������
			OnlineFriendActivity
					.setRequestCode(OnlineFriendActivity.SERVER_REQUEST_DELETE_FRIEND);
			// ��������
			MyBBSRequest.mGet(MyConstants.GET_URL, keys, values,
					"OnlineFriendActivity", true, context);

			break;
		default:
			break;
		}
	}
}