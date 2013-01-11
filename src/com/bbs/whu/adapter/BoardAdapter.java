package com.bbs.whu.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.activity.TopicActivity;
import com.bbs.whu.model.BoardBean;
import com.bbs.whu.model.board.Board;

public class BoardAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<BoardBean> groups;
	private List<List<Board>> childs;

	/**
	 * @param context
	 *            对象
	 * @param groups
	 *            一级列表数据源
	 * @param childs
	 *            二级列表数据源
	 */
	public BoardAdapter(Context context, List<BoardBean> groups,
			List<List<Board>> childs) {
		super();
		this.context = context;
		this.groups = groups;
		this.childs = childs;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childs.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// 获取二级列表的View对象
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final BoardChildViewHolder holder;
		if (convertView == null) {
			holder = new BoardChildViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.board_child_item, null);
			// 获取列表元素中的控件对象
			holder.holderBoardChildName = (TextView) convertView
					.findViewById(R.id.board_child_name);
			holder.holderBoardChildNumber = (TextView) convertView
					.findViewById(R.id.board_child_number);
			holder.holderBoardChildEnglishname = (TextView) convertView
					.findViewById(R.id.board_child_englishname);
			// 设置控件集到convertView
			convertView.setTag(holder);
		} else {
			holder = (BoardChildViewHolder) convertView.getTag();
		}

		String nameStr = ((Board) getChild(groupPosition, childPosition))
				.getName().toString();
		String numberStr = ((Board) getChild(groupPosition, childPosition))
				.getNum().toString();
		final String englishnameStr = ((Board) getChild(groupPosition,
				childPosition)).getId().toString();

		holder.holderBoardChildName.setText(nameStr);
		holder.holderBoardChildNumber.setText(numberStr);
		holder.holderBoardChildEnglishname.setText(englishnameStr);

		// 添加点击响应事件
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 跳转到帖子列表界面
				Intent mIntent = new Intent(context, TopicActivity.class);
				// 添加参数 app=topics&board=PieFriends&page=1
				mIntent.putExtra("board", englishnameStr);
				context.startActivity(mIntent);
			}
		});

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childs.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 获取一级列表View对象
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 获取一级列表布局文件,设置相应元素属性
		LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(
				R.layout.board_group_item, null);
		TextView name = (TextView) linearLayout
				.findViewById(R.id.board_group_name);
		name.setText(groups.get(groupPosition).getName().toString());

		return linearLayout;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}