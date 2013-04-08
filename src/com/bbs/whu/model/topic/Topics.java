package com.bbs.whu.model.topic;

import java.util.ArrayList;
import java.util.List;

import com.bbs.whu.model.TopicBean;

public class Topics {
	private board board;
	private page page;
	private totalPages totalPages;
	private List<TopicBean> topics = new ArrayList<TopicBean>();

	public Topics() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Topics(com.bbs.whu.model.topic.board board,
			com.bbs.whu.model.topic.page page,
			com.bbs.whu.model.topic.totalPages totalPages,
			List<TopicBean> topics) {
		super();
		this.board = board;
		this.page = page;
		this.totalPages = totalPages;
		this.topics = topics;
	}

	public board getBoard() {
		return board;
	}

	public void setBoard(board board) {
		this.board = board;
	}

	public page getPage() {
		return page;
	}

	public void setPage(page page) {
		this.page = page;
	}

	public totalPages getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(totalPages totalPages) {
		this.totalPages = totalPages;
	}

	public List<TopicBean> getTopics() {
		return topics;
	}

	public void setTopics(List<TopicBean> topics) {
		this.topics = topics;
	}

	@Override
	public String toString() {
		return "Topics [board=" + board + ", page=" + page + ", totalPages="
				+ totalPages + ", topics=" + topics + "]";
	}
}
