/*    */ package com.qq.connect.javabeans.weibo;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class SimpleTweetInfo
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 4742904673643859727L;
/* 11 */   private String from = "";
/* 12 */   private String id = "";
/* 13 */   private String text = "";
/* 14 */   private long timestamp = 0L;
/*    */ 
/*    */   public String getFrom()
/*    */   {
/* 21 */     return this.from;
/*    */   }
/*    */ 
/*    */   public String getId()
/*    */   {
/* 29 */     return this.id;
/*    */   }
/*    */ 
/*    */   public String getText()
/*    */   {
/* 37 */     return this.text;
/*    */   }
/*    */ 
/*    */   public long getTimestamp()
/*    */   {
/* 45 */     return this.timestamp;
/*    */   }
/*    */ 
/*    */   public SimpleTweetInfo(String from, String id, String text, long timestamp) {
/* 49 */     this.from = from;
/* 50 */     this.id = id;
/* 51 */     this.text = text;
/* 52 */     this.timestamp = timestamp;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 58 */     return "SimpleTweetInfo{from='" + this.from + '\'' + ", id='" + this.id + '\'' + ", text='" + this.text + '\'' + ", timestamp=" + this.timestamp + '}';
/*    */   }
/*    */ }

