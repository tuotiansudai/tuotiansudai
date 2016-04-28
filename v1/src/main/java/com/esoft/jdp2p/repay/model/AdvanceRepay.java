package com.esoft.jdp2p.repay.model;

import com.esoft.jdp2p.loan.model.Loan;

/**
 * 提前还款
 * 
 * @author Administrator
 * 
 */
public class AdvanceRepay {
	private Loan loan;
	/**本金总额*/
	private Double corpus;
	/**还款手续费*/
	private Double repayFee;
	/**给系统罚金*/
	private Double feeToSystem;
	/**给投资人罚金*/
	private Double feeToInvestor;

	public Loan getLoan() {
		return loan;
	}

	public void setLoan(Loan loan) {
		this.loan = loan;
	}

	public Double getCorpus() {
		return corpus;
	}

	public void setCorpus(Double corpus) {
		this.corpus = corpus;
	}

	public Double getFeeToSystem() {
		return feeToSystem;
	}

	public void setFeeToSystem(Double feeToSystem) {
		this.feeToSystem = feeToSystem;
	}

	public Double getFeeToInvestor() {
		return feeToInvestor;
	}

	public void setFeeToInvestor(Double feeToInvestor) {
		this.feeToInvestor = feeToInvestor;
	}

	public Double getRepayFee() {
		return repayFee;
	}

	public void setRepayFee(Double repayFee) {
		this.repayFee = repayFee;
	}

}
