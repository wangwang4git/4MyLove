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
 * 收藏版块列表数据适配器
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
			// 获取列表元素中的控件对象
			holder.holderFavBoardName = (TextView) convertView
					.findViewById(R.id.fav_board_name);
			// 设置控件集到convertView
			convertView.setTag(holder);
		} else {
			holder = (FavBoardViewHolder) convertView.getTag();
		}
		// 从数据源获取收藏版块标题
		String name = ((FavBrdBean) mItems.get(position)).getDesc().toString();
		// 填充控件
		holder.holderFavBoardName.setText(name);
		// 设置点击事件
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FavBrdBean mFavBrdBean = (FavBrdBean) mItems.get(position);
				String mClass = mFavBrdBean.getCLASS().toString();
				if (mClass.equalsIgnoreCase("目录")) {
					// 跳转入新的收藏夹版块
					Intent mIntent = new Intent(context, FavBoardActivity.class);
					mIntent.putExtra("select", mFavBrdBean.getSelect()
							.toString());
					context.startActivity(mIntent);
				} else {
					// 跳转到帖子列表界面
					Intent mIntent = new Intent(context, TopicActivity.class);
					// 添加参数 app=topics&board=PieFriends&page=1
					mIntent.putExtra("board", mFavBrdBean.getName().toString());
					mIntent.putExtra("name", mFavBrdBean.getDesc().toString());
					context.startActivity(mIntent);
				}
			}
		});
		return convertView;
	}
}