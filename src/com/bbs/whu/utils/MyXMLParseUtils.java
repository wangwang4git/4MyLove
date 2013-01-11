package com.bbs.whu.utils;

import java.util.List;

import com.bbs.whu.model.PlaybillBean;
import com.bbs.whu.model.RecommendBean;
import com.bbs.whu.model.TopTenBean;
import com.bbs.whu.model.UserInfoBean;
import com.bbs.whu.model.bulletin.Page;
import com.bbs.whu.model.bulletin.numConverter;
import com.bbs.whu.model.bulletin.totalConverter;
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
		// 反序列化
		@SuppressWarnings("unchecked")
		List<RecommendBean> recommendList = (List<RecommendBean>) xstream
				.fromXML(XMLStream);
		return recommendList;
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
		// 反序列化
		@SuppressWarnings("unchecked")
		List<PlaybillBean> playbillList = (List<PlaybillBean>) xstream
				.fromXML(XMLStream);
		return playbillList;
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
		return (UserInfoBean) xstream.fromXML(XMLStream);
	}
}