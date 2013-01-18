package com.bbs.whu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bbs.whu.R;
import com.bbs.whu.model.FaceBean;

public class FaceGridAdapter extends MyBaseAdapter {

	public FaceGridAdapter(Context context,
			ArrayList<FaceBean> items, int rLayoutList) {
		super(context, items, rLayoutList);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView imageView = null;
		if (convertView == null) {
			convertView = View.inflate(context, mRLayoutList, null);
			imageView = (ImageView) convertView
					.findViewById(R.id.face_imageview);

			convertView.setTag(imageView);
		} else {
			imageView = (ImageView) convertView.getTag();
		}
		// œ‘ æ±Ì«È
		imageView.setImageResource(((FaceBean) mItems
				.get(position)).getId());
		
		return convertView;
	}
}
