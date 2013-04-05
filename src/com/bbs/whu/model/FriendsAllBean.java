package com.bbs.whu.model;

import com.bbs.whu.model.friend.ID;
import com.bbs.whu.model.friend.experience;
import com.bbs.whu.model.friend.userfaceimg;


/**
 * 全部好友列表元素结构定义
 * 
 * @author WWang
 * 
 */
public class FriendsAllBean {
	// BBS用户经验值
	private experience experience;
	// BBS用户ID，即用户名
	private ID ID;
	// 头像url
	private userfaceimg userfaceimg;
	
	public FriendsAllBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FriendsAllBean(com.bbs.whu.model.friend.experience experience,
			com.bbs.whu.model.friend.ID iD,
			com.bbs.whu.model.friend.userfaceimg userfaceimg) {
		super();
		this.experience = experience;
		ID = iD;
		this.userfaceimg = userfaceimg;
	}

	public experience getExperience() {
		return experience;
	}

	public void setExperience(experience experience) {
		this.experience = experience;
	}

	public ID getID() {
		return ID;
	}

	public void setID(ID iD) {
		ID = iD;
	}

	public userfaceimg getUserfaceimg() {
		return userfaceimg;
	}

	public void setUserfaceimg(userfaceimg userfaceimg) {
		this.userfaceimg = userfaceimg;
	}

	@Override
	public String toString() {
		return "FriendsAllBean [experience=" + experience + ", ID=" + ID
				+ ", userfaceimg=" + userfaceimg + "]";
	}
}
