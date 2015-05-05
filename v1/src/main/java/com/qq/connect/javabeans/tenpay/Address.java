/*     */ package com.qq.connect.javabeans.tenpay;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class Address
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4742904673643859727L;
/*  11 */   private String regionID = "0";
/*  12 */   private int usedCount = 0;
/*  13 */   private String name = "";
/*  14 */   private String tel = "";
/*  15 */   private String zipcode = "";
/*  16 */   private String mobile = "";
/*  17 */   private String addrStreet = "";
/*  18 */   private String lastUseTime = "";
/*  19 */   private String modTime = "";
/*  20 */   private String createTime = "";
/*  21 */   private String index = "";
/*     */ 
/*     */   public Address(String regionID, int usedCount, String name, String tel, String zipcode, String mobile, String addrStreet, String lastUseTime, String modTime, String createTime, String index)
/*     */   {
/*  26 */     this.regionID = regionID;
/*  27 */     this.usedCount = usedCount;
/*  28 */     this.name = name;
/*  29 */     this.tel = tel;
/*  30 */     this.zipcode = zipcode;
/*  31 */     this.mobile = mobile;
/*  32 */     this.addrStreet = addrStreet;
/*  33 */     this.lastUseTime = lastUseTime;
/*  34 */     this.modTime = modTime;
/*  35 */     this.createTime = createTime;
/*  36 */     this.index = index;
/*     */   }
/*     */ 
/*     */   public Address()
/*     */   {
/*     */   }
/*     */ 
/*     */   public String getRegionID()
/*     */   {
/*  48 */     return this.regionID;
/*     */   }
/*     */ 
/*     */   public int getUsedCount()
/*     */   {
/*  56 */     return this.usedCount;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  64 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getTel()
/*     */   {
/*  72 */     return this.tel;
/*     */   }
/*     */ 
/*     */   public String getZipcode()
/*     */   {
/*  80 */     return this.zipcode;
/*     */   }
/*     */ 
/*     */   public String getMobile()
/*     */   {
/*  88 */     return this.mobile;
/*     */   }
/*     */ 
/*     */   public String getAddrStreet()
/*     */   {
/*  96 */     return this.addrStreet;
/*     */   }
/*     */ 
/*     */   public String getLastUseTime()
/*     */   {
/* 104 */     return this.lastUseTime;
/*     */   }
/*     */ 
/*     */   public String getModTime()
/*     */   {
/* 112 */     return this.modTime;
/*     */   }
/*     */ 
/*     */   public String getCreateTime()
/*     */   {
/* 120 */     return this.createTime;
/*     */   }
/*     */ 
/*     */   public String getIndex()
/*     */   {
/* 128 */     return this.index;
/*     */   }
/*     */ }

