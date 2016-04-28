package com.esoft.archer.node.service;

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
import com.esoft.core.annotations.Logger;

/**
 * 敏感词过滤 (评论和文章)
 * @author wangz
 *
 */
public interface WordFilterService {

	/**
	 * 初始化patterns
	 */
	public List<Pattern> initPatterns();
	/**
	 * 敏感词过滤
	 * @param targetStr
	 * @return
	 */
	public String wordFilter(String targetStr);
}
