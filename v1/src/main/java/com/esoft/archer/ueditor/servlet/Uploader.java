package com.esoft.archer.ueditor.servlet;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.ttsd.aliyun.AliyunUtils;
import com.ttsd.aliyun.PropertiesUtils;
import com.ttsd.aliyun.WaterMarkUtils;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase.InvalidContentTypeException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.webapp.MultipartRequest;

import sun.misc.BASE64Decoder;

/**
 * UEditor文件上传辅助类
 * 
 */
public class Uploader {
	// 输出文件地址
	private String url = "";
	// 上传文件名
	private String fileName = "";
	// 状态
	private String state = "";
	// 文件类型
	private String type = "";
	// 原始文件名
	private String originalName = "";
	// 文件大小
	private String size = "";

	private HttpServletRequest request = null;
	private String title = "";


	// 保存路径
	private String savePath = "upload";
	// 文件允许格式
	private String[] allowFiles = { ".rar", ".doc", ".docx", ".zip", ".pdf",
			".txt", ".swf", ".wmv", ".gif", ".png", ".jpg", ".jpeg", ".bmp" };
	// 文件大小限制，单位KB
	private int maxSize = 1024*10;

	private HashMap<String, String> errorInfo = new HashMap<String, String>();

	public Uploader(HttpServletRequest request) {
		this.request = request;
		HashMap<String, String> tmp = this.errorInfo;
		tmp.put("SUCCESS", "SUCCESS"); // 默认成功
		tmp.put("NOFILE", "未包含文件上传域");
		tmp.put("TYPE", "不允许的文件格式");
		tmp.put("SIZE", "文件大小超出限制");
		tmp.put("ENTYPE", "请求类型ENTYPE错误");
		tmp.put("REQUEST", "上传请求异常");
		tmp.put("IO", "IO异常");
		tmp.put("DIR", "目录创建失败");
		tmp.put("UNKNOWN", "未知错误");

	}

	public void upload() throws Exception {
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
		String path = "/upload/" + formater.format(new Date());
		boolean isMultipart = ServletFileUpload.isMultipartContent(this.request);
		if (!isMultipart) {
			this.state = this.errorInfo.get("NOFILE");
			return;
		}
		if (this.request instanceof MultipartRequest) {
			Boolean switchBlur = request.getParameter("switchBlur")!=null;
			DiskFileItem dfi = (DiskFileItem) ((MultipartRequest) this.request).getFileItem("upfile");
			this.title = ((MultipartRequest) this.request).getParameter("pictitle");
			this.originalName = dfi.getName().substring(dfi.getName().lastIndexOf(System.getProperty("file.separator")) + 1);
			if (!this.checkFileType(this.originalName)) {
				this.state = this.errorInfo.get("TYPE");
				return;
			}
			this.fileName = this.getName(this.originalName);
			this.type = this.getFileExt(this.fileName);
			this.title = fileName;
			//add line mkdir
			String filepath = mkdir(this.getPhysicalPath(this.url)+"upload");
			String savefile = filepath +"/"+ this.fileName;
			this.url = savePath  + "/" + this.fileName;
			BufferedInputStream in = new BufferedInputStream(dfi.getInputStream());

			String switchOss = PropertiesUtils.getPro("plat.is.start");
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			if(switchOss.equals("oss")){
				if(switchBlur){
					this.url = AliyunUtils.uploadFileBlur(fileName, dfi.getInputStream(), rootPath);
				}else{
					this.url = AliyunUtils.uploadFile(fileName, dfi.getInputStream());
				}
				this.title = url;
			}else{
				String waterPath = rootPath + "/site/themes/default/images/watermark.png";
				ByteArrayInputStream bin = new ByteArrayInputStream(WaterMarkUtils.pressImage(waterPath, dfi.getInputStream(), 0, 0).toByteArray());
				FileOutputStream out = new FileOutputStream(new File(savefile));
				BufferedOutputStream output = new BufferedOutputStream(out);
				Streams.copy(bin, output, true);
			}
			this.state = this.errorInfo.get("SUCCESS");
		} else {
			DiskFileItemFactory dff = new DiskFileItemFactory();
			String savePath = this.getFolder(this.savePath);
			dff.setRepository(new File(savePath));
			try {
				ServletFileUpload sfu = new ServletFileUpload(dff);
				sfu.setSizeMax(this.maxSize * 1024);
				sfu.setHeaderEncoding("UTF-8");
				FileItemIterator fii = sfu.getItemIterator(this.request);
				while (fii.hasNext()) {
					FileItemStream fis = fii.next();
					if (!fis.isFormField()) {
						this.originalName = fis
								.getName()
								.substring(
										fis.getName()
												.lastIndexOf(
														System.getProperty("file.separator")) + 1);
						if (!this.checkFileType(this.originalName)) {
							this.state = this.errorInfo.get("TYPE");
							continue;
						}
						this.fileName = this.getName(this.originalName);
						this.type = this.getFileExt(this.fileName);
						this.url = savePath + "/" + this.fileName;
						BufferedInputStream in = new BufferedInputStream(
								fis.openStream());
						FileOutputStream out = new FileOutputStream(new File(
								this.getPhysicalPath(this.url)));
						BufferedOutputStream output = new BufferedOutputStream(
								out);
						Streams.copy(in, output, true);
						this.state = this.errorInfo.get("SUCCESS");
						// UE中只会处理单张上传，完成后即退出
						break;
					} else {
						String fname = fis.getFieldName();
						// 只处理title，其余表单请自行处理
						if (!fname.equals("pictitle")) {
							continue;
						}
						BufferedInputStream in = new BufferedInputStream(
								fis.openStream());
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(in));
						StringBuffer result = new StringBuffer();
						while (reader.ready()) {
							result.append((char) reader.read());
						}
						this.title = new String(result.toString().getBytes(),
								"utf-8");
						reader.close();
					}
				}
			} catch (SizeLimitExceededException e) {
				this.state = this.errorInfo.get("SIZE");
			} catch (InvalidContentTypeException e) {
				this.state = this.errorInfo.get("ENTYPE");
			} catch (FileUploadException e) {
				this.state = this.errorInfo.get("REQUEST");
			} catch (Exception e) {
				this.state = this.errorInfo.get("UNKNOWN");
			}
		}
		// log.debug("上传后的文件名为:"+this.title);
	}

	/**
	 * 接受并保存以base64格式上传的文件
	 * 
	 * @param fieldName
	 */
	public void uploadBase64(String fieldName) {
		String savePath = this.getFolder(this.savePath);
		String base64Data = this.request.getParameter(fieldName);
		this.fileName = this.getName("test.png");
		this.url = savePath + "/" + this.fileName;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			File outFile = new File(this.getPhysicalPath(this.url));
			OutputStream ro = new FileOutputStream(outFile);
			byte[] b = decoder.decodeBuffer(base64Data);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			ro.write(b);
			ro.flush();
			ro.close();
			this.state = this.errorInfo.get("SUCCESS");
		} catch (Exception e) {
			this.state = this.errorInfo.get("IO");
		}
	}

	/**
	 * 文件类型判断
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean checkFileType(String fileName) {
		Iterator<String> type = Arrays.asList(this.allowFiles).iterator();
		while (type.hasNext()) {
			String ext = type.next();
			if (fileName.toLowerCase().endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @return string
	 */
	private String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 依据原始文件名生成新文件名
	 * 
	 * @return
	 */
	private String getName(String fileName) {
		Random random = new Random();
		return this.fileName = "" + random.nextInt(10000)
				+ System.currentTimeMillis() + this.getFileExt(fileName);
	}

	/**
	 * 根据字符串创建本地目录 并按照日期建立子目录返回
	 * 
	 * @param path
	 * @return
	 */
	private String getFolder(String path) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
		path += "/" + formater.format(new Date());
		File dir = new File(this.getPhysicalPath(path));
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				this.state = this.errorInfo.get("DIR");
				return "";
			}
		}
		return path;
	}


	/**
	 * add method by lance
	 * mkdir
	 * @param path
	 */
	private String mkdir(final String path) {

		File dir = new File(path);
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return path;
	}

	/**
	 * 根据传入的虚拟路径获取物理路径
	 * 
	 * @param path
	 * @return
	 */
	private String getPhysicalPath(String path) {
		String servletPath = this.request.getServletPath();
		//modify by lance fixbug jetty container realPath is null
		String realPath = this.request.getSession().getServletContext().getRealPath("") + servletPath;
		return new File(realPath).getParent() + "/" + path;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public void setAllowFiles(String[] allowFiles) {
		this.allowFiles = allowFiles;
	}

	public void setMaxSize(int size) {
		this.maxSize = size;
	}

	public String getSize() {
		return this.size;
	}

	public String getUrl() {
		return this.url;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getState() {
		return this.state;
	}

	public String getTitle() {
		return this.title;
	}

	public String getType() {
		return this.type;
	}

	public String getOriginalName() {
		return this.originalName;
	}
}
