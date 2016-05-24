package com.esoft.archer.node.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.node.NodeConstants;
import com.esoft.archer.node.model.WordFilter;
import com.esoft.archer.node.service.WordFilterService;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.REQUEST)
public class WordFilterHome extends EntityHome<WordFilter> {
	@Logger
	static Log log;
	@Resource
	private WordFilterService wfs;
	private final static StringManager sm = StringManager
			.getManager(NodeConstants.Package);
	
	public WordFilterHome(){
		setUpdateView(FacesUtil.redirect("/admin/node/wordFilterList"));
		setDeleteView(FacesUtil.redirect("/admin/node/wordFilterList"));
	}
	
	@Transactional(readOnly=false)
	public String save() {
		wfs.initPatterns();
		return super.save();
	}

	@Override
	@Transactional(readOnly=false)
	public String delete() {
		if (log.isInfoEnabled()) {
			log.info(sm.getString("log.info.deleteWordFilter",
					FacesUtil
					.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
		}
		return super.delete();
	}

}
