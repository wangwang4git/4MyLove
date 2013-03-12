package com.bbs.whu.model;

/**
 * 用户信息元素结构定义
 * 
 * @author WWang
 * 
 */
public class UserInfoBean {
	// 基本资料
	// 用户头像url，绝对路径需要添加前缀http://bbs.whu.edu.cn/
	private String userface_img;
	// 用户照片url，绝对路径需要添加前缀http://bbs.whu.edu.cn/
	private String photo_url;
	// 用户昵称
	private String nickname;
	// 用户性别
	private String sex;
	// 用户QQ号码
	private String OICQ;
	// 用户ICQ号码
	private String ICQ;
	// 用户MSN号码
	private String MSN;
	// 用户主页
	private String homepage;
	
	// 论坛属性
	// 用户论坛职务
	private String userlevel;
	// 用户帖子总数
	private String numposts;
	// 用户门 派
	private String menpai;
	// 用户登录次数
	private String numlogins;
	// 用户经验值
	private String userexp;
	// 用户经验等级
	private String explevel;
	// 用户生命力
	private String uservalue;
	// 用户上次登录时间
	private String lastlogin;
	// 用户当前状态，是否在线
	private String usermode;
	// 用户最后来访IP
	private String lasthostIP;
	// 签名档
	private String sigcontent;

	public UserInfoBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserInfoBean(String userface_img, String photo_url, String nickname,
			String sex, String oICQ, String iCQ, String mSN, String homepage,
			String userlevel, String numposts, String menpai, String numlogins,
			String userexp, String explevel, String uservalue,
			String lastlogin, String usermode, String lasthostIP,
			String sigcontent) {
		super();
		this.userface_img = userface_img;
		this.photo_url = photo_url;
		this.nickname = nickname;
		this.sex = sex;
		OICQ = oICQ;
		ICQ = iCQ;
		MSN = mSN;
		this.homepage = homepage;
		this.userlevel = userlevel;
		this.numposts = numposts;
		this.menpai = menpai;
		this.numlogins = numlogins;
		this.userexp = userexp;
		this.explevel = explevel;
		this.uservalue = uservalue;
		this.lastlogin = lastlogin;
		this.usermode = usermode;
		this.lasthostIP = lasthostIP;
		this.setSigcontent(sigcontent);
	}

	public String getUserface_img() {
		return userface_img;
	}

	public void setUserface_img(String userface_img) {
		this.userface_img = userface_img;
	}

	public String getPhoto_url() {
		return photo_url;
	}

	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getOICQ() {
		return OICQ;
	}

	public void setOICQ(String oICQ) {
		OICQ = oICQ;
	}

	public String getICQ() {
		return ICQ;
	}

	public void setICQ(String iCQ) {
		ICQ = iCQ;
	}

	public String getMSN() {
		return MSN;
	}

	public void setMSN(String mSN) {
		MSN = mSN;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getUserlevel() {
		return userlevel;
	}

	public void setUserlevel(String userlevel) {
		this.userlevel = userlevel;
	}

	public String getNumposts() {
		return numposts;
	}

	public void setNumposts(String numposts) {
		this.numposts = numposts;
	}

	public String getMenpai() {
		return menpai;
	}

	public void setMenpai(String menpai) {
		this.menpai = menpai;
	}

	public String getNumlogins() {
		return numlogins;
	}

	public void setNumlogins(String numlogins) {
		this.numlogins = numlogins;
	}

	public String getUserexp() {
		return userexp;
	}

	public void setUserexp(String userexp) {
		this.userexp = userexp;
	}

	public String getExplevel() {
		return explevel;
	}

	public void setExplevel(String explevel) {
		this.explevel = explevel;
	}

	public String getUservalue() {
		return uservalue;
	}

	public void setUservalue(String uservalue) {
		this.uservalue = uservalue;
	}

	public String getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(String lastlogin) {
		this.lastlogin = lastlogin;
	}

	public String getUsermode() {
		return usermode;
	}

	public void setUsermode(String usermode) {
		this.usermode = usermode;
	}

	public String getLasthostIP() {
		return lasthostIP;
	}

	public void setLasthostIP(String lasthostIP) {
		this.lasthostIP = lasthostIP;
	}

	public void setSigcontent(String sigcontent) {
		this.sigcontent = sigcontent;
	}

	public String getSigcontent() {
		return sigcontent;
	}
	
	@Override
	public String toString() {
		return "UserInfoBean [userface_img=" + userface_img + ", photo_url="
				+ photo_url + ", nickname=" + nickname + ", sex=" + sex
				+ ", OICQ=" + OICQ + ", ICQ=" + ICQ + ", MSN=" + MSN
				+ ", homepage=" + homepage + ", userlevel=" + userlevel
				+ ", numposts=" + numposts + ", menpai=" + menpai
				+ ", numlogins=" + numlogins + ", userexp=" + userexp
				+ ", explevel=" + explevel + ", uservalue=" + uservalue
				+ ", lastlogin=" + lastlogin + ", usermode=" + usermode
				+ ", lasthostIP=" + lasthostIP + ", sigcontent=" + sigcontent
				+ "]";
	}
}
