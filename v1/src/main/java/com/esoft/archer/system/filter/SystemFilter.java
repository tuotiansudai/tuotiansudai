package com.esoft.archer.system.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.esoft.archer.system.service.AppLocalFilter;
import com.esoft.core.util.SpringBeanUtil;

public class SystemFilter implements Filter {

	private final static Log log = LogFactory.getLog(SystemFilter.class);

	private final static String FILTER_SERVICE_NAME = "filterServices";

	private final static String FILTER_SERVICE_IMPL_PACKAGE = "com.esoft.archer.system.service.impl";

	private static AppLocalFilter[] filters;

	public void destroy() {
		if (log.isInfoEnabled()) {
			log.info("SystemFilter destroyed...");
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		for (AppLocalFilter appFilter : filters) {
			AppLocalFilter thisFilter = appFilter;
			thisFilter.doFilter(httpRequest, httpResponse, filter);

		}

		filter.doFilter(request, response);

	}

	public void init(FilterConfig config) throws ServletException {
		if (log.isInfoEnabled()) {
			log.info("SystemFilter init start ...");
		}
		final String filterServices = config
				.getInitParameter(FILTER_SERVICE_NAME);

		if (log.isDebugEnabled())
			log.debug("Found services:" + filterServices);
		String[] services = filterServices.split(",");
		filters = new AppLocalFilter[services.length];
		for (int i = 0; i < services.length; i++) {
			filters[i] = (AppLocalFilter) SpringBeanUtil
					.getBeanByName(services[i]);
			if (filters[i] == null) {
				log.warn("Not fount Spring bean ,Bean name:" + services[i]);
			} else {
				log.debug("init systefilter filter:" + filters[i]);
			}
		}

		if (log.isInfoEnabled()) {
			log.info("SystemFilter init started ...");
		}
	}

}
