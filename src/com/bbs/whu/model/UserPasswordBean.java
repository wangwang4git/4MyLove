package com.bbs.whu.model;

/**
 * �û����������Ԫ�ؽṹ����
 * 
 * @author WWang
 * 
 */
public class UserPasswordBean {
	// �û���
	private String name;
	// ����
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
