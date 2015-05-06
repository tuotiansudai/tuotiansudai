/*     */ package com.qq.connect.javabeans;
/*     */ 
/*     */ import com.qq.connect.QQConnectException;
/*     */ import com.qq.connect.QQConnectResponse;
/*     */ import com.qq.connect.utils.http.Response;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ import java.io.Serializable;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class AccessToken extends QQConnectResponse
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5499186506535919974L;
/*  22 */   private String accessToken = "";
/*  23 */   private String expireIn = "";
/*     */ 
/*  28 */   private String refreshToken = "";
/*     */   private String openid;
/*     */ 
/*     */   public AccessToken()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AccessToken(Response res)
/*     */     throws QQConnectException
/*     */   {
/*  36 */     super(res);
/*  37 */     JSONObject json = null;
/*  38 */     String result = "";
/*     */     try
/*     */     {
/*  41 */       json = res.asJSONObject();
/*     */       try {
/*  43 */         this.accessToken = json.getString("access_token");
/*  44 */         this.expireIn = json.getString("expires_in");
/*  45 */         this.refreshToken = json.getString("refresh_token");
/*  46 */         this.openid = json.getString("openid");
/*     */       } catch (JSONException je) {
/*  48 */         throw new QQConnectException(je.getMessage() + ":" + json.toString(), je);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  52 */       result = res.asString();
/*  53 */       Matcher m = Pattern.compile("^access_token=(\\w+)&expires_in=(\\w+)&refresh_token=(\\w+)$").matcher(result);
/*  54 */       if (m.find()) {
/*  55 */         this.accessToken = m.group(1);
/*  56 */         this.expireIn = m.group(2);
/*  57 */         this.refreshToken = m.group(3);
/*     */       } else {
/*  59 */         Matcher m2 = Pattern.compile("^access_token=(\\w+)&expires_in=(\\w+)$").matcher(result);
/*  60 */         if (m2.find()) {
/*  61 */           this.accessToken = m2.group(1);
/*  62 */           this.expireIn = m2.group(2);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   AccessToken(String res) throws QQConnectException, JSONException {
/*  69 */     JSONObject json = new JSONObject(res);
/*  70 */     this.accessToken = json.getString("access_token");
/*  71 */     this.expireIn = json.getString("expires_in");
/*  72 */     this.refreshToken = json.getString("refresh_token");
/*  73 */     this.openid = json.getString("openid");
/*     */   }
/*     */ 
/*     */   public String getAccessToken()
/*     */   {
/*  82 */     return this.accessToken;
/*     */   }
/*     */ 
/*     */   public long getExpireIn()
/*     */   {
/*  90 */     return Long.valueOf(this.expireIn).longValue();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 102 */     int prime = 31;
/* 103 */     int result = 1;
/* 104 */     result = 31 * result + (this.accessToken == null ? 0 : this.accessToken.hashCode());
/*     */ 
/* 106 */     result = 31 * result + (this.expireIn == null ? 0 : this.expireIn.hashCode());
/*     */ 
/* 108 */     result = 31 * result + (this.refreshToken == null ? 0 : this.refreshToken.hashCode());
/*     */ 
/* 110 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj) {
/* 114 */     if (this == obj)
/* 115 */       return true;
/* 116 */     if (obj == null)
/* 117 */       return false;
/* 118 */     if (getClass() != obj.getClass())
/* 119 */       return false;
/* 120 */     AccessToken other = (AccessToken)obj;
/* 121 */     if (this.accessToken == null) {
/* 122 */       if (other.accessToken != null)
/* 123 */         return false;
/* 124 */     } else if (!this.accessToken.equals(other.accessToken))
/* 125 */       return false;
/* 126 */     if (this.expireIn == null) {
/* 127 */       if (other.expireIn != null)
/* 128 */         return false;
/* 129 */     } else if (!this.expireIn.equals(other.expireIn))
/* 130 */       return false;
/* 131 */     if (this.refreshToken == null) {
/* 132 */       if (other.refreshToken != null)
/* 133 */         return false;
/* 134 */     } else if (!this.refreshToken.equals(other.refreshToken))
/* 135 */       return false;
/* 136 */     return true;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 140 */     return "AccessToken [accessToken=" + this.accessToken + ", expireIn=" + this.expireIn + "]";
/*     */   }
/*     */ }

