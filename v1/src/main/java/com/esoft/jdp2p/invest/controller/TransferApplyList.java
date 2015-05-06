package com.esoft.jdp2p.invest.controller;

import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityQuery;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.ScopeType;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.invest.model.TransferApply;

@Component
@Scope(ScopeType.VIEW)
public class TransferApplyList extends EntityQuery<TransferApply> {

	private Double minCorpus;
	private Double maxCorpus;
	private Double maxRate;
	private Double minRate;
	private Double maxPremium;
	private Double minPremium;

	public void setMinAndMaxCorpus(double minCorpus, double maxCorpus) {
		this.minCorpus = minCorpus;
		this.maxCorpus = maxCorpus;
	}

	public void setMinAndMaxRate(double minRate, double maxRate) {
		this.minRate = minRate;
		this.maxRate = maxRate;
	}

	public void setMinAndMaxPremium(double minPremium, double maxPremium) {
		this.minPremium = minPremium;
		this.maxPremium = maxPremium;
	}

	public TransferApplyList() {
		final String[] RESTRICTIONS = {
				"transferApply.status like #{transferApplyList.example.status}",
				"transferApply.corpus >= #{transferApplyList.minCorpus}",
				"transferApply.corpus <= #{transferApplyList.maxCorpus}",
				"transferApply.invest.rate <= #{transferApplyList.maxRate}",
				"transferApply.invest.rate >= #{transferApplyList.minRate}",
				"transferApply.premium <= #{transferApplyList.maxPremium}",
				"transferApply.premium >= #{transferApplyList.minPremium}",
				"transferApply.invest.user.username like #{transferApplyList.example.invest.user.username}" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	@Override
	protected void initExample() {
		TransferApply ta = new TransferApply();
		Invest i = new Invest();
		i.setUser(new User());
		ta.setInvest(i);
		this.setExample(ta);
	}

	public Double getMinCorpus() {
		return minCorpus;
	}

	public void setMinCorpus(Double minCorpus) {
		this.minCorpus = minCorpus;
	}

	public Double getMaxCorpus() {
		return maxCorpus;
	}

	public void setMaxCorpus(Double maxCorpus) {
		this.maxCorpus = maxCorpus;
	}

	public Double getMaxRate() {
		return maxRate;
	}

	public void setMaxRate(Double maxRate) {
		this.maxRate = maxRate;
	}

	public Double getMinRate() {
		return minRate;
	}

	public void setMinRate(Double minRate) {
		this.minRate = minRate;
	}

	public Double getMaxPremium() {
		return maxPremium;
	}

	public void setMaxPremium(Double maxPremium) {
		this.maxPremium = maxPremium;
	}

	public Double getMinPremium() {
		return minPremium;
	}

	public void setMinPremium(Double minPremium) {
		this.minPremium = minPremium;
	}

}
