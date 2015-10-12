package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.ReferrerRewardDto;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.ReferrerRewardService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.paywrapper.service.UserBillService;
import com.tuotiansudai.repository.model.ReferrerRewardMessageTemplate;
import com.tuotiansudai.repository.model.SystemBillBusinessType;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.utils.AmountUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ReferrerRewardServiceImpl implements ReferrerRewardService {

    static Logger logger = Logger.getLogger(ReferrerRewardServiceImpl.class);

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private UserBillService userBillService;

    @Autowired
    private SystemBillService systemBillService;

    @Override
    @Transactional
    //TODO: puzzle
    public BaseDto<PayDataDto> getReferrerReward(ReferrerRewardDto referrerRewardDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        long orderId = referrerRewardDto.getOrderId();
        String referrerLoginName = referrerRewardDto.getReferrerLoginName();
        PayDataDto dataDto = new PayDataDto();
        long amount = AmountUtil.convertStringToCent(referrerRewardDto.getBonus());
        TransferRequestModel requestModel = new TransferRequestModel(referrerRewardDto.getParticUserId(), String.valueOf(amount), String.valueOf(orderId));
        try {
            TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class,
                    requestModel,
                    TransferResponseModel.class);
            if (responseModel.isSuccess()) {
                userBillService.transferInBalance(referrerLoginName, orderId, amount, UserBillBusinessType.REFERRER_REWARD);
                String transferOutDetailFormat = ReferrerRewardMessageTemplate.TRANSFER_OUT_DETAIL.getDescription();
                String transferOutDetail = MessageFormat.format(transferOutDetailFormat, String.valueOf(orderId), referrerLoginName, referrerRewardDto.getBonus());
                systemBillService.transferOut(amount, String.valueOf(orderId), SystemBillBusinessType.REFERRER_REWARD, transferOutDetail);
            }
            dataDto.setStatus(responseModel.isSuccess());
            dataDto.setCode(responseModel.getRetCode());
            dataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            dataDto.setStatus(false);
            logger.error(orderId + ":普通转账免密(划账)失败");
            logger.error(e.getLocalizedMessage(), e);
        }
        baseDto.setData(dataDto);
        return baseDto;
    }
}
