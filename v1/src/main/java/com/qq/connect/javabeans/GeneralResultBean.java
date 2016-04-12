/*    */ package com.qq.connect.javabeans;
/*    */ 
/*    */ import com.qq.connect.QQConnectException;
/*    */ import com.qq.connect.QQConnectResponse;
/*    */ import org.json.JSONException;
/*    */ import org.json.JSONObject;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class GeneralResultBean extends QQConnectResponse
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 4742904673643859727L;
/* 20 */   private int ret = 0;
/* 21 */   private String msg = "";
/* 22 */   private int errcode = 0;
/*    */ 
/*    */   public int getErrCode()
/*    */   {
/* 29 */     return this.errcode;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 35 */     return "GeneralResultBean{ret=" + this.ret + ", msg='" + this.msg + '\'' + ", errcode=" + this.errcode + '}';
/*    */   }
/*    */ 
/*    */   public int getRet()
/*    */   {
/* 47 */     return this.ret;
/*    */   }
/*    */ 
/*    */   public String getMsg()
/*    */   {
/* 55 */     return this.msg;
/*    */   }
/*    */ 
/*    */   public GeneralResultBean(JSONObject json) throws QQConnectException
/*    */   {
/* 60 */     init(json);
/*    */   }
/*    */ 
/*    */   private void init(JSONObject json) throws QQConnectException
/*    */   {
/* 65 */     if (json != null)
/*    */       try {
/* 67 */         this.ret = json.getInt("ret");
/* 68 */         if (0 != this.ret)
/*    */         {
/* 70 */           this.msg = json.getString("msg");
/* 71 */           this.errcode = json.getInt("errcode");
/*    */         } else {
/* 73 */           this.msg = "";
/*    */         }
/*    */       } catch (JSONException jsone) {
/* 76 */         throw new QQConnectException(jsone.getMessage() + ":" + json.toString(), jsone);
/*    */       }
/*    */   }
/*    */ }

