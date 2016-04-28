/*    */ package com.qq.connect.utils;
/*    */ 
/*    */ class Range
/*    */ {
/*    */   public static int[] range(int n)
/*    */   {
/* 15 */     int[] result = new int[n];
/* 16 */     for (int i = 0; i < n; i++) {
/* 17 */       result[i] = i;
/*    */     }
/* 19 */     return result;
/*    */   }
/*    */ 
/*    */   public static int[] range(int start, int end)
/*    */   {
/* 29 */     int sz = end - start;
/* 30 */     int[] result = new int[sz];
/* 31 */     for (int i = 0; i < sz; i++) {
/* 32 */       result[i] = (start + i);
/*    */     }
/* 34 */     return result;
/*    */   }
/*    */ 
/*    */   public static int[] range(int start, int end, int step)
/*    */   {
/* 45 */     int sz = (end - start) / step;
/* 46 */     int[] result = new int[sz];
/* 47 */     for (int i = 0; i < sz; i++) {
/* 48 */       result[i] = (start + i * step);
/*    */     }
/* 50 */     return result;
/*    */   }
/*    */ }

