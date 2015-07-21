package com.esoft.archer.ueditor.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

public class GetRemoteImage extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public GetRemoteImage() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	String url = request.getParameter("upfile");
		System.out.println("url--" + url);
    	String state = "远程图片抓取成功！";
    	String[] arr = url.split("ue_separate_ue");
    	String[] outSrc = new String[arr.length];
    	for(int i=0;i<arr.length;i++){
    		String type = getFileType(arr[i]);
			if(type.equals("")){
				state = "image type is not right";
				continue;
			}
    		//大小验证
    		HttpURLConnection.setFollowRedirects(false); 
		    HttpURLConnection   conn   = (HttpURLConnection) new URL(arr[i]).openConnection(); 
//		    if(conn.getContentType().indexOf("image")==-1){
//		    	state = "请求地址头不正确";
//		    	continue;
//		    }
		    if(conn.getResponseCode() != 200){
		    	state = "request address is not exists";
		    	continue;
		    }
			outSrc[i] = arr[i];
    	}
   	String outstr = "";
   	for(int i=0;i<outSrc.length;i++){
   		outstr+=outSrc[i]+"ue_separate_ue";
   	}
   	outstr = outstr.substring(0,outstr.lastIndexOf("ue_separate_ue"));
   	response.getWriter().print("{'url':'" + outstr + "','tip':'"+state+"','srcUrl':'" + url + "'}" );
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	private String getFileType(String fileName){
	    	String[] fileType = {".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp"};
	    	Iterator<String> type = Arrays.asList(fileType).iterator();
	    	while(type.hasNext()){
	    		String t = type.next();
	    		if(fileName.endsWith(t)){
	    			return t;
	    		}
	    	}
	    	return "";
	    }

}
