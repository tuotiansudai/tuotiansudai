package com.esoft.core.util;

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.esoft.archer.lucene.LuceneConstants;

public class FilterCharUtil {
	private final static Log log = LogFactory.getLog(FilterCharUtil.class);
	private final static StringManager sm = StringManager
			.getManager(LuceneConstants.Package);

	public static String Html2Text(String inputString) {
		String htmlStr = inputString;
		String textStr = "";
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			String regEx_html = "</?[^<>]*>";

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll("");

			textStr = htmlStr;

		} catch (Exception e) {
			e.printStackTrace();
			log.error(sm.getString("log.changeHtml.error",e));
		}

		return textStr;
	}

}
