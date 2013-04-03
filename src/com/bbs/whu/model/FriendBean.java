package com.bbs.whu.model;

public class FriendBean {
	private String ID;
	private boolean isOnline;

	public FriendBean(String id, boolean isOnline) {
		this.ID = id;
		this.isOnline = isOnline;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

}
