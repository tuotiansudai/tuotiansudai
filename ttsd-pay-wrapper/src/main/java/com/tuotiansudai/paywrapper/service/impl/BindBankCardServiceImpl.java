package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.BankCardApplyNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.BankCardNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.PtpMerBindCardMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.AsyncServiceType;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BankCardApplyNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BankCardNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.PtpMerBindCardRequestModel;
import com.tuotiansudai.paywrapper.service.BindBankCardService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class BindBankCardServiceImpl implements BindBankCardService {
    static Logger logger = Logger.getLogger(BindBankCardServiceImpl.class);
    @Autowired
    private PayAsyncClient payAsyncClient;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private BankCardMapper bankCardMapper;
    @Autowired
    private SystemBillService systemBillService;

    @Override
    public BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto) {
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());

        BankCardModel bankCardModel = new BankCardModel(dto);
        bankCardModel.setId(idGenerator.generate());
        bankCardModel.setStatus(BankCardStatus.UNCHECKED);

        PtpMerBindCardRequestModel requestModel = new PtpMerBindCardRequestModel(String.valueOf(bankCardModel.getId()),
                dto.getCardNumber(),
                accountModel.getPayUserId(),
                accountModel.getUserName(), accountModel.getIdentityNumber(),dto.getSource());
        try {
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(PtpMerBindCardMapper.class, requestModel);
            bankCardMapper.create(bankCardModel);

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
    public String bindBankCardCallback(Map<String, String> paramsMap, String originalQueryString) {

        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, BankCardNotifyMapper.class, BankCardNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        try {
            String service = callbackRequest.getService();
            if (AsyncServiceType.MER_BIND_CARD_NOTIFY.getCode().equals(service)) {
                this.postBankCardCallback(callbackRequest, paramsMap.get("gate_id"));
            }
        } catch (AmountTransferException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return callbackRequest.getResponseData();
    }

    @Override
    public String bindBankCardApplyCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, BankCardApplyNotifyMapper.class, BankCardApplyNotifyRequestModel.class);
        if (callbackRequest == null) {
            return null;
        }

        return callbackRequest.getResponseData();
    }

    @Transactional
    private void postBankCardCallback(BaseCallbackRequestModel callbackRequestModel, String bankCode) throws AmountTransferException {
        if (StringUtils.isNotEmpty(callbackRequestModel.getOrderId())) {
            long orderId = Long.parseLong(callbackRequestModel.getOrderId());
            BankCardModel bankCardModel = bankCardMapper.findById(orderId);
            if (callbackRequestModel.isSuccess()) {
                if (bankCardModel != null) {
                    bankCardModel.setStatus(BankCardStatus.PASSED);
                    bankCardModel.setBankCode(bankCode);
                    bankCardMapper.updateBankCard(bankCardModel);
                    if ("CMB".equals(bankCode)) {
                        String detailTemplate = "用户{0}绑定{1}银行卡";
                        systemBillService.transferOut(1L,
                                callbackRequestModel.getOrderId(),
                                SystemBillBusinessType.BIND_CARD,
                                MessageFormat.format(detailTemplate, bankCardModel.getLoginName(), bankCardModel.getCardNumber()));
                    }
                }
            } else {
                bankCardMapper.update(orderId, BankCardStatus.FAILED);
            }


        }
    }


}
