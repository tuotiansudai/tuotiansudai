/*     */ package com.qq.connect.javabeans.weibo;
/*     */ 
/*     */ import com.qq.connect.QQConnectException;
/*     */ import com.qq.connect.QQConnectResponse;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class WeiboBean extends QQConnectResponse
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4742904673643859727L;
/*  20 */   private int ret = 0;
/*  21 */   private String msg = "";
/*  22 */   private int errcode = 0;
/*  23 */   private String id = "";
/*  24 */   private Long time = Long.valueOf(0L);
/*  25 */   private String imageURL = "";
/*     */ 
/*     */   public String getImageURL()
/*     */   {
/*  32 */     return this.imageURL;
/*     */   }
/*     */ 
/*     */   public int getErrcode()
/*     */   {
/*  39 */     return this.errcode;
/*     */   }
/*     */ 
/*     */   public String getID()
/*     */   {
/*  47 */     return this.id;
/*     */   }
/*     */ 
/*     */   public Long getTime()
/*     */   {
/*  54 */     return this.time;
/*     */   }
/*     */ 
/*     */   public int getRet()
/*     */   {
/*  63 */     return this.ret;
/*     */   }
/*     */ 
/*     */   public String getMsg()
/*     */   {
/*  71 */     return this.msg;
/*     */   }
/*     */ 
/*     */   public WeiboBean(JSONObject json) throws QQConnectException
/*     */   {
/*  76 */     init(json);
/*     */   }
/*     */ 
/*     */   private void init(JSONObject json) throws QQConnectException
/*     */   {
/*  81 */     if (json != null)
/*     */       try {
/*  83 */         this.ret = json.getInt("ret");
/*  84 */         if (0 != this.ret)
/*     */         {
/*  86 */           this.msg = json.getString("msg");
/*  87 */           this.errcode = json.getInt("errcode");
/*     */         } else {
/*  89 */           this.msg = json.getString("msg");
/*  90 */           this.errcode = 0;
/*  91 */           JSONObject jo = json.getJSONObject("data");
/*  92 */           this.id = jo.getString("id");
/*  93 */           this.time = Long.valueOf(jo.getLong("time"));
/*  94 */           this.imageURL = json.getString("imgurl");
/*  95 */           jo = null;
/*     */         }
/*     */       } catch (JSONException jsone) {
/*  98 */         throw new QQConnectException(jsone.getMessage() + ":" + json.toString(), jsone);
/*     */       }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 107 */     return "WeiboBean{ret=" + this.ret + ", msg='" + this.msg + '\'' + ", errcode=" + this.errcode + ", id='" + this.id + '\'' + ", time=" + this.time + ", imageURL='" + this.imageURL + '\'' + '}';
/*     */   }
/*     */ }

