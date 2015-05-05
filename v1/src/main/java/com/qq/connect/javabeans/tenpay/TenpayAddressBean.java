/*    */ package com.qq.connect.javabeans.tenpay;
/*    */ 
/*    */ import com.qq.connect.QQConnectException;
/*    */ import org.json.JSONException;
/*    */ import org.json.JSONObject;
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class TenpayAddressBean
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 4742904673643859727L;
/* 34 */   private int ret = 0;
/* 35 */   private String msg = "";
/* 36 */   private int retNum = 0;
/* 37 */   private ArrayList<Address> addresses = new ArrayList();
/*    */ 
/*    */   public int getRet()
/*    */   {
/* 23 */     return this.ret;
/*    */   }
/*    */ 
/*    */   public String getMsg()
/*    */   {
/* 31 */     return this.msg;
/*    */   }
/*    */ 
/*    */   public TenpayAddressBean(JSONObject json)
/*    */     throws QQConnectException
/*    */   {
/* 42 */     init(json);
/*    */   }
/*    */ 
/*    */   public int getRetNum()
/*    */   {
/* 50 */     return this.retNum;
/*    */   }
/*    */ 
/*    */   public ArrayList<Address> getAddresses()
/*    */   {
/* 58 */     return this.addresses;
/*    */   }
/*    */ 
/*    */   private void init(JSONObject json) throws QQConnectException {
/* 62 */     if (json != null)
/*    */       try {
/* 64 */         this.ret = json.getInt("ret");
/* 65 */         if (0 != this.ret)
/*    */         {
/* 67 */           this.msg = json.getString("msg");
/*    */         } else {
/* 69 */           this.retNum = json.getInt("ret_num");
/* 70 */           if (this.retNum != 0)
/*    */           {
/* 74 */             for (int i = 0; i < this.retNum; i++) {
/* 75 */               this.addresses.add(new Address(json.getString("FRegionId_" + i), json.getInt("FUsedCount_" + i), json.getString("Fname_" + i), json.getString("Ftel_" + i), json.getString("Fzipcode_" + i), json.getString("Fmobile_" + i), json.getString("Faddrstreet_" + i), json.getString("Flastuse_time_" + i), json.getString("Fmod_time_" + i), json.getString("Fcreate_time_" + i), json.getString("Findex_" + i)));
/*    */             }
/*    */ 
/*    */           }
/*    */ 
/*    */         }
/*    */ 
/*    */       }
/*    */       catch (JSONException jsone)
/*    */       {
/* 85 */         throw new QQConnectException(jsone.getMessage() + ":" + json.toString(), jsone);
/*    */       }
/*    */   }
/*    */ }

