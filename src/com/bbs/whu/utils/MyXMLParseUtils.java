package com.bbs.whu.utils;

import java.util.List;

import com.bbs.whu.model.BoardBean;
import com.bbs.whu.model.TopTenBean;
import com.bbs.whu.model.TopicBean;
import com.bbs.whu.model.board.Board;
import com.bbs.whu.model.board.idConverter;
import com.bbs.whu.model.board.nameConverter;
import com.bbs.whu.model.bulletin.Page;
import com.bbs.whu.model.bulletin.numConverter;
import com.bbs.whu.model.bulletin.totalConverter;
import com.bbs.whu.model.topic.Topics;
import com.bbs.whu.model.topic.boardConverter;
import com.bbs.whu.model.topic.pageConverter;
import com.bbs.whu.model.topic.totalPagesConverter;
import com.thoughtworks.xstream.XStream;

/**
 * XML序列化与反序列化工具
 * 
 * @author WWang
 * 
 */
public class MyXMLParseUtils {

	/**
	 * 反序列化十大文摘列表
	 * 
	 * @param XMLStream
	 * @return List<TopTenBean>
	 */
	public static List<TopTenBean> readXml2TopTenList(String XMLStream) {
		if (null == XMLStream) {
			return null;
		}
		XMLStream = XMLStream.trim();
		XMLStream = XMLStream.replaceAll("&", "&amp;");
		// 替换<hots>标签为<list>标签
		XMLStream = XMLStream.replace("<hots>", "<list>");
		XMLStream = XMLStream.replace("</hots>", "</list>");
		XStream xstream = new XStream();
		// 类重命名
		xstream.alias("hot", TopTenBean.class);
		// 反序列化
		@SuppressWarnings("unchecked")
		List<TopTenBean> topTenList = (List<TopTenBean>) xstream
				.fromXML(XMLStream);
		return topTenList;
	}

	/**
	 * 反序列化帖子详情列表
	 * 
	 * @param XMLStream
	 * @return Page
	 */
	public static Page readXml2Page(String XMLStream) {
		if (null == XMLStream) {
			return null;
		}
		XMLStream = XMLStream.trim();
		XMLStream = XMLStream.replaceAll("&", "&amp;");
		// 替换<article>标签为<articles><article>标签
		XMLStream = XMLStream.replaceFirst("<article>", "<articles><article>");
		// 替换</page>标签为</articles></page>标签
		XMLStream = XMLStream.replace("</page>", "</articles></page>");
		XStream xstream = new XStream();
		xstream.alias("page", Page.class);
		xstream.alias("article", Page.Article.class);
		xstream.useAttributeFor(Page.class, "num");
		xstream.useAttributeFor(Page.class, "total");
		xstream.registerConverter(new numConverter());
		xstream.registerConverter(new totalConverter());
		return (Page) xstream.fromXML(XMLStream);
	}

	/**
	 * 反序列化版块列表
	 * 
	 * @param XMLStream
	 * @return List<BoardBean>
	 */
	public static List<BoardBean> readXml2BoardList(String XMLStream) {
		if (null == XMLStream) {
			return null;
		}
		XMLStream = MyRegexParseUtils.delTwoLevelBoard(XMLStream);
		XMLStream = XMLStream.trim();
		XMLStream = XMLStream.replaceAll("&", "&amp;");
		// 替换<Sections>标签为<list>标签
		XMLStream = XMLStream.replace("<Sections>", "<list>");
		XMLStream = XMLStream.replace("</Sections>", "</list>");
		// 插入<boards>标签
		XMLStream = XMLStream.replaceAll("(<Section name=.*?>)(<board)",
				"$1<boards>$2");
		// 插入</boards>标签
		XMLStream = XMLStream.replaceAll("(</Section>)", "</boards>$1");
		XStream xstream = new XStream();
		// 类重命名
		xstream.alias("Section", BoardBean.class);
		xstream.alias("board", Board.class);
		xstream.useAttributeFor(BoardBean.class, "name");
		xstream.registerConverter(new nameConverter());
		xstream.useAttributeFor(Board.class, "num");
		xstream.registerConverter(new numConverter());
		xstream.useAttributeFor(Board.class, "name");
		xstream.registerConverter(new nameConverter());
		xstream.useAttributeFor(Board.class, "id");
		xstream.registerConverter(new idConverter());
		// 反序列化
		@SuppressWarnings("unchecked")
		List<BoardBean> boardList = (List<BoardBean>) xstream
				.fromXML(XMLStream);
		return boardList;
	}

	/**
	 * 反序列化版面帖子列表
	 * 
	 * @param XMLStream
	 * @return Topics
	 */
	public static Topics readXml2Topics(String XMLStream) {
		if (null == XMLStream) {
			return null;
		}
		XMLStream = XMLStream.trim();
		XMLStream = XMLStream.replaceAll("&", "&amp;");
		// 插入<topics>标签
		XMLStream = XMLStream.replaceFirst("(<topics[^<>]*?>)(<topic>)",
				"$1<topics>$2");
		// 插入</topics>标签
		XMLStream = XMLStream.replace("</topic></topics>",
				"</topic></topics></topics>");
		XStream xstream = new XStream();
		xstream.alias("topics", Topics.class);
		xstream.alias("topic", TopicBean.class);
		xstream.useAttributeFor(Topics.class, "board");
		xstream.useAttributeFor(Topics.class, "page");
		xstream.useAttributeFor(Topics.class, "totalPages");
		xstream.registerConverter(new boardConverter());
		xstream.registerConverter(new pageConverter());
		xstream.registerConverter(new totalPagesConverter());
		return (Topics) xstream.fromXML(XMLStream);
	}
}