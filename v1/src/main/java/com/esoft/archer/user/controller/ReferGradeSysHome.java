package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.user.exception.UserFoundException;
import com.esoft.archer.user.model.ReferGradeProfitSys;
import com.esoft.archer.user.service.ReferGradePtSysService;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.thoughtworks.xstream.mapper.Mapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Component
@Scope(ScopeType.VIEW)
public class ReferGradeSysHome extends EntityHome<ReferGradeProfitSys> implements java.io.Serializable {
	@Resource
	private ReferGradePtSysService referGradePtSysService;

	/**
	 * 后台保存用户
	 */
	@Override
	@Transactional(readOnly = false)
	public String save() {
		// FIXME:放在service中
		try {
			if(getInstance().getGrade() != null){
				referGradePtSysService.getByGrade(getInstance().getGrade());
			}
		} catch (UserFoundException e) {
			FacesUtil.addErrorMessage("推荐层级"+getInstance().getGrade()+"在系统中已经进行维护,不能新增该层级!");
			return null;
		}
		if(StringUtils.isEmpty(getInstance().getId())){
			String uuid =UUID.randomUUID().toString().replaceAll("-","");
			getInstance().setId(uuid);
		}
		getInstance().setInputDate(new Date());
		getInstance().setUpdateTime(new Date());
		setUpdateView(FacesUtil.redirect("/admin/user/referGradeProfitListSys"));
		return super.save();
	}
	@Transactional(readOnly = false)
	public String delete() {

		return super.delete();

	}
	@Transactional(rollbackFor = Exception.class)
	public String modifyForRefGd() {
		getInstance().setUpdateTime(new Date());
		getBaseService().merge(getInstance());
		return FacesUtil.redirect("/admin/user/referGradeProfitListSys");
	}


}
