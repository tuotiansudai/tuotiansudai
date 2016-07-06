package com.tuotiansudai.paywrapper.extrarate.service;


import com.tuotiansudai.repository.model.InvestExtraRateModel;

public interface InvestRateService {

    void updateExtraRateData(InvestExtraRateModel investExtraRateModel, long actualInterest, long actualFee) throws Exception;

}
