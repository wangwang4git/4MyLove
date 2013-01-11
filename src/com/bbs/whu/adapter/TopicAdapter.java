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
 * �����еİ���µ������б�����������
 * 
 * @author ljp
 * 
 */
public class TopicAdapter extends MyBaseAdapter {
	// ����Ӣ����
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
			// ��ȡ�б�Ԫ���еĿؼ�����
			holder.holderTopicTitle = (TextView) convertView
					.findViewById(R.id.topic_title);
			holder.holderTopicAuthor = (TextView) convertView
					.findViewById(R.id.topic_author);
			holder.holderTopicDatetime = (TextView) convertView
					.findViewById(R.id.topic_datetime);
			holder.holderTopicNumber = (TextView) convertView
					.findViewById(R.id.topic_number);
			// ���ÿؼ�����convertView
			convertView.setTag(holder);
		} else {
			holder = (TopicViewHolder) convertView.getTag();
		}
		// ���ӱ���
		String title = ((TopicBean) mItems.get(position)).getTitle();
		// ��������
		String author = ((TopicBean) mItems.get(position)).getAuthor();
		// ���ӷ���ʱ��
		String datetime = ((TopicBean) mItems.get(position)).getPosttime();
		// ���ӻظ���
		String number = ((TopicBean) mItems.get(position)).getReplyNum();
		// ����ID
		final String groupid = ((TopicBean) mItems.get(position)).getGID();

		// ���ؼ�
		holder.holderTopicTitle.setText(title);
		holder.holderTopicAuthor.setText(author);
		holder.holderTopicDatetime.setText(datetime);
		holder.holderTopicNumber.setText(number);

		// ��ӵ����Ӧ�¼�
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// ��ת�������������
				Intent mIntent = new Intent(context, BulletinActivity.class);
				// ��Ӳ���
				mIntent.putExtra("board", board);
				mIntent.putExtra("groupid", groupid);
				context.startActivity(mIntent);
			}
		});
		return convertView;
	}
}