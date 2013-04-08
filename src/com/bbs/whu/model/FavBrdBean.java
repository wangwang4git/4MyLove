package com.bbs.whu.model;

import com.bbs.whu.model.attr.name;
import com.bbs.whu.model.favor.CLASS;
import com.bbs.whu.model.favor.articles;
import com.bbs.whu.model.favor.bid;
import com.bbs.whu.model.favor.bm;
import com.bbs.whu.model.favor.desc;
import com.bbs.whu.model.favor.flag;
import com.bbs.whu.model.favor.select;
import com.bbs.whu.model.favor.users;



/**
 * 我的收藏版面列表元素结构定义
 * 
 * @author WWang
 * 
 */
public class FavBrdBean {
	// select
	private select select;
	// 英文名称
	private name name;
	// 中文名称
	private desc desc;
	// flag
	private flag flag;
	// 类别
	private CLASS CLASS;
	
	// 版块id
	private bid bid;
	// bm
	private bm bm;
	// 本版块发贴总数
	private articles articles;
	// 本版块当前在线人数
	private users users;
	
	public FavBrdBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public FavBrdBean(com.bbs.whu.model.favor.select select,
			com.bbs.whu.model.attr.name name,
			com.bbs.whu.model.favor.desc desc,
			com.bbs.whu.model.favor.flag flag,
			com.bbs.whu.model.favor.CLASS cLASS,
			com.bbs.whu.model.favor.bid bid, com.bbs.whu.model.favor.bm bm,
			com.bbs.whu.model.favor.articles articles,
			com.bbs.whu.model.favor.users users) {
		super();
		this.select = select;
		this.name = name;
		this.desc = desc;
		this.flag = flag;
		CLASS = cLASS;
		this.bid = bid;
		this.bm = bm;
		this.articles = articles;
		this.users = users;
	}

	public select getSelect() {
		return select;
	}

	public void setSelect(select select) {
		this.select = select;
	}

	public name getName() {
		return name;
	}

	public void setName(name name) {
		this.name = name;
	}

	public desc getDesc() {
		return desc;
	}

	public void setDesc(desc desc) {
		this.desc = desc;
	}

	public flag getFlag() {
		return flag;
	}

	public void setFlag(flag flag) {
		this.flag = flag;
	}

	public CLASS getCLASS() {
		return CLASS;
	}

	public void setCLASS(CLASS cLASS) {
		CLASS = cLASS;
	}

	public bid getBid() {
		return bid;
	}

	public void setBid(bid bid) {
		this.bid = bid;
	}

	public bm getBm() {
		return bm;
	}

	public void setBm(bm bm) {
		this.bm = bm;
	}

	public articles getArticles() {
		return articles;
	}

	public void setArticles(articles articles) {
		this.articles = articles;
	}

	public users getUsers() {
		return users;
	}

	public void setUsers(users users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "FavBrdBean [select=" + select + ", name=" + name + ", desc="
				+ desc + ", flag=" + flag + ", CLASS=" + CLASS + ", bid=" + bid
				+ ", bm=" + bm + ", articles=" + articles + ", users=" + users
				+ "]";
	}
}
