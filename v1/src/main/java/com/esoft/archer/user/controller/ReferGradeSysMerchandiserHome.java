package com.esoft.archer.user.controller;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.user.model.ReferGradeProfitSys;
import com.esoft.archer.user.service.ReferGradePtSysService;
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
public class ReferGradeSysMerchandiserHome extends EntityHome<ReferGradeProfitSys> implements java.io.Serializable {
	@Resource
	private ReferGradePtSysService referGradePtSysService;

	/**
	 * 后台保存用户

	 */
	@Override
	@Transactional(readOnly = false)
	public String save() {
		if (getInstance().getProfitRate() == null){
			FacesUtil.addErrorMessage("请您输入收益比例!");
			return null;
		}

		boolean isExistGradeFlag = false;

		isExistGradeFlag = referGradePtSysService.isExistMerchandiserGrade(getInstance().getGrade());
		if (isExistGradeFlag){
			FacesUtil.addErrorMessage("推荐层级"+getInstance().getGrade()+"在系统中已经进行维护,不能新增该层级!");
			return null;
		}

		if(StringUtils.isEmpty(getInstance().getId())){
			String uuid =UUID.randomUUID().toString().replaceAll("-","");
			getInstance().setId(uuid);
		}
		getInstance().setGradeRole("ROLE_MERCHANDISER");
		getInstance().setInputDate(new Date());
		getInstance().setUpdateTime(new Date());
		setUpdateView(FacesUtil.redirect("/admin/user/referGradeProfitListSysMerchandiser"));
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
		return FacesUtil.redirect("/admin/user/referGradeProfitListSysMerchandiser");
	}
	@Override
	@Transactional(readOnly=false)
	public String delete(){
		Integer maxGradeDb = referGradePtSysService.getMerchandiserMaxGrade();//数据配置最大层级
		initInstance();
		Integer gradeFace = getInstance().getGrade();//页面删除层级
		if (gradeFace.intValue() < maxGradeDb.intValue()){
			FacesUtil.addErrorMessage("删除推荐层级应按照最高层级" + maxGradeDb + "往下依次删除!");
			return null;
		}
		return super.delete();

	}
	@Override
	protected void initInstance() {
		super.initInstance();
		ReferGradeProfitSys instance = getInstance();
		if (this.instance.getGrade() == null) {
			this.instance.setGrade(referGradePtSysService.getAddHighestMerchandiserGrade());
		}
	}
}
