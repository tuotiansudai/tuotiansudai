package com.esoft.jdp2p.invest.controller;

import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.core.annotations.ScopeType;

/**
 * Filename: InvestList.java Description: Copyright: Copyright (c)2013 Company:
 * jdp2p
 * 
 * @author: yinjunlu
 * @version: 1.0 Create at: 2014-1-11 下午4:27:32
 * 
 *           Modification History: Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-11 yinjunlu 1.0 1.0 Version
 */
@Component
@Scope(ScopeType.VIEW)
public class InvestList3 extends InvestList {

	public InvestList3() {
		final String[] RESTRICTIONS = {
				"invest.id like #{investList3.example.id}",
				"invest.status like #{investList3.example.status}",
				"invest.loan.user.id like #{investList3.example.loan.user.id}",
				"invest.loan.id like #{investList3.example.loan.id}",
				"invest.loan.name like #{investList3.example.loan.name}",
				"invest.loan.type like #{investList3.example.loan.type}",
				"invest.user.id = #{investList3.example.user.id}",
				"invest.user.username = #{investList3.example.user.username}",
				"invest.time >= #{investList3.searchcommitMinTime}",
				"invest.status like #{investList3.example.status}",
				"invest.time <= #{investList3.searchcommitMaxTime}"};
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

}
