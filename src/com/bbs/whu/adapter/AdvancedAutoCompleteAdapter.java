package com.bbs.whu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.model.UserPasswordBean;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyFileUtils;

/**
 * AutoCompleteTextView�ؼ��Զ���Adapter
 * 
 * @author double
 * 
 */
public class AdvancedAutoCompleteAdapter extends BaseAdapter implements
		Filterable {
	private Context context;
	// ���е�item
	private ArrayList<UserPasswordBean> mOriginalValues;
	// ���˺��item
	private List<UserPasswordBean> mFilterValues;
	// �����ʾ���ٸ�ѡ��,������ʾȫ��
	private int maxMatch = 10;
	private ArrayFilter mFilter;

	public AdvancedAutoCompleteAdapter(Context context,
			ArrayList<UserPasswordBean> mOriginalValues) {
		this.context = context;
		this.mOriginalValues = mOriginalValues;
	}

	public AdvancedAutoCompleteAdapter(Context context,
			ArrayList<UserPasswordBean> mOriginalValues, int maxMatch) {
		this.context = context;
		this.mOriginalValues = mOriginalValues;
		this.maxMatch = maxMatch;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			// TODO Auto-generated method stub
			FilterResults results = new FilterResults();

			if (prefix == null || prefix.length() == 0) {
				ArrayList<UserPasswordBean> list = new ArrayList<UserPasswordBean>(
						mOriginalValues);
				results.values = list;
				results.count = list.size();
				return results;
			} else {
				String prefixString = prefix.toString().toLowerCase();
				int count = mOriginalValues.size();
				ArrayList<UserPasswordBean> newValues = new ArrayList<UserPasswordBean>(
						count);

				for (int i = 0; i < count; i++) {
					String valueText = mOriginalValues.get(i).getName()
							.toLowerCase();
					if (valueText.startsWith(prefixString)) { // Դ�� ,ƥ�俪ͷ
						newValues.add(mOriginalValues.get(i));
					}
					if (maxMatch > 0) {// ����������
						if (newValues.size() > maxMatch - 1) {// ��Ҫ̫��
							break;
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// TODO Auto-generated method stub
			mFilterValues = (List<UserPasswordBean>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFilterValues.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return mFilterValues.get(position).getName();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.advanced_autocomplete_item,
					null);
			holder.tv = (TextView) convertView
					.findViewById(R.id.advanced_autocomplete_username);
			holder.iv = (ImageView) convertView
					.findViewById(R.id.advanced_autocomplete_delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv.setText(mFilterValues.get(position).getName());
		holder.iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final UserPasswordBean userPasswordBean = mFilterValues
						.remove(position);
				notifyDataSetChanged();

				// ��ʾ�û��Ƿ�h�����˺���Ϣ
				AlertDialog.Builder builder = new Builder(context);
				builder.setMessage("ȷ��Ҫɾ�����˺�������Ϣ��?");
				builder.setTitle("��ʾ");
				builder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// ɾ��������Ϣ
								mOriginalValues.remove(userPasswordBean);
								// ���л����û���������json�ļ�
								MyBBSCache.setUserPasswordList(mOriginalValues,
										MyFileUtils.USERPASSWORDNAME);
								MyFileUtils.delFolder(MyFileUtils
										.getSdcardDataCacheDir(userPasswordBean
												.getName()));

								// �Ի����˳�
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("ȡ��",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// �Ի����˳�
								dialog.dismiss();
							}
						});
				builder.create().show();
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv;
		ImageView iv;
	}
}
