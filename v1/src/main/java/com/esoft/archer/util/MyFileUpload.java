package com.esoft.archer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;

/**
 * @author yinjunlu
 * 
 * 专业打补丁
 * 
 * create at 2013-1-10
 * 
 */
public class MyFileUpload {

	public static void main(String[] args) {
		upLoad1();
	}

	public static void upLoad1() {
		MyFileUpload up = new MyFileUpload();

		//上线时间（格式2013.6.20）
//		String date = "2013.10.24";
		String date = "";
		
		//生成本地目录
		String localpath = "E:\\360data\\userDir\\Desktop\\";
		
		// 服务器目录
		String path = "";

		//项目源文件本地路径
		String path1 = "E:\\myeclipse9_workspace\\p2p_loan\\WebRoot\\";
	
		// class文件
		String[] classPath = {
//				"com.esoft.archer.user.controller.investorBillList",
//				"com.esoft.archer.user.controller.loanerBillList",
//				"com.esoft.archer.user.service.LoanerBillService",
//				"com.esoft.archer.user.service.impl.LoanerBillServiceImpl"
		};
   
		// sqlMap.xml文件
		String[] xmlPath = {
		};

		// conf配置文件(propertites)
		String[] confPath = {
//				"jdbc.properties"
		};

		// 其他xhtml、js文件等
		String[] otherPath = {
           "\\admin\\banner\\bannerEdit.xhtml",
//           "\\site\\themes\\default\\templates\\user\\myBDIts.xhtml",
//           "\\site\\themes\\default\\templates\\user\\myFedIts.xhtml",
//           "\\site\\themes\\default\\templates\\user\\myFinishedLoans.xhtml",
//           "\\site\\themes\\default\\templates\\user\\myInvestments.xhtml",
//           "\\site\\themes\\default\\templates\\user\\myLoans.xhtml",
//           "\\site\\themes\\default\\templates\\user\\myRaisingLoans.xhtml",
//           "\\site\\themes\\default\\templates\\user\\myRepayingLoans.xhtml",
//           "\\site\\themes\\default\\templates\\user\\myRepayments.xhtml",
//           "\\site\\themes\\default\\templates\\user\\myRmedIts.xhtml",
//           "\\site\\themes\\default\\templates\\user\\myUnverifiedLoans.xhtml",
//           "\\site\\themes\\default\\templates\\user\\myInvestmentsIndex.xhtml",
//           "\\site\\themes\\default\\templates\\user\\userCenterTop.xhtml"
		};

		if (classPath.length > 0) {
			System.out.println("开始拷贝class文件");

			// 拷贝class文件
			for (String string : classPath) {

				string = up.str_replace(".", "\\", string);

				string = "\\" + string;
				// 末尾加上.class
				string += ".class";

				up.writeFile(path1 + "WEB-INF\\classes\\" + string,
						   localpath+date+"\\"
								+ path
								+ "\\WEB-INF\\classes\\"
								+ string.substring(string.indexOf("\\"), string
										.length()));

			}
			System.out.println("class文件拷贝完毕！");
		}

		if (xmlPath.length > 0) {
			System.out.println("开始拷贝sqlMap.xml文件");

			// 拷贝sqlMap.xml文件
			for (String string : xmlPath) {
				string = up.str_replace("/", "\\", string);
				up.writeFile(path1 + "WEB-INF\\classes\\" + string,
						  localpath+date+"\\"
								+ path
								+ "WEB-INF\\classes\\"
								+ string.substring(string.indexOf("\\"), string
										.length()));
			}

			System.out.println("sqlMap.xml文件拷贝完毕！");
		}

		if (confPath.length > 0) {
			System.out.println("开始拷贝conf配置文件");

			for (String string : confPath) {
				string = up.str_replace("/", "\\", string);
				up.writeFile(path1 + "WEB-INF\\" + string,
						  localpath+date+"\\"
								+ path
								+ "WEB-INF\\"
								+ string.substring(string.indexOf("\\"), string
										.length()));
			}

			System.out.println("conf配置文件拷贝完毕！");
		}

		if (otherPath.length > 0) {
			System.out.println("开始拷贝jsp、js等文件");

			for (String string : otherPath) {
				string = up.str_replace("/", "\\", string);
				// dqmhA20130314_war.ear

					up.writeFile(path1 + string, localpath+date+"\\"
							                    + path
							                    + string.substring(string.indexOf("\\"), string
									.length()));

			}
			System.out.println("jsp、js等文件拷贝完毕！");
		}

	}

	public Object[] writeFile(String in_file_path, String fileAllPath) {
		if (in_file_path.indexOf("*") != -1) {
			String url_n = in_file_path.substring(0, in_file_path.indexOf("*"));
			String url_n2 = null;
			File directory = new File(url_n);
			String[] name_list = directory.list();
			for (String name : name_list) {
				url_n2 = url_n + name;
				if (url_n2.indexOf(".") != -1
						&& url_n2.substring(url_n2.lastIndexOf("\\") - 1,
								url_n2.length()).indexOf(".") != -1) {
					this.writeFile2(url_n2, fileAllPath.substring(0,
							fileAllPath.lastIndexOf("\\") + 1)
							+ name);
				}
			}
			return null;
		} else {
			this.writeFile2(in_file_path, fileAllPath);
		}
		return null;
	}

	@SuppressWarnings("resource")
	public Object[] writeFile2(String in_file_path, String fileAllPath) {
		InputStream is = null;
		try {
			is = new FileInputStream(new File(in_file_path));
		} catch (Exception e) {
			e.printStackTrace();
			return new Object[] { false, e.getMessage() };
		}
		OutputStream os = null;
		String WEB_PATH = fileAllPath.substring(0, fileAllPath
				.lastIndexOf("\\"));
		try {
			File directory = new File(WEB_PATH);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			os = new FileOutputStream(fileAllPath);

			int bytesRead = 0;

			byte[] buffer = new byte[8192];

			while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {

				os.write(buffer, 0, bytesRead);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Object[] { false, e.getMessage() };
		}
		return null;
	}

	public String str_replace(String from, String to, String source) {
		StringBuffer bf = new StringBuffer("");
		StringTokenizer st = new StringTokenizer(source, from, true);
		while (st.hasMoreTokens()) {
			String tmp = st.nextToken();
			if (tmp.equals(from)) {
				bf.append(to);
			} else {
				bf.append(tmp);
			}
		}
		return bf.toString();
	}
}
