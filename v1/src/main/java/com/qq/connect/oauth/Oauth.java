/*     */ package com.qq.connect.oauth;
/*     */ 
/*     */ import com.qq.connect.QQConnect;
/*     */ import com.qq.connect.QQConnectException;
/*     */ import com.qq.connect.javabeans.AccessToken;
/*     */ import com.qq.connect.utils.QQConnectConfig;
/*     */ import com.qq.connect.utils.RandomStatusGenerator;
/*     */ import com.qq.connect.utils.http.HttpClient;
/*     */ import com.qq.connect.utils.http.PostParameter;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
/*     */ 
/*     */ public class Oauth extends QQConnect
/*     */ {
/*     */   private static final long serialVersionUID = -7860508274941797293L;
/*     */ 
/*     */   private String[] extractionAuthCodeFromUrl(String url)
/*     */     throws QQConnectException
/*     */   {
/*  33 */     if (url == null) {
/*  34 */       throw new QQConnectException("you pass a null String object");
/*     */     }
/*  36 */     Matcher m = Pattern.compile("code=(\\w+)&state=(\\w+)&?").matcher(url);
/*  37 */     String authCode = "";
/*  38 */     String state = "";
/*  39 */     if (m.find()) {
/*  40 */       authCode = m.group(1);
/*  41 */       state = m.group(2);
/*     */     }
/*     */ 
/*  45 */     return new String[] { authCode, state };
/*     */   }
/*     */ 
/*     */   public AccessToken getAccessTokenByRequest(ServletRequest request)
/*     */     throws QQConnectException
/*     */   {
/*  59 */     String queryString = ((HttpServletRequest)request).getQueryString();
/*  60 */     if (queryString == null) {
/*  61 */       return new AccessToken();
/*     */     }
/*  63 */     String state = (String)((HttpServletRequest)request).getSession().getAttribute("qq_connect_state");
/*  64 */     if ((state == null) || (state.equals(""))) {
/*  65 */       return new AccessToken();
/*     */     }
/*     */ 
/*  68 */     String[] authCodeAndState = extractionAuthCodeFromUrl(queryString);
/*  69 */     String returnState = authCodeAndState[1];
/*  70 */     String returnAuthCode = authCodeAndState[0];
/*     */ 
/*  72 */     AccessToken accessTokenObj = null;
/*     */ 
/*  75 */     if ((returnState.equals("")) || (returnAuthCode.equals("")))
/*     */     {
/*  77 */       accessTokenObj = new AccessToken();
/*     */     }
/*  80 */     else if (!state.equals(returnState))
/*     */     {
/*  82 */       accessTokenObj = new AccessToken();
/*     */     }
/*  84 */     else accessTokenObj = new AccessToken(this.client.post(QQConnectConfig.getValue("accessTokenURL"), new PostParameter[] { new PostParameter("client_id", QQConnectConfig.getValue("app_ID")), new PostParameter("client_secret", QQConnectConfig.getValue("app_KEY")), new PostParameter("grant_type", "authorization_code"), new PostParameter("code", returnAuthCode), new PostParameter("redirect_uri", QQConnectConfig.getValue("redirect_URI")) }, Boolean.valueOf(false)));
/*     */ 
/*  98 */     return accessTokenObj;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public AccessToken getAccessTokenByQueryString(String queryString, String state)
/*     */     throws QQConnectException
/*     */   {
/* 110 */     if (queryString == null) {
/* 111 */       return new AccessToken();
/*     */     }
/*     */ 
/* 114 */     String[] authCodeAndState = extractionAuthCodeFromUrl(queryString);
/* 115 */     String returnState = authCodeAndState[1];
/* 116 */     String returnAuthCode = authCodeAndState[0];
/*     */ 
/* 118 */     AccessToken accessTokenObj = null;
/*     */ 
/* 121 */     if ((returnState.equals("")) || (returnAuthCode.equals("")))
/*     */     {
/* 123 */       accessTokenObj = new AccessToken();
/*     */     }
/* 126 */     else if (!state.equals(returnState))
/*     */     {
/* 128 */       accessTokenObj = new AccessToken();
/*     */     }
/* 130 */     else accessTokenObj = new AccessToken(this.client.post(QQConnectConfig.getValue("accessTokenURL"), new PostParameter[] { new PostParameter("client_id", QQConnectConfig.getValue("app_ID")), new PostParameter("client_secret", QQConnectConfig.getValue("app_KEY")), new PostParameter("grant_type", "authorization_code"), new PostParameter("code", returnAuthCode), new PostParameter("redirect_uri", QQConnectConfig.getValue("redirect_URI")) }, Boolean.valueOf(false)));
/*     */ 
/* 144 */     return accessTokenObj;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public String getAuthorizeURL(String scope, String state)
/*     */     throws QQConnectException
/*     */   {
/* 158 */     return QQConnectConfig.getValue("authorizeURL").trim() + "?client_id=" + QQConnectConfig.getValue("app_ID").trim() + "&redirect_uri=" + QQConnectConfig.getValue("redirect_URI").trim() + "&response_type=" + "code" + "&state=" + state + "&scope=" + scope;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public String getAuthorizeURL(String state)
/*     */     throws QQConnectException
/*     */   {
/* 180 */     String scope = QQConnectConfig.getValue("scope");
/* 181 */     if ((scope != null) && (!scope.equals(""))) {
/* 182 */       return getAuthorizeURL("code", state, scope);
/*     */     }
/* 184 */     return QQConnectConfig.getValue("authorizeURL").trim() + "?client_id=" + QQConnectConfig.getValue("app_ID").trim() + "&redirect_uri=" + QQConnectConfig.getValue("redirect_URI").trim() + "&response_type=" + "code" + "&state=" + state;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public String getAuthorizeURLByScope(String scope, ServletRequest request)
/*     */     throws QQConnectException
/*     */   {
/* 202 */     String state = RandomStatusGenerator.getUniqueState();
/* 203 */     ((HttpServletRequest)request).setAttribute("qq_connect_state", state);
/*     */ 
/* 205 */     return QQConnectConfig.getValue("authorizeURL").trim() + "?client_id=" + QQConnectConfig.getValue("app_ID").trim() + "&redirect_uri=" + QQConnectConfig.getValue("redirect_URI").trim() + "&response_type=" + "code" + "&state=" + state + "&scope=" + scope;
/*     */   }
/*     */ 
/*     */   public String getAuthorizeURL(ServletRequest request)
/*     */     throws QQConnectException
/*     */   {
/* 223 */     String state = RandomStatusGenerator.getUniqueState();
/* 224 */     ((HttpServletRequest)request).getSession().setAttribute("qq_connect_state", state);
/* 225 */     String scope = QQConnectConfig.getValue("scope");
/* 226 */     if ((scope != null) && (!scope.equals(""))) {
/* 227 */       return getAuthorizeURL("code", state, scope);
/*     */     }
/* 229 */     return QQConnectConfig.getValue("authorizeURL").trim() + "?client_id=" + QQConnectConfig.getValue("app_ID").trim() + "&redirect_uri=" + QQConnectConfig.getValue("redirect_URI").trim() + "&response_type=" + "code" + "&state=" + state;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public String getAuthorizeURL(String response_type, String state, String scope)
/*     */     throws QQConnectException
/*     */   {
/* 250 */     return QQConnectConfig.getValue("authorizeURL").trim() + "?client_id=" + QQConnectConfig.getValue("app_ID").trim() + "&redirect_uri=" + QQConnectConfig.getValue("redirect_URI").trim() + "&response_type=" + response_type + "&state=" + state + "&scope=" + scope;
/*     */   }
/*     */ }

