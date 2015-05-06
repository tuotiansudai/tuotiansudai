package com.esoft.core.application;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.faces.application.PrettyNavigationHandler;
import com.ocpsoft.pretty.faces.application.PrettyRedirector;
import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.dynaview.DynaviewEngine;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.servlet.PrettyFacesWrappedResponse;
import com.ocpsoft.pretty.faces.url.QueryString;
import com.ocpsoft.pretty.faces.url.URL;
import com.ocpsoft.pretty.faces.util.FacesNavigationURLCanonicalizer;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-2-9 下午2:59:00
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-2-9 wangzhi 1.0
 */
public class ArcherNavigationHandler extends ConfigurableNavigationHandler {

	private static final Log log = LogFactory
			.getLog(PrettyNavigationHandler.class);

	private final ConfigurableNavigationHandler parent;
	private final PrettyRedirector pr = PrettyRedirector.getInstance();
	private final DynaviewEngine dynaview = new DynaviewEngine();

	private static final Pattern REDIRECT_REGEX = Pattern.compile("&?faces-redirect=true");

	public ArcherNavigationHandler(ConfigurableNavigationHandler parent) {
		this.parent = parent;
	}

	@Override
	public NavigationCase getNavigationCase(FacesContext context,
			String fromAction, String outcome) {
		if (outcome != null) {
			outcome = replaceThemepath(context, outcome);
		}
		PrettyContext prettyContext = PrettyContext.getCurrentInstance(context);
		PrettyConfig config = prettyContext.getConfig();
		if ((outcome != null) && PrettyContext.PRETTY_PREFIX.equals(outcome)) {
			String viewId = context.getViewRoot().getViewId();
			NavigationCase navigationCase = parent.getNavigationCase(context,
					fromAction, viewId);
			return navigationCase;
		} else if ((outcome != null)
				&& outcome.startsWith(PrettyContext.PRETTY_PREFIX)
				&& config.isMappingId(outcome)) {
			/*
			 * FIXME this will not work with dynamic view IDs... figure out
			 * another solution
			 * (<rewrite-view>/faces/views/myview.xhtml</rewrite-view> ? For
			 * now. Do not support it.
			 */
			UrlMapping mapping = config.getMappingById(outcome);
			String viewId = mapping.getViewId();
			if (mapping.isDynaView()) {
				viewId = dynaview.calculateDynaviewId(context, mapping);
			}
			viewId = FacesNavigationURLCanonicalizer.normalizeRequestURI(
					context, viewId);
			if (viewId.contains("#{pageName}")) {
				viewId = viewId.replace("#{pageName}",
						(CharSequence) ((HttpServletRequest) context
								.getExternalContext().getRequest())
								.getAttribute("pageName"));
			}
			URL url = new URL(viewId);
			url.getMetadata().setLeadingSlash(true);
			QueryString qs = QueryString.build("");
			if (viewId.contains("?")) {
				qs.addParameters(viewId);
			}
			qs.addParameters("?"
					+ PrettyFacesWrappedResponse.REWRITE_MAPPING_ID_KEY + "="
					+ mapping.getId());

			viewId = url.toString() + qs.toQueryString();

			NavigationCase navigationCase = parent.getNavigationCase(context,
					fromAction, viewId);
			return navigationCase;
		} else {
			NavigationCase navigationCase = parent.getNavigationCase(context,
					fromAction, outcome);
			return navigationCase;
		}
	}

	private String replaceThemepath(FacesContext context, String outcome) {

		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		if (outcome.startsWith("themepath:")) {
			String pageName = outcome.replaceFirst("themepath:",
					"");
			Matcher matcher = REDIRECT_REGEX.matcher(pageName);
			if (matcher.find()) {
				// redirect
				pageName = matcher.replaceAll("");
				request.setAttribute("pageName", pageName.endsWith("?")?pageName.substring(0,pageName.length()-1):pageName);
				outcome = "pretty:spage";
			} else {
				// dispatcher
				PrettyContext prettyContext = PrettyContext.getCurrentInstance(context);
				PrettyConfig config = prettyContext.getConfig();
				UrlMapping mapping = config.getMappingById("spage");
				String viewId = mapping.getViewId();
				viewId = FacesNavigationURLCanonicalizer.normalizeRequestURI(
						context, viewId);
				if (viewId.contains("#{pageName}")) {
					viewId = viewId.replace("#{pageName}",pageName);
				}
				URL url = new URL(viewId);
				url.getMetadata().setLeadingSlash(true);
				QueryString qs = QueryString.build("");
				if (viewId.contains("?")) {
					qs.addParameters(viewId);
				}
				qs.addParameters("?"
						+ PrettyFacesWrappedResponse.REWRITE_MAPPING_ID_KEY
						+ "=" + mapping.getId());

				viewId = url.toString() + qs.toQueryString();
				outcome = viewId;
			}
		}
		return outcome;
	}

	@Override
	public void handleNavigation(FacesContext context, String fromAction,
			String outcome) {
		if (outcome != null) {
			outcome = replaceThemepath(context, outcome);
		}
		log.debug("Navigation requested: fromAction [" + fromAction
				+ "], outcome [" + outcome + "]");
		if (!pr.redirect(context, outcome)) {
			log.debug("Not a PrettyFaces navigation string - passing control to default nav-handler");
			PrettyContext prettyContext = PrettyContext
					.getCurrentInstance(context);
			prettyContext.setInNavigation(true);

			String originalViewId = context.getViewRoot().getViewId();
			parent.handleNavigation(context, fromAction, outcome);
			String newViewId = context.getViewRoot().getViewId();

			/*
			 * Navigation is complete if the viewId has not changed or the
			 * response is complete
			 */
			if ((true == context.getResponseComplete())
					|| originalViewId.equals(newViewId)) {

				prettyContext.setInNavigation(false);
			}
		}
	}

	@Override
	public Map<String, Set<NavigationCase>> getNavigationCases() {
		return parent.getNavigationCases();
	}

	@Override
	public void performNavigation(final String outcome) {
		parent.performNavigation(outcome);
	}
}
