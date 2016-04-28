/*    */ package com.qq.connect.javabeans.weibo;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Education
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -7543591029987598302L;
/* 13 */   private String id = "";
/*    */ 
/* 75 */   private String year = "";
/* 76 */   private String schoolID = "";
/* 77 */   private String departmentID = "";
/* 78 */   private String level = "";
/*    */ 
/*    */   public String getID()
/*    */   {
/* 20 */     return this.id;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 25 */     return "Education{id='" + this.id + '\'' + ", year='" + this.year + '\'' + ", schoolID='" + this.schoolID + '\'' + ", departmentID='" + this.departmentID + '\'' + ", level='" + this.level + '\'' + '}';
/*    */   }
/*    */ 
/*    */   public Education(String id, String year, String schoolID, String departmentID, String level)
/*    */   {
/* 37 */     this.id = id;
/* 38 */     this.year = year;
/* 39 */     this.schoolID = schoolID;
/* 40 */     this.departmentID = departmentID;
/* 41 */     this.level = level;
/*    */   }
/*    */ 
/*    */   public String getYear()
/*    */   {
/* 49 */     return this.year;
/*    */   }
/*    */ 
/*    */   public String getSchoolID()
/*    */   {
/* 57 */     return this.schoolID;
/*    */   }
/*    */ 
/*    */   public String getDepartmentID()
/*    */   {
/* 65 */     return this.departmentID;
/*    */   }
/*    */ 
/*    */   public String getLevel()
/*    */   {
/* 73 */     return this.level;
/*    */   }
/*    */ }

