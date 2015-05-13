package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.ReferGradeProfitUser;
import com.esoft.archer.user.service.ReferGradePtUserService;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

import java.util.Date;
import java.util.UUID;

@Component
@Scope(ScopeType.VIEW)
public class ReferGradeUserHome extends EntityHome<ReferGradeProfitUser> implements java.io.Serializable {
	@Resource
	private ReferGradePtUserService referGradePtUserService;

	/**
	 * 后台保存推荐人
	 */
	@Override
	@Transactional(readOnly = false)
	public String save() {
		// FIXME:放在service中
		if(!StringUtils.isEmpty(getInstance().getReferrerid())){
			try {
				referGradePtUserService.getUserById(getInstance().getReferrerid());
			} catch (UserNotFoundException e) {
				FacesUtil.addErrorMessage("推荐人"+getInstance().getReferrerid()+"在系统中未进行维护,不能新增层级和收益比例!");
				return null;
			}
		}
		if(StringUtils.isEmpty(getInstance().getId())){
			String uuid =UUID.randomUUID().toString().replaceAll("-","");
			getInstance().setId(uuid);
		}
		if(StringUtils.isEmpty(getInstance().getReferrername())){
			getInstance().setReferrername(getInstance().getReferrerid());
		}
		getInstance().setInputdate(new Date());
		getInstance().setUpdatetime(new Date());
		setUpdateView(FacesUtil.redirect("/admin/user/referGradeProfitListUser"));
		return super.save();
	}
	@Transactional(readOnly = false)
	public String delete() {

		return super.delete();

	}
	@Transactional(rollbackFor = Exception.class)
	public String modifyForRefGd() {
		getInstance().setUpdatetime(new Date());
		getBaseService().merge(getInstance());
		return FacesUtil.redirect("/admin/user/referGradeProfitListUser");
	}


}
