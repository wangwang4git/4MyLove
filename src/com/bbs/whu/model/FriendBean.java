package com.bbs.whu.model;

public class FriendBean {
	private String ID;
	private String userFaceImg;
	private boolean isOnline;

	public FriendBean(String id, String userFaceImg, boolean isOnline) {
		this.ID = id;
		this.userFaceImg = userFaceImg;
		this.isOnline = isOnline;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getUserFaceImg() {
		return userFaceImg;
	}

	public void setUserFaceImg(String userFaceImg) {
		this.userFaceImg = userFaceImg;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

}
