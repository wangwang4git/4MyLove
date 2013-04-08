package com.bbs.whu.model.bulletin;

import java.util.ArrayList;
import java.util.List;

import com.bbs.whu.model.attr.num;

public class Page {
	private num num;
	private total total;
	private List<Article> articles = new ArrayList<Article>();

	public Page() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Page(num num, total total, List<Article> articles) {
		super();
		this.num = num;
		this.total = total;
		this.articles = articles;
	}

	public num getNum() {
		return num;
	}

	public void setNum(num num) {
		this.num = num;
	}

	public total getTotal() {
		return total;
	}

	public void setTotal(total total) {
		this.total = total;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String ret = null;
		int i = 0;
		for (; i < this.articles.size() - 1; ++i) {
			ret += this.articles.get(i).toString() + "/";
		}
		ret += this.articles.get(i).toString();
		return ret;
	}

	public static class Article {
		// 楼层
		private String floor;
		// 文章id（每层楼都是一篇文章，每篇文章都有一个id）
		private String id;
		// 作者
		private String author;
		// 头像url
		private String userface_img;
		// 内容
		private String content;

		public Article() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Article(String floor, String id, String author,
				String userface_img, String content) {
			super();
			this.floor = floor;
			this.id = id;
			this.author = author;
			this.userface_img = userface_img;
			this.content = content;
		}

		public String getFloor() {
			return floor;
		}

		public void setFloor(String floor) {
			this.floor = floor;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getUserface_img() {
			return userface_img;
		}

		public void setUserface_img(String userface_img) {
			this.userface_img = userface_img;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		@Override
		public String toString() {
			return "Article [floor=" + floor + ", id=" + id + ", author="
					+ author + ", userface_img=" + userface_img + ", content="
					+ content + "]";
		}
	}
}
