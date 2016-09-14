package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPriceResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipPurchaseService;
import com.tuotiansudai.membership.repository.mapper.MembershipPriceMapper;
import com.tuotiansudai.membership.repository.model.MembershipPriceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppMembershipPurchaseServiceImpl implements MobileAppMembershipPurchaseService {

    @Autowired
    private MembershipPriceMapper membershipPriceMapper;

    @Override
    public BaseResponseDto<MembershipPriceResponseDto> getMembershipPrice() {
        List<MembershipPriceModel> all = membershipPriceMapper.findAll();

        BaseResponseDto<MembershipPriceResponseDto> dto = new BaseResponseDto<>();
        dto.setData(new MembershipPriceResponseDto(all));
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());

        return dto;
    }
}
