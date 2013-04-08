package com.bbs.whu.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.bbs.whu.activity.BulletinActivity;
import com.bbs.whu.model.TopicBean;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyFileUtils;

/**
 * �����еİ���µ������б�����������
 * 
 * @author ljp
 * 
 */
public class TopicAdapter extends MyBaseAdapter {
	// TOP��HOT��LOCK��NORMAL��DIGEST
	// �ö����������������ϼ�������ͨ������
	final static private String TOP = "TOP"; 
	final static private String HOT = "HOT"; 
	final static private String LOCK = "LOCK"; 
	final static private String NORMAL = "NORMAL";
	final static private String DIGEST = "DIGEST"; 
	
	// ����Ӣ����
	private String board;

	public TopicAdapter(Context context, ArrayList<TopicBean> items,
			int rLayoutList, String board) {
		super(context, items, rLayoutList);
		this.board = board;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final TopicViewHolder holder;
		if (convertView == null) {
			holder = new TopicViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					this.mRLayoutList, null);
			// ��ȡ�б�Ԫ���еĿؼ�����
			holder.holderTopicTag = (ImageView) convertView
					.findViewById(R.id.topic_tag);
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
		// ��������
		String flag = ((TopicBean) mItems.get(position)).getFlag();
		// ����ID
		final String groupid = ((TopicBean) mItems.get(position)).getGID();

		// ��ȡ�Ѷ����
		HashMap<String, Byte> readedTagMap = MyApplication.getInstance()
				.getReadedTag();
		String key = groupid;
		if (readedTagMap.containsKey(key)) {
			Byte tag = (Byte) readedTagMap.get(key);
			if ((byte) 0 == tag) {
				// δ��������Ϊ��ɫ
				holder.holderTopicTitle.setTextColor(Color.BLACK);
			} else {
				// �Ѷ�������Ϊ��ɫ
				holder.holderTopicTitle.setTextColor(Color.GRAY);
			}
		} else {
			// δ��������Ϊ��ɫ
			holder.holderTopicTitle.setTextColor(Color.BLACK);
			readedTagMap.put(key, (byte) 0);
		}
		
		// ���ؼ�
		if (flag.equals(NORMAL)) {
			if (isNewTopic(datetime)) {
				holder.holderTopicTag.setVisibility(View.VISIBLE);
				holder.holderTopicTag.setImageResource(R.drawable.topic_new);
			} else {
				holder.holderTopicTag.setVisibility(View.INVISIBLE);
			}
		} else if (flag.equals(TOP)) {
			holder.holderTopicTag.setVisibility(View.VISIBLE);
			holder.holderTopicTag.setImageResource(R.drawable.topic_top);
		} else if (flag.equals(HOT)) {
			holder.holderTopicTag.setVisibility(View.VISIBLE);
			holder.holderTopicTag.setImageResource(R.drawable.topic_hot);
		} else if (flag.equals(LOCK)) {
			holder.holderTopicTag.setVisibility(View.VISIBLE);
			holder.holderTopicTag.setImageResource(R.drawable.topic_lock);
		} else if (flag.equals(DIGEST)) {
			holder.holderTopicTag.setVisibility(View.VISIBLE);
			holder.holderTopicTag.setImageResource(R.drawable.topic_digest);
		}
		holder.holderTopicTitle.setText(title);
		holder.holderTopicAuthor.setText(author);
		holder.holderTopicDatetime.setText(datetime);
		holder.holderTopicNumber.setText(number);

		// ��ӵ����Ӧ�¼�
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// �Ѷ�������Ϊ��ɫ
				holder.holderTopicTitle.setTextColor(Color.GRAY);
				// �����Ѷ����
				HashMap<String, Byte> readedTagMap = MyApplication
						.getInstance().getReadedTag();
				String key = groupid;
				// �������Ŀδ������Ҫ���Ϊ�Ѷ�
				readedTagMap.put(key, (byte) 1);
				MyApplication.getInstance().setReadedTag(readedTagMap);
				// ���л����Ѷ����json�ļ�
				MyBBSCache.setReadedTagMap(MyApplication.getInstance()
						.getReadedTag(), MyFileUtils.READEDTAGNAME);
				
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
	
	private boolean isNewTopic(String topicDateString) {
		// ����ʱ���ʽ
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// ��ȡ��ǰʱ��
		Date nowDate = new Date();
		// ���ַ������췢��ʱ��
		Date topicDate = null;
		try {
			topicDate = sdf.parse(topicDateString);
			// �뵱ǰʱ����һ���ڵ����ӣ�Լ��Ϊ����
			if ((nowDate.getTime() - topicDate.getTime()) / (1000 * 60 * 60) <= 24) {
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}