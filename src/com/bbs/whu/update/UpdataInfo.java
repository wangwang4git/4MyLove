package com.bbs.whu.update;

/**
 * �汾����ģ��汾��Ϣ�ṹ����
 * 
 * @author WWang
 * 
 */

public class UpdataInfo {
	// �汾��
	private int version;
	// ����URL
	private String url;
	// �汾�������
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