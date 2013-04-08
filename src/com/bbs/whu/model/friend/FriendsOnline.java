package com.bbs.whu.model.friend;

import java.util.ArrayList;
import java.util.List;

import com.bbs.whu.model.FriendsOnlineBean;

public class FriendsOnline {
	private Type Type;
	private List<FriendsOnlineBean> friends = new ArrayList<FriendsOnlineBean>();
	
	public FriendsOnline() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FriendsOnline(com.bbs.whu.model.friend.Type type,
			List<FriendsOnlineBean> friends) {
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

	public List<FriendsOnlineBean> getFriends() {
		return friends;
	}

	public void setFriends(List<FriendsOnlineBean> friends) {
		this.friends = friends;
	}

	@Override
	public String toString() {
		return "FriendsOnline [Type=" + Type + ", friends=" + friends + "]";
	}
}
