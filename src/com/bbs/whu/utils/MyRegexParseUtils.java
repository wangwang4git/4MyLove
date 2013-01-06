package com.bbs.whu.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bbs.whu.model.BulletinBean;
import com.bbs.whu.model.bulletin.Page;
import com.bbs.whu.model.bulletin.Page.Article;

/**
 * 帖子内容扣取正则表达式
 * 为与web端获取的数据统一，本文件采用“utf-8”编码
 * 
 * @author wwang
 * 
 */
public class MyRegexParseUtils {
	// 获取发信人名称
	final static private String AUTHOR_REGEX_STRING = "(?<=prints\\('发信人: ).*?(?= \\()";
	// 获取信区（板块）信息
	final static private String BOARD_REGEX_STRING = "(?<=\\), 信区: ).*?(?=\\\\n)";
	// 获取标题信息
	final static private String TITLE_REGEX_STRING = "(?<=\\\\n标  题: ).*?(?=\\\\n)";
	// 获取发站信信息
	final static private String SITE_REGEX_STRING = "(?<=\\\\n发信站: ).*?(?=\\\\n\\\\n)";
	// 获取发帖（回复）时间信息
	final static private String TIME_REGEX_STRING = "(?<=\\\\n发信站: 珞珈山水 \\().*?(?=\\), .*\\\\n\\\\n)";
	// 获取帖子头部信息
	final static private String HEAD_REGEX_STRING = "(?<=prints\\(').*?(?=\\\\n\\\\n)";
	// 获取帖子主体信息
	final static private String BODY_REGEX_STRING = "(?<=\\\\n\\\\n).*(?=\\\\n--\\\\n)|(?<=\\\\n\\\\n).*?(?=\\\\n\\\\n\\\\r\\[)";
	// 获取帖子回复部分
	final static private String REPLY_REGEX_STRING = "(?<=\\\\n)【 .*?(?=\\\\n)|【 .*?(?=\\\\n)|\\\\n:.*?(?=\\\\n)";
	// 获取帖子正文部分
	// final static private String TEXT_REGEX_STRING = "主体信息-回复部分，同时去掉首位\n";
	// 获取签名档信息
	final static private String SIGN_REGEX_STRING = "(?<=\\\\n)--.*?(?=\\\\n\\\\n\\\\r)";
	// 获取来源信息
	final static private String FROM_REGEX_STRING = "(※ 来源:).*?(?=\\\\r\\[m\\\\n)";

	// 获取发信人名称
	static public String getAuthor(String content) {
		Pattern pattern = Pattern.compile(AUTHOR_REGEX_STRING);
		Matcher matcher = pattern.matcher(content);
		// 输出第一个匹配项
		while (matcher.find()) {
			return matcher.group();
		}
		return "null";
	}

	// 获取信区（板块）信息
	static public String getBoard(String content) {
		Pattern pattern = Pattern.compile(BOARD_REGEX_STRING);
		Matcher matcher = pattern.matcher(content);
		// 输出第一个匹配项
		while (matcher.find()) {
			return matcher.group();
		}
		return "null";
	}

	// 获取标题信息
	static public String getTitle(String content) {
		Pattern pattern = Pattern.compile(TITLE_REGEX_STRING);
		Matcher matcher = pattern.matcher(content);
		// 输出第一个匹配项
		while (matcher.find()) {
			return matcher.group();
		}
		return "null";
	}

	// 获取发站信信息
	static public String getSite(String content) {
		Pattern pattern = Pattern.compile(SITE_REGEX_STRING);
		Matcher matcher = pattern.matcher(content);
		// 输出第一个匹配项
		while (matcher.find()) {
			return matcher.group();
		}
		return "null";
	}

	// 获取发帖（回复）时间信息
	static public String getTime(String content) {
		Pattern pattern = Pattern.compile(TIME_REGEX_STRING);
		Matcher matcher = pattern.matcher(content);
		// 输出第一个匹配项
		while (matcher.find()) {
			return matcher.group();
		}
		return "null";
	}

	// 获取帖子头部信息
	static public String getHead(String content) {
		Pattern pattern = Pattern.compile(HEAD_REGEX_STRING);
		Matcher matcher = pattern.matcher(content);
		// 输出第一个匹配项
		while (matcher.find()) {
			return matcher.group().replaceAll("\\\\n", "\n");
		}
		return "null";
	}

	// 获取帖子主体信息
	static public String getBody(String content) {
		return getBody(content, false).replaceAll("\\\\n", "\n").replaceAll("\\\\/", "/");
	}

	static public String getBody(String content, boolean flag) {
		Pattern pattern = Pattern.compile(BODY_REGEX_STRING);
		Matcher matcher = pattern.matcher(content);
		// 输出第一个匹配项
		while (matcher.find()) {
			return trimFirstAndLastNewline(matcher.group());
		}
		return "null";
	}

	// 获取帖子回复部分
	static public String getReply(String content) {
		return getReply(content, false).replaceAll("\\\\n", "\n").replaceAll("\\\\/", "/");
	}

	static public String getReply(String content, boolean flag) {
		Pattern pattern = Pattern.compile(REPLY_REGEX_STRING);
		Matcher matcher = pattern.matcher(content);
		String ret = new String();
		Boolean bool = matcher.find();
		if (!bool) {
			return "null";
		}
		while (bool) {
			ret += matcher.group();
			bool = matcher.find();
		}
		return trimFirstAndLastNewline(ret);
	}

	// 获取帖子正文部分
	static public String getText(String content) {
		String body = getBody(content, false);
		String reply = getReply(content, false);
		if (body.equals("null")) {
			return "null";
		}
		if (reply.equals("null")) {
			return body.replaceAll("\\\\n", "\n").replaceAll("\\\\/", "/");
		}
		return trimFirstAndLastNewline(body.replace(reply, "")).replaceAll(
				"\\\\n", "\n").replaceAll("\\\\/", "/");
	}

	// 获取签名档信息
	static public String getSign(String content) {
		Pattern pattern = Pattern.compile(SIGN_REGEX_STRING);
		Matcher matcher = pattern.matcher(content);
		// 输出第一个匹配项
		while (matcher.find()) {
			return trimFirstAndLastNewline(matcher.group()).replaceAll("\\\\n",
					"\n").replaceAll("\\\\/", "/");
		}
		return "null";
	}

	// 获取来源信息
	static public String getFrom(String content) {
		Pattern pattern = Pattern.compile(FROM_REGEX_STRING);
		Matcher matcher = pattern.matcher(content);
		// 输出第一个匹配项
		while (matcher.find()) {
			return matcher.group().replaceAll("\\\\/", "/");
		}
		return "null";
	}

	// 去除字符串首尾出现的某个字符
	public static String trimFirstAndLastNewline(String source) {
		source = source.replaceAll("^(\\\\n)*", "");
		source = source.replaceAll("(\\\\n)*$", "");
		return source;
	}

	// 返回帖子详情列表
	public static List<BulletinBean> getContentList(Page page) {
		List<BulletinBean> contents = new ArrayList<BulletinBean>();
		List<Article> articles = page.getArticles();
		for (int i = 0; i < articles.size(); ++i) {
			BulletinBean content = new BulletinBean();
			String temp = articles.get(i).getContent();
			content.setFloor(articles.get(i).getFloor());
			content.setContent(temp);
			content.setId(articles.get(i).getId());
			content.setAuthor(getAuthor(temp));
			content.setBoard(getBoard(temp));
			content.setTitle(getTitle(temp));
			content.setSite(getSite(temp));
			content.setTime(getTime(temp));
			content.setHead(getHead(temp));
			content.setBody(getBody(temp));
			content.setReply(getReply(temp));
			content.setText(getText(temp));
			content.setSign(getSign(temp));
			content.setFrom(getFrom(temp));
			contents.add(content);
		}
		return contents;
	}
}
