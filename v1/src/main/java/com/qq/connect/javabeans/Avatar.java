/*    */ package com.qq.connect.javabeans;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Avatar
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -8402565179459840811L;
/* 13 */   private String avatarURL30 = ""; private String avatarURL50 = ""; private String avatarURL100 = "";
/*    */ 
/*    */   public Avatar(String avatarURL)
/*    */   {
/* 19 */     if (avatarURL.equals("")) {
/* 20 */       return;
/*    */     }
/* 22 */     this.avatarURL30 = (avatarURL + "/30");
/* 23 */     this.avatarURL50 = (avatarURL + "/50");
/* 24 */     this.avatarURL100 = (avatarURL + "/100");
/*    */   }
/*    */ 
/*    */   public Avatar(String avatarURL30, String avatarURL50, String avatarURL100) {
/* 28 */     this.avatarURL30 = avatarURL30;
/* 29 */     this.avatarURL50 = avatarURL50;
/* 30 */     this.avatarURL100 = avatarURL100;
/*    */   }
/*    */ 
/*    */   public String getAvatarURL30()
/*    */   {
/* 38 */     return this.avatarURL30;
/*    */   }
/*    */ 
/*    */   public String getAvatarURL50()
/*    */   {
/* 46 */     return this.avatarURL50;
/*    */   }
/*    */ 
/*    */   public String getAvatarURL100()
/*    */   {
/* 55 */     return this.avatarURL100;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 60 */     int prime = 31;
/* 61 */     int result = 1;
/* 62 */     result = 31 * result + (this.avatarURL30 == null ? 0 : this.avatarURL30.hashCode());
/* 63 */     return result;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 68 */     if (this == obj)
/* 69 */       return true;
/* 70 */     if (obj == null)
/* 71 */       return false;
/* 72 */     if (getClass() != obj.getClass())
/* 73 */       return false;
/* 74 */     Avatar other = (Avatar)obj;
/* 75 */     if (getAvatarURL30() == null) {
/* 76 */       if (other.getAvatarURL30() != null)
/* 77 */         return false;
/* 78 */     } else if (!getAvatarURL30().equals(other.getAvatarURL30()))
/* 79 */       return false;
/* 80 */     return true;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 85 */     return "AvatarInfo [figureurl30 : " + getAvatarURL30() + " , " + "figureurl50 : " + getAvatarURL50() + " , " + "figureurl100 : " + getAvatarURL100() + " , " + "]";
/*    */   }
/*    */ }

