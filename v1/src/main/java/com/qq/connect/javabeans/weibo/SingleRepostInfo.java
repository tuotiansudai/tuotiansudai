/*     */ package com.qq.connect.javabeans.weibo;
/*     */ 
/*     */ import com.qq.connect.javabeans.Avatar;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class SingleRepostInfo
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8844167881360046175L;
/*  14 */   private String cityCode = "";
/*  15 */   private int count = 0;
/*  16 */   private String countryCode = "";
/*  17 */   private String emotionType = "";
/*  18 */   private String emotionURL = "";
/*  19 */   private String from = "";
/*  20 */   private String fromURL = "";
/*  21 */   private String geo = "";
/*  22 */   private Avatar avatar = null;
/*  23 */   private String id = "";
/*  24 */   private String image = "";
/*  25 */   private boolean realName = false;
/*  26 */   private boolean vip = false;
/*  27 */   private boolean essence = false;
/*  28 */   private float latitude = 0.0F;
/*  29 */   private float longitude = 0.0F;
/*  30 */   private String location = "";
/*  31 */   private int mcount = 0;
/*  32 */   private Music music = null;
/*  33 */   private String name = "";
/*  34 */   private String nick = "";
/*  35 */   private String openID = "";
/*  36 */   private String origText = "";
/*  37 */   private String provinceCode = "";
/*  38 */   private boolean self = false;
/*  39 */   private TweetSourceBean tsb = null;
/*  40 */   private int status = 0;
/*  41 */   private String text = "";
/*  42 */   private long timeStamp = 0L;
/*  43 */   private int type = 0;
/*  44 */   private Video video = null;
/*     */ 
/*     */   public String getCityCode()
/*     */   {
/*  51 */     return this.cityCode;
/*     */   }
/*     */ 
/*     */   public int getCount()
/*     */   {
/*  59 */     return this.count;
/*     */   }
/*     */ 
/*     */   public String getCountryCode()
/*     */   {
/*  67 */     return this.countryCode;
/*     */   }
/*     */ 
/*     */   public String getEmotionType()
/*     */   {
/*  75 */     return this.emotionType;
/*     */   }
/*     */ 
/*     */   public String getEmotionURL()
/*     */   {
/*  83 */     return this.emotionURL;
/*     */   }
/*     */ 
/*     */   public String getFrom()
/*     */   {
/*  91 */     return this.from;
/*     */   }
/*     */ 
/*     */   public String getFromURL()
/*     */   {
/*  99 */     return this.fromURL;
/*     */   }
/*     */ 
/*     */   public String getGeo()
/*     */   {
/* 107 */     return this.geo;
/*     */   }
/*     */ 
/*     */   public Avatar getAvatar()
/*     */   {
/* 115 */     return this.avatar;
/*     */   }
/*     */ 
/*     */   public String getID()
/*     */   {
/* 123 */     return this.id;
/*     */   }
/*     */ 
/*     */   public String getImage()
/*     */   {
/* 131 */     return this.image;
/*     */   }
/*     */ 
/*     */   public boolean isRealName()
/*     */   {
/* 139 */     return this.realName;
/*     */   }
/*     */ 
/*     */   public boolean isVip()
/*     */   {
/* 147 */     return this.vip;
/*     */   }
/*     */ 
/*     */   public boolean isEssence()
/*     */   {
/* 155 */     return this.essence;
/*     */   }
/*     */ 
/*     */   public float getLatitude()
/*     */   {
/* 163 */     return this.latitude;
/*     */   }
/*     */ 
/*     */   public float getLongitude()
/*     */   {
/* 171 */     return this.longitude;
/*     */   }
/*     */ 
/*     */   public String getLocation()
/*     */   {
/* 179 */     return this.location;
/*     */   }
/*     */ 
/*     */   public int getMCount()
/*     */   {
/* 187 */     return this.mcount;
/*     */   }
/*     */ 
/*     */   public Music getMusic()
/*     */   {
/* 195 */     return this.music;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 203 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getNick()
/*     */   {
/* 211 */     return this.nick;
/*     */   }
/*     */ 
/*     */   public String getOpenID()
/*     */   {
/* 219 */     return this.openID;
/*     */   }
/*     */ 
/*     */   public String getOrigText()
/*     */   {
/* 227 */     return this.origText;
/*     */   }
/*     */ 
/*     */   public String getProvinceCode()
/*     */   {
/* 235 */     return this.provinceCode;
/*     */   }
/*     */ 
/*     */   public boolean isSelf()
/*     */   {
/* 243 */     return this.self;
/*     */   }
/*     */ 
/*     */   public TweetSourceBean getTsb()
/*     */   {
/* 251 */     return this.tsb;
/*     */   }
/*     */ 
/*     */   public int getStatus()
/*     */   {
/* 264 */     return this.status;
/*     */   }
/*     */ 
/*     */   public String getText()
/*     */   {
/* 272 */     return this.text;
/*     */   }
/*     */ 
/*     */   public long getTimestamp()
/*     */   {
/* 280 */     return this.timeStamp;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 295 */     return this.type;
/*     */   }
/*     */ 
/*     */   public Video getVideo()
/*     */   {
/* 303 */     return this.video;
/*     */   }
/*     */ 
/*     */   public SingleRepostInfo(String cityCode, int count, String countryCode, String emotionType, String emotionURL, String from, String fromURL, String geo, Avatar avatar, String id, String image, boolean realName, boolean vip, boolean essence, float latitude, float longitude, String location, int mcount, Music music, String name, String nick, String openID, String origText, String provinceCode, boolean self, TweetSourceBean tsb, int status, String text, String timeStamp, int type, Video video)
/*     */   {
/* 313 */     this.cityCode = cityCode;
/* 314 */     this.count = count;
/* 315 */     this.countryCode = countryCode;
/* 316 */     this.emotionType = emotionType;
/* 317 */     this.emotionURL = emotionURL;
/* 318 */     this.from = from;
/* 319 */     this.fromURL = fromURL;
/* 320 */     this.geo = geo;
/* 321 */     this.avatar = avatar;
/* 322 */     this.id = id;
/* 323 */     this.image = image;
/* 324 */     this.realName = realName;
/* 325 */     this.vip = vip;
/* 326 */     this.essence = essence;
/* 327 */     this.latitude = latitude;
/* 328 */     this.longitude = longitude;
/* 329 */     this.location = location;
/* 330 */     this.mcount = mcount;
/* 331 */     this.music = music;
/* 332 */     this.name = name;
/* 333 */     this.nick = nick;
/* 334 */     this.openID = openID;
/* 335 */     this.origText = origText;
/* 336 */     this.provinceCode = provinceCode;
/* 337 */     this.self = self;
/* 338 */     this.tsb = tsb;
/* 339 */     this.status = status;
/* 340 */     this.text = text;
/* 341 */     this.timeStamp = Long.valueOf(timeStamp).longValue();
/* 342 */     this.type = type;
/* 343 */     this.video = video;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 348 */     return "SingleRepostInfo{cityCode='" + this.cityCode + '\'' + ", count=" + this.count + ", countryCode='" + this.countryCode + '\'' + ", emotionType='" + this.emotionType + '\'' + ", emotionURL='" + this.emotionURL + '\'' + ", from='" + this.from + '\'' + ", fromURL='" + this.fromURL + '\'' + ", geo='" + this.geo + '\'' + ", avatar=" + this.avatar + ", id='" + this.id + '\'' + ", image='" + this.image + '\'' + ", realName=" + this.realName + ", vip=" + this.vip + ", essence=" + this.essence + ", latitude=" + this.latitude + ", longitude=" + this.longitude + ", location='" + this.location + '\'' + ", mcount=" + this.mcount + ", music=" + this.music + ", name='" + this.name + '\'' + ", nick='" + this.nick + '\'' + ", openID='" + this.openID + '\'' + ", origText='" + this.origText + '\'' + ", provinceCode='" + this.provinceCode + '\'' + ", self=" + this.self + ", tsb=" + this.tsb + ", status=" + this.status + ", text='" + this.text + '\'' + ", timeStamp=" + this.timeStamp + ", type=" + this.type + ", video=" + this.video + '}';
/*     */   }
/*     */ }

