/*     */ package com.qq.connect.javabeans.weibo;
/*     */ 
/*     */ import com.qq.connect.javabeans.Avatar;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class TweetSourceBean
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -356352137054008452L;
/*  34 */   private TweetInfo ti = null;
/*  35 */   private boolean vip = false;
/*  36 */   private boolean essence = false;
/*  37 */   private int mCount = 0;
/*  38 */   private String name = "";
/*  39 */   private String nick = "";
/*  40 */   private String openID = "";
/*     */ 
/*  94 */   private int count = 0;
/*     */ 
/* 104 */   private Avatar avatar = null;
/*     */ 
/*     */   public TweetSourceBean(String cityCode, int count, String countryCode, String from, String fromUrl, String geo, Avatar avatar, String id, String image, boolean vip, boolean essence, float latitude, String location, float longitude, int mcount, Music music, String name, String nick, String openid, String origText, String provinceCode, boolean self, int status, String text, long timeStamp, int type, Video video)
/*     */   {
/*  19 */     this.ti = new TweetInfo(cityCode, countryCode, "", "", from, fromUrl, geo, id, image, latitude, location, longitude, music, origText, provinceCode, self, status, text, timeStamp, type, video);
/*  20 */     this.count = count;
/*  21 */     this.avatar = avatar;
/*  22 */     this.vip = vip;
/*  23 */     this.essence = essence;
/*  24 */     this.mCount = mcount;
/*  25 */     this.name = name;
/*  26 */     this.nick = nick;
/*  27 */     this.openID = openid;
/*     */   }
/*     */ 
/*     */   public TweetSourceBean()
/*     */   {
/*     */   }
/*     */ 
/*     */   public String getOpenID()
/*     */   {
/*  47 */     return this.openID;
/*     */   }
/*     */ 
/*     */   public String getNick()
/*     */   {
/*  55 */     return this.nick;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  63 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int getMCount()
/*     */   {
/*  70 */     return this.mCount;
/*     */   }
/*     */ 
/*     */   public boolean isEssence()
/*     */   {
/*  78 */     return this.essence;
/*     */   }
/*     */ 
/*     */   public boolean isVip()
/*     */   {
/*  86 */     return this.vip;
/*     */   }
/*     */ 
/*     */   public int getCount()
/*     */   {
/* 101 */     return this.count;
/*     */   }
/*     */ 
/*     */   public Avatar getAvatar()
/*     */   {
/* 111 */     return this.avatar;
/*     */   }
/*     */ 
/*     */   public String getCityCode()
/*     */   {
/* 120 */     return this.ti.getCityCode();
/*     */   }
/*     */ 
/*     */   public String getCountryCode()
/*     */   {
/* 128 */     return this.ti.getCountryCode();
/*     */   }
/*     */ 
/*     */   public String getFrom()
/*     */   {
/* 136 */     return this.ti.getFrom();
/*     */   }
/*     */ 
/*     */   public String getFromURL()
/*     */   {
/* 144 */     return this.ti.getFromURL();
/*     */   }
/*     */ 
/*     */   public String getGeo()
/*     */   {
/* 152 */     return this.ti.getGeo();
/*     */   }
/*     */ 
/*     */   public String getID()
/*     */   {
/* 160 */     return this.ti.getID();
/*     */   }
/*     */ 
/*     */   public String getImage()
/*     */   {
/* 168 */     return this.ti.getImage();
/*     */   }
/*     */ 
/*     */   public float getLatitude()
/*     */   {
/* 176 */     return this.ti.getLatitude();
/*     */   }
/*     */ 
/*     */   public float getLongitude()
/*     */   {
/* 184 */     return this.ti.getLongitude();
/*     */   }
/*     */ 
/*     */   public String getLocation()
/*     */   {
/* 192 */     return this.ti.getLocation();
/*     */   }
/*     */ 
/*     */   public Music getMusic()
/*     */   {
/* 200 */     return this.ti.getMusic();
/*     */   }
/*     */ 
/*     */   public String getOrigText()
/*     */   {
/* 208 */     return this.ti.getOrigText();
/*     */   }
/*     */ 
/*     */   public String getProvinceCode()
/*     */   {
/* 216 */     return this.ti.getProvinceCode();
/*     */   }
/*     */ 
/*     */   public boolean isSelf()
/*     */   {
/* 224 */     return this.ti.isSelf();
/*     */   }
/*     */ 
/*     */   public int getStatus()
/*     */   {
/* 237 */     return this.ti.getStatus();
/*     */   }
/*     */ 
/*     */   public String getText()
/*     */   {
/* 246 */     return this.ti.getText();
/*     */   }
/*     */ 
/*     */   public long getTimestamp()
/*     */   {
/* 254 */     return this.ti.getTimestamp();
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 269 */     return this.ti.getType();
/*     */   }
/*     */ 
/*     */   public Video getVideo()
/*     */   {
/* 277 */     return this.ti.getVideo();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 283 */     return "TweetSourceBean{ti=" + this.ti + ", vip=" + this.vip + ", essence=" + this.essence + ", mCount=" + this.mCount + ", name='" + this.name + '\'' + ", nick='" + this.nick + '\'' + ", openID='" + this.openID + '\'' + ", count=" + this.count + ", avatar=" + this.avatar + '}';
/*     */   }
/*     */ }

