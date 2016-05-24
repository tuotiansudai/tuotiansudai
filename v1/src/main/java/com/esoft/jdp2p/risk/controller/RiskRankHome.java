package com.esoft.jdp2p.risk.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.risk.model.RiskRank;

@Component
@Scope(ScopeType.REQUEST)
public class RiskRankHome extends EntityHome<RiskRank> {

	private final static String UPDATE_VIEW = FacesUtil
			.redirect("/admin/risk/riskRankList");

	public RiskRankHome() {
		// FIXME：保存角色的时候会执行一条更新User的语句
		setUpdateView(UPDATE_VIEW);
	}
	
}
