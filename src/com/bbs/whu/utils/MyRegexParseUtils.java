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
		return getBody(content, false).replaceAll("\\\\n", "\n").replaceAll(
				"\\\\/", "/");
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
		return getReply(content, false).replaceAll("\\\\n", "\n").replaceAll(
				"\\\\/", "/");
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

	// 获取二级版块信息
	final static private String TWO_LEVEL_BOARD_REGEX_STRING = "<board[^<>]*?hasChildren=true >(.*?)</board>";

	// 去除二级目录信息
	static public String delTwoLevelBoard(String content) {
		return content.replaceAll(TWO_LEVEL_BOARD_REGEX_STRING, "$1");
	}
	
	// 在文本中提取表情的正则表达式
	final static private String EXPRESSION_REGEX_STRING = "\\[(em[0,1,2,3,4,5,6][0,1,2,3,4,5,6,7,8,9])\\]";
	// 在文本中提取URL的正则表达式，出自《精通正则表达式》
	final static private String URL_REGEX_STRING = "([http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.]]*)";
	// 在文本中提取字体颜色的正则表达式
	final static private String COLOR_REGEX_STRING = "(\\[color=(#[0-9A-Z]{6})\\])([\\s\\S]*?)(\\[/color\\])";
	// 在文本中提取字体大小的正则表达式
	final static private String SIZE_REGEX_STRING = "(\\[size=(\\d)\\])([\\s\\S]*?)(\\[/size\\])";
	// 在文本中提取字体类型的正则表达式
	final static private String FACE__REGEX_STRING = "(\\[face=(.*?)\\])([\\s\\S]*?)(\\[/face\\])";
	// 在文本中提取粗体字的正则表达式
	final static private String BOLD_REGEX_STRING = "\\[B\\]([\\s\\S]*?)\\[/B\\]";
	// 在文本中提取斜体字的正则表达式
	final static private String ITALIC_REGEX_STRING = "\\[I\\]([\\s\\S]*?)\\[/I\\]";
	// 在文本中提取下划线的正则表达式
	final static private String UNDERLINE_REGEX_STRING = "\\[U\\]([\\s\\S]*?)\\[/U\\]";

	// TextView富文本（表情、URL等）显示替换操作
	// 替换规则，参见http://blog.csdn.net/a_mean/article/details/6930968
	static public String getRichTextString(String source) {
		source = source.replaceAll(EXPRESSION_REGEX_STRING, "<img src='$1' />");
		source = source.replaceAll(URL_REGEX_STRING,
				"<img src='url' /><a href='$1'>$1</a>");
		source = source.replaceAll(COLOR_REGEX_STRING,
				"<font color=\"$2\">$3</font>");
		source = source.replaceAll(SIZE_REGEX_STRING,
				"<big><font size=\"$2\">$3</font></big>");
		source = source.replaceAll(FACE__REGEX_STRING,
				"<font face=\"$2\">$3</font>");
		source = source.replaceAll(BOLD_REGEX_STRING, "<big><b>$1</b></big>");
		source = source.replaceAll(ITALIC_REGEX_STRING, "<i>$1</i>");
		source = source.replaceAll(UNDERLINE_REGEX_STRING, "<u>$1</u>");
		source = source.replaceAll("\n", "<br>");
		return source;
	}
}
