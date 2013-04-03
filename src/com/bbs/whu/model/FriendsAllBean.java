package com.bbs.whu.model;

import com.bbs.whu.model.friend.ID;
import com.bbs.whu.model.friend.experience;
import com.bbs.whu.model.friend.userface_img;


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
	private userface_img userface_img;
	
	public FriendsAllBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FriendsAllBean(com.bbs.whu.model.friend.experience experience,
			com.bbs.whu.model.friend.ID iD,
			com.bbs.whu.model.friend.userface_img userface_img) {
		super();
		this.experience = experience;
		ID = iD;
		this.userface_img = userface_img;
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

	public userface_img getUserface_img() {
		return userface_img;
	}

	public void setUserface_img(userface_img userface_img) {
		this.userface_img = userface_img;
	}

	@Override
	public String toString() {
		return "FriendsAllBean [experience=" + experience + ", ID=" + ID
				+ ", userface_img=" + userface_img + "]";
	}
}
