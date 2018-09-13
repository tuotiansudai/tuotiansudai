package com.tuotiansudai.borrow.service;

import com.tuotiansudai.borrow.dto.response.AuthenticationResponseDto;
import com.tuotiansudai.borrow.dto.response.BaseResponseDto;
import com.tuotiansudai.borrow.dto.response.PayFormResponseDto;
import com.tuotiansudai.borrow.utils.CommonUtils;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.AnxinSignPropertyMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

@Service
public class BorrowService {

    private static Logger logger = LoggerFactory.getLogger(BorrowService.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    public BaseResponseDto<AuthenticationResponseDto> isAuthentication(String mobile) {
        BaseResponseDto<AuthenticationResponseDto> baseResponseDto = new BaseResponseDto<>(true);
        UserModel userModel = userMapper.findByMobile(mobile);
        if (userModel == null) {
            baseResponseDto.setData(new AuthenticationResponseDto());
            return baseResponseDto;
        }
        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        baseResponseDto.setData(new AuthenticationResponseDto(accountModel != null,
                bankCardMapper.findPassedBankCardByLoginName(userModel.getLoginName()) != null,
                accountModel != null && accountModel.isAutoRepay(),
                anxinSignPropertyMapper.findByLoginName(userModel.getLoginName()) != null));
        return baseResponseDto;
    }

    public BaseResponseDto openAutoRepay(String mobile) {
        UserModel userModel = userMapper.findByMobile(mobile);
        AccountModel accountModel = userModel == null ? null : accountMapper.findByLoginName(userModel.getLoginName());
        if (accountModel == null) {
            return new BaseResponseDto("未实名认证");
        }

        if (accountModel.isAutoRepay()) {
            return new BaseResponseDto("已开通免密还款");
        }
        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setAutoRepay(true);
        agreementDto.setLoginName(userModel.getLoginName());
        agreementDto.setSource(Source.HUI_ZU);
        BaseDto<PayFormDataDto> payFormDataDto = payWrapperClient.agreement(agreementDto);

        try {
            if (payFormDataDto.isSuccess()) {
                PayFormResponseDto payFormResponseDto = new PayFormResponseDto(payFormDataDto.getData().getUrl(), CommonUtils.mapToFormData(payFormDataDto.getData().getFields()));
                BaseResponseDto<PayFormResponseDto> baseResponseDto = new BaseResponseDto<>();
                baseResponseDto.setStatus(true);
                baseResponseDto.setData(payFormResponseDto);
                return baseResponseDto;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(MessageFormat.format("borrow open auto repay fail error:{0}", e.getMessage()));
        }
        return new BaseResponseDto("免密还款开通失败");
    }
}
