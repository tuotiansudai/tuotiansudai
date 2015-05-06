/*     */ package com.qq.connect.javabeans.weibo;
/*     */ 
/*     */ import com.qq.connect.QQConnectException;
/*     */ import com.qq.connect.QQConnectResponse;
/*     */ import com.qq.connect.javabeans.Avatar;
/*     */ import org.json.JSONArray;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class FansIdolsBean extends QQConnectResponse
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4742904673643859727L;
/*  19 */   private int ret = 0;
/*  20 */   private int errcode = 0;
/*  21 */   private String msg = "";
/*  22 */   private int curNum = 0;
/*  23 */   private boolean next = false;
/*  24 */   private long timestamp = 0L;
/*  25 */   private int nextStartPos = 0;
/*  26 */   private ArrayList<SingleFanIdolBean> fanIdols = new ArrayList();
/*     */ 
/*     */   public ArrayList<SingleFanIdolBean> getFanIdols()
/*     */   {
/*  33 */     return this.fanIdols;
/*     */   }
/*     */ 
/*     */   public int getNextStartPos()
/*     */   {
/*  42 */     return this.nextStartPos;
/*     */   }
/*     */ 
/*     */   public long getTimestamp()
/*     */   {
/*  50 */     return this.timestamp;
/*     */   }
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/*  58 */     return this.next;
/*     */   }
/*     */ 
/*     */   public int getCurNum()
/*     */   {
/*  66 */     return this.curNum;
/*     */   }
/*     */ 
/*     */   public FansIdolsBean(JSONObject json)
/*     */     throws QQConnectException
/*     */   {
/*  73 */     init(json);
/*     */   }
/*     */ 
/*     */   public int getRet()
/*     */   {
/*  82 */     return this.ret;
/*     */   }
/*     */ 
/*     */   public int getErrcode()
/*     */   {
/*  90 */     return this.errcode;
/*     */   }
/*     */ 
/*     */   public String getMsg()
/*     */   {
/*  98 */     return this.msg;
/*     */   }
/*     */ 
/*     */   private void init(JSONObject json) throws QQConnectException {
/* 102 */     if (json != null)
/*     */       try {
/* 104 */         this.ret = json.getInt("ret");
/* 105 */         if (0 != this.ret)
/*     */         {
/* 107 */           this.msg = json.getString("msg");
/* 108 */           this.errcode = json.getInt("errcode");
/*     */         } else {
/* 110 */           this.msg = json.getString("msg");
/* 111 */           this.errcode = 0;
/* 112 */           String dataString = json.getString("data");
/* 113 */           if ((dataString != null) && (!dataString.equals("")) && (!dataString.equals("null")))
/*     */           {
/* 116 */             JSONObject jo = json.getJSONObject("data");
/* 117 */             this.curNum = jo.getInt("curnum");
/* 118 */             this.next = (jo.getInt("hasnext") == 0);
/* 119 */             this.timestamp = jo.getLong("timestamp");
/* 120 */             this.nextStartPos = jo.getInt("nextstartpos");
/*     */ 
/* 122 */             String infoString = jo.getString("info");
/* 123 */             if ((infoString != null) && (!infoString.equals("")) && (!infoString.equals("null")))
/*     */             {
/* 126 */               JSONArray infoArray = jo.getJSONArray("info");
/* 127 */               int i = 0; for (int j = infoArray.length(); i < j; i++) {
/* 128 */                 JSONObject singleInfo = infoArray.getJSONObject(i);
/*     */ 
/* 130 */                 String tagsString = singleInfo.getString("tag");
/* 131 */                 ArrayList tags = new ArrayList();
/* 132 */                 if ((tagsString != null) && (!tagsString.equals("")) && (!tagsString.equals("null")))
/*     */                 {
/* 135 */                   JSONArray tagsJA = singleInfo.getJSONArray("tag");
/* 136 */                   int k = 0; for (int p = tagsJA.length(); k < p; k++) {
/* 137 */                     JSONObject tagJO = tagsJA.getJSONObject(k);
/* 138 */                     tags.add(new Tag(tagJO.getString("id"), tagJO.getString("name")));
/*     */                   }
/*     */ 
/*     */                 }
/*     */ 
/* 143 */                 JSONObject tweetInfo = null;
/* 144 */                 String tweetInfoString = singleInfo.getString("tweet");
/* 145 */                 if ((tweetInfoString != null) && (!tweetInfoString.equals("")) && (!tweetInfoString.equals("null")))
/*     */                 {
/* 148 */                   JSONArray tweetInfos = singleInfo.getJSONArray("tweet");
/* 149 */                   tweetInfo = tweetInfos.getJSONObject(0);
/*     */                 }
/*     */ 
/* 152 */                 this.fanIdols.add(new SingleFanIdolBean(singleInfo.getString("city_code"), singleInfo.getString("province_code"), singleInfo.getString("country_code"), singleInfo.getString("location"), singleInfo.getInt("fansnum"), new Avatar(singleInfo.getString("head")), singleInfo.getInt("idolnum"), singleInfo.getBoolean("isfans"), singleInfo.getBoolean("isidol"), singleInfo.getInt("isrealname") == 1, singleInfo.getInt("vip") == 1, singleInfo.getString("name"), singleInfo.getString("nick"), singleInfo.getString("openid"), singleInfo.getInt("sex") == 1 ? "男" : "女", tags, new SimpleTweetInfo(tweetInfo.getString("from"), tweetInfo.getString("id"), tweetInfo.getString("text"), tweetInfo.getLong("timestamp"))));
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 175 */             jo = null;
/*     */           }
/*     */         }
/*     */       } catch (JSONException jsone) {
/* 179 */         throw new QQConnectException(jsone.getMessage() + ":" + json.toString(), jsone);
/*     */       }
/*     */   }
/*     */ }

