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
			// ��ȡ�б�Ԫ���еĿؼ�����
			holder.holderMailTag = (ImageView) convertView
					.findViewById(R.id.mail_tag);
			holder.holderMailTitle = (TextView) convertView
					.findViewById(R.id.mail_title);
			holder.holderMailAuthor = (TextView) convertView
					.findViewById(R.id.mail_author);
			holder.holderMailDatetime = (TextView) convertView
					.findViewById(R.id.mail_datetime);
			// ���ÿؼ�����convertView
			convertView.setTag(holder);
		} else {
			holder = (MailViewHolder) convertView.getTag();
		}
		// �ʼ�����
		String title = ((MailBean) mItems.get(position)).getTitle();
		// �ʼ�����
		String author = ((MailBean) mItems.get(position)).getSender()
				.toString();
		// �ʼ�����ʱ��
		String datetime = ((MailBean) mItems.get(position)).getTime()
				.toString();
		// �ʼ�����
		// String flag = ((TopicBean) mItems.get(position)).getFlag();
		// ����ID
		final String size = ((MailBean) mItems.get(position)).getSize()
				.toString();

		// ���ؼ�
		holder.holderMailTitle.setText(title);
		holder.holderMailAuthor.setText(author);
		holder.holderMailDatetime.setText(datetime);

		// ��ӵ����Ӧ�¼�
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// // �Ѷ�������Ϊ��ɫ
				// holder.holderTopicTitle.setTextColor(Color.GRAY);
				// // �����Ѷ����
				// HashMap<String, Byte> readedTagMap = MyApplication
				// .getInstance().getReadedTag();
				// String key = groupid;
				// // �������Ŀδ������Ҫ���Ϊ�Ѷ�
				// readedTagMap.put(key, (byte) 1);
				// MyApplication.getInstance().setReadedTag(readedTagMap);
				// // ���л����Ѷ����json�ļ�
				// MyBBSCache.setReadedTagMap(MyApplication.getInstance()
				// .getReadedTag(), MyFileUtils.READEDTAGNAME);
				//
				// // ��ת�������������
				// Intent mIntent = new Intent(context, BulletinActivity.class);
				// // ��Ӳ���
				// mIntent.putExtra("board", board);
				// mIntent.putExtra("groupid", groupid);
				// context.startActivity(mIntent);
			}
		});
		return convertView;
	}
}
