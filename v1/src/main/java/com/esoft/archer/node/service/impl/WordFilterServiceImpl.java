package com.esoft.archer.node.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.node.model.WordFilter;
import com.esoft.archer.node.service.WordFilterService;
import com.esoft.core.annotations.Logger;

@Service("wordFilterService")
@Transactional
public class WordFilterServiceImpl implements WordFilterService{

	@Resource
	HibernateTemplate ht;
	@Logger
	static Log log;

	private List<Pattern> patterns;

	private List<Pattern> getPatterns() {
		if (patterns == null) {
			patterns = initPatterns();
		}
		return patterns;
	}

	public void setPatterns(List<Pattern> patterns) {
		this.patterns = patterns;
	}

	public String wordFilter(String targetStr) {
//		long time = System.currentTimeMillis();
		for (Pattern pattern : getPatterns()) {
			//替换词语
			targetStr = pattern.matcher(targetStr).replaceAll("***");
		}
//		System.out.println(System.currentTimeMillis() - time);
		return targetStr;
	}

	// FIXME:敏感词字与字之间用空格隔开
	public List<Pattern> initPatterns() {
		List<WordFilter> wordFilters = ht
				.findByNamedQuery("WordFilter.findAllWordFilters");
		List<Pattern> ptns = new ArrayList<Pattern>();
		for (WordFilter wordFilter : wordFilters) {
			ptns.add(Pattern.compile("(?:"
					+ wordFilter.getWord().replace(" ", "\\p{ASCII}{0,100}")
					+ ")"));
		}
		return ptns;
	}
}
