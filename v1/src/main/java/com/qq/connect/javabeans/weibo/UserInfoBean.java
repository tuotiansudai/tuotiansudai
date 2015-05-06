package com.qq.connect.javabeans.weibo;

import com.qq.connect.QQConnectException;

import com.qq.connect.QQConnectResponse;

import com.qq.connect.javabeans.Avatar;

import com.qq.connect.javabeans.Birthday;

import org.json.JSONArray;

import org.json.JSONException;

import org.json.JSONObject;

import java.io.Serializable;

import java.util.ArrayList;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

public class UserInfoBean extends QQConnectResponse implements Serializable {
	private static final long serialVersionUID = 4742904673643859727L;
	private int ret = 0;

	private int errcode = 0;
	private String msg = "";
	private Birthday birthday = null;
	private String cityCode = "";
	private String countryCode = "";
	private String email = "";

	private ArrayList<Company> companys = new ArrayList();
	private ArrayList<Education> educations = new ArrayList();

	private int fansNum = 0;
	private int favNum = 0;
	private Avatar avatar = null;
	private String homeCountryCode = "";
	private String homeProvinceCode = "";
	private String homeCityCode = "";
	private String homeTownCode = "";
	private String homePage = "";
	private int idolnum = 0;
	private String industryCode = "";
	private String introduction = "";
	private boolean ent = false;
	private boolean myBlack = false;

	private boolean myFans = false;
	private boolean myIdol = false;
	private boolean realName = false;
	private boolean vip = false;
	private String location = "";
	private int mutualFansNum = 0;
	private String name = "";
	private String nickName = "";
	private String provinceCode = "";
	private String regTime = "";
	private PrivateFlag privateFlag = null;
	private String sex = "";
	private ArrayList<Tag> tags = new ArrayList();
	private TweetInfo tweetInfo = new TweetInfo();
	private int tweetNum = 0;
	private String verifyInfo = "";
	private int exp = 0;
	private int level = 0;
	private String seqid = "";

	public int getErrcode() {
		return this.errcode;
	}

	public Birthday getBirthday() {
		return this.birthday;
	}

	public String getCityCode() {
		return this.cityCode;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public ArrayList<Company> getCompanies() {
		return this.companys;
	}

	public ArrayList<Education> getEducations() {
		return this.educations;
	}

	public int getFansNum() {
		return this.fansNum;
	}

	public int getFavNum() {
		return this.favNum;
	}

	public Avatar getAvatar() {
		return this.avatar;
	}

	public String getHomeCountryCode() {
		return this.homeCountryCode;
	}

	public String getHomeProvinceCode() {
		return this.homeProvinceCode;
	}

	public String getHomeCityCode() {
		return this.homeCityCode;
	}

	public String getHomeTownCode() {
		return this.homeTownCode;
	}

	public String getHomePage() {
		return this.homePage;
	}

	public int getIdolNum() {
		return this.idolnum;
	}

	public String getIndustryCode() {
		return this.industryCode;
	}

	public String getIntroduction() {
		return this.introduction;
	}

	public boolean isEnt() {
		return this.ent;
	}

	public boolean isMyFans() {
		return this.myFans;
	}

	public boolean isMyIdol() {
		return this.myIdol;
	}

	public boolean isRealName() {
		return this.realName;
	}

	public boolean isVip() {
		return this.vip;
	}

	public String getLocation() {
		return this.location;
	}

	public int getMutualFansNum() {
		return this.mutualFansNum;
	}

	public String getName() {
		return this.name;
	}

	public String getNickName() {
		return this.nickName;
	}

	public String getProvinceCode() {
		return this.provinceCode;
	}

	public String getRegTime() {
		return this.regTime;
	}

	public PrivateFlag getPrivateFlag() {
		return this.privateFlag;
	}

	public String getSex() {
		return this.sex;
	}

	public ArrayList<Tag> getTags() {
		return this.tags;
	}

	public TweetInfo getTweetInfo() {
		return this.tweetInfo;
	}

	public int getTweetNum() {
		return this.tweetNum;
	}

	public String getVerifyInfo() {
		return this.verifyInfo;
	}

	public int getExp() {
		return this.exp;
	}

	public int getLevel() {
		return this.level;
	}

	public String getSeqid() {
		return this.seqid;
	}

	public String getEmail() {
		return this.email;
	}

	public boolean isMyBlack() {
		return this.myBlack;
	}

	public int getRet() {
		return this.ret;
	}

	public String getMsg() {
		return this.msg;
	}

	public UserInfoBean(JSONObject json) throws QQConnectException {
		init(json);
	}

	private void init(JSONObject json) throws QQConnectException {
		if (json != null)
			try {
				this.ret = json.getInt("ret");
				if (0 != this.ret) {
					this.msg = json.getString("msg");
					this.errcode = json.getInt("errorcode");
				} else {
					this.msg = json.getString("msg");
					this.errcode = 0;
					this.seqid = json.getString("seqid");
					String dataString = null;
					try {
						dataString = json.getString("data");
						if ((dataString != null) && (!dataString.equals(""))
								&& (!dataString.equals("null"))) {
							throw new QQConnectException(
									"user name is not exist");
						}
					} catch (Exception ex) {
						JSONObject data = json.getJSONObject("data");
						this.birthday = new Birthday(data.getInt("birth_year"),
								data.getInt("birth_month"),
								data.getInt("birth_day"));

						this.cityCode = data.getString("city_code");
						this.countryCode = data.getString("country_code");
						this.email = data.getString("email");
						this.exp = data.getInt("exp");
						this.fansNum = data.getInt("fansnum");
						this.favNum = data.getInt("favnum");
						this.avatar = new Avatar(data.getString("head"));
						this.homeCityCode = data.getString("homecity_code");
						this.homeCountryCode = data
								.getString("homecountry_code");
						this.homePage = data.getString("homepage");
						this.homeProvinceCode = data
								.getString("homeprovince_code");
						this.homeTownCode = data.getString("hometown_code");
						this.idolnum = data.getInt("idolnum");
						this.industryCode = data.getString("industry_code");
						this.introduction = data.getString("introduction");
						this.ent = (data.getInt("isent") == 1);
						this.myBlack = (data.getInt("ismyblack") == 1);
						this.myFans = (data.getInt("ismyfans") == 1);
						this.myIdol = (data.getInt("ismyidol") == 1);
						this.realName = (data.getInt("isrealname") == 1);
						this.vip = (data.getInt("vip") == 1);
						this.location = data.getString("location");
						this.mutualFansNum = data.getInt("mutual_fans_num");
						this.name = data.getString("name");
						this.nickName = data.getString("nick");
						this.provinceCode = data.getString("province_code");
						this.regTime = data.getString("regtime");
						this.privateFlag = new PrivateFlag(
								data.getInt("send_private_flag"));

						switch (data.getInt("sex")) {
						case 0:
							this.sex = "未填写";
							break;
						case 1:
							this.sex = "男";
							break;
						case 2:
							this.sex = "女";
						}

						String tweetInfoString = null;
						JSONArray tweetInfoJSONArray = null;
						JSONObject tjo = null;
						try {
							tweetInfoString = data.getString("tweetinfo");
							if ((tweetInfoString == null)
									|| (tweetInfoString.equals(""))
									|| (tweetInfoString.equals("null"))) {
								this.tweetInfo = new TweetInfo();
							} else
								throw new QQConnectException(
										"tweetinfo is lost");
						} catch (Exception e) {
							tweetInfoJSONArray = data.getJSONArray("tweetinfo");
							tjo = tweetInfoJSONArray.getJSONObject(0);

							JSONObject mjo = null;
							String mjoString = null;
							String musicAuthorString = "";
							String musicUrlString = "";
							String musicTitleString = "";
							try {
								mjoString = tjo.getString("music");
							} catch (Exception e1) {
								mjo = tjo.getJSONObject("music");
								musicAuthorString = mjo.getString("author");
								musicUrlString = mjo.getString("url");
								musicTitleString = mjo.getString("title");
							}
							if ((mjoString == null)
									|| (mjoString.equals("null"))) {
								musicAuthorString = "";
								musicUrlString = "";
								musicTitleString = "";
							} else {
								try {
									mjo = tjo.getJSONObject("music");
									musicAuthorString = mjo.getString("author");
									musicUrlString = mjo.getString("url");
									musicTitleString = mjo.getString("title");
								} catch (Exception e2) {
								}
							}

							String videoString = null;
							JSONObject vjo = null;
							String picurl = "";
							String player = "";
							String realurl = "";
							String shorturl = "";
							String title = "";
							try {
								videoString = tjo.getString("video");
							} catch (Exception e3) {
								vjo = tjo.getJSONObject("video");
								picurl = vjo.getString("picurl");
								player = vjo.getString("player");
								realurl = vjo.getString("realurl");
								shorturl = vjo.getString("shorturl");
								title = vjo.getString("title");
							}
							if ((null != videoString)
									&& (!videoString.equals("null"))) {
								vjo = tjo.getJSONObject("video");
								picurl = vjo.getString("picurl");
								player = vjo.getString("player");
								realurl = vjo.getString("realurl");
								shorturl = vjo.getString("shorturl");
								title = vjo.getString("title");
							}

							String imageString = null;
							String imageURL = "";
							try {
								imageString = tjo.getString("image");
								if ((imageString != null)
										&& (!imageString.equals("null"))) {
									Matcher m = Pattern.compile(
											"\\[\"(\\S+)\"\\]").matcher(
											imageString);

									if (m.find())
										imageURL = m.group(1);
									else
										imageURL = "";
								}
							} catch (Exception e4) {
							}
							this.tweetInfo = new TweetInfo(
									tjo.getString("city_code"),
									tjo.getString("country_code"),
									tjo.getString("emotiontype"),
									tjo.getString("emotionurl"),
									tjo.getString("from"),
									tjo.getString("fromurl"),
									tjo.getString("geo"), tjo.getString("id"),
									imageURL,
									(float) tjo.getDouble("latitude"),
									tjo.getString("location"),
									(float) tjo.getDouble("longitude"),
									new Music(musicAuthorString,
											musicUrlString, musicTitleString),
									tjo.getString("origtext"),
									tjo.getString("province_code"),
									tjo.getInt("self") == 1,
									tjo.getInt("status"),
									tjo.getString("text"),
									tjo.getLong("timestamp"),
									tjo.getInt("type"), new Video(picurl,
											player, realurl, shorturl, title));

							vjo = null;
						}
						tjo = null;

						this.tweetNum = data.getInt("tweetnum");
						this.verifyInfo = data.getString("verifyinfo");

						String tagString = null;
						JSONArray tagsJSONArray = null;
						JSONObject tag = null;
						int i = 0;
						int tagsNum = 0;
						try {
							tagString = data.getString("tag");
							if ((tagString != null)
									&& (!tagString.equals("null"))) {
								throw new QQConnectException(
										"has tag information");
							}
						} catch (Exception e) {
							tagsJSONArray = data.getJSONArray("tag");
							i = 0;
							tagsNum = tagsJSONArray.length();
						}
						for (; i < tagsNum; i++) {
							tag = tagsJSONArray.getJSONObject(i);
							this.tags.add(new Tag(tag.getString("id"), tag
									.getString("name")));
						}
						this.level = data.getInt("level");

						String companyString = null;
						JSONArray companysJSONArray = null;
						JSONObject companyJO = null;
						int j = 0;
						int companyNum = 0;
						try {
							companyString = data.getString("comp");
							if ((companyString != null)
									&& (!companyString.equals("null"))) {
								throw new QQConnectException(
										"has comp information");
							}
						} catch (Exception e) {
							companysJSONArray = data.getJSONArray("comp");
							j = 0;
							companyNum = companysJSONArray.length();
						}
						for (; j < companyNum; j++) {
							companyJO = companysJSONArray.getJSONObject(j);
							this.companys.add(new Company(companyJO
									.getString("begin_year"), companyJO
									.getString("company_name"), companyJO
									.getString("department_name"), companyJO
									.getString("end_year"), companyJO
									.getString("id")));
						}

						String eduString = null;
						JSONArray eduJSONArray = null;
						JSONObject eduJO = null;
						try {
							eduString = data.getString("edu");
							if ((eduString != null)
									&& (!eduString.equals("null"))) {
								throw new QQConnectException(
										"has edu information");
							}
						} catch (Exception e) {
							eduJSONArray = data.getJSONArray("edu");
							int k = 0;
							for (int eduNum = eduJSONArray.length(); k < eduNum; k++) {
								eduJO = eduJSONArray.getJSONObject(k);
								this.educations.add(new Education(eduJO
										.getString("id"), eduJO
										.getString("year"), eduJO
										.getString("schoolid"), eduJO
										.getString("departmentid"), eduJO
										.getString("level")));
							}

						}

					}

				}

			} catch (JSONException jsone) {
				throw new QQConnectException(jsone.getMessage() + ":"
						+ json.toString(), jsone);
			}
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserInfoBean other = (UserInfoBean) obj;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		return true;
	}

	public String toString() {
		return "UserInfoBean{ret=" + this.ret + ", errcode=" + this.errcode
				+ ", msg='" + this.msg + '\'' + ", birthday=" + this.birthday
				+ ", cityCode='" + this.cityCode + '\'' + ", countryCode='"
				+ this.countryCode + '\'' + ", email='" + this.email + '\''
				+ ", companys=" + this.companys + ", educations="
				+ this.educations + ", fansNum=" + this.fansNum + ", favNum="
				+ this.favNum + ", avatar=" + this.avatar
				+ ", homeCountryCode='" + this.homeCountryCode + '\''
				+ ", homeProvinceCode='" + this.homeProvinceCode + '\''
				+ ", homeCityCode='" + this.homeCityCode + '\''
				+ ", homeTownCode='" + this.homeTownCode + '\''
				+ ", homePage='" + this.homePage + '\'' + ", idolnum="
				+ this.idolnum + ", industryCode='" + this.industryCode + '\''
				+ ", introduction='" + this.introduction + '\'' + ", ent="
				+ this.ent + ", myBlack=" + this.myBlack + ", myFans="
				+ this.myFans + ", myIdol=" + this.myIdol + ", realName="
				+ this.realName + ", vip=" + this.vip + ", location='"
				+ this.location + '\'' + ", mutualFansNum="
				+ this.mutualFansNum + ", name='" + this.name + '\''
				+ ", nickName='" + this.nickName + '\'' + ", provinceCode='"
				+ this.provinceCode + '\'' + ", regTime='" + this.regTime
				+ '\'' + ", privateFlag=" + this.privateFlag + ", sex='"
				+ this.sex + '\'' + ", tags=" + this.tags + ", tweetInfo="
				+ this.tweetInfo + ", tweetNum=" + this.tweetNum
				+ ", verifyInfo='" + this.verifyInfo + '\'' + ", exp="
				+ this.exp + ", level=" + this.level + ", seqid='" + this.seqid
				+ '\'' + '}';
	}

}
