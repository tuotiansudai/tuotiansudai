/*    */ package com.qq.connect.utils;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class Print
/*    */ {
/*    */   public static void print(Object obj)
/*    */   {
/* 16 */     System.out.println(obj);
/*    */   }
/*    */ 
/*    */   public static void print()
/*    */   {
/* 22 */     System.out.println();
/*    */   }
/*    */ 
/*    */   public static void printnl(Object obj)
/*    */   {
/* 30 */     System.out.print(obj);
/*    */   }
/*    */ 
/*    */   public static PrintStream printf(String format, Object[] objects)
/*    */   {
/* 40 */     return System.out.printf(format, objects);
/*    */   }
/*    */ }

