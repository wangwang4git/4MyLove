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
import com.bbs.whu.model.TopTenBean;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyFileUtils;

/**
 * ��ʮ���б�����������
 * 
 * @author wwang
 * 
 */
public class TopTenAdapter extends MyBaseAdapter {

	public TopTenAdapter(Context context, ArrayList<TopTenBean> items,
			int rLayoutList) {
		super(context, items, rLayoutList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final TopTenViewHolder holder;
		if (convertView == null) {
			holder = new TopTenViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					this.mRLayoutList, null);
			// ��ȡ�б�Ԫ���еĿؼ�����
			holder.holderTopTenTitle = (TextView) convertView
					.findViewById(R.id.top_ten_title);
			holder.holderTopTenNumber = (TextView) convertView
					.findViewById(R.id.top_ten_number);
			holder.holderTopTenAuthor = (TextView) convertView
					.findViewById(R.id.top_ten_author);
			holder.holderTopTenBoardName = (TextView) convertView
					.findViewById(R.id.top_ten_boardname);
			// ���ÿؼ�����convertView
			convertView.setTag(holder);
		} else {
			holder = (TopTenViewHolder) convertView.getTag();
		}
		TopTenBean mItem = (TopTenBean) mItems.get(position);
		// ���ӱ���
		String title = mItem.getTitle();
		// ���ӻظ���
		String number = mItem.getNumber();
		// ��������
		String author = mItem.getAuthor();
		// ���Ӱ���������
		String boardName = mItem.getBoardname();
		// ���Ӱ���Ӣ����
		final String board = mItem.getBoard();
		// ����ID
		final String groupid = mItem.getGroupid();

		// ��ȡ�Ѷ����
		HashMap<String, Byte> readedTagMap = MyApplication.getInstance()
				.getReadedTag();
		String key = groupid;
		if (readedTagMap.containsKey(key)) {
			Byte tag = (Byte) readedTagMap.get(key);
			if ((byte) 0 == tag) {
				// δ��������Ϊ��ɫ
				holder.holderTopTenTitle.setTextColor(Color.BLACK);
			} else {
				// �Ѷ�������Ϊ��ɫ
				holder.holderTopTenTitle.setTextColor(Color.GRAY);
			}
		} else {
			// δ��������Ϊ��ɫ
			holder.holderTopTenTitle.setTextColor(Color.BLACK);
			readedTagMap.put(key, (byte) 0);
		}
		
		// ���ؼ�
		holder.holderTopTenTitle.setText(title);
		holder.holderTopTenNumber.setText(number);
		holder.holderTopTenAuthor.setText(author);
		holder.holderTopTenBoardName.setText(boardName);

		// ��ӵ����Ӧ�¼�
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// �Ѷ�������Ϊ��ɫ
				holder.holderTopTenTitle.setTextColor(Color.GRAY);
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
}