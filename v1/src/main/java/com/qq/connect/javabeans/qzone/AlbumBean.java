/*     */ package com.qq.connect.javabeans.qzone;
/*     */ 
/*     */ import com.qq.connect.QQConnectException;
/*     */ import com.qq.connect.QQConnectResponse;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class AlbumBean extends QQConnectResponse
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4742904673643859727L;
/*  20 */   private int ret = 0;
/*     */ 
/*  65 */   private String msg = "";
/*  66 */   private String albumid = "";
/*  67 */   private String classid = "";
/*  68 */   private Long createTime = Long.valueOf(0L);
/*  69 */   private String desc = "";
/*  70 */   private String name = "";
/*  71 */   private int priv = 0;
/*  72 */   private String coverurl = "";
/*     */ 
/* 100 */   private int picnum = 0;
/*     */ 
/*     */   public String getAlbumID()
/*     */   {
/*  28 */     return this.albumid;
/*     */   }
/*     */ 
/*     */   public String getClassID()
/*     */   {
/*  36 */     return this.classid;
/*     */   }
/*     */ 
/*     */   public String getDesc()
/*     */   {
/*  45 */     return this.desc;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  53 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int getPriv()
/*     */   {
/*  61 */     return this.priv;
/*     */   }
/*     */ 
/*     */   public Long getCreateTime()
/*     */   {
/*  79 */     return this.createTime;
/*     */   }
/*     */ 
/*     */   public String getCoverurl()
/*     */   {
/*  87 */     return this.coverurl;
/*     */   }
/*     */ 
/*     */   public int getPicnum()
/*     */   {
/*  95 */     return this.picnum;
/*     */   }
/*     */ 
/*     */   public int getRet()
/*     */   {
/* 107 */     return this.ret;
/*     */   }
/*     */ 
/*     */   public String getMsg()
/*     */   {
/* 115 */     return this.msg;
/*     */   }
/*     */ 
/*     */   public AlbumBean(JSONObject json) throws QQConnectException
/*     */   {
/* 120 */     init(json);
/*     */   }
/*     */ 
/*     */   public AlbumBean(String albumid, String classid, Long createtime, String desc, String name, int priv, String coverurl, int picnum) throws QQConnectException
/*     */   {
/* 125 */     this.ret = 0;
/* 126 */     this.msg = "";
/* 127 */     this.albumid = albumid;
/* 128 */     this.classid = classid;
/* 129 */     this.createTime = createtime;
/* 130 */     this.desc = desc;
/* 131 */     this.name = name;
/* 132 */     this.priv = priv;
/* 133 */     this.coverurl = coverurl;
/* 134 */     this.picnum = picnum;
/*     */   }
/*     */ 
/*     */   public AlbumBean(int ret, String msg)
/*     */   {
/* 139 */     this.ret = ret;
/* 140 */     this.msg = msg;
/*     */   }
/*     */ 
/*     */   private void init(JSONObject json) throws QQConnectException {
/* 144 */     if (json != null)
/*     */       try {
/* 146 */         this.ret = json.getInt("ret");
/* 147 */         if (0 != this.ret)
/*     */         {
/* 149 */           this.msg = json.getString("msg");
/*     */         } else {
/* 151 */           this.msg = "";
/* 152 */           this.albumid = json.getString("albumid");
/* 153 */           this.classid = json.getString("classid");
/* 154 */           this.createTime = Long.valueOf(json.getLong("createtime"));
/* 155 */           this.desc = json.getString("desc");
/* 156 */           this.name = json.getString("name");
/* 157 */           this.priv = json.getInt("priv");
/* 158 */           this.coverurl = json.getString("coverurl");
/* 159 */           this.picnum = json.getInt("picnum");
/*     */         }
/*     */       } catch (JSONException jsone) {
/* 162 */         throw new QQConnectException(jsone.getMessage() + ":" + json.toString(), jsone);
/*     */       }
/*     */   }
/*     */ }

