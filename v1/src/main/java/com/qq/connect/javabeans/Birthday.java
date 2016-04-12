/*    */ package com.qq.connect.javabeans;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Birthday
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 4794043997574076151L;
/*    */   private int year;
/*    */   private int month;
/*    */   private int day;
/*    */ 
/*    */   public int getYear()
/*    */   {
/* 17 */     return this.year;
/*    */   }
/*    */ 
/*    */   public Birthday(int year, int month, int day) {
/* 21 */     this.year = year;
/* 22 */     this.month = month;
/* 23 */     this.day = day;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 28 */     return "Birthday{year=" + this.year + ", month=" + this.month + ", day=" + this.day + '}';
/*    */   }
/*    */ 
/*    */   public int getMonth()
/*    */   {
/* 40 */     return this.month;
/*    */   }
/*    */ 
/*    */   public int getDay()
/*    */   {
/* 48 */     return this.day;
/*    */   }
/*    */ }

