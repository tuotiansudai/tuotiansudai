/*    */ package com.qq.connect.utils.http;
/*    */ 
/*    */ public class BASE64Encoder
/*    */ {
/*  8 */   private static final char last2byte = (char)Integer.parseInt("00000011", 2);
/*  9 */   private static final char last4byte = (char)Integer.parseInt("00001111", 2);
/* 10 */   private static final char last6byte = (char)Integer.parseInt("00111111", 2);
/* 11 */   private static final char lead6byte = (char)Integer.parseInt("11111100", 2);
/* 12 */   private static final char lead4byte = (char)Integer.parseInt("11110000", 2);
/* 13 */   private static final char lead2byte = (char)Integer.parseInt("11000000", 2);
/* 14 */   private static final char[] encodeTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*    */ 
/*    */   public static String encode(byte[] from)
/*    */   {
/* 20 */     StringBuffer to = new StringBuffer((int)(from.length * 1.34D) + 3);
/* 21 */     int num = 0;
/* 22 */     char currentByte = '\000';
/* 23 */     for (int i = 0; i < from.length; i++) {
/* 24 */       num %= 8;
/* 25 */       while (num < 8) {
/* 26 */         switch (num) {
/*    */         case 0:
/* 28 */           currentByte = (char)(from[i] & lead6byte);
/* 29 */           currentByte = (char)(currentByte >>> '\002');
/* 30 */           break;
/*    */         case 2:
/* 32 */           currentByte = (char)(from[i] & last6byte);
/* 33 */           break;
/*    */         case 4:
/* 35 */           currentByte = (char)(from[i] & last4byte);
/* 36 */           currentByte = (char)(currentByte << '\002');
/* 37 */           if (i + 1 < from.length)
/* 38 */             currentByte = (char)(currentByte | (from[(i + 1)] & lead2byte) >>> 6); break;
/*    */         case 6:
/* 42 */           currentByte = (char)(from[i] & last2byte);
/* 43 */           currentByte = (char)(currentByte << '\004');
/* 44 */           if (i + 1 < from.length)
/* 45 */             currentByte = (char)(currentByte | (from[(i + 1)] & lead4byte) >>> 4); break;
/*    */         case 1:
/*    */         case 3:
/*    */         case 5:
/* 49 */         }to.append(encodeTable[currentByte]);
/* 50 */         num += 6;
/*    */       }
/*    */     }
/* 53 */     if (to.length() % 4 != 0) {
/* 54 */       for (int i = 4 - to.length() % 4; i > 0; i--) {
/* 55 */         to.append("=");
/*    */       }
/*    */     }
/* 58 */     return to.toString();
/*    */   }
/*    */ }

