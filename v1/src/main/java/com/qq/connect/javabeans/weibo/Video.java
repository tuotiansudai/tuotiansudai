/*    */ package com.qq.connect.javabeans.weibo;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Video
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1378516151120961177L;
/* 13 */   private String picUrl = "";
/*    */ 
/* 63 */   private String player = "";
/* 64 */   private String realUrl = "";
/* 65 */   private String shortUrl = "";
/* 66 */   private String title = "";
/*    */ 
/*    */   public Video(String picUrl, String player, String realUrl, String shortUrl, String title)
/*    */   {
/* 17 */     this.picUrl = picUrl;
/* 18 */     this.player = player;
/* 19 */     this.realUrl = realUrl;
/* 20 */     this.shortUrl = shortUrl;
/* 21 */     this.title = title;
/*    */   }
/*    */ 
/*    */   public String getPicUrl()
/*    */   {
/* 29 */     return this.picUrl;
/*    */   }
/*    */ 
/*    */   public String getPlayer()
/*    */   {
/* 37 */     return this.player;
/*    */   }
/*    */ 
/*    */   public String getRealUrl()
/*    */   {
/* 45 */     return this.realUrl;
/*    */   }
/*    */ 
/*    */   public String getShortUrl()
/*    */   {
/* 53 */     return this.shortUrl;
/*    */   }
/*    */ 
/*    */   public String getTitle()
/*    */   {
/* 61 */     return this.title;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 71 */     return "Video{picUrl='" + this.picUrl + '\'' + ", player='" + this.player + '\'' + ", realUrl='" + this.realUrl + '\'' + ", shortUrl='" + this.shortUrl + '\'' + ", title='" + this.title + '\'' + '}';
/*    */   }
/*    */ }

