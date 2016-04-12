/*     */ package com.qq.connect.javabeans.qzone;
/*     */ 
/*     */ import com.qq.connect.QQConnectException;
/*     */ import com.qq.connect.QQConnectResponse;
/*     */ import com.qq.connect.javabeans.Avatar;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class UserInfoBean extends QQConnectResponse
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5606709876246698659L;
/*  21 */   private Avatar avatar = new Avatar("");
/*     */   private String nickname;
/*     */   private String gender;
/*     */   private boolean vip;
/*     */   private int level;
/*     */   private boolean yellowYearVip;
/*     */   private int ret;
/*     */   private String msg;
/*     */ 
/*     */   public String getNickname()
/*     */   {
/*  30 */     return this.nickname;
/*     */   }
/*     */ 
/*     */   public String getGender()
/*     */   {
/*  38 */     return this.gender;
/*     */   }
/*     */ 
/*     */   public boolean isVip()
/*     */   {
/*  46 */     return this.vip;
/*     */   }
/*     */ 
/*     */   public Avatar getAvatar()
/*     */   {
/*  51 */     return this.avatar;
/*     */   }
/*     */ 
/*     */   public int getLevel()
/*     */   {
/*  59 */     return this.level;
/*     */   }
/*     */ 
/*     */   public void setLevel(int level) {
/*  63 */     this.level = level;
/*     */   }
/*     */ 
/*     */   public boolean isYellowYearVip()
/*     */   {
/*  71 */     return this.yellowYearVip;
/*     */   }
/*     */ 
/*     */   public int getRet()
/*     */   {
/*  89 */     return this.ret;
/*     */   }
/*     */ 
/*     */   public String getMsg()
/*     */   {
/*  97 */     return this.msg;
/*     */   }
/*     */ 
/*     */   public UserInfoBean(JSONObject json)
/*     */     throws QQConnectException
/*     */   {
/* 103 */     init(json);
/*     */   }
/*     */ 
/*     */   private void init(JSONObject json) throws QQConnectException {
/* 107 */     if (json != null)
/*     */       try {
/* 109 */         this.ret = json.getInt("ret");
/* 110 */         if (0 != this.ret)
/*     */         {
/* 112 */           this.msg = json.getString("msg");
/*     */         }
/*     */         else {
/* 115 */           this.msg = "";
/* 116 */           this.nickname = json.getString("nickname");
/* 117 */           this.gender = json.getString("gender");
/* 118 */           this.vip = (json.getInt("vip") == 1);
/* 119 */           this.avatar = new Avatar(json.getString("figureurl"), json.getString("figureurl_1"), json.getString("figureurl_2"));
/* 120 */           this.level = json.getInt("level");
/* 121 */           this.yellowYearVip = (json.getInt("is_yellow_year_vip") == 1);
/*     */         }
/*     */       }
/*     */       catch (JSONException jsone) {
/* 125 */         throw new QQConnectException(jsone.getMessage() + ":" + json.toString(), jsone);
/*     */       }
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 136 */     int prime = 31;
/* 137 */     int result = 1;
/* 138 */     result = 31 * result + (this.nickname == null ? 0 : this.nickname.hashCode());
/* 139 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 144 */     if (this == obj)
/* 145 */       return true;
/* 146 */     if (obj == null)
/* 147 */       return false;
/* 148 */     if (getClass() != obj.getClass())
/* 149 */       return false;
/* 150 */     UserInfoBean other = (UserInfoBean)obj;
/* 151 */     if (this.nickname == null) {
/* 152 */       if (other.nickname != null)
/* 153 */         return false;
/* 154 */     } else if (!this.nickname.equals(other.nickname))
/* 155 */       return false;
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 161 */     return "UserInfo [nickname : " + this.nickname + " , " + "figureurl30 : " + this.avatar.getAvatarURL30() + " , " + "figureurl50 : " + this.avatar.getAvatarURL50() + " , " + "figureurl100 : " + this.avatar.getAvatarURL100() + " , " + "gender : " + this.gender + " , " + "vip : " + this.vip + " , " + "level : " + this.level + " , " + "isYellowYeaarVip : " + this.yellowYearVip + "]";
/*     */   }
/*     */ }

