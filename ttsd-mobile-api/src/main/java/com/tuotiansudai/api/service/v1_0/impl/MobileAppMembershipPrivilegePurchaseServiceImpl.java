package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipPrivilegePurchaseService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.membership.exception.MembershipPrivilegeIsPurchasedException;
import com.tuotiansudai.membership.exception.NotEnoughAmountException;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePriceType;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MobileAppMembershipPrivilegePurchaseServiceImpl implements MobileAppMembershipPrivilegePurchaseService {

    static Logger logger = Logger.getLogger(MobileAppMembershipPrivilegePurchaseServiceImpl.class);

    @Autowired
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    @Override
    public MembershipPrivilegePriceResponseDto obtainMembershipPrivilegePrices() {
        List<MembershipPrivilegePriceResponseDataDto> membershipPrivilegePriceResponseDataDtos = Arrays.stream(MembershipPrivilegePriceType.values())
                .map(membershipPrivilegePriceType -> new MembershipPrivilegePriceResponseDataDto(membershipPrivilegePriceType))
                .collect(Collectors.toList());

        return new MembershipPrivilegePriceResponseDto(membershipPrivilegePriceResponseDataDtos);
    }

    @Override
    public BaseResponseDto<MembershipPrivilegePurchaseResponseDataDto> purchase(MembershipPrivilegePurchaseRequestDto requestDto) {
        BaseResponseDto<MembershipPrivilegePurchaseResponseDataDto> response = new BaseResponseDto<>(ReturnMessage.MEMBERSHIP_PRIVILEGE_PURCHASE_FAILED);

        try {
            MembershipPrivilegePriceType membershipPrivilegePriceType = MembershipPrivilegePriceType.getPriceTypeByDuration(requestDto.getDuration());
            if (membershipPrivilegePriceType == null) {
                logger.info(String.format("[membership privilege purchase] duration%s is invalid", String.valueOf(requestDto.getDuration())));
                return response;
            }
            BaseDto<PayFormDataDto> formData = membershipPrivilegePurchaseService.purchase(requestDto.getBaseParam().getUserId(), membershipPrivilegePriceType, Source.valueOf(requestDto.getBaseParam().getPlatform().toUpperCase()));
            if (formData.isSuccess()) {
                response = new BaseResponseDto<>(ReturnMessage.SUCCESS);
                MembershipPrivilegePurchaseResponseDataDto dataDto = new MembershipPrivilegePurchaseResponseDataDto();
                dataDto.setRequestData(CommonUtils.mapToFormData(formData.getData().getFields()));
                dataDto.setUrl(formData.getData().getUrl());
                response.setData(dataDto);
            }
        } catch (MembershipPrivilegeIsPurchasedException e) {
            logger.warn(e.getLocalizedMessage(), e);
            response = new BaseResponseDto<>(ReturnMessage.MEMBERSHIP_PRIVILEGE_IS_PURCHASED);
        } catch (NotEnoughAmountException e) {
            logger.warn(e.getLocalizedMessage(), e);
            response = new BaseResponseDto<>(ReturnMessage.MEMBERSHIP_PRIVILEGE_PURCHASE_NO_ENOUGH_AMOUNT);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return response;
    }

}
