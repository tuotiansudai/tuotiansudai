package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.BankCardNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.PtpMerBindCardMapper;
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
        bankCardModel.setStatus(BankCardStatus.UNCHECK);


        PtpMerBindCardRequestModel requestModel = new PtpMerBindCardRequestModel(String.valueOf(bankCardModel.getId()),
                dto.getCardNumber(),
                accountModel.getPayUserId(),
                accountModel.getUserName(), accountModel.getIdentityNumber());
        try {
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(PtpMerBindCardMapper.class, requestModel);
            bankCardMapper.create(bankCardModel);

            return baseDto;
        } catch (PayException e) {
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setStatus(false);
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
            this.postBankCardCallback(callbackRequest, paramsMap);
        } catch (AmountTransferException e) {
            logger.error(e.getLocalizedMessage(),e);
        }

        return callbackRequest.getResponseData();
    }

    @Transactional
    private void postBankCardCallback(BaseCallbackRequestModel callbackRequestModel, Map<String, String> paramsMa) throws AmountTransferException {


        if (StringUtils.isNotEmpty(callbackRequestModel.getOrderId()) && !isCardNoBound(callbackRequestModel.getOrderId())) {
            long orderId = Long.parseLong(callbackRequestModel.getOrderId());
            BankCardModel bankCardModel = bankCardMapper.findById(orderId);
            if (callbackRequestModel.isSuccess()) {
                if (bankCardModel != null) {
                    bankCardModel.setStatus(BankCardStatus.PASSED);
                    bankCardModel.setBankNumber(paramsMa.get("gate_id"));
                    bankCardMapper.updateBankCard(bankCardModel);
                    //TODO 招商银行扣一分钱
                    if("CMB".equals(paramsMa.get("gate_id"))){
                        String detailTemplate = "用户{0}绑定{1}银行卡";
                        systemBillService.transferOut(1L, MessageFormat.format(detailTemplate,bankCardModel.getLoginName(),bankCardModel.getCardNumber()),"binding_card");
                    }
                }


            } else {
                bankCardMapper.update(orderId,BankCardStatus.FAIL);
            }


        }
    }

    @Override
    public boolean isCardNoBound(String id) {
        return bankCardMapper.findBankCardIsExistById(Long.parseLong(id)) > 0;
    }

}
