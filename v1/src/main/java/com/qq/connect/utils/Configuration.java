/*     */ package com.qq.connect.utils;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public class Configuration
/*     */ {
/*     */   private static Properties defaultProperty;
/*     */   private static boolean DALVIK;
/*     */ 
/*     */   static void init()
/*     */   {
/*  24 */     defaultProperty = new Properties();
/*  25 */     defaultProperty.setProperty("qqConnect.debug", "false");
/*  26 */     defaultProperty.setProperty("qqConnect.clientURL", "https://graph.qq.com/user/get_user_info");
/*  27 */     defaultProperty.setProperty("qqConnect.http.useSSL", "false");
/*  28 */     defaultProperty.setProperty("qqConnect.http.connectionTimeout", "20000");
/*  29 */     defaultProperty.setProperty("qqConnect.http.readTimeout", "120000");
/*  30 */     defaultProperty.setProperty("qqConnect.http.retryCount", "3");
/*  31 */     defaultProperty.setProperty("qqConnect.http.retryIntervalSecs", "10");
/*  32 */     defaultProperty.setProperty("qqConnect.async.numThreads", "1");
/*  33 */     defaultProperty.setProperty("qqConnect.clientVersion", Version.getVersion());
/*     */     try {
/*  35 */       Class.forName("dalvik.system.VMRuntime");
/*  36 */       defaultProperty.setProperty("qqConnect.dalvik", "true");
/*     */     } catch (ClassNotFoundException cnfe) {
/*  38 */       defaultProperty.setProperty("qqConnect.dalvik", "false");
/*     */     }
/*  40 */     DALVIK = getBoolean("qqConnect.dalvik");
/*  41 */     String qqConnectProps = "qqconnectconfig.properties";
/*  42 */     boolean loaded = (loadProperties(defaultProperty, "." + File.separatorChar + qqConnectProps)) || (loadProperties(defaultProperty, Configuration.class.getResourceAsStream("/WEB-INF/" + qqConnectProps))) || (loadProperties(defaultProperty, Configuration.class.getResourceAsStream("/" + qqConnectProps)));
/*     */   }
/*     */ 
/*     */   private static boolean loadProperties(Properties props, String path)
/*     */   {
/*     */     try
/*     */     {
/*  49 */       File file = new File(path);
/*  50 */       if ((file.exists()) && (file.isFile())) {
/*  51 */         props.load(new FileInputStream(file));
/*  52 */         return true;
/*     */       }
/*     */     } catch (Exception ignore) {
/*     */     }
/*  56 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean loadProperties(Properties props, InputStream is) {
/*     */     try {
/*  61 */       props.load(is);
/*  62 */       return true;
/*     */     } catch (Exception ignore) {
/*     */     }
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isDalvik()
/*     */   {
/*  72 */     return DALVIK;
/*     */   }
/*     */ 
/*     */   public static boolean useSSL() {
/*  76 */     return getBoolean("qqConnect.http.useSSL");
/*     */   }
/*     */   public static String getScheme() {
/*  79 */     return useSSL() ? "https://" : "http://";
/*     */   }
/*     */ 
/*     */   public static String getCilentVersion() {
/*  83 */     return getProperty("qqConnect.clientVersion");
/*     */   }
/*     */ 
/*     */   public static String getClientURL() {
/*  87 */     return getProperty("qqConnect.clientURL");
/*     */   }
/*     */ 
/*     */   public static int getReadTimeout()
/*     */   {
/*  94 */     return getIntProperty("qqConnect.http.readTimeout");
/*     */   }
/*     */ 
/*     */   public static int getRetryCount()
/*     */   {
/* 100 */     return getIntProperty("qqConnect.http.retryCount");
/*     */   }
/*     */ 
/*     */   public static int getRetryIntervalSecs()
/*     */   {
/* 105 */     return getIntProperty("qqConnect.http.retryIntervalSecs");
/*     */   }
/*     */ 
/*     */   public static String getUser()
/*     */   {
/* 111 */     return getProperty("qqConnect.user");
/*     */   }
/*     */ 
/*     */   public static String getPassword()
/*     */   {
/* 116 */     return getProperty("qqConnect.password");
/*     */   }
/*     */ 
/*     */   public static String getUserAgent()
/*     */   {
/* 121 */     return getProperty("qqConnect.http.userAgent");
/*     */   }
/*     */ 
/*     */   public static String getOAuthConsumerKey()
/*     */   {
/* 127 */     return getProperty("qqConnect.oauth.consumerKey");
/*     */   }
/*     */ 
/*     */   public static String getOAuthConsumerSecret()
/*     */   {
/* 132 */     return getProperty("qqConnect.oauth.consumerSecret");
/*     */   }
/*     */ 
/*     */   public static boolean getBoolean(String name)
/*     */   {
/* 138 */     String value = getProperty(name);
/* 139 */     return Boolean.valueOf(value).booleanValue();
/*     */   }
/*     */ 
/*     */   public static int getIntProperty(String name) {
/* 143 */     String value = getProperty(name);
/*     */     try {
/* 145 */       return Integer.parseInt(value); } catch (NumberFormatException nfe) {
/*     */     }
/* 147 */     return -1;
/*     */   }
/*     */ 
/*     */   public static long getLongProperty(String name)
/*     */   {
/* 153 */     String value = getProperty(name);
/*     */     try {
/* 155 */       return Long.parseLong(value); } catch (NumberFormatException nfe) {
/*     */     }
/* 157 */     return -1L;
/*     */   }
/*     */ 
/*     */   public static String getProperty(String name)
/*     */   {
/* 162 */     return defaultProperty.getProperty(name);
/*     */   }
/*     */ 
/*     */   public static int getNumberOfAsyncThreads()
/*     */   {
/* 168 */     return getIntProperty("qqConnect.async.numThreads");
/*     */   }
/*     */ 
/*     */   public static boolean getDebug() {
/* 172 */     return getBoolean("qqConnect.debug");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  20 */     init();
/*     */   }
/*     */ }

