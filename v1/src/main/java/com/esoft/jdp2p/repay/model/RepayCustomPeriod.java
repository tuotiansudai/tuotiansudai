package com.esoft.jdp2p.repay.model;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 自定义还款阶段
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-5-22 上午11:57:54
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-5-22 wangzhi 1.0
 */
public class RepayCustomPeriod {
	/**
	 * 第几期
	 */
	private int period;
	/**
	 * 时间长度
	 */
	private int length;
	/**
	 * 本金所占总金额的比率
	 */
	private double corpusRatio;

	public double getCorpusRatio() {
		return corpusRatio;
	}

	public int getLength() {
		return length;
	}

	public int getPeriod() {
		return period;
	}

	public void setCorpusRatio(double corpusRatio) {
		this.corpusRatio = corpusRatio;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

}
