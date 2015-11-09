package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.AgreementNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.PtpMerBindAgreementRequestMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.AgreementNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.PtpMerBindAgreementRequestModel;
import com.tuotiansudai.paywrapper.service.AgreementService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.AgreementType;
import com.tuotiansudai.repository.model.BankCardModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class AgreementServiceImpl implements AgreementService {

    static Logger logger = Logger.getLogger(AgreementServiceImpl.class);

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> agreement(AgreementDto dto) {
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        AgreementType agreementType = null;
        if (dto.isAutoInvest()) {
            agreementType = AgreementType.ZTBB0G00;
        } else if (dto.isFastPay()) {
            agreementType = AgreementType.ZKJP0700;
        }

        PtpMerBindAgreementRequestModel ptpMerBindAgreementRequestModel = new PtpMerBindAgreementRequestModel(accountModel.getPayUserId(), agreementType);
        try {
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(PtpMerBindAgreementRequestMapper.class, ptpMerBindAgreementRequestModel);
            return baseDto;
        } catch (PayException e) {
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setStatus(false);
            payFormDataDto.setMessage(e.getMessage());
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
    }

    @Override
    public String agreementCallback(Map<String, String> paramsMap, String queryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, queryString, AgreementNotifyMapper.class, AgreementNotifyRequestModel.class);
        if (callbackRequest == null) {
            return null;
        }
        this.postAgreementCallback(callbackRequest);
        return callbackRequest.getResponseData();
    }

    @Transactional
    private void postAgreementCallback(BaseCallbackRequestModel callbackRequestModel) {
        AgreementNotifyRequestModel agreementNotifyRequestModel = (AgreementNotifyRequestModel) callbackRequestModel;
        AccountModel accountModel = accountMapper.findByPayUserId(agreementNotifyRequestModel.getUserId());
        if (accountModel != null && callbackRequestModel.isSuccess()) {
            if (agreementNotifyRequestModel.isAutoInvest()) {
                accountModel.setAutoInvest(true);
            }
            if (agreementNotifyRequestModel.isFastPay()) {
                String loginName = accountModel.getLoginName();
                BankCardModel bankCardModel = bankCardMapper.findByLoginName(loginName);
                bankCardModel.setIsFastPayOn(true);
                bankCardMapper.updateBankCard(bankCardModel);

            }
            accountMapper.update(accountModel);
        } else {
            logger.error(MessageFormat.format("Agreement callback failed (userId = {0})", ((AgreementNotifyRequestModel) callbackRequestModel).getUserId()));
        }
    }

}
