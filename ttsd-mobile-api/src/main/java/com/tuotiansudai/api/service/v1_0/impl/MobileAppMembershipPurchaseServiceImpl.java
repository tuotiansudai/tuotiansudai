package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipPurchaseService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.membership.exception.MembershipIsPurchasedException;
import com.tuotiansudai.membership.exception.NotEnoughAmountException;
import com.tuotiansudai.membership.repository.mapper.MembershipPriceMapper;
import com.tuotiansudai.membership.repository.model.MembershipPriceModel;
import com.tuotiansudai.membership.service.MembershipPurchaseService;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.List;

@Service
public class MobileAppMembershipPurchaseServiceImpl implements MobileAppMembershipPurchaseService {

    private static Logger logger = Logger.getLogger(MobileAppMembershipPurchaseServiceImpl.class);

    @Autowired
    private MembershipPriceMapper membershipPriceMapper;

    @Autowired
    private MembershipPurchaseService membershipPurchaseService;

    @Override
    public BaseResponseDto<MembershipPriceResponseDto> getMembershipPrice() {
        List<MembershipPriceModel> all = membershipPriceMapper.findAll();

        BaseResponseDto<MembershipPriceResponseDto> dto = new BaseResponseDto<>();
        dto.setData(new MembershipPriceResponseDto(all));
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());

        return dto;
    }

    @Override
    public BaseResponseDto<MembershipPurchaseResponseDataDto> purchase(MembershipPurchaseRequestDto requestDto) {
        BaseResponseDto<MembershipPurchaseResponseDataDto> response = new BaseResponseDto<>(ReturnMessage.MEMBERSHIP_PURCHASE_FAILED);
        try {
            BaseDto<PayFormDataDto> formData = membershipPurchaseService.purchase(LoginUserInfo.getLoginName(), requestDto.getLevel(), requestDto.getDuration(), Source.valueOf(requestDto.getBaseParam().getPlatform().toUpperCase()));
            if (formData.isSuccess()) {
                response = new BaseResponseDto<>(ReturnMessage.SUCCESS);
                MembershipPurchaseResponseDataDto dataDto = new MembershipPurchaseResponseDataDto();
                dataDto.setRequestData(CommonUtils.mapToFormData(formData.getData().getFields()));
                dataDto.setUrl(formData.getData().getUrl());
                response.setData(dataDto);
            }
        } catch (MembershipIsPurchasedException e) {
            logger.error(e.getLocalizedMessage(), e);
            response = new BaseResponseDto<>(ReturnMessage.MEMBERSHIP_IS_PURCHASED);
        } catch (NotEnoughAmountException e) {
            logger.error(e.getLocalizedMessage(), e);
            response = new BaseResponseDto<>(ReturnMessage.MEMBERSHIP_PURCHASE_NO_ENOUGH_AMOUNT);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return response;
    }
}
