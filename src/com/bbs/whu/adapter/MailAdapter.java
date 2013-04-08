package com.bbs.whu.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.activity.MailContentActivity;
import com.bbs.whu.model.MailBean;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyFileUtils;

public class MailAdapter extends MyBaseAdapter {
	// ISNEW
	// ���ʼ�
	final static private String ISNEW = "N";
	
	// ��������
	private int mailBoxType;

	public void setMailBoxType(int mailBoxType) {
		this.mailBoxType = mailBoxType;
	}

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
		// �ʼ�����ֵ
		final String num = ((MailBean) mItems.get(position)).getNum()
				.toString();
		// �ʼ�����
		String title = ((MailBean) mItems.get(position)).getTitle();
		// �ʼ�����
		String author = ((MailBean) mItems.get(position)).getSender()
				.toString();
		// �ʼ�����ʱ��
		String datetime = ((MailBean) mItems.get(position)).getTime()
				.toString();
		// �ʼ�����
		String flag = ((MailBean) mItems.get(position)).getIsnew().toString()
				.trim();
		
		// ��ȡ�Ѷ����
		HashMap<String, Byte> readedTagMap = MyApplication.getInstance()
				.getReadedTag();
		String key = mailBoxType + num;
		if (readedTagMap.containsKey(key)) {
			Byte tag = (Byte) readedTagMap.get(key);
			if ((byte) 0 == tag) {
				// δ��������Ϊ��ɫ
				holder.holderMailTitle.setTextColor(Color.BLACK);
			} else {
				// �Ѷ�������Ϊ��ɫ
				holder.holderMailTitle.setTextColor(Color.GRAY);
			}
		} else {
			// δ��������Ϊ��ɫ
			holder.holderMailTitle.setTextColor(Color.BLACK);
			readedTagMap.put(key, (byte) 0);
		}

		// ���ؼ�
		if (flag.equals(ISNEW)) {
			holder.holderMailTag.setVisibility(View.VISIBLE);
			holder.holderMailTag.setImageResource(R.drawable.topic_new);
		} else {
			holder.holderMailTag.setVisibility(View.INVISIBLE);
		}
		holder.holderMailTitle.setText(title);
		holder.holderMailAuthor.setText(author);
		holder.holderMailDatetime.setText(datetime);

		// ��ӵ����Ӧ�¼�
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// �Ѷ�������Ϊ��ɫ
				holder.holderMailTitle.setTextColor(Color.GRAY);
				// �����Ѷ����
				HashMap<String, Byte> readedTagMap = MyApplication
						.getInstance().getReadedTag();
				String key = mailBoxType + num;
				// �������Ŀδ������Ҫ���Ϊ�Ѷ�
				readedTagMap.put(key, (byte) 1);
				MyApplication.getInstance().setReadedTag(readedTagMap);
				// ���л����Ѷ����json�ļ�
				MyBBSCache.setReadedTagMap(MyApplication.getInstance()
						.getReadedTag(), MyFileUtils.READEDTAGNAME);

				// ��ת���½����ݽ���
				Intent mIntent = new Intent(context, MailContentActivity.class);
				// ��Ӳ���
				mIntent.putExtra("boxname", mailBoxType);
				mIntent.putExtra("read", Integer.parseInt(num.trim()));
				context.startActivity(mIntent);
			}
		});
		return convertView;
	}
}
