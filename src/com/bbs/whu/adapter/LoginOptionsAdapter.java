package com.bbs.whu.adapter;
 
import java.util.ArrayList;

import com.bbs.whu.R;
import com.bbs.whu.utils.MyConstants;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//�Զ���������Adapter
public class LoginOptionsAdapter extends BaseAdapter {

	private ArrayList<String> list = new ArrayList<String>(); 
    private Activity activity = null; 
	private Handler handler;
    
	/**
	 * �Զ��幹�췽��
	 * @param activity
	 * @param handler
	 * @param list
	 */
    public LoginOptionsAdapter(Activity activity,Handler handler,ArrayList<String> list){
    	this.activity = activity;
    	this.handler = handler;
    	this.list = list;
    }
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null; 
        if (convertView == null) { 
            holder = new ViewHolder(); 
            //�������
            convertView = LayoutInflater.from(activity).inflate(R.layout.login_option_item, null); 
            holder.textView = (TextView) convertView.findViewById(R.id.text_user_item); 
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_delete_user); 
            
            convertView.setTag(holder); 
        } else { 
            holder = (ViewHolder) convertView.getTag(); 
        } 
        
        holder.textView.setText(list.get(position));
        
        //Ϊ������ѡ�����ֲ��������¼�������Ч���ǵ������������䵽�ı���
        holder.textView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				Bundle data = new Bundle();
				//����ѡ������
				data.putInt("selIndex", position);
				msg.setData(data);
				msg.what = MyConstants.LOGIN_SELECT_USER;
				//������Ϣ
				handler.sendMessage(msg);
			}
		});
        
        //Ϊ������ѡ��ɾ��ͼ�겿�������¼�������Ч���ǵ������ѡ��ɾ��
        holder.imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				Bundle data = new Bundle();
				//����ɾ������
				data.putInt("delIndex", position);
				msg.setData(data);
				msg.what = MyConstants.LOGIN_DELETE_USER;
				//������Ϣ
				handler.sendMessage(msg);
			}
		});
        
        return convertView; 
	}

}


class ViewHolder { 
    TextView textView; 
    ImageView imageView; 
} 
