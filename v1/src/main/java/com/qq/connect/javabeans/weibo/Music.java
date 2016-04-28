/*    */ package com.qq.connect.javabeans.weibo;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Music
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 13 */   private String author = "";
/*    */ 
/* 47 */   private String url = "";
/* 48 */   private String title = "";
/*    */ 
/*    */   public String getAuthor()
/*    */   {
/* 20 */     return this.author;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 25 */     return "Music{author='" + this.author + '\'' + ", url='" + this.url + '\'' + ", title='" + this.title + '\'' + '}';
/*    */   }
/*    */ 
/*    */   public String getUrl()
/*    */   {
/* 37 */     return this.url;
/*    */   }
/*    */ 
/*    */   public String getTitle()
/*    */   {
/* 45 */     return this.title;
/*    */   }
/*    */ 
/*    */   public Music(String author, String url, String title)
/*    */   {
/* 52 */     this.author = author;
/* 53 */     this.url = url;
/* 54 */     this.title = title;
/*    */   }
/*    */ }

