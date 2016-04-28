/*    */ package com.qq.connect.utils;
/*    */ 
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class QQConnectConfig
/*    */ {
/* 18 */   private static Properties props = new Properties();
/*    */ 
/*    */   public static String getValue(String key)
/*    */   {
/* 35 */     return props.getProperty(key);
/*    */   }
/*    */ 
/*    */   public static void updateProperties(String key, String value)
/*    */   {
/* 44 */     props.setProperty(key, value);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 21 */       props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("qqconnectconfig.properties"));
/*    */     } catch (FileNotFoundException e) {
/* 23 */       e.printStackTrace();
/*    */     } catch (IOException e) {
/* 25 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

