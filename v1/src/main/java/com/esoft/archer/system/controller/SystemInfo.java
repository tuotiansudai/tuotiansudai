package com.esoft.archer.system.controller;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;

@Component
@Scope(ScopeType.APPLICATION)
public class SystemInfo implements Serializable {
	private static final long serialVersionUID = -6837235120728149607L;

	@Logger
	Log log;

	@Resource
	HibernateTemplate ht;

	private String javaVersion;
	/**
	 * 操作系统信息
	 */
	private String os;

	/**
	 * 服务器名称
	 */
	private String serverName;
	/**
	 * 监听端口
	 */
	private Integer serverPort;
	/**
	 * 当前目录
	 */
	private String currentDir;

	public String getJavaVersion() {
		if (javaVersion == null) {
			javaVersion = System.getProperty("java.version");
		}
		return javaVersion;
	}

	public String getOs() {
		if (os == null) {
			// FIXME:我是win7，怎么获取出来是vista？难道jdk版本太老了？
			os = System.getProperty("os.name") + " "
					+ System.getProperty("os.arch") + " "
					+ System.getProperty("os.version");
		}
		return os;
	}

	/**
	 * 可用内存(MB)
	 */
	public Long getUsedRam() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime()
				.freeMemory()) / 1024 / 1024;
	}

	/**
	 * 总内存(MB)
	 */
	public Long getTotalRam() {
		return Runtime.getRuntime().totalMemory() / 1024 / 1024;
	}

	public String getServerName() {
		if (serverName == null) {
			serverName = FacesUtil.getHttpSession().getServletContext()
					.getServerInfo();
		}
		return serverName;
	}

	public int getServerPort() {
		if (serverPort == null) {
			serverPort = FacesUtil.getHttpServletRequest().getLocalPort();
		}
		return serverPort;
	}

	public String getCurrentDir() {
		if (currentDir == null) {
			currentDir = FacesUtil.getAppRealPath();
		}
		return currentDir;
	}

}
