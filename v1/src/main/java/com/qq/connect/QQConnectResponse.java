/*     */ package com.qq.connect;
/*     */ 
/*     */ import com.qq.connect.utils.http.Response;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ import java.io.Serializable;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ 
/*     */ public class QQConnectResponse
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3519962197957449562L;
/*     */ 
/*     */   public QQConnectResponse()
/*     */   {
/*     */   }
/*     */ 
/*     */   public QQConnectResponse(Response res)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected static String getString(String name, JSONObject json, boolean decode)
/*     */   {
/* 127 */     String returnValue = null;
/*     */     try {
/* 129 */       returnValue = json.getString(name);
/* 130 */       if (decode)
/*     */         try {
/* 132 */           returnValue = URLDecoder.decode(returnValue, "UTF-8");
/*     */         }
/*     */         catch (UnsupportedEncodingException ignore) {
/*     */         }
/*     */     }
/*     */     catch (JSONException ignore) {
/*     */     }
/* 139 */     return returnValue;
/*     */   }
/*     */ 
/*     */   protected static int getInt(String key, JSONObject json)
/*     */     throws JSONException
/*     */   {
/* 174 */     String str = json.getString(key);
/* 175 */     if ((null == str) || ("".equals(str)) || ("null".equals(str))) {
/* 176 */       return -1;
/*     */     }
/* 178 */     return Integer.parseInt(str);
/*     */   }
/*     */ }

