package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.Maps;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectAccountSearchMapper;
import com.tuotiansudai.paywrapper.repository.mapper.PtpMerQueryMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferSearchMapper;
import com.tuotiansudai.paywrapper.repository.mapper.UserSearchMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.ProjectAccountSearchRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.PtpMerQueryRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferSearchRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.UserSearchRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectAccountSearchResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.PtpMerQueryResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferSearchResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.UserSearchResponseModel;
import com.tuotiansudai.paywrapper.service.UMPayRealTimeStatusService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UMPayRealTimeStatusServiceImpl implements UMPayRealTimeStatusService {

    static Logger logger = Logger.getLogger(UMPayRealTimeStatusServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Override
    public Map<String, String> getUserStatus(String loginName) {
        AccountModel model = accountMapper.findByLoginName(loginName);
        if (model == null) {
            return Maps.newHashMap();
        }

        try {
            UserSearchResponseModel responseModel = paySyncClient.send(UserSearchMapper.class, new UserSearchRequestModel(model.getPayUserId()), UserSearchResponseModel.class);
            if (responseModel.isSuccess()) {
                return responseModel.generateHumanReadableInfo();
            }
        } catch (PayException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    @Override
    public Map<String, String> getPlatformStatus() {
        try {
            PtpMerQueryResponseModel responseModel = paySyncClient.send(PtpMerQueryMapper.class, new PtpMerQueryRequestModel(), PtpMerQueryResponseModel.class);
            if (responseModel.isSuccess()) {
                return responseModel.generateHumanReadableInfo();
            }
        } catch (PayException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Maps.newHashMap();
    }

    @Override
    public Map<String, String> getLoanStatus(long loanId) {
        try {
            ProjectAccountSearchResponseModel responseModel = paySyncClient.send(ProjectAccountSearchMapper.class, new ProjectAccountSearchRequestModel(String.valueOf(loanId)), ProjectAccountSearchResponseModel.class);
            if (responseModel.isSuccess()) {
                return responseModel.generateHumanReadableInfo();
            }
        } catch (PayException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Maps.newHashMap();
    }

    @Override
    public Map<String, String> getTransferStatus(String orderId, String merDate, String businessType) {
        try {
            TransferSearchResponseModel responseModel = paySyncClient.send(TransferSearchMapper.class, new TransferSearchRequestModel(orderId, merDate, businessType), TransferSearchResponseModel.class);
            if (responseModel.isSuccess()) {
                return responseModel.generateHumanReadableInfo();
            }
        } catch (PayException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Maps.newHashMap();
    }
}
