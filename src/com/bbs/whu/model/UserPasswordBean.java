package com.bbs.whu.model;

/**
 * 用户名、密码对元素结构定义
 * 
 * @author WWang
 * 
 */
public class UserPasswordBean {
	// 用户名
	private String name;
	// 密码
	private String password;
	
	public UserPasswordBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserPasswordBean(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserPasswordBean [name=" + name + ", password=" + password
				+ "]";
	}
}
