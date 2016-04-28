package com.esoft.archer.user.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.exception.MinPointLimitCannotMattchSeqNumException;
import com.esoft.archer.user.exception.SeqNumAlreadyExistException;
import com.esoft.archer.user.model.LevelForUser;
import com.esoft.archer.user.model.UserLevelHistory;
import com.esoft.archer.user.service.LevelService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.StringManager;

@Component
@Scope(ScopeType.VIEW)
public class LevelHome extends EntityHome<LevelForUser> {

	@Resource
	private LevelService levelService;

	/**
	 * 增加或修改等级方法
	 */
	@Override
	public String save() {
		if (StringUtils.isEmpty(getInstance().getId())) {
			getInstance().setId(IdGenerator.randomUUID());
		}
		try {
			levelService.saveOrUpdate(getInstance());
		} catch (SeqNumAlreadyExistException e) {
			FacesUtil.addErrorMessage("等级序号已存在！");
		} catch (MinPointLimitCannotMattchSeqNumException e) {
			FacesUtil.addErrorMessage(" 等级积分下限的顺序，与等级序号的顺序不匹配！");
		}
		FacesUtil.addInfoMessage("保存成功！");
		// TODO:return 等级list
		return "等级list";
	}
}
