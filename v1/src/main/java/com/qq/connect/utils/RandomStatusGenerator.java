/*     */ package com.qq.connect.utils;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class RandomStatusGenerator
/*     */ {
/*  18 */   private static String valueBeforeMD5 = "";
/*  19 */   private static String valueAfterMD5 = "";
/*     */   private static Random myRand;
/*  30 */   private static SecureRandom mySecureRand = new SecureRandom();
/*     */   private static String s_id;
/*     */   private static final int PAD_BELOW = 16;
/*     */   private static final int TWO_BYTES = 255;
/*     */ 
/*     */   public RandomStatusGenerator()
/*     */   {
/*     */   }
/*     */ 
/*     */   public RandomStatusGenerator(boolean secure)
/*     */   {
/*     */   }
/*     */ 
/*     */   public static String getUniqueState()
/*     */   {
/*  56 */     if (valueAfterMD5.equals("")) {
/*  57 */       getRandomGUID(false);
/*     */     }
/*  59 */     return valueAfterMD5;
/*     */   }
/*     */ 
/*     */   private static void getRandomGUID(boolean secure) {
/*  63 */     MessageDigest md5 = null;
/*  64 */     StringBuffer sbValueBeforeMD5 = new StringBuffer(128);
/*     */     try
/*     */     {
/*  67 */       md5 = MessageDigest.getInstance("MD5");
/*     */     }
/*     */     catch (NoSuchAlgorithmException e)
/*     */     {
/*     */     }
/*     */     try {
/*  73 */       long time = System.currentTimeMillis();
/*  74 */       long rand = 0L;
/*     */ 
/*  76 */       if (secure)
/*  77 */         rand = mySecureRand.nextLong();
/*     */       else {
/*  79 */         rand = myRand.nextLong();
/*     */       }
/*  81 */       sbValueBeforeMD5.append(s_id);
/*  82 */       sbValueBeforeMD5.append(":");
/*  83 */       sbValueBeforeMD5.append(Long.toString(time));
/*  84 */       sbValueBeforeMD5.append(":");
/*  85 */       sbValueBeforeMD5.append(Long.toString(rand));
/*     */ 
/*  87 */       valueBeforeMD5 = sbValueBeforeMD5.toString();
/*  88 */       md5.update(valueBeforeMD5.getBytes());
/*     */ 
/*  90 */       byte[] array = md5.digest();
/*  91 */       StringBuffer sb = new StringBuffer(32);
/*  92 */       for (int j = 0; j < array.length; j++) {
/*  93 */         int b = array[j] & 0xFF;
/*  94 */         if (b < 16)
/*  95 */           sb.append('0');
/*  96 */         sb.append(Integer.toHexString(b));
/*     */       }
/*     */ 
/*  99 */       valueAfterMD5 = sb.toString();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 107 */     String raw = valueAfterMD5.toUpperCase();
/* 108 */     StringBuffer sb = new StringBuffer(64);
/* 109 */     sb.append(raw.substring(0, 8));
/* 110 */     sb.append("-");
/* 111 */     sb.append(raw.substring(8, 12));
/* 112 */     sb.append("-");
/* 113 */     sb.append(raw.substring(12, 16));
/* 114 */     sb.append("-");
/* 115 */     sb.append(raw.substring(16, 20));
/* 116 */     sb.append("-");
/* 117 */     sb.append(raw.substring(20));
/*     */ 
/* 119 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  31 */     long secureInitializer = mySecureRand.nextLong();
/*  32 */     myRand = new Random(secureInitializer);
/*     */     try {
/*  34 */       s_id = InetAddress.getLocalHost().toString();
/*     */     } catch (UnknownHostException e) {
/*  36 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }
