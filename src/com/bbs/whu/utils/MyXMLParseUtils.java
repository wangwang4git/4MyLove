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
 * XML���л��뷴���л�����
 * 
 * @author WWang
 * 
 */
public class MyXMLParseUtils {
	// ������ơ����id��HaspMap
	private static HashMap<String, String> BoardIdMap;

	public static HashMap<String, String> getBoardIdMap(Context context) {
		if (BoardIdMap != null) {
			return BoardIdMap;
		}

		BoardIdMap = new HashMap<String, String>();
		// ��ȡassert/boards.xml
		String result = "";
		try {
			InputStream in = context.getResources().getAssets()
					.open("boards.xml");
			// ��ȡ�ļ����ֽ���
			int lenght = in.available();
			// ����byte����
			byte[] buffer = new byte[lenght];
			// ���ļ��е����ݶ���byte������
			in.read(buffer);
			result = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// �����л�
		List<BoardBean> boards = readXml2BoardList(result);
		// ����HaspMap
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
	 * �����л�ʮ����ժ�б�
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
		// �滻<hots>��ǩΪ<list>��ǩ
		XMLStream = XMLStream.replace("<hots>", "<list>");
		XMLStream = XMLStream.replace("</hots>", "</list>");
		XStream xstream = new XStream();
		// ��������
		xstream.alias("hot", TopTenBean.class);
		// �����쳣����
		try
		{
			// �����л�
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
	 * �����л����������б�
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
		// �滻<article>��ǩΪ<articles><article>��ǩ
		XMLStream = XMLStream.replaceFirst("<article>", "<articles><article>");
		// �滻</page>��ǩΪ</articles></page>��ǩ
		XMLStream = XMLStream.replace("</page>", "</articles></page>");
		XStream xstream = new XStream();
		xstream.alias("page", Page.class);
		xstream.alias("article", Page.Article.class);
		xstream.useAttributeFor(Page.class, "num");
		xstream.useAttributeFor(Page.class, "total");
		xstream.registerConverter(new numConverter());
		xstream.registerConverter(new totalConverter());
		// �����쳣����
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
	 * �����л��Ƽ������б�
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
		// �滻<recomms>��ǩΪ<list>��ǩ
		XMLStream = XMLStream.replace("<recomms>", "<list>");
		XMLStream = XMLStream.replace("</recomms>", "</list>");
		XStream xstream = new XStream();
		// ��������
		xstream.alias("recomm", RecommendBean.class);
		// �����쳣����
		try {
			// �����л�
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
	 * �����л�У԰�����б�
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
		// �滻<posters>��ǩΪ<list>��ǩ
		XMLStream = XMLStream.replace("<posters>", "<list>");
		XMLStream = XMLStream.replace("</posters>", "</list>");
		XStream xstream = new XStream();
		// ��������
		xstream.alias("poster", PlaybillBean.class);
		// �����쳣����
		try {
			// �����л�
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
	 * �����л�������Ϣ
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
		// �����쳣����
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
	 * �����л�����б�
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
		// �滻<Sections>��ǩΪ<list>��ǩ
		XMLStream = XMLStream.replace("<Sections>", "<list>");
		XMLStream = XMLStream.replace("</Sections>", "</list>");
		// ����<boards>��ǩ
		XMLStream = XMLStream.replaceAll("(<Section name=.*?>)(<board)",
				"$1<boards>$2");
		// ����</boards>��ǩ
		XMLStream = XMLStream.replaceAll("(</Section>)", "</boards>$1");
		XStream xstream = new XStream();
		// ��������
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
		// �����쳣����
		try {
			// �����л�
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
	 * �����л����������б�
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
		// ����<topics>��ǩ
		XMLStream = XMLStream.replaceFirst("(<topics[^<>]*?>)(<topic>)",
				"$1<topics>$2");
		// ����</topics>��ǩ
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
		// �����쳣����
		try {
			// �����л�
			return (Topics) xstream.fromXML(XMLStream);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(XMLStream);
			return null;
		}
	}
	
}