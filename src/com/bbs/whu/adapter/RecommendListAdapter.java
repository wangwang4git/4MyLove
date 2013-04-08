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
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.activity.BulletinActivity;
import com.bbs.whu.model.RecommendBean;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyFileUtils;

/**
 * �Ƽ���������������
 * 
 * @author wwang
 * 
 */
public class RecommendListAdapter extends MyBaseAdapter {

	public RecommendListAdapter(Context context,
			ArrayList<RecommendBean> items, int rLayoutList) {
		super(context, items, rLayoutList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final RecommendListViewHolder holder;
		if (convertView == null) {
			holder = new RecommendListViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					this.mRLayoutList, null);
			// ��ȡ�б�Ԫ���еĿؼ�����
			holder.holderRecommendTitle = (TextView) convertView
					.findViewById(R.id.recommend_title);
			holder.holderRecommendAuthor = (TextView) convertView
					.findViewById(R.id.recommend_author);
			holder.holderRecommendRecommendPeople = (TextView) convertView
					.findViewById(R.id.recommend_recommend_people);
			holder.holderRecommendBoard = (TextView) convertView
					.findViewById(R.id.recommend_board);
			holder.holderRecommendTime = (TextView) convertView
					.findViewById(R.id.recommend_time);
			holder.holderRecommendReplyNumber = (TextView) convertView
					.findViewById(R.id.recommend_reply_number);
			// ���ÿؼ�����convertView
			convertView.setTag(holder);
		} else {
			holder = (RecommendListViewHolder) convertView.getTag();
		}
		// �Ƽ����±���
		String title = ((RecommendBean) mItems.get(position)).getTitle();
		// ԭ������
		String author = ((RecommendBean) mItems.get(position)).getAuthor();
		// �Ƽ���
		String recommby = ((RecommendBean) mItems.get(position)).getRecommby();
		// ԭ������������������
		String originBoardName = ((RecommendBean) mItems.get(position))
				.getOriginBoardName();
		// �Ƽ�ʱ��
		String recommTime = ((RecommendBean) mItems.get(position))
				.getRecommTime();
		// ��������ԭʼ����Ӣ����
		final String board = ((RecommendBean) mItems.get(position))
				.getOriginBoard();
		// ��������ԭʼ���ı��
		final String recommGID = ((RecommendBean) mItems.get(position))
				.getOriginGID();

		// ��ȡ�Ѷ����
		HashMap<String, Byte> readedTagMap = MyApplication.getInstance()
				.getReadedTag();
		String key = recommGID;
		if (readedTagMap.containsKey(key)) {
			Byte tag = (Byte) readedTagMap.get(key);
			if ((byte) 0 == tag) {
				// δ��������Ϊ��ɫ
				holder.holderRecommendTitle.setTextColor(Color.BLACK);
			} else {
				// �Ѷ�������Ϊ��ɫ
				holder.holderRecommendTitle.setTextColor(Color.GRAY);
			}
		} else {
			// δ��������Ϊ��ɫ
			holder.holderRecommendTitle.setTextColor(Color.BLACK);
			readedTagMap.put(key, (byte) 0);
		}
		
		// ���ؼ�
		holder.holderRecommendTitle.setText(title);
		holder.holderRecommendAuthor.setText(author);
		holder.holderRecommendRecommendPeople.setText(recommby);
		holder.holderRecommendBoard.setText(originBoardName);
		// ��ȡ�����ղ��֣�ʱ���ʽ��2012-11-25 18:02:08
		holder.holderRecommendTime.setText(recommTime.split(" ")[0]);

		// ��ӵ����Ӧ�¼�
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// �Ѷ�������Ϊ��ɫ
				holder.holderRecommendTitle.setTextColor(Color.GRAY);
				// �����Ѷ����
				HashMap<String, Byte> readedTagMap = MyApplication
						.getInstance().getReadedTag();
				String key = recommGID;
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
				mIntent.putExtra("groupid", recommGID);
				context.startActivity(mIntent);
			}
		});

		return convertView;
	}
}