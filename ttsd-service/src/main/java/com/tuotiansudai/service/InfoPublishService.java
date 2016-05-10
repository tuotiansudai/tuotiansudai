package com.tuotiansudai.service;

import com.tuotiansudai.dto.AccountItemDataDto;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.InvestDataView;

import java.util.List;

public interface InfoPublishService {

    List<InvestDataView> getInvestDetail();

    void createInfoPublishInvestDetail();

}
