package com.esoft.jdp2p.bankcode.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.jdp2p.bankcode.model.BankCode;

@Component
@Scope(ScopeType.VIEW)
public class BankCodeHome extends EntityHome<BankCode> {

	public BankCodeHome() {
		setUpdateView(FacesUtil.redirect("/admin/bankCode/bankCodeList"));
	}
}
