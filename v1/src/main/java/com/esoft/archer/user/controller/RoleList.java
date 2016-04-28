package com.esoft.archer.user.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.Role;
import com.esoft.core.annotations.ScopeType;

@Component
@Scope(ScopeType.VIEW)
public class RoleList extends EntityQuery<Role> {

	private List<Role> rolesExceptInvestorLoaner;

	public RoleList() {
		final String[] RESTRICTIONS = { "id like #{roleList.example.id}",
				"name like #{roleList.example.name}", };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	public List<Role> getRolesExceptInvestorLoaner() {
		if (rolesExceptInvestorLoaner == null) {
			rolesExceptInvestorLoaner = getHt()
					.find("from Role role where role.id!='INVESTOR' and role.id!='LOANER'");
		}
		return rolesExceptInvestorLoaner;
	}

}
