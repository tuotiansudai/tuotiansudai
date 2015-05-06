/*     */ package com.qq.connect.javabeans.weibo;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class TweetInfo
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -356352137054008452L;
/*  13 */   private String cityCode = "";
/*     */ 
/* 229 */   private String countryCode = "";
/* 230 */   private String emotionType = "";
/* 231 */   private String emotionUrl = "";
/* 232 */   private String from = "";
/* 233 */   private String fromURL = "";
/* 234 */   private String geo = "";
/* 235 */   private String id = "";
/* 236 */   private String image = "";
/* 237 */   private float latitude = 0.0F;
/* 238 */   private String location = "";
/* 239 */   private float longitude = 0.0F;
/* 240 */   private Music music = null;
/* 241 */   private String origText = "";
/* 242 */   private String provinceCode = "";
/* 243 */   private boolean self = false;
/* 244 */   private int status = 0;
/*     */ 
/* 253 */   private String text = "";
/* 254 */   private long timestamp = 0L;
/* 255 */   private int type = 0;
/* 256 */   private Video video = null;
/*     */ 
/*     */   public String toString()
/*     */   {
/*  17 */     return "TweetInfo{cityCode='" + this.cityCode + '\'' + ", countryCode='" + this.countryCode + '\'' + ", emotionType='" + this.emotionType + '\'' + ", emotionUrl='" + this.emotionUrl + '\'' + ", from='" + this.from + '\'' + ", fromURL='" + this.fromURL + '\'' + ", geo='" + this.geo + '\'' + ", id='" + this.id + '\'' + ", image='" + this.image + '\'' + ", latitude=" + this.latitude + ", location='" + this.location + '\'' + ", longitude=" + this.longitude + ", music=" + this.music + ", origText='" + this.origText + '\'' + ", provinceCode='" + this.provinceCode + '\'' + ", self=" + this.self + ", status=" + this.status + ", text='" + this.text + '\'' + ", timestamp=" + this.timestamp + ", type=" + this.type + ", video=" + this.video + '}';
/*     */   }
/*     */ 
/*     */   public TweetInfo(String cityCode, String countryCode, String emotionType, String emotionUrl, String from, String fromURL, String geo, String id, String image, float latitude, String location, float longitude, Music music, String origText, String provinceCode, boolean self, int status, String text, long timestamp, int type, Video video)
/*     */   {
/*  48 */     this.cityCode = cityCode;
/*  49 */     this.countryCode = countryCode;
/*  50 */     this.emotionType = emotionType;
/*  51 */     this.emotionUrl = emotionUrl;
/*  52 */     this.from = from;
/*  53 */     this.fromURL = fromURL;
/*  54 */     this.geo = geo;
/*  55 */     this.id = id;
/*  56 */     this.image = image;
/*  57 */     this.latitude = latitude;
/*  58 */     this.location = location;
/*  59 */     this.longitude = longitude;
/*  60 */     this.music = music;
/*  61 */     this.origText = origText;
/*  62 */     this.provinceCode = provinceCode;
/*  63 */     this.self = self;
/*  64 */     this.text = text;
/*  65 */     this.status = status;
/*  66 */     this.timestamp = timestamp;
/*  67 */     this.type = type;
/*  68 */     this.video = video;
/*     */   }
/*     */ 
/*     */   public TweetInfo()
/*     */   {
/*  73 */     this.music = new Music("", "", "");
/*  74 */     this.video = new Video("", "", "", "", "");
/*     */   }
/*     */ 
/*     */   public String getCityCode()
/*     */   {
/*  83 */     return this.cityCode;
/*     */   }
/*     */ 
/*     */   public String getCountryCode()
/*     */   {
/*  91 */     return this.countryCode;
/*     */   }
/*     */ 
/*     */   public String getEmotionType()
/*     */   {
/*  99 */     return this.emotionType;
/*     */   }
/*     */ 
/*     */   public String getEmotionURL()
/*     */   {
/* 107 */     return this.emotionUrl;
/*     */   }
/*     */ 
/*     */   public String getFrom()
/*     */   {
/* 115 */     return this.from;
/*     */   }
/*     */ 
/*     */   public String getFromURL()
/*     */   {
/* 123 */     return this.fromURL;
/*     */   }
/*     */ 
/*     */   public String getGeo()
/*     */   {
/* 131 */     return this.geo;
/*     */   }
/*     */ 
/*     */   public String getID()
/*     */   {
/* 139 */     return this.id;
/*     */   }
/*     */ 
/*     */   public String getImage()
/*     */   {
/* 147 */     return this.image;
/*     */   }
/*     */ 
/*     */   public float getLatitude()
/*     */   {
/* 155 */     return this.latitude;
/*     */   }
/*     */ 
/*     */   public String getLocation()
/*     */   {
/* 163 */     return this.location;
/*     */   }
/*     */ 
/*     */   public float getLongitude()
/*     */   {
/* 171 */     return this.longitude;
/*     */   }
/*     */ 
/*     */   public Music getMusic()
/*     */   {
/* 179 */     return this.music;
/*     */   }
/*     */ 
/*     */   public String getOrigText()
/*     */   {
/* 187 */     return this.origText;
/*     */   }
/*     */ 
/*     */   public String getProvinceCode()
/*     */   {
/* 195 */     return this.provinceCode;
/*     */   }
/*     */ 
/*     */   public boolean isSelf()
/*     */   {
/* 203 */     return this.self;
/*     */   }
/*     */ 
/*     */   public String getText()
/*     */   {
/* 211 */     return this.text;
/*     */   }
/*     */ 
/*     */   public long getTimestamp()
/*     */   {
/* 219 */     return this.timestamp;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 227 */     return this.type;
/*     */   }
/*     */ 
/*     */   public int getStatus()
/*     */   {
/* 251 */     return this.status;
/*     */   }
/*     */ 
/*     */   public Video getVideo()
/*     */   {
/* 263 */     return this.video;
/*     */   }
/*     */ }

