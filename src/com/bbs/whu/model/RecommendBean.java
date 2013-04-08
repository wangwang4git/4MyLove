package com.bbs.whu.model;

/**
 * �Ƽ������б�Ԫ�ؽṹ����
 * 
 * @author WWang
 * 
 */
public class RecommendBean {
	// �Ƽ����±���
	private String title;
	// �Ƽ���
	private String recommby;
	// �Ƽ�ʱ��
	private String recommTime;
	// ���������Ƽ�����Ӣ����
	private String board;
	// ���������Ƽ����ı��
	private String recommGID;
	// ԭ������
	private String Author;
	// ԭ����������Ӣ������
	private String originBoard;
	// ԭ������������������
	private String originBoardName;
	// ԭ�����ڰ��ı��
	private String originArticleID;
	// ԭ��ȫ�ֱ��
	private String originGID;
	// �Ƽ�����ժҪ
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
