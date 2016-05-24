package com.esoft.jdp2p.risk.service;


/**
 * 风控service
 * @author Administrator
 *
 */
public interface RiskService {

	/**
	 * 获取风险保障金（投资时候扣除的）利率
	 * @return
	 */
	public double getRIRateByRank(String riskRankId);
	
	/**
	 * 获取风险准备金利率（发起借款时候收取）
	 * @return
	 */
	public double getRPRateByRank(String riskRankId);
	
	/**
	 * 获取风险准备金利率（企业借款收取）
	 * @return
	 */
	public double getELRPRateByRank(String riskRankId);
	
}
