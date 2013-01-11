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
import com.bbs.whu.model.TopTenBean;

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
		TopTenViewHolder holder;
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
		// ���ӱ���
		String title = ((TopTenBean) mItems.get(position)).getTilte();
		// ���ӻظ���
		Long number = ((TopTenBean) mItems.get(position)).getNumber();
		// ��������
		String author = ((TopTenBean) mItems.get(position)).getAuthor();
		// ���Ӱ���������
		String boardName = ((TopTenBean) mItems.get(position)).getBoardname();
		// ���Ӱ���Ӣ����
		final String board = ((TopTenBean) mItems.get(position)).getBoard();
		// ����ID
		final Long groupid = ((TopTenBean) mItems.get(position)).getGroupid();
		
		// ���ؼ�
		holder.holderTopTenTitle.setText(title);
		holder.holderTopTenNumber.setText(number.toString());
		holder.holderTopTenAuthor.setText(author);
		holder.holderTopTenBoardName.setText(boardName);
		
		// ��ӵ����Ӧ�¼�
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// ��ת�������������
				Intent mIntent = new Intent(context, BulletinActivity.class);
				// ��Ӳ���
				mIntent.putExtra("board", board);
				mIntent.putExtra("groupid", groupid.toString());
				context.startActivity(mIntent);
			}
		});
		return convertView;
	}
}