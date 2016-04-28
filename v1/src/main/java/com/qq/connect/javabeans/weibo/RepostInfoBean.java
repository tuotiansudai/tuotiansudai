/*     */ package com.qq.connect.javabeans.weibo;
/*     */ 
/*     */ import com.qq.connect.QQConnectException;
/*     */ import com.qq.connect.javabeans.Avatar;
/*     */ import org.json.JSONArray;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class RepostInfoBean
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8844167881360046175L;
/*  21 */   private int ret = 0;
/*  22 */   private int errcode = 0;
/*  23 */   private String msg = "";
/*  24 */   private long timeStamp = 0L;
/*  25 */   private ArrayList<SingleRepostInfo> sris = new ArrayList();
/*     */ 
/*  75 */   private HashMap<String, String> users = new HashMap();
/*     */ 
/*  77 */   private boolean next = false;
/*     */ 
/*     */   public int getErrcode()
/*     */   {
/*  32 */     return this.errcode;
/*     */   }
/*     */ 
/*     */   public String getMsg()
/*     */   {
/*  40 */     return this.msg;
/*     */   }
/*     */ 
/*     */   public long getTimeStamp()
/*     */   {
/*  48 */     return this.timeStamp;
/*     */   }
/*     */ 
/*     */   public ArrayList<SingleRepostInfo> getRepostInfo()
/*     */   {
/*  56 */     return this.sris;
/*     */   }
/*     */ 
/*     */   public HashMap<String, String> getUsers()
/*     */   {
/*  64 */     return this.users;
/*     */   }
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/*  72 */     return this.next;
/*     */   }
/*     */ 
/*     */   public RepostInfoBean()
/*     */   {
/*     */   }
/*     */ 
/*     */   public RepostInfoBean(JSONObject json)
/*     */     throws QQConnectException
/*     */   {
/*  84 */     init(json);
/*     */   }
/*     */ 
/*     */   public int getRet()
/*     */   {
/*  93 */     return this.ret;
/*     */   }
/*     */ 
/*     */   private void init(JSONObject json) throws QQConnectException {
/*  97 */     if (json != null)
/*     */       try {
/*  99 */         this.ret = json.getInt("ret");
/* 100 */         if (0 != this.ret)
/*     */         {
/* 102 */           this.msg = json.getString("msg");
/* 103 */           this.errcode = json.getInt("errcode");
/*     */         } else {
/* 105 */           this.msg = json.getString("msg");
/* 106 */           this.errcode = 0;
/* 107 */           String dataString = json.getString("data");
/* 108 */           JSONObject jo = null;
/* 109 */           if ((dataString != null) && (!dataString.equals("")) && (!dataString.equals("null")))
/*     */           {
/* 112 */             jo = json.getJSONObject("data");
/*     */ 
/* 114 */             this.next = (jo.getInt("hasnext") == 0);
/* 115 */             this.timeStamp = jo.getLong("timestamp");
/*     */ 
/* 118 */             String userString = jo.getString("user");
/* 119 */             if ((userString != null) && (!userString.equals("")) && (!userString.equals("null")))
/*     */             {
/* 122 */               JSONObject userJSONObj = jo.getJSONObject("user");
/* 123 */               String[] names = JSONObject.getNames(userJSONObj);
/* 124 */               int i = 0; for (int j = names.length; i < j; i++) {
/* 125 */                 this.users.put(names[i], userJSONObj.getString(names[i]));
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 131 */             String infoString = jo.getString("info");
/* 132 */             JSONArray infoJSONArray = null;
/*     */ 
/* 134 */             if ((infoString != null) && (!infoString.equals("")) && (!infoString.equals("null")))
/*     */             {
/* 138 */               infoJSONArray = jo.getJSONArray("info");
/* 139 */               int i = 0; for (int j = infoJSONArray.length(); i < j; i++) {
/* 140 */                 JSONObject sInfo = infoJSONArray.getJSONObject(i);
/* 141 */                 String imageURL = "";
/*     */                 try {
/* 143 */                   String imageString = sInfo.getString("image");
/* 144 */                   if ((imageString != null) && (!imageString.equals("null")))
/*     */                   {
/* 148 */                     Matcher m = Pattern.compile("\\[\"(\\S+)\"\\]").matcher(imageString);
/*     */ 
/* 151 */                     if (m.find())
/* 152 */                       imageURL = m.group(1);
/*     */                     else {
/* 154 */                       imageURL = "";
/*     */                     }
/*     */                   }
/*     */                 }
/*     */                 catch (Exception e4)
/*     */                 {
/*     */                 }
/* 161 */                 JSONObject mjo = null;
/* 162 */                 String mjoString = null;
/* 163 */                 String musicAuthorString = "";
/* 164 */                 String musicUrlString = "";
/* 165 */                 String musicTitleString = "";
/*     */                 try {
/* 167 */                   mjoString = sInfo.getString("music");
/*     */                 } catch (Exception e1) {
/* 169 */                   mjo = sInfo.getJSONObject("music");
/* 170 */                   musicAuthorString = mjo.getString("author");
/* 171 */                   musicUrlString = mjo.getString("url");
/* 172 */                   musicTitleString = mjo.getString("title");
/*     */                 }
/* 174 */                 if ((mjoString == null) || (mjoString.equals("null"))) {
/* 175 */                   musicAuthorString = "";
/* 176 */                   musicUrlString = "";
/* 177 */                   musicTitleString = "";
/*     */                 } else {
/*     */                   try {
/* 180 */                     mjo = sInfo.getJSONObject("music");
/* 181 */                     musicAuthorString = mjo.getString("author");
/* 182 */                     musicUrlString = mjo.getString("url");
/* 183 */                     musicTitleString = mjo.getString("title");
/*     */                   }
/*     */                   catch (Exception e2)
/*     */                   {
/*     */                   }
/*     */                 }
/*     */ 
/* 190 */                 String videoString = null;
/* 191 */                 JSONObject vjo = null;
/* 192 */                 String picurl = ""; String player = ""; String realurl = ""; String shorturl = ""; String title = "";
/*     */                 try {
/* 194 */                   videoString = sInfo.getString("video");
/*     */                 } catch (Exception e3) {
/* 196 */                   vjo = sInfo.getJSONObject("video");
/* 197 */                   picurl = vjo.getString("picurl");
/* 198 */                   player = vjo.getString("player");
/* 199 */                   realurl = vjo.getString("realurl");
/* 200 */                   shorturl = vjo.getString("shorturl");
/* 201 */                   title = vjo.getString("title");
/*     */                 }
/* 203 */                 if ((null != videoString) && (!videoString.equals("null")))
/*     */                 {
/* 207 */                   vjo = sInfo.getJSONObject("video");
/* 208 */                   picurl = vjo.getString("picurl");
/* 209 */                   player = vjo.getString("player");
/* 210 */                   realurl = vjo.getString("realurl");
/* 211 */                   shorturl = vjo.getString("shorturl");
/* 212 */                   title = vjo.getString("title");
/*     */                 }
/*     */ 
/* 219 */                 TweetSourceBean tsb = null;
/* 220 */                 String sourceInfoString = sInfo.getString("source");
/* 221 */                 if ((sourceInfoString == null) || (sourceInfoString.equals("")) || (sourceInfoString.equals("null")))
/*     */                 {
/* 223 */                   tsb = new TweetSourceBean();
/*     */                 } else {
/* 225 */                   JSONObject joSource = sInfo.getJSONObject("source");
/*     */ 
/* 228 */                   String sourceImageURL = "";
/*     */                   try {
/* 230 */                     String imageString = joSource.getString("image");
/* 231 */                     if ((imageString != null) && (!imageString.equals("null")))
/*     */                     {
/* 235 */                       Matcher m = Pattern.compile("\\[\"(\\S+)\"\\]").matcher(imageString);
/*     */ 
/* 238 */                       if (m.find())
/* 239 */                         sourceImageURL = m.group(1);
/*     */                       else {
/* 241 */                         sourceImageURL = "";
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                   catch (Exception e4)
/*     */                   {
/*     */                   }
/* 248 */                   JSONObject sourcemjo = null;
/* 249 */                   String sourcemjoString = null;
/* 250 */                   String sourcemusicAuthorString = "";
/* 251 */                   String sourcemusicUrlString = "";
/* 252 */                   String sourcemusicTitleString = "";
/*     */                   try {
/* 254 */                     sourcemjoString = joSource.getString("music");
/*     */                   } catch (Exception e1) {
/* 256 */                     sourcemjo = joSource.getJSONObject("music");
/* 257 */                     sourcemusicAuthorString = mjo.getString("author");
/* 258 */                     sourcemusicUrlString = mjo.getString("url");
/* 259 */                     sourcemusicTitleString = mjo.getString("title");
/*     */                   }
/* 261 */                   if ((sourcemjoString == null) || (sourcemjoString.equals("null"))) {
/* 262 */                     sourcemusicAuthorString = "";
/* 263 */                     sourcemusicUrlString = "";
/* 264 */                     sourcemusicTitleString = "";
/*     */                   } else {
/*     */                     try {
/* 267 */                       sourcemjo = joSource.getJSONObject("music");
/* 268 */                       sourcemusicAuthorString = sourcemjo.getString("author");
/* 269 */                       sourcemusicUrlString = sourcemjo.getString("url");
/* 270 */                       sourcemusicTitleString = sourcemjo.getString("title");
/*     */                     }
/*     */                     catch (Exception e2)
/*     */                     {
/*     */                     }
/*     */                   }
/*     */ 
/* 277 */                   String sourcevideoString = null;
/* 278 */                   JSONObject sourcevjo = null;
/* 279 */                   String sourcepicurl = ""; String sourceplayer = ""; String sourcerealurl = ""; String sourceshorturl = ""; String sourcetitle = "";
/*     */                   try {
/* 281 */                     sourcevideoString = joSource.getString("video");
/*     */                   } catch (Exception e3) {
/* 283 */                     sourcevjo = joSource.getJSONObject("video");
/* 284 */                     sourcepicurl = sourcevjo.getString("picurl");
/* 285 */                     sourceplayer = sourcevjo.getString("player");
/* 286 */                     sourcerealurl = sourcevjo.getString("realurl");
/* 287 */                     sourceshorturl = sourcevjo.getString("shorturl");
/* 288 */                     sourcetitle = sourcevjo.getString("title");
/*     */                   }
/* 290 */                   if ((null != sourcevideoString) && (!sourcevideoString.equals("null")))
/*     */                   {
/* 294 */                     sourcevjo = joSource.getJSONObject("video");
/* 295 */                     sourcepicurl = sourcevjo.getString("picurl");
/* 296 */                     sourceplayer = sourcevjo.getString("player");
/* 297 */                     sourcerealurl = sourcevjo.getString("realurl");
/* 298 */                     sourceshorturl = sourcevjo.getString("shorturl");
/* 299 */                     sourcetitle = sourcevjo.getString("title");
/*     */                   }
/*     */ 
/* 307 */                   tsb = new TweetSourceBean(joSource.getString("city_code"), joSource.getInt("count"), joSource.getString("country_code"), joSource.getString("from"), joSource.getString("fromurl"), joSource.getString("geo"), new Avatar(joSource.getString("head")), joSource.getString("id"), sourceImageURL, joSource.getInt("isvip") == 1, joSource.getInt("jing") == 1, (float)joSource.getLong("latitude"), joSource.getString("location"), (float)joSource.getLong("longitude"), joSource.getInt("mcount"), new Music(sourcemusicAuthorString, sourcemusicUrlString, sourcemusicTitleString), joSource.getString("name"), joSource.getString("nick"), joSource.getString("openid"), joSource.getString("origtext"), joSource.getString("province_code"), joSource.getInt("self") == 1, joSource.getInt("status"), joSource.getString("text"), joSource.getLong("timestamp"), joSource.getInt("type"), new Video(sourcepicurl, sourceplayer, sourcerealurl, sourceshorturl, sourcetitle));
/*     */                 }
/*     */ 
/* 341 */                 this.sris.add(new SingleRepostInfo(sInfo.getString("city_code"), sInfo.getInt("count"), sInfo.getString("country_code"), sInfo.getString("emotiontype"), sInfo.getString("emotionurl"), sInfo.getString("from"), sInfo.getString("fromurl"), sInfo.getString("geo"), new Avatar(sInfo.getString("head")), sInfo.getString("id"), imageURL, sInfo.getInt("isrealname") == 1, sInfo.getInt("isvip") == 1, sInfo.getInt("jing") == 1, (float)sInfo.getLong("latitude"), (float)sInfo.getLong("longitude"), sInfo.getString("location"), sInfo.getInt("mcount"), new Music(musicAuthorString, musicUrlString, musicTitleString), sInfo.getString("name"), sInfo.getString("nick"), sInfo.getString("openid"), sInfo.getString("origtext"), sInfo.getString("province_code"), sInfo.getInt("self") == 1, tsb, sInfo.getInt("status"), sInfo.getString("text"), sInfo.getString("timestamp"), sInfo.getInt("type"), new Video(picurl, player, realurl, shorturl, title)));
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (JSONException jsone)
/*     */       {
/* 385 */         throw new QQConnectException(jsone.getMessage() + ":" + json.toString(), jsone);
/*     */       }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 393 */     return "RepostInfoBean{ret=" + this.ret + ", errcode=" + this.errcode + ", msg='" + this.msg + '\'' + ", timeStamp=" + this.timeStamp + ", sris=" + this.sris + ", users=" + this.users + ", next=" + this.next + '}';
/*     */   }
/*     */ }

