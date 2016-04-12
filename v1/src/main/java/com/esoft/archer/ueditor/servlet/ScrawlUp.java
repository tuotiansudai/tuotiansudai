package com.esoft.archer.ueditor.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ScrawlUp extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ScrawlUp() {
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
		
		String param = request.getParameter("action");
	    Uploader up = new Uploader(request);
	    String path = "upload";
	    up.setSavePath(path);
	    String[] fileType = {".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp"};
	    up.setAllowFiles(fileType);
	    up.setMaxSize(10000); //单位KB
	    
	    if(param!=null && param.equals("tmpImg")){
	    	try {
				up.upload();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	response.getWriter().print("<script>parent.ue_callback('" + up.getUrl() + "','" + up.getState() + "')</script>");
	    }else{
	    	up.uploadBase64("content");
	    	response.getWriter().print("{'url':'" + up.getUrl()+"',state:'"+up.getState()+"'}");
	    }
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
