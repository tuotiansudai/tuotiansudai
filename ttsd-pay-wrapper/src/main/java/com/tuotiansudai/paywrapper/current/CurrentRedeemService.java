package com.tuotiansudai.paywrapper.current;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.current.dto.RedeemRequestDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNopwdMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.AmountTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class CurrentRedeemService {

    private final static Logger logger = LoggerFactory.getLogger(CurrentRedeemService.class);

    private final static String ORDER_ID_SEPARATOR = "X";

    private final static String ORDER_ID_TEMPLATE = "{0}" + ORDER_ID_SEPARATOR + "{1}";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private AmountTransfer amountTransfer;


    public BaseDto<PayDataDto> redeemToLoan(RedeemRequestDto redeemRequestDto) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);

        String loginName = redeemRequestDto.getLoginName();
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel == null) {
            logger.error(MessageFormat.format("{0} does not exist", loginName));
            return baseDto;
        }

        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newCurrentRedeemToLoanRequest(
                MessageFormat.format(ORDER_ID_TEMPLATE, String.valueOf(redeemRequestDto.getId()), String.valueOf(new Date().getTime())),
                accountModel.getPayUserId(),
                String.valueOf(redeemRequestDto.getAmount()));

        try {
            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(ProjectTransferNopwdMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);

            if (responseModel.isSuccess()) {
                logger.info("redeem to loan success, redeem_id:{}, login_name:{}, amount:{}", redeemRequestDto.getId(), loginName, String.valueOf(redeemRequestDto.getAmount()));
                amountTransfer.transferOutBalance(loginName, redeemRequestDto.getId(), redeemRequestDto.getAmount(), UserBillBusinessType.CURRENT_REDEEM_TO_LOAN, null, null);
                // TODO: call redeem_to_user method
            }

            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
            payDataDto.setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("order_id", String.valueOf(redeemRequestDto.getId()))
                    .build()));
        } catch (Exception e) {
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getLocalizedMessage());
            logger.error(MessageFormat.format("redeem failed (id={0}, loginName={1}, amount={2}, source={3}",
                    String.valueOf(redeemRequestDto.getId()),
                    redeemRequestDto.getLoginName(),
                    String.valueOf(redeemRequestDto.getAmount()),
                    redeemRequestDto.getSource()), e);
        }
        return baseDto;
    }

}
