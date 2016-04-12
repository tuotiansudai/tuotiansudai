package com.esoft.archer.picture.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.picture.AutcMtrTypeConstants;
import com.esoft.archer.picture.model.AutcMtrPicture;
import com.esoft.archer.picture.model.AutcMtrType;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;

/**
 * 认证材料查询Home
 * 
 * @author Administrator
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class AutcMtrTypeHome extends EntityHome<AutcMtrType> {

	List<AutcMtrPicture> pics;

	public AutcMtrTypeHome() {
		setUpdateView(FacesUtil
				.redirect(AutcMtrTypeConstants.View.AUCT_TYPE_LIST));
	}

	@Override
	protected AutcMtrType createInstance() {

		AutcMtrType type = new AutcMtrType();
		return type;
	}

	@Override
	@Transactional(readOnly = false)
	public String save() {

		if (StringUtils.isEmpty(getInstance().getId())) {

			return null;

		} else {

			return super.save();
		}

	}

	public List<AutcMtrPicture> getPics() {
		return pics;
	}

	public void setPics(List<AutcMtrPicture> pics) {
		this.pics = pics;
	}

}
