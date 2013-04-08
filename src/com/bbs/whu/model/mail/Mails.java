package com.bbs.whu.model.mail;

import java.util.ArrayList;
import java.util.List;

import com.bbs.whu.model.MailBean;

public class Mails {
	private Page Page;
	private TotalPage TotalPage;
	private List<MailBean> mails = new ArrayList<MailBean>();

	public Mails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Mails(Page Page, TotalPage TotalPage, List<MailBean> mails) {
		super();
		this.Page = Page;
		this.TotalPage = TotalPage;
		this.mails = mails;
	}

	public Page getPage() {
		return Page;
	}

	public void setPage(Page Page) {
		this.Page = Page;
	}

	public TotalPage getTotalPage() {
		return TotalPage;
	}

	public void setTotalPage(TotalPage TotalPage) {
		this.TotalPage = TotalPage;
	}

	public List<MailBean> getMails() {
		return mails;
	}

	public void setMails(List<MailBean> mails) {
		this.mails = mails;
	}

	@Override
	public String toString() {
		return "Mails [Page=" + Page + ", TotalPage=" + TotalPage + ", mails="
				+ mails + "]";
	}
}
