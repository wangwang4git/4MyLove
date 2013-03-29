package com.bbs.whu.model.friend;

import java.util.ArrayList;
import java.util.List;

import com.bbs.whu.model.FriendsAllBean;

public class FriendsAll {
	private Type Type;
	private List<FriendsAllBean> friends = new ArrayList<FriendsAllBean>();
	
	public FriendsAll() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FriendsAll(com.bbs.whu.model.friend.Type type, List<FriendsAllBean> friends) {
		super();
		Type = type;
		this.friends = friends;
	}

	public Type getType() {
		return Type;
	}

	public void setType(Type type) {
		Type = type;
	}

	public List<FriendsAllBean> getFriends() {
		return friends;
	}

	public void setFriends(List<FriendsAllBean> friends) {
		this.friends = friends;
	}

	@Override
	public String toString() {
		return "FriendsAll [Type=" + Type + ", friends=" + friends + "]";
	}
}
