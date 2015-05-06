/*    */ package com.qq.connect.utils.http;
/*    */ 
/*    */ import com.qq.connect.QQConnectException;
/*    */ import com.sun.imageio.plugins.bmp.BMPImageReader;
/*    */ import com.sun.imageio.plugins.gif.GIFImageReader;
/*    */ import com.sun.imageio.plugins.jpeg.JPEGImageReader;
/*    */ import com.sun.imageio.plugins.png.PNGImageReader;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Iterator;
/*    */ import javax.imageio.ImageIO;
/*    */ import javax.imageio.ImageReader;
/*    */ import javax.imageio.stream.MemoryCacheImageInputStream;
/*    */ 
/*    */ public class ImageItem
/*    */ {
/*    */   private byte[] content;
/*    */   private String name;
/*    */   private String contentType;
/*    */ 
/*    */   public ImageItem(byte[] content)
/*    */     throws QQConnectException
/*    */   {
/* 27 */     this("pic", content);
/*    */   }
/*    */   public ImageItem(String name, byte[] content) throws QQConnectException {
/* 30 */     String imgtype = null;
/*    */     try {
/* 32 */       imgtype = getContentType(content);
/*    */     } catch (IOException e) {
/* 34 */       throw new QQConnectException(e);
/*    */     }
/*    */ 
/* 37 */     if ((imgtype != null) && ((imgtype.equalsIgnoreCase("image/gif")) || (imgtype.equalsIgnoreCase("image/png")) || (imgtype.equalsIgnoreCase("image/jpeg"))))
/*    */     {
/* 39 */       this.content = content;
/* 40 */       this.name = name;
/* 41 */       this.contentType = imgtype;
/*    */     } else {
/* 43 */       throw new QQConnectException("Unsupported image type, Only Suport JPG ,GIF,PNG!");
/*    */     }
/*    */   }
/*    */ 
/*    */   public byte[] getContent()
/*    */   {
/* 49 */     return this.content;
/*    */   }
/*    */   public String getName() {
/* 52 */     return this.name;
/*    */   }
/*    */   public String getContentType() {
/* 55 */     return this.contentType;
/*    */   }
/*    */ 
/*    */   public static String getContentType(byte[] mapObj) throws IOException
/*    */   {
/* 60 */     String type = "";
/* 61 */     ByteArrayInputStream bais = null;
/* 62 */     MemoryCacheImageInputStream mcis = null;
/*    */     try {
/* 64 */       bais = new ByteArrayInputStream(mapObj);
/* 65 */       mcis = new MemoryCacheImageInputStream(bais);
/* 66 */       Iterator itr = ImageIO.getImageReaders(mcis);
/* 67 */       while (itr.hasNext()) {
/* 68 */         ImageReader reader = (ImageReader)itr.next();
/* 69 */         if ((reader instanceof GIFImageReader))
/* 70 */           type = "image/gif";
/* 71 */         else if ((reader instanceof JPEGImageReader))
/* 72 */           type = "image/jpeg";
/* 73 */         else if ((reader instanceof PNGImageReader))
/* 74 */           type = "image/png";
/* 75 */         else if ((reader instanceof BMPImageReader))
/* 76 */           type = "application/x-bmp";
/*    */       }
/*    */     }
/*    */     finally {
/* 80 */       if (bais != null)
/*    */         try {
/* 82 */           bais.close();
/*    */         }
/*    */         catch (IOException ioe)
/*    */         {
/*    */         }
/* 87 */       if (mcis != null)
/*    */         try {
/* 89 */           mcis.close();
/*    */         }
/*    */         catch (IOException ioe)
/*    */         {
/*    */         }
/*    */     }
/* 95 */     return type;
/*    */   }
/*    */ }

