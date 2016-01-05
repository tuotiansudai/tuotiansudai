package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.repository.model.InvestModel;

public interface InvestSuccessService {

    void investSuccess(long orderId, InvestModel investModel, String loginName);

}
