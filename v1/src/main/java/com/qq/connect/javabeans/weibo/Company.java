/*    */ package com.qq.connect.javabeans.weibo;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Company
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -8645673045632578193L;
/* 22 */   private String beginYear = "";
/*    */ 
/* 74 */   private String companyName = "";
/* 75 */   private String departmentName = "";
/* 76 */   private String endYear = "";
/* 77 */   private String id = "";
/*    */ 
/*    */   public Company(String beginYear, String companyName, String departmentName, String endYear, String id)
/*    */   {
/* 16 */     this.beginYear = beginYear;
/* 17 */     this.companyName = companyName;
/* 18 */     this.departmentName = departmentName;
/* 19 */     this.endYear = endYear;
/* 20 */     this.id = id;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 26 */     return "Company{beginYear='" + this.beginYear + '\'' + ", companyName='" + this.companyName + '\'' + ", departmentName='" + this.departmentName + '\'' + ", endYear='" + this.endYear + '\'' + ", id='" + this.id + '\'' + '}';
/*    */   }
/*    */ 
/*    */   public String getBeginYear()
/*    */   {
/* 40 */     return this.beginYear;
/*    */   }
/*    */ 
/*    */   public String getCompanyName()
/*    */   {
/* 48 */     return this.companyName;
/*    */   }
/*    */ 
/*    */   public String getDepartmentName()
/*    */   {
/* 56 */     return this.departmentName;
/*    */   }
/*    */ 
/*    */   public String getEndYear()
/*    */   {
/* 64 */     return this.endYear;
/*    */   }
/*    */ 
/*    */   public String getID()
/*    */   {
/* 72 */     return this.id;
/*    */   }
/*    */ }

