package com.bbs.whu.update;

/**
 * 版本更新模块版本信息结构定义
 * 
 * @author WWang
 * 
 */

public class UpdataInfo {
	// 版本号
	private int version;
	// 下载URL
	private String url;
	// 版本变更描述
	private String description;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}