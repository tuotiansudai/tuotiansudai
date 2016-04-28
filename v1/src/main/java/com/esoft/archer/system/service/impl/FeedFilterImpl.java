package com.esoft.archer.system.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.output.XMLOutputter;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.archer.comment.model.Comment;
import com.esoft.archer.config.ConfigConstants;
import com.esoft.archer.config.model.Config;
import com.esoft.archer.node.NodeConstants;
import com.esoft.archer.node.model.Node;
import com.esoft.archer.system.service.AppLocalFilter;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.SpringBeanUtil;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedOutput;

@Service
public class FeedFilterImpl implements AppLocalFilter {

	@Logger
	static Log log;

	// @Resource
	HibernateTemplate ht;

	// FIMXE:这些配置都放在config表里面
	/**
	 * rss文件刷新时间，单位：秒
	 */
	private static final long FEED_REFREASH_TIME = 20;
	private static final String FEED_TAG = "/rss/";
	private static final int RESULT_SIZE = 10;
	// rss_0.90, rss_0.91, rss_0.92, rss_0.93, rss_0.94, rss_1.0, rss_2.0,
	// atom_0.3
	private static final String FEED_TYPE = "rss_2.0";

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		String requestURL = httpRequest.getRequestURL().toString();

		int index = requestURL.indexOf(FEED_TAG);
		boolean isValid = requestURL.endsWith(".rss");
		if (index == -1 || !isValid) {
			return;
		}
		String termId = requestURL.substring(index + FEED_TAG.length())
				.replace(".rss", "");
		if (StringUtils.isEmpty(termId)) {
			// FIXME:处理rss出错，可以往前台写一个符合rss格式的错误页面
			return;
		}

		HttpServletRequest hRequest = (HttpServletRequest) request;
		File termFile = new File(hRequest.getSession().getServletContext()
				.getRealPath("/")
				+ "rss\\" + termId + ".rss");
		if (!termFile.exists()
				|| (System.currentTimeMillis() - termFile.lastModified()) / 1000 > FEED_REFREASH_TIME) {
			DetachedCriteria criteria = DetachedCriteria.forClass(Node.class);
			criteria.addOrder(Order.desc("updateTime"));
			// FIXME:这里的常量？？
			criteria.add(Restrictions.eq("status", "1"));
			if (!termId.equals("mainPage")) {
				// 订阅某个term
				criteria.createAlias("categoryTerms", "term");
				criteria.add(Restrictions.eq("term.id", termId));
			}

			ht = (HibernateTemplate) SpringBeanUtil.getBeanByName("ht");

			List<Node> nodes = ht.findByCriteria(criteria, 0, RESULT_SIZE);

			// 把nodes变成rss xml
			SyndFeed feed = new SyndFeedImpl();
			feed.setFeedType(FEED_TYPE);
			feed.setTitle(ht.get(Config.class,
					ConfigConstants.Website.SITE_NAME).getValue());
			// 标题还有连接信息，node的连接地址
			feed.setLink(ht.get(Config.class, ConfigConstants.Website.SITE_DNS)
					.getValue());
			feed.setDescription(ht.get(Config.class,
					ConfigConstants.Website.SITE_SLOGAN).getValue());

			List<SyndEntry> entries = new ArrayList<SyndEntry>();
			SyndEntry entry;
			SyndContent description;
			for (Node node : nodes) {
				// 遍历node 填写内容
				entry = new SyndEntryImpl();
				entry.setTitle(node.getTitle());
				entry.setLink(ht.get(Config.class,
						ConfigConstants.Website.SITE_DNS).getValue()
						+ "/node/" + node.getId());
				entry.setPublishedDate(node.getUpdateTime());

				description = new SyndContentImpl();
				description.setType("text/html");
				description.setValue(node.getDescription());
				entry.setDescription(description);
				entries.add(entry);
			}
			feed.setEntries(entries);

			WireFeedOutput feedOutput = new WireFeedOutput();
			Document doc = null;
			try {
				doc = feedOutput.outputJDom(feed.createWireFeed());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (FeedException e) {
				e.printStackTrace();
			}
			// create the XSL processing instruction
			Map xsl = new HashMap();
			xsl.put("href",
					"http://163.dynamic.feedsportal.com/xsl/eng/rss.xsl");
			xsl.put("type", "text/xsl");
			ProcessingInstruction pXsl = new ProcessingInstruction(
					"xml-stylesheet", xsl);
			doc.addContent(0, pXsl);

			Element ele = doc.getRootElement();
			Namespace itunesNS = Namespace.getNamespace("itunes",
					"http://www.itunes.com/dtds/podcast-1.0.dtd");
			Namespace taxoNS = Namespace.getNamespace("taxo",
					"http://purl.org/rss/1.0/modules/taxonomy/");
			Namespace rdfNS = Namespace.getNamespace("rdf",
					"http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			ele.addNamespaceDeclaration(itunesNS);
			ele.addNamespaceDeclaration(taxoNS);
			ele.addNamespaceDeclaration(rdfNS);
			XMLOutputter outputter = new XMLOutputter();
			outputter.output(doc, new FileOutputStream(termFile));
			// 直接传给页面，否则会找不到文件
			response.setCharacterEncoding("UTF-8");
			outputter.output(doc, response.getOutputStream());
		}
	}
}
