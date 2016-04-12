/*    */ package com.qq.connect;
/*    */ 
/*    */ import com.qq.connect.utils.http.HttpClient;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class QQConnect
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 2403532632395197292L;
/* 15 */   protected HttpClient client = new HttpClient();
/*    */ 
/*    */   protected QQConnect()
/*    */   {
/*    */   }
/*    */ 
/*    */   protected QQConnect(String token, String openID)
/*    */   {
/* 23 */     this.client.setToken(token);
/* 24 */     this.client.setOpenID(openID);
/*    */   }
/*    */ 
/*    */   protected void setToken(String token)
/*    */   {
/* 32 */     this.client.setToken(token);
/*    */   }
/*    */ 
/*    */   protected void setOpenID(String openID)
/*    */   {
/* 41 */     this.client.setOpenID(openID);
/*    */   }
/*    */ }

