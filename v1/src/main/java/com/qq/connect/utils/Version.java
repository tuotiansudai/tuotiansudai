/*    */ package com.qq.connect.utils;
/*    */ 
/*    */ public class Version
/*    */ {
/* 10 */   private static String VERSION = "2.0.0.0";
/*    */   private static final String TITLE = "qq_connect_sdk";
/*    */ 
/*    */   public static String getVersion()
/*    */   {
/* 25 */     return VERSION;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 29 */     return "qq_connect_sdk " + VERSION;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 15 */       VERSION = QQConnectConfig.getValue("version");
/*    */     } catch (Exception e) {
/* 17 */       VERSION = "2.0.0.0";
/*    */     }
/* 19 */     if (VERSION.equals(""))
/* 20 */       VERSION = "2.0.0.0";
/*    */   }
/*    */ }

