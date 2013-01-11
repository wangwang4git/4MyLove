package com.bbs.whu.model;

import java.util.ArrayList;
import java.util.List;
 
import com.bbs.whu.model.board.*;

/**
 * 一级版块元素结构定义
 * 
 * @author WWang
 * 
 */
public class BoardBean {
	// 一级版块名称
	private name name;
	// 所包含的二级版块列表
	private List<Board> boards = new ArrayList<Board>();

	public BoardBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BoardBean(name name, List<Board> boards) {
		super();
		this.name = name;
		this.boards = boards;
	}

	public name getName() {
		return name;
	}

	public void setName(name name) {
		this.name = name;
	}

	public List<Board> getBoards() {
		return boards;
	}

	public void setBoards(List<Board> boards) {
		this.boards = boards;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String ret = this.name.toString() + "/";
		int i = 0;
		for (; i < this.boards.size() - 1; ++i) {
			ret += this.boards.get(i).toString() + "/";
		}
		ret += this.boards.get(i).toString();
		return ret;
	}
}
