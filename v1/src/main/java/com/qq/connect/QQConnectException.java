/*    */ package com.qq.connect;
/*    */ 
/*    */ import org.json.JSONException;
/*    */ import org.json.JSONObject;
/*    */ 
/*    */ public class QQConnectException extends Exception
/*    */ {
/* 12 */   private int statusCode = -1;
/*    */   private static final long serialVersionUID = -2623309261327598087L;
/*    */ 
/*    */   public QQConnectException(String msg)
/*    */   {
/* 16 */     super(msg);
/*    */   }
/*    */ 
/*    */   public QQConnectException(Exception cause) {
/* 20 */     super(cause);
/*    */   }
/*    */ 
/*    */   public QQConnectException(String msg, int statusCode) throws JSONException {
/* 24 */     super(msg);
/* 25 */     this.statusCode = statusCode;
/*    */   }
/*    */ 
/*    */   public QQConnectException(String msg, JSONObject json, int statusCode) throws JSONException {
/* 29 */     super(msg + "\n error:" + json.getString("msg"));
/* 30 */     this.statusCode = statusCode;
/*    */   }
/*    */ 
/*    */   public QQConnectException(String msg, Exception cause) {
/* 34 */     super(msg, cause);
/*    */   }
/*    */ 
/*    */   public QQConnectException(String msg, Exception cause, int statusCode) {
/* 38 */     super(msg, cause);
/* 39 */     this.statusCode = statusCode;
/*    */   }
/*    */ 
/*    */   public int getStatusCode()
/*    */   {
/* 48 */     return this.statusCode;
/*    */   }
/*    */ }

