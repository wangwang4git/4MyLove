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
import com.bbs.whu.activity.FavBoardActivity;
import com.bbs.whu.activity.TopicActivity;
import com.bbs.whu.model.FavBrdBean;

/**
 * �ղذ���б�����������
 * 
 * @author wwang
 * 
 */
public class FavBoardAdapter extends MyBaseAdapter {

	public FavBoardAdapter(Context context, ArrayList<FavBrdBean> items,
			int rLayoutList) {
		super(context, items, rLayoutList);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		FavBoardViewHolder holder;
		if (convertView == null) {
			holder = new FavBoardViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					this.mRLayoutList, null);
			// ��ȡ�б�Ԫ���еĿؼ�����
			holder.holderFavBoardName = (TextView) convertView
					.findViewById(R.id.fav_board_name);
			// ���ÿؼ�����convertView
			convertView.setTag(holder);
		} else {
			holder = (FavBoardViewHolder) convertView.getTag();
		}
		// ������Դ��ȡ�ղذ�����
		String name = ((FavBrdBean) mItems.get(position)).getDesc().toString();
		// ���ؼ�
		holder.holderFavBoardName.setText(name);
		// ���õ���¼�
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FavBrdBean mFavBrdBean = (FavBrdBean) mItems.get(position);
				String mClass = mFavBrdBean.getCLASS().toString();
				if (mClass.equalsIgnoreCase("Ŀ¼")) {
					// ��ת���µ��ղؼа��
					Intent mIntent = new Intent(context, FavBoardActivity.class);
					mIntent.putExtra("select", mFavBrdBean.getSelect()
							.toString());
					context.startActivity(mIntent);
				} else {
					// ��ת�������б����
					Intent mIntent = new Intent(context, TopicActivity.class);
					// ��Ӳ��� app=topics&board=PieFriends&page=1
					mIntent.putExtra("board", mFavBrdBean.getName().toString());
					mIntent.putExtra("name", mFavBrdBean.getDesc().toString());
					context.startActivity(mIntent);
				}
			}
		});
		return convertView;
	}
}