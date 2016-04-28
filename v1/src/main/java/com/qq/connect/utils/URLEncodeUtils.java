/*     */ package com.qq.connect.utils;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.BitSet;
/*     */ 
/*     */ public class URLEncodeUtils
/*     */ {
/*  47 */   static BitSet dontNeedEncoding = new BitSet(256);
/*     */ 
/*     */   public static final boolean isURLEncoded(String str)
/*     */   {
/*  78 */     if ((str == null) || ("".equals(str))) {
/*  79 */       return false;
/*     */     }
/*  81 */     char[] chars = str.toCharArray();
/*  82 */     boolean containsPercent = false;
/*  83 */     for (char c : chars) {
/*  84 */       if (Character.isWhitespace(c)) {
/*  85 */         return false;
/*     */       }
/*  87 */       if (!dontNeedEncoding.get(c)) {
/*  88 */         return false;
/*     */       }
/*  90 */       if (c == '%') {
/*  91 */         containsPercent = true;
/*     */       }
/*     */     }
/*  94 */     if (!containsPercent) {
/*  95 */       return false;
/*     */     }
/*  97 */     return true;
/*     */   }
/*     */ 
/*     */   public static final String encodeURL(String str)
/*     */   {
/*     */     try
/*     */     {
/* 108 */       return URLEncoder.encode(str, "utf-8");
/*     */     } catch (UnsupportedEncodingException e) {
/* 110 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final String decodeURL(String str)
/*     */   {
/*     */     try
/*     */     {
/* 121 */       return URLDecoder.decode(str, "utf-8");
/*     */     } catch (UnsupportedEncodingException e) {
/* 123 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  49 */     for (int i = 97; i <= 122; i++) {
/*  50 */       dontNeedEncoding.set(i);
/*     */     }
/*  52 */     for (int i = 65; i <= 90; i++) {
/*  53 */       dontNeedEncoding.set(i);
/*     */     }
/*  55 */     for (int i = 48; i <= 57; i++) {
/*  56 */       dontNeedEncoding.set(i);
/*     */     }
/*  58 */     dontNeedEncoding.set(32);
/*     */ 
/*  62 */     dontNeedEncoding.set(45);
/*  63 */     dontNeedEncoding.set(95);
/*  64 */     dontNeedEncoding.set(46);
/*  65 */     dontNeedEncoding.set(42);
/*     */ 
/*  67 */     dontNeedEncoding.set(43);
/*  68 */     dontNeedEncoding.set(37);
/*     */   }
/*     */ }

