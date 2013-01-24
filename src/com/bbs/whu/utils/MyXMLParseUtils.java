package com.bbs.whu.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import android.content.Context;

import com.bbs.whu.model.BoardBean;
import com.bbs.whu.model.PlaybillBean;
import com.bbs.whu.model.RecommendBean;
import com.bbs.whu.model.TopTenBean;
import com.bbs.whu.model.TopicBean;
import com.bbs.whu.model.UserInfoBean;
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
	// 版块名称、版块id号HaspMap
	private static HashMap<String, String> BoardIdMap;

	public static HashMap<String, String> getBoardIdMap(Context context) {
		if (BoardIdMap != null) {
			return BoardIdMap;
		}

		BoardIdMap = new HashMap<String, String>();
		// 读取assert/boards.xml
		String result = "";
		try {
			InputStream in = context.getResources().getAssets()
					.open("boards.xml");
			// 获取文件的字节数
			int lenght = in.available();
			// 创建byte数组
			byte[] buffer = new byte[lenght];
			// 将文件中的数据读到byte数组中
			in.read(buffer);
			result = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 反序列化
		List<BoardBean> boards = readXml2BoardList(result);
		// 构造HaspMap
		for (int i = 0; i < boards.size(); ++i) {
			for (int j = 0; j < boards.get(i).getBoards().size(); ++j) {
				BoardIdMap.put(boards.get(i).getBoards().get(j).getId()
						.getAttributeValue(), boards.get(i).getBoards().get(j)
						.getNum().getAttributeValue());
			}
		}
		return BoardIdMap;
	}

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
		// 加入异常处理
		try
		{
			// 反序列化
			@SuppressWarnings("unchecked")
			List<TopTenBean> topTenList = (List<TopTenBean>) xstream
					.fromXML(XMLStream);
			return topTenList;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(XMLStream);
			return null;
		}
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
		// 加入异常处理
		try {
			return (Page) xstream.fromXML(XMLStream);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(XMLStream);
			return null;
		}
	}
	
	/**
	 * 反序列化推荐文章列表
	 * 
	 * @param XMLStream
	 * @return List<RecommendBean>
	 */
	public static List<RecommendBean> readXml2RecommendList(String XMLStream) {
		if (null == XMLStream) {
			return null;
		}
		XMLStream = XMLStream.trim();
		XMLStream = XMLStream.replaceAll("&", "&amp;");
		// 替换<recomms>标签为<list>标签
		XMLStream = XMLStream.replace("<recomms>", "<list>");
		XMLStream = XMLStream.replace("</recomms>", "</list>");
		XStream xstream = new XStream();
		// 类重命名
		xstream.alias("recomm", RecommendBean.class);
		// 加入异常处理
		try {
			// 反序列化
			@SuppressWarnings("unchecked")
			List<RecommendBean> recommendList = (List<RecommendBean>) xstream
					.fromXML(XMLStream);
			return recommendList;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(XMLStream);
			return null;
		}
	}
	
	/**
	 * 反序列化校园海报列表
	 * 
	 * @param XMLStream
	 * @return List<PlaybillBean>
	 */
	public static List<PlaybillBean> readXml2PlaybillList(String XMLStream) {
		if (null == XMLStream) {
			return null;
		}
		XMLStream = XMLStream.trim();
		XMLStream = XMLStream.replaceAll("&", "&amp;");
		// 替换<posters>标签为<list>标签
		XMLStream = XMLStream.replace("<posters>", "<list>");
		XMLStream = XMLStream.replace("</posters>", "</list>");
		XStream xstream = new XStream();
		// 类重命名
		xstream.alias("poster", PlaybillBean.class);
		// 加入异常处理
		try {
			// 反序列化
			@SuppressWarnings("unchecked")
			List<PlaybillBean> playbillList = (List<PlaybillBean>) xstream
					.fromXML(XMLStream);
			return playbillList;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(XMLStream);
			return null;
		}
	}
	
	/**
	 * 反序列化个人信息
	 * 
	 * @param XMLStream
	 * @return UserInfoBean
	 */
	public static UserInfoBean readXml2UserInfo(String XMLStream) {
		if (null == XMLStream) {
			return null;
		}
		XMLStream = XMLStream.trim();
		XMLStream = XMLStream.replaceAll("&", "&amp;");
		XStream xstream = new XStream();
		xstream.alias("user", UserInfoBean.class);
		// 加入异常处理
		try {
			return (UserInfoBean) xstream.fromXML(XMLStream);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(XMLStream);
			return null;
		}
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
		// 加入异常处理
		try {
			// 反序列化
			@SuppressWarnings("unchecked")
			List<BoardBean> boardList = (List<BoardBean>) xstream
					.fromXML(XMLStream);
			return boardList;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(XMLStream);
			return null;
		}
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
		// 加入异常处理
		try {
			// 反序列化
			return (Topics) xstream.fromXML(XMLStream);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(XMLStream);
			return null;
		}
	}
	
}