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

    private final static Logger logger = Logger.getLogger(UMPayTransferBillServiceImpl.class);

    private final static int PAGE_SIZE = 10;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PaySyncClient paySyncClient;


    @Override
    public List<List<String>> getTransferBill(String loginName, Date startDate, Date endDate) {
        List<List<String>> data = Lists.newArrayList();
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel == null) {
            return data;
        }
        try {
            int pageNum = 1;
            int totalNum = 0;
            do {
                TranseqSearchResponseModel responseModel = paySyncClient.send(TranseqSearchMapper.class, new TranseqSearchRequestModel(accountModel.getPayAccountId(), pageNum, startDate, endDate), TranseqSearchResponseModel.class);
                if (responseModel.isSuccess()) {
                    totalNum = Integer.parseInt(responseModel.getTotalNum());
                    List<List<String>> humanReadableData = responseModel.generateHumanReadableData();
                    data.addAll(humanReadableData);
                    pageNum += 1;
                }
            } while ((totalNum % PAGE_SIZE == 0 ? totalNum / PAGE_SIZE : totalNum / PAGE_SIZE + 1) >= pageNum);

            return data;

        } catch (PayException | ParseException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return data;
    }
}
