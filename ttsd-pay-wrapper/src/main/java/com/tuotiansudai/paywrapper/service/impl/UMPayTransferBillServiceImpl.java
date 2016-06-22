package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.TranseqSearchMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TranseqSearchRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TranseqSearchResponseModel;
import com.tuotiansudai.paywrapper.service.UMPayTransferBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class UMPayTransferBillServiceImpl implements UMPayTransferBillService {

    static Logger logger = Logger.getLogger(UMPayTransferBillServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Override
    public List<List<String>> getTransferBill(String loginName, Date startDate, Date endDate) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        try {
            TranseqSearchResponseModel responseModel = paySyncClient.send(TranseqSearchMapper.class,
                    new TranseqSearchRequestModel("02000157970975", startDate, endDate), TranseqSearchResponseModel.class);
            if (responseModel.isSuccess()) {
                return responseModel.generateHumanReadableData();
            }
        } catch (PayException | ParseException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Lists.newArrayList();
    }
}
