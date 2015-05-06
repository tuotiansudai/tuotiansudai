/*     */ package com.qq.connect.utils.http;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.List;
/*     */ 
/*     */ public class PostParameter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -3204490050091778041L;
/*     */   String name;
/*     */   String value;
/*  21 */   private File file = null;
/*     */   private static final String JPEG = "image/jpeg";
/*     */   private static final String GIF = "image/gif";
/*     */   private static final String PNG = "image/png";
/*     */   private static final String OCTET = "application/octet-stream";
/*     */ 
/*     */   public PostParameter(String name, String value)
/*     */   {
/*  25 */     this.name = name;
/*  26 */     this.value = value;
/*     */   }
/*     */ 
/*     */   public PostParameter(String name, double value) {
/*  30 */     this.name = name;
/*  31 */     this.value = String.valueOf(value);
/*     */   }
/*     */ 
/*     */   public PostParameter(String name, int value) {
/*  35 */     this.name = name;
/*  36 */     this.value = String.valueOf(value);
/*     */   }
/*     */ 
/*     */   public PostParameter(String name, File file) {
/*  40 */     this.name = name;
/*  41 */     this.file = file;
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  45 */     return this.name;
/*     */   }
/*     */   public String getValue() {
/*  48 */     return this.value;
/*     */   }
/*     */ 
/*     */   public File getFile() {
/*  52 */     return this.file;
/*     */   }
/*     */ 
/*     */   public boolean isFile() {
/*  56 */     return null != this.file;
/*     */   }
/*     */ 
/*     */   public String getContentType()
/*     */   {
/*  69 */     if (!isFile()) {
/*  70 */       throw new IllegalStateException("not a file");
/*     */     }
/*     */ 
/*  73 */     String extensions = this.file.getName();
/*  74 */     int index = extensions.lastIndexOf(".");
/*     */     String contentType;
/*  75 */     if (-1 == index)
/*     */     {
/*  77 */       contentType = "application/octet-stream";
/*     */     } else {
/*  79 */       extensions = extensions.substring(extensions.lastIndexOf(".") + 1).toLowerCase();
/*  80 */       if (extensions.length() == 3)
/*     */       {
/*  81 */         if ("gif".equals(extensions)) {
/*  82 */           contentType = "image/gif";
/*     */         }
/*     */         else
/*     */         {
/*  83 */           if ("png".equals(extensions)) {
/*  84 */             contentType = "image/png";
/*     */           }
/*     */           else
/*     */           {
/*  85 */             if ("jpg".equals(extensions))
/*  86 */               contentType = "image/jpeg";
/*     */             else
/*  88 */               contentType = "application/octet-stream";
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  90 */         if (extensions.length() == 4)
/*     */         {
/*  91 */           if ("jpeg".equals(extensions))
/*  92 */             contentType = "image/jpeg";
/*     */           else
/*  94 */             contentType = "application/octet-stream";
/*     */         }
/*     */         else {
/*  97 */           contentType = "application/octet-stream";
/*     */         }
/*     */       }
/*     */     }
/* 100 */     return contentType;
/*     */   }
/*     */ 
/*     */   public static boolean containsFile(PostParameter[] params)
/*     */   {
/* 105 */     boolean containsFile = false;
/* 106 */     if (null == params) {
/* 107 */       return false;
/*     */     }
/* 109 */     for (PostParameter param : params) {
/* 110 */       if (param.isFile()) {
/* 111 */         containsFile = true;
/* 112 */         break;
/*     */       }
/*     */     }
/* 115 */     return containsFile;
/*     */   }
/*     */   static boolean containsFile(List<PostParameter> params) {
/* 118 */     boolean containsFile = false;
/* 119 */     for (PostParameter param : params) {
/* 120 */       if (param.isFile()) {
/* 121 */         containsFile = true;
/* 122 */         break;
/*     */       }
/*     */     }
/* 125 */     return containsFile;
/*     */   }
/*     */ 
/*     */   public static PostParameter[] getParameterArray(String name, String value) {
/* 129 */     return new PostParameter[] { new PostParameter(name, value) };
/*     */   }
/*     */   public static PostParameter[] getParameterArray(String name, int value) {
/* 132 */     return getParameterArray(name, String.valueOf(value));
/*     */   }
/*     */ 
/*     */   public static PostParameter[] getParameterArray(String name1, String value1, String name2, String value2)
/*     */   {
/* 137 */     return new PostParameter[] { new PostParameter(name1, value1), new PostParameter(name2, value2) };
/*     */   }
/*     */ 
/*     */   public static PostParameter[] getParameterArray(String name1, int value1, String name2, int value2)
/*     */   {
/* 142 */     return getParameterArray(name1, String.valueOf(value1), name2, String.valueOf(value2));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 147 */     int result = this.name.hashCode();
/* 148 */     result = 31 * result + this.value.hashCode();
/* 149 */     result = 31 * result + (this.file != null ? this.file.hashCode() : 0);
/* 150 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj) {
/* 154 */     if (null == obj) {
/* 155 */       return false;
/*     */     }
/* 157 */     if (this == obj) {
/* 158 */       return true;
/*     */     }
/* 160 */     if ((obj instanceof PostParameter)) {
/* 161 */       PostParameter that = (PostParameter)obj;
/*     */ 
/* 163 */       if (this.file != null ? !this.file.equals(that.file) : that.file != null) {
/* 164 */         return false;
/*     */       }
/* 166 */       return (this.name.equals(that.name)) && (this.value.equals(that.value));
/*     */     }
/*     */ 
/* 169 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 174 */     return "PostParameter{name='" + this.name + '\'' + ", value='" + this.value + '\'' + ", file=" + this.file + '}';
/*     */   }
/*     */ 
/*     */   public int compareTo(Object o)
/*     */   {
/* 183 */     PostParameter that = (PostParameter)o;
/* 184 */     int compared = this.name.compareTo(that.name);
/* 185 */     if (0 == compared) {
/* 186 */       compared = this.value.compareTo(that.value);
/*     */     }
/* 188 */     return compared;
/*     */   }
/*     */ 
/*     */   public static String encodeParameters(PostParameter[] httpParams) {
/* 192 */     if (null == httpParams) {
/* 193 */       return "";
/*     */     }
/* 195 */     StringBuffer buf = new StringBuffer();
/* 196 */     for (int j = 0; j < httpParams.length; j++) {
/* 197 */       if (httpParams[j].isFile()) {
/* 198 */         throw new IllegalArgumentException("parameter [" + httpParams[j].name + "]should be text");
/*     */       }
/* 200 */       if (j != 0)
/* 201 */         buf.append("&");
/*     */       try
/*     */       {
/* 204 */         buf.append(URLEncoder.encode(httpParams[j].name, "UTF-8")).append("=").append(URLEncoder.encode(httpParams[j].value, "UTF-8"));
/*     */       }
/*     */       catch (UnsupportedEncodingException neverHappen) {
/*     */       }
/*     */     }
/* 209 */     return buf.toString();
/*     */   }
/*     */ }

