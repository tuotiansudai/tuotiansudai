package com.esoft.archer.urlmapping.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.urlmapping.UrlMappingConstants;
import com.esoft.archer.urlmapping.model.UrlMapping;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;
import com.ocpsoft.pretty.MyPrettyFilter;
import com.ocpsoft.pretty.PrettyContext;

@Component
@Scope(ScopeType.VIEW)
public class UrlMappingHome extends EntityHome<UrlMapping> {
	@Logger
	static Log log;
	private final static StringManager sm = StringManager
			.getManager(UrlMappingConstants.Package);

	public UrlMappingHome() {
		setUpdateView(FacesUtil
				.redirect(UrlMappingConstants.View.URL_MAPPING_LIST));
		setDeleteView(FacesUtil
				.redirect(UrlMappingConstants.View.URL_MAPPING_LIST));
	}

	@Transactional(readOnly = false)
	public String save() {

		// 判重 判断pattern是否重复
		List<UrlMapping> mappings = getBaseService().findByNamedQuery(
				"UrlMapping.findByPattern", getInstance().getPattern());
		if (mappings.size() > 0) {
			// 编辑mapping的时候本身的的 pattern已经存在于系统中
			if (!(mappings.size() == 1 && StringUtils.equals(mappings.get(0)
					.getId(), getInstance().getId()))) {
				FacesUtil.addErrorMessage(sm.getString("patternExist"));
				return null;
			}
		}

		// 清除当前用户session里的pretty-config
		// FacesUtil.getHttpSession().removeAttribute(PrettyContext.CONFIG_KEY);
		// 清除application中的pretty-config
		FacesUtil.getHttpSession().getServletContext()
				.removeAttribute(MyPrettyFilter.CONFIG_KEY);
		return super.save();
	}

	@Override
	@Transactional(readOnly = false)
	public String delete() {
		if (log.isInfoEnabled()) {
			log.info(sm.getString("log.info.deleteUrlMapping", FacesUtil
					.getExpressionValue("#{loginUserInfo.loginUserId}"),
					new Date(), getId()));
		}
		// 清除当前用户session里的pretty-config
		FacesUtil.getHttpSession().removeAttribute(PrettyContext.CONFIG_KEY);
		return super.delete();
	}
}
