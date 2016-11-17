package com.tuotiansudai.cfca.dto;


public class ContractResponseView {

    private long investId;
    private String contractNo;
    private String retCode;


    public ContractResponseView(long investId, String contractNo, String retCode) {
        this.investId = investId;
        this.contractNo = contractNo;
        this.retCode = retCode;
    }

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }


}
