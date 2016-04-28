/*    */ package com.qq.connect.javabeans.weibo;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Tag
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 8844167881360046175L;
/* 13 */   private String id = "";
/* 14 */   private String name = "";
/*    */ 
/*    */   public String toString()
/*    */   {
/* 18 */     return "Tag{id='" + this.id + '\'' + ", name='" + this.name + '\'' + '}';
/*    */   }
/*    */ 
/*    */   public String getID()
/*    */   {
/* 29 */     return this.id;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 37 */     return this.name;
/*    */   }
/*    */ 
/*    */   public Tag(String id, String name)
/*    */   {
/* 42 */     this.id = id;
/* 43 */     this.name = name;
/*    */   }
/*    */ }

