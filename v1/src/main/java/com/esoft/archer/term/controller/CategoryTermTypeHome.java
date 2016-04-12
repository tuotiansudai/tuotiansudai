package com.esoft.archer.term.controller;

import java.util.Date;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.term.TermConstants;
import com.esoft.archer.term.model.CategoryTerm;
import com.esoft.archer.term.model.CategoryTermType;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.REQUEST)
public class CategoryTermTypeHome extends EntityHome<CategoryTermType> {

	@Logger
	static Log log;
	private static final StringManager sm = StringManager
			.getManager(TermConstants.Package);

	public CategoryTermTypeHome() {
		setUpdateView(FacesUtil.redirect(TermConstants.View.TERM_TYPE_LIST));
	}

	@Override
	@Transactional(readOnly=false)
	public String delete() {
		if (log.isInfoEnabled()) {
			log.info(sm.getString("log.info.deleteTermType",
					FacesUtil
					.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
		}
		Set<CategoryTerm> termSets =  getInstance().getCategoryTerms();
		if(termSets != null && termSets.size() > 0){
			log.info(sm.getString("log.info.deleteTermTypeUnccessful",
					FacesUtil
					.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
			FacesUtil.addWarnMessage(sm.getString("deleteTermTypeUnccessful"));
			return null;
		}else{
			return super.delete();
		}
	}

}
