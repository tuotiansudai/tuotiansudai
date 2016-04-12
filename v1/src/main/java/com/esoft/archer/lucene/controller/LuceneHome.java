package com.esoft.archer.lucene.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.model.PageModel;
import com.esoft.archer.lucene.LuceneConstants;
import com.esoft.archer.lucene.model.ResultBean;
import com.esoft.archer.lucene.service.LuceneService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.REQUEST)
public class LuceneHome {
	@Logger
	static Log log;
	private final static StringManager sm = StringManager
			.getManager(LuceneConstants.Package);
	@Resource
	private LuceneService luceneService;
	private PageModel<ResultBean> pageModel;
	private String key;
	private String isEmpty;
	private Integer pageNo;
	private Integer pageSize;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public LuceneService getLuceneService() {
		return luceneService;
	}

	public void setLuceneService(LuceneService luceneService) {
		this.luceneService = luceneService;
	}

	public PageModel<ResultBean> getPageModel() {
		return pageModel;
	}

	public void setPageModel(PageModel<ResultBean> pageModel) {
		this.pageModel = pageModel;
	}

	public String getIsEmpty() {
		return isEmpty;
	}

	public void setIsEmpty(String isEmpty) {
		this.isEmpty = isEmpty;
	}

	public Integer getPageNo() {
		if (pageNo == null || pageNo == 0) {
			pageNo = 1;
		}
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		if (pageSize == null || pageSize == 0) {
			// TODO 可在数据库中配置
			pageSize = 15;
		}
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public PageModel<ResultBean> getResults(String value, Integer pageNo) {
		if(StringUtils.isEmpty(value.trim()))
			return this.pageModel;
		
		if (pageNo == null || pageNo == 0) {
			pageNo = 1;
		}
		if(this.pageModel == null)
		this.pageModel = luceneService.paginationResult(value,
				LuceneConstants.LUCENE_INDEX_PATH, pageNo, getPageSize());
		return pageModel;
	}

	public String rebuildIndex() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss:SS");
			String start = sdf.format(new Date());
			luceneService.rebuildIndex(LuceneConstants.LUCENE_INDEX_PATH);
			String end = sdf.format(new Date());
			FacesUtil.addInfoMessage(sm.getString("rebuildIndexSuccessful"));
			if (log.isInfoEnabled()) {
				log.info(sm.getString("log.info.rebuildIndexSuccessful", start,
						end));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(sm.getString("log.error.rebuildIndexSuccessful", e));
		}
		return null;
	}

//	/**
//	 * 获取索引所在目录
//	 * 
//	 * @param request
//	 * @return
//	 */
//	private String getAbsulotlyPath(HttpServletRequest request) {
//		String absulotlyPath = "";
//		absulotlyPath = request.getSession().getServletContext()
//				.getRealPath("");
//		// TODO /lucene/index 路径可从数据库中读取
//		absulotlyPath = absulotlyPath + "/lucene/index";
//		return absulotlyPath;
//	}

}
