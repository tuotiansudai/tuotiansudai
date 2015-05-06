/*    */ package com.qq.connect.javabeans.weibo;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class PrivateFlag
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 12 */   private int privateFlag = 0;
/*    */ 
/*    */   public PrivateFlag(int i) {
/* 15 */     this.privateFlag = i;
/*    */   }
/*    */ 
/*    */   public int getPrivateFlag()
/*    */   {
/* 23 */     return this.privateFlag;
/*    */   }
/*    */ 
/*    */   public String getPrivateDesc()
/*    */   {
/* 31 */     switch (this.privateFlag) {
/*    */     case 0:
/* 33 */       return "仅有偶像";
/*    */     case 1:
/* 35 */       return "名人,听众";
/*    */     case 2:
/* 37 */       return "所有人";
/*    */     }
/* 39 */     return "";
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 45 */     return "PrivateFlag{privateFlag=" + this.privateFlag + '}';
/*    */   }
/*    */ }

