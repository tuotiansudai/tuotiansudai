package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.ReferGradeProfitUser;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.ReferGradePtSysService;
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

	@Resource
	private ReferGradePtSysService referGradePtSysService;

	/**
	 * 后台保存推荐人
	 */
	@Override
	@Transactional(readOnly = false)
	public String save() {

		if (getInstance().getProfitRate() == null){
			FacesUtil.addErrorMessage("请您输入收益比例!");
			return null;
		}

		boolean isExistUserFlag = false;
		isExistUserFlag = referGradePtUserService.isExistUser(getInstance().getReferrer().getId());
		if (!isExistUserFlag){
			FacesUtil.addErrorMessage("推荐人"+getInstance().getReferrer().getId()+"在系统中未进行维护,不能新增层级和收益比例!");
			return null;
		}

		Integer maxGradeSys = referGradePtSysService.getMaxGradeByRole(getInstance().getReferrer().getId());//系统允许最大层级
		Integer faceGrade = getInstance().getGrade();//页面录入层级

		if (maxGradeSys != null && faceGrade > maxGradeSys){
			FacesUtil.addErrorMessage("推荐人"+getInstance().getReferrer().getId()+"增加层级不能超过系统配置最高" + maxGradeSys+"层级");
			return null;
		}
		String referrerId = getInstance().getReferrer().getId();
		boolean isExistReferrerGrade = referGradePtUserService.isExistReferrerGrade(referrerId,faceGrade);
		if (isExistReferrerGrade){
			FacesUtil.addErrorMessage("推荐人"+getInstance().getReferrer().getId()+"的层级" + faceGrade+"已经进行了配置!");
			return null;
		}


		if(StringUtils.isEmpty(getInstance().getId())){
			String uuid =UUID.randomUUID().toString().replaceAll("-","");
			getInstance().setId(uuid);
		}
		if(StringUtils.isEmpty(getInstance().getReferrerName())){
			getInstance().setReferrerName(getInstance().getReferrer().getId());
		}
		getInstance().setInputDate(new Date());
		getInstance().setUpdateTime(new Date());
		setUpdateView(FacesUtil.redirect("/admin/user/referGradeProfitListUser"));
		return super.save();
	}

	@Transactional(rollbackFor = Exception.class)
	public String modifyReferrerGrade() {
		if (getInstance().getProfitRate() == null){
			FacesUtil.addErrorMessage("请您输入收益比例!");
			return null;
		}
		getInstance().setUpdateTime(new Date());
		getBaseService().merge(getInstance());
		return FacesUtil.redirect("/admin/user/referGradeProfitListUser");
	}
	@Override
	@Transactional(readOnly=false)
	public String delete(){
		initInstance();
		return  super.delete();
	}
	@Override
	protected void initInstance() {
		super.initInstance();
		ReferGradeProfitUser instance = getInstance();
		if (this.instance.getReferrer() == null){
			this.instance.setReferrer(new User());
		}

	}


}
