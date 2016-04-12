/*     */ package com.qq.connect.utils.http;
/*     */ 
/*     */ import com.qq.connect.QQConnectException;
/*     */ import com.qq.connect.utils.Configuration;
/*     */ import org.json.JSONArray;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.util.Date;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class Response
/*     */ {
/*  35 */   private static final boolean DEBUG = Configuration.getDebug();
/*  36 */   static Logger log = Logger.getLogger(Response.class.getName());
/*     */ 
/*  39 */   private static ThreadLocal<DocumentBuilder> builders = new ThreadLocal()
/*     */   {
/*     */     protected DocumentBuilder initialValue()
/*     */     {
/*     */       try {
/*  44 */         return DocumentBuilderFactory.newInstance().newDocumentBuilder();
/*     */       }
/*     */       catch (ParserConfigurationException ex)
/*     */       {
/*  48 */         throw new ExceptionInInitializerError(ex);
/*     */       }
/*     */     }
/*  39 */   };
/*     */   private int statusCode;
/*  54 */   private Document responseAsDocument = null;
/*  55 */   private String responseAsString = null;
/*     */   private InputStream is;
/*     */   private HttpURLConnection con;
/*  58 */   private boolean streamConsumed = false;
/*     */ 
/* 208 */   private static Pattern escaped = Pattern.compile("&#([0-9]{3,5});");
/*     */ 
/*     */   public Response()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Response(HttpURLConnection con)
/*     */     throws IOException
/*     */   {
/*  64 */     this.con = con;
/*  65 */     this.statusCode = con.getResponseCode();
/*  66 */     if (null == (this.is = con.getErrorStream())) {
/*  67 */       this.is = con.getInputStream();
/*     */     }
/*  69 */     if ((null != this.is) && ("gzip".equals(con.getContentEncoding())))
/*     */     {
/*  71 */       this.is = new GZIPInputStream(this.is);
/*     */     }
/*     */   }
/*     */ 
/*     */   Response(String content)
/*     */   {
/*  77 */     this.responseAsString = content;
/*     */   }
/*     */ 
/*     */   public int getStatusCode() {
/*  81 */     return this.statusCode;
/*     */   }
/*     */ 
/*     */   public String getResponseHeader(String name) {
/*  85 */     if (this.con != null) {
/*  86 */       return this.con.getHeaderField(name);
/*     */     }
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   public InputStream asStream()
/*     */   {
/* 102 */     if (this.streamConsumed) {
/* 103 */       throw new IllegalStateException("Stream has already been consumed.");
/*     */     }
/* 105 */     return this.is;
/*     */   }
/*     */ 
/*     */   public String asString()
/*     */     throws QQConnectException
/*     */   {
/* 115 */     if (null == this.responseAsString) {
/*     */       try
/*     */       {
/* 118 */         InputStream stream = asStream();
/* 119 */         if (null == stream) {
/* 120 */           return null;
/*     */         }
/* 122 */         BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
/*     */ 
/* 124 */         StringBuffer buf = new StringBuffer();
/*     */         String line;
/* 126 */         while (null != (line = br.readLine())) {
/* 127 */           buf.append(line).append("\n");
/*     */         }
/* 129 */         this.responseAsString = buf.toString();
/* 130 */         if (Configuration.isDalvik()) {
/* 131 */           this.responseAsString = unescape(this.responseAsString);
/*     */         }
/* 133 */         log(this.responseAsString);
/* 134 */         stream.close();
/* 135 */         this.con.disconnect();
/* 136 */         this.streamConsumed = true;
/*     */       }
/*     */       catch (NullPointerException npe) {
/* 139 */         throw new QQConnectException(npe.getMessage(), npe);
/*     */       } catch (IOException ioe) {
/* 141 */         throw new QQConnectException(ioe.getMessage(), ioe);
/*     */       }
/*     */     }
/* 144 */     return this.responseAsString;
/*     */   }
/*     */ 
/*     */   public Document asDocument()
/*     */     throws QQConnectException
/*     */   {
/* 154 */     if (null == this.responseAsDocument)
/*     */     {
/*     */       try
/*     */       {
/* 158 */         this.responseAsDocument = ((DocumentBuilder)builders.get()).parse(new ByteArrayInputStream(asString().getBytes("UTF-8")));
/*     */       } catch (SAXException saxe) {
/* 160 */         throw new QQConnectException("The response body was not well-formed:\n" + this.responseAsString, saxe);
/*     */       } catch (IOException ioe) {
/* 162 */         throw new QQConnectException("There's something with the connection.", ioe);
/*     */       }
/*     */     }
/* 165 */     return this.responseAsDocument;
/*     */   }
/*     */ 
/*     */   public JSONObject asJSONObject()
/*     */     throws QQConnectException
/*     */   {
/*     */     try
/*     */     {
/* 176 */       return new JSONObject(asString());
/*     */     } catch (JSONException jsone) {
/* 178 */       throw new QQConnectException(jsone.getMessage() + ":" + this.responseAsString, jsone);
/*     */     }
/*     */   }
/*     */ 
/*     */   public JSONArray asJSONArray()
/*     */     throws QQConnectException
/*     */   {
/*     */     try
/*     */     {
/* 190 */       return new JSONArray(asString());
/*     */     } catch (Exception jsone) {
/* 192 */       throw new QQConnectException(jsone.getMessage() + ":" + this.responseAsString, jsone);
/*     */     }
/*     */   }
/*     */ 
/*     */   public InputStreamReader asReader() {
/*     */     try {
/* 198 */       return new InputStreamReader(this.is, "UTF-8"); } catch (UnsupportedEncodingException uee) {
/*     */     }
/* 200 */     return new InputStreamReader(this.is);
/*     */   }
/*     */ 
/*     */   public void disconnect()
/*     */   {
/* 205 */     this.con.disconnect();
/*     */   }
/*     */ 
/*     */   public static String unescape(String original)
/*     */   {
/* 218 */     Matcher mm = escaped.matcher(original);
/* 219 */     StringBuffer unescaped = new StringBuffer();
/* 220 */     while (mm.find()) {
/* 221 */       mm.appendReplacement(unescaped, Character.toString((char)Integer.parseInt(mm.group(1), 10)));
/*     */     }
/*     */ 
/* 224 */     mm.appendTail(unescaped);
/* 225 */     return unescaped.toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 230 */     if (null != this.responseAsString) {
/* 231 */       return this.responseAsString;
/*     */     }
/* 233 */     return "Response{statusCode=" + this.statusCode + ", response=" + this.responseAsDocument + ", responseString='" + this.responseAsString + '\'' + ", is=" + this.is + ", con=" + this.con + '}';
/*     */   }
/*     */ 
/*     */   private void log(String message)
/*     */   {
/* 243 */     if (DEBUG)
/* 244 */       log.debug("[" + new Date() + "]" + message);
/*     */   }
/*     */ 
/*     */   private void log(String message, String message2)
/*     */   {
/* 249 */     if (DEBUG)
/* 250 */       log(message + message2);
/*     */   }
/*     */ 
/*     */   public String getResponseAsString()
/*     */   {
/* 255 */     return this.responseAsString;
/*     */   }
/*     */ 
/*     */   public void setResponseAsString(String responseAsString) {
/* 259 */     this.responseAsString = responseAsString;
/*     */   }
/*     */ 
/*     */   public void setStatusCode(int statusCode) {
/* 263 */     this.statusCode = statusCode;
/*     */   }
/*     */ }

