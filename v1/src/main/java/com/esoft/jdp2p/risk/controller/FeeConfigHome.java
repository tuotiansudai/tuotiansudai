package com.esoft.jdp2p.risk.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.risk.model.FeeConfig;
import com.esoft.jdp2p.risk.model.RiskRank;

@Component
@Scope(ScopeType.VIEW)
public class FeeConfigHome extends EntityHome<FeeConfig> {

	private final static String UPDATE_VIEW = FacesUtil
			.redirect("/admin/risk/feeConfigList");

	public FeeConfigHome() {
		setUpdateView(UPDATE_VIEW);
	}
	
}
