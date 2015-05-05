/*     */ package com.qq.connect.javabeans.qzone;
/*     */ 
/*     */ import com.qq.connect.QQConnectException;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class AlbumPicBean
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4742904673643859727L;
/*  15 */   private int ret = 0;
/*  16 */   private String albumID = "";
/*  17 */   private String lloc = "";
/*  18 */   private String sloc = "";
/*  19 */   private String largeURL = "";
/*  20 */   private String smallURL = "";
/*  21 */   private int height = 0;
/*  22 */   private int width = 0;
/*  23 */   private String msg = "";
/*     */ 
/*     */   public AlbumPicBean(JSONObject json) throws QQConnectException
/*     */   {
/*  27 */     init(json);
/*     */   }
/*     */ 
/*     */   private void init(JSONObject json) throws QQConnectException {
/*  31 */     if (json != null)
/*     */       try {
/*  33 */         this.ret = json.getInt("ret");
/*  34 */         if (0 != this.ret)
/*     */         {
/*  36 */           this.msg = json.getString("msg");
/*     */         } else {
/*  38 */           this.msg = "";
/*  39 */           this.albumID = json.getString("albumid");
/*  40 */           this.lloc = json.getString("lloc");
/*  41 */           this.sloc = json.getString("sloc");
/*  42 */           this.largeURL = json.getString("large_url");
/*  43 */           this.smallURL = json.getString("small_url");
/*  44 */           this.height = json.getInt("height");
/*  45 */           this.width = json.getInt("width");
/*     */         }
/*     */       } catch (JSONException jsone) {
/*  48 */         throw new QQConnectException(jsone.getMessage() + ":" + json.toString(), jsone);
/*     */       }
/*     */   }
/*     */ 
/*     */   public int getRet()
/*     */   {
/*  60 */     return this.ret;
/*     */   }
/*     */ 
/*     */   public String getAlbumID()
/*     */   {
/*  68 */     return this.albumID;
/*     */   }
/*     */ 
/*     */   public String getLloc()
/*     */   {
/*  76 */     return this.lloc;
/*     */   }
/*     */ 
/*     */   public String getSloc()
/*     */   {
/*  84 */     return this.sloc;
/*     */   }
/*     */ 
/*     */   public String getLargeURL()
/*     */   {
/*  92 */     return this.largeURL;
/*     */   }
/*     */ 
/*     */   public String getSmallURL()
/*     */   {
/* 100 */     return this.smallURL;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 108 */     return this.height;
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/* 116 */     return this.width;
/*     */   }
/*     */ 
/*     */   public String getMsg()
/*     */   {
/* 124 */     return this.msg;
/*     */   }
/*     */ }

