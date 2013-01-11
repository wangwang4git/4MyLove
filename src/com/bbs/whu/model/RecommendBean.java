package com.bbs.whu.model;

/**
 * 推荐文章列表元素结构定义
 * 
 * @author WWang
 * 
 */
public class RecommendBean {
	// 推荐文章标题
	private String title;
	// 推荐人
	private String recommby;
	// 推荐时间
	private String recommTime;
	// 文章所在推荐版块的英文名
	private String board;
	// 文章所在推荐版块的编号
	private String recommGID;
	// 原文作者
	private String Author;
	// 原文所属版面英文名称
	private String originBoard;
	// 原文所属版面中文名称
	private String originBoardName;
	// 原文所在版块的编号
	private String originArticleID;
	// 原文全局编号
	private String originGID;
	// 推荐文章摘要
	private String brief;

	public RecommendBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RecommendBean(String title, String recommby, String recommTime,
			String board, String recommGID, String author, String originBoard,
			String originBoardName, String originArticleID, String originGID,
			String brief) {
		super();
		this.title = title;
		this.recommby = recommby;
		this.recommTime = recommTime;
		this.board = board;
		this.recommGID = recommGID;
		Author = author;
		this.originBoard = originBoard;
		this.originBoardName = originBoardName;
		this.originArticleID = originArticleID;
		this.originGID = originGID;
		this.brief = brief;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRecommby() {
		return recommby;
	}

	public void setRecommby(String recommby) {
		this.recommby = recommby;
	}

	public String getRecommTime() {
		return recommTime;
	}

	public void setRecommTime(String recommTime) {
		this.recommTime = recommTime;
	}

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public String getRecommGID() {
		return recommGID;
	}

	public void setRecommGID(String recommGID) {
		this.recommGID = recommGID;
	}

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public String getOriginBoard() {
		return originBoard;
	}

	public void setOriginBoard(String originBoard) {
		this.originBoard = originBoard;
	}

	public String getOriginBoardName() {
		return originBoardName;
	}

	public void setOriginBoardName(String originBoardName) {
		this.originBoardName = originBoardName;
	}

	public String getOriginArticleID() {
		return originArticleID;
	}

	public void setOriginArticleID(String originArticleID) {
		this.originArticleID = originArticleID;
	}

	public String getOriginGID() {
		return originGID;
	}

	public void setOriginGID(String originGID) {
		this.originGID = originGID;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	@Override
	public String toString() {
		return "RecommendBean [title=" + title + ", recommby=" + recommby
				+ ", recommTime=" + recommTime + ", board=" + board
				+ ", recommGID=" + recommGID + ", Author=" + Author
				+ ", originBoard=" + originBoard + ", originBoardName="
				+ originBoardName + ", originArticleID=" + originArticleID
				+ ", originGID=" + originGID + ", brief=" + brief + "]";
	}

}
