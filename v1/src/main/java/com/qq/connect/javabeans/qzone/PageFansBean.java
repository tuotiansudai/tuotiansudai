/*    */ package com.qq.connect.javabeans.qzone;
/*    */ 
/*    */ import com.qq.connect.QQConnectException;
/*    */ import com.qq.connect.QQConnectResponse;
/*    */ import org.json.JSONException;
/*    */ import org.json.JSONObject;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class PageFansBean extends QQConnectResponse
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 4742904673643859727L;
/* 20 */   private int ret = 0;
/* 21 */   private String msg = "";
/* 22 */   private boolean fans = false;
/*    */ 
/*    */   public boolean isFans()
/*    */   {
/* 28 */     return this.fans;
/*    */   }
/*    */ 
/*    */   public int getRet()
/*    */   {
/* 36 */     return this.ret;
/*    */   }
/*    */ 
/*    */   public String getMsg()
/*    */   {
/* 44 */     return this.msg;
/*    */   }
/*    */ 
/*    */   public PageFansBean(JSONObject json) throws QQConnectException
/*    */   {
/* 49 */     init(json);
/*    */   }
/*    */ 
/*    */   private void init(JSONObject json) throws QQConnectException
/*    */   {
/* 54 */     if (json != null)
/*    */       try {
/* 56 */         this.ret = json.getInt("ret");
/* 57 */         if (0 != this.ret)
/*    */         {
/* 59 */           this.msg = json.getString("msg");
/*    */         } else {
/* 61 */           this.msg = "";
/* 62 */           this.fans = (json.getInt("isfans") == 1);
/*    */         }
/*    */       } catch (JSONException jsone) {
/* 65 */         throw new QQConnectException(jsone.getMessage() + ":" + json.toString(), jsone);
/*    */       }
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 73 */     int prime = 31;
/* 74 */     int result = 1;
/* 75 */     result = 31 * result + (this.fans == true ? 0 : String.valueOf(this.fans).hashCode());
/* 76 */     return result;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 81 */     if (this == obj)
/* 82 */       return true;
/* 83 */     if (obj == null)
/* 84 */       return false;
/* 85 */     if (getClass() != obj.getClass())
/* 86 */       return false;
/* 87 */     PageFansBean other = (PageFansBean)obj;
/* 88 */     if (this.fans != other.fans) {
/* 89 */       return false;
/*    */     }
/* 91 */     return true;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 96 */     return "PageFansInfo [pageFans : " + this.fans + " , " + "]";
/*    */   }
/*    */ }

