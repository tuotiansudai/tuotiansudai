/*     */ package com.qq.connect.javabeans.weibo;
/*     */ 
/*     */ import com.qq.connect.javabeans.Avatar;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class SingleFanIdolBean
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4742904673643859727L;
/*  14 */   private String cityCode = "";
/*  15 */   private String provinceCode = "";
/*  16 */   private String countryCode = "";
/*  17 */   private String location = "";
/*  18 */   private int fansNum = 0;
/*  19 */   private Avatar avatar = null;
/*  20 */   private int idolNum = 0;
/*  21 */   private boolean fans = false;
/*  22 */   private boolean idol = false;
/*  23 */   private boolean realName = false;
/*  24 */   private boolean vip = false;
/*  25 */   private String name = "";
/*     */ 
/*  50 */   private String nick = "";
/*  51 */   private String openID = "";
/*  52 */   private String sex = "男";
/*  53 */   private ArrayList<Tag> tags = new ArrayList();
/*  54 */   private SimpleTweetInfo sti = null;
/*     */ 
/*     */   public String toString()
/*     */   {
/*  29 */     return "SingleFanIdolBean{cityCode='" + this.cityCode + '\'' + ", provinceCode='" + this.provinceCode + '\'' + ", countryCode='" + this.countryCode + '\'' + ", location='" + this.location + '\'' + ", fansNum=" + this.fansNum + ", avatar=" + this.avatar + ", idolNum=" + this.idolNum + ", fans=" + this.fans + ", idol=" + this.idol + ", realName=" + this.realName + ", vip=" + this.vip + ", name='" + this.name + '\'' + ", nick='" + this.nick + '\'' + ", openID='" + this.openID + '\'' + ", sex='" + this.sex + '\'' + ", tags=" + this.tags + ", sti=" + this.sti + '}';
/*     */   }
/*     */ 
/*     */   public String getCityCode()
/*     */   {
/*  61 */     return this.cityCode;
/*     */   }
/*     */ 
/*     */   public String getProvinceCode()
/*     */   {
/*  69 */     return this.provinceCode;
/*     */   }
/*     */ 
/*     */   public String getCountryCode()
/*     */   {
/*  77 */     return this.countryCode;
/*     */   }
/*     */ 
/*     */   public String getLocation()
/*     */   {
/*  85 */     return this.location;
/*     */   }
/*     */ 
/*     */   public int getFansNum()
/*     */   {
/*  93 */     return this.fansNum;
/*     */   }
/*     */ 
/*     */   public Avatar getAvatar()
/*     */   {
/* 101 */     return this.avatar;
/*     */   }
/*     */ 
/*     */   public int getIdolNum()
/*     */   {
/* 109 */     return this.idolNum;
/*     */   }
/*     */ 
/*     */   public boolean isFans()
/*     */   {
/* 117 */     return this.fans;
/*     */   }
/*     */ 
/*     */   public boolean isIdol()
/*     */   {
/* 125 */     return this.idol;
/*     */   }
/*     */ 
/*     */   public boolean isRealName()
/*     */   {
/* 133 */     return this.realName;
/*     */   }
/*     */ 
/*     */   public boolean isVip()
/*     */   {
/* 141 */     return this.vip;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 149 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getNick()
/*     */   {
/* 157 */     return this.nick;
/*     */   }
/*     */ 
/*     */   public String getOpenID()
/*     */   {
/* 165 */     return this.openID;
/*     */   }
/*     */ 
/*     */   public String getSex()
/*     */   {
/* 173 */     return this.sex;
/*     */   }
/*     */ 
/*     */   public ArrayList<Tag> getTags()
/*     */   {
/* 181 */     return this.tags;
/*     */   }
/*     */ 
/*     */   public SimpleTweetInfo getSti()
/*     */   {
/* 189 */     return this.sti;
/*     */   }
/*     */ 
/*     */   public SingleFanIdolBean(String cityCode, String provinceCode, String countryCode, String location, int fansNum, Avatar avatar, int idolNum, boolean fans, boolean idol, boolean realName, boolean vip, String name, String nick, String openID, String sex, ArrayList<Tag> tags, SimpleTweetInfo sti) {
/* 193 */     this.cityCode = cityCode;
/* 194 */     this.provinceCode = provinceCode;
/* 195 */     this.countryCode = countryCode;
/* 196 */     this.location = location;
/* 197 */     this.fansNum = fansNum;
/* 198 */     this.avatar = avatar;
/* 199 */     this.idolNum = idolNum;
/* 200 */     this.fans = fans;
/* 201 */     this.idol = idol;
/* 202 */     this.realName = realName;
/* 203 */     this.vip = vip;
/* 204 */     this.name = name;
/* 205 */     this.nick = nick;
/* 206 */     this.openID = openID;
/* 207 */     this.sex = sex;
/* 208 */     this.tags = tags;
/* 209 */     this.sti = sti;
/*     */   }
/*     */ }

