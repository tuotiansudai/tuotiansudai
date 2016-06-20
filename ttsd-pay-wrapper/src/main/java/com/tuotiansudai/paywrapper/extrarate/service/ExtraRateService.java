package com.tuotiansudai.paywrapper.extrarate.service;


public interface ExtraRateService {

    void normalRepay(long loanRepayId);

    void advanceRepay(long loanRepayId);

}
