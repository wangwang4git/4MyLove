package com.bbs.whu.model;

import com.bbs.whu.model.friend.idle;
import com.bbs.whu.model.friend.mode;
import com.bbs.whu.model.friend.userfaceimg;
import com.bbs.whu.model.friend.userfrom;
import com.bbs.whu.model.friend.userid;
import com.bbs.whu.model.friend.username;


/**
 * ���ߺ����б�Ԫ�ؽṹ����
 * 
 * @author WWang
 * 
 */
public class FriendsOnlineBean {
	// BBS�����û�ID�����û���
	private userid userid;
	// BBS�����û��ǳ�
	private username username;
	// BBS�����û���¼��ԴIP��ַ
	private userfrom userfrom;
	// BBS�����û�����ʱ�䣬��λ��
	private idle idle;
	// BBS�����û���ǰ���״̬
	private mode mode;
	// ͷ��url
	private userfaceimg userfaceimg;
	
	public FriendsOnlineBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FriendsOnlineBean(com.bbs.whu.model.friend.userid userid,
			com.bbs.whu.model.friend.username username,
			com.bbs.whu.model.friend.userfrom userfrom,
			com.bbs.whu.model.friend.idle idle,
			com.bbs.whu.model.friend.mode mode,
			com.bbs.whu.model.friend.userfaceimg userfaceimg) {
		super();
		this.userid = userid;
		this.username = username;
		this.userfrom = userfrom;
		this.idle = idle;
		this.mode = mode;
		this.userfaceimg = userfaceimg;
	}

	public userid getUserid() {
		return userid;
	}

	public void setUserid(userid userid) {
		this.userid = userid;
	}

	public username getUsername() {
		return username;
	}

	public void setUsername(username username) {
		this.username = username;
	}

	public userfrom getUserfrom() {
		return userfrom;
	}

	public void setUserfrom(userfrom userfrom) {
		this.userfrom = userfrom;
	}

	public idle getIdle() {
		return idle;
	}

	public void setIdle(idle idle) {
		this.idle = idle;
	}

	public mode getMode() {
		return mode;
	}

	public void setMode(mode mode) {
		this.mode = mode;
	}

	public userfaceimg getUserfaceimg() {
		return userfaceimg;
	}

	public void setUserfaceimg(userfaceimg userfaceimg) {
		this.userfaceimg = userfaceimg;
	}

	@Override
	public String toString() {
		return "FriendsOnlineBean [userid=" + userid + ", username=" + username
				+ ", userfrom=" + userfrom + ", idle=" + idle + ", mode="
				+ mode + ", userfaceimg=" + userfaceimg + "]";
	}
}
