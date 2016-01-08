package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.Maps;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectAccountSearchMapper;
import com.tuotiansudai.paywrapper.repository.mapper.PtpMerQueryMapper;
import com.tuotiansudai.paywrapper.repository.mapper.UserSearchMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.ProjectAccountSearchRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.PtpMerQueryRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.UserSearchRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectAccountSearchResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.PtpMerQueryResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.UserSearchResponseModel;
import com.tuotiansudai.paywrapper.service.UMPayRealTimeStatusService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.InvestModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UMPayRealTimeStatusServiceImpl implements UMPayRealTimeStatusService {

    static Logger logger = Logger.getLogger(UMPayRealTimeStatusServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

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
            return responseModel.generateHumanReadableInfo();
        } catch (PayException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Maps.newHashMap();
    }

    @Override
    public Map<String, String> getPlatformStatus() {
        try {
            PtpMerQueryResponseModel responseModel = paySyncClient.send(PtpMerQueryMapper.class, new PtpMerQueryRequestModel(), PtpMerQueryResponseModel.class);
            return responseModel.generateHumanReadableInfo();
        } catch (PayException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Maps.newHashMap();
    }

    @Override
    public Map<String, String> getLoanStatus(long loanId) {
        try {
            ProjectAccountSearchResponseModel responseModel = paySyncClient.send(ProjectAccountSearchMapper.class, new ProjectAccountSearchRequestModel(String.valueOf(loanId)), ProjectAccountSearchResponseModel.class);
            return responseModel.generateHumanReadableInfo();
        } catch (PayException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Maps.newHashMap();
    }

    @Override
    public BaseDto<PayDataDto> checkLoanAmount(long loanId) {
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        try {
            ProjectAccountSearchResponseModel responseModel = paySyncClient.send(ProjectAccountSearchMapper.class, new ProjectAccountSearchRequestModel(String.valueOf(loanId)), ProjectAccountSearchResponseModel.class);
            dataDto.setCode(responseModel.getRetCode());
            dataDto.setMessage(responseModel.getRetMsg());
            if (responseModel.isSuccess()) {
                List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanId);
                long investAmount = 0;
                for (InvestModel successInvestModel : successInvestModels) {
                    investAmount += successInvestModel.getAmount();
                }
                dataDto.setStatus(Long.parseLong(responseModel.getBalance()) == investAmount);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return dto;
    }
}
