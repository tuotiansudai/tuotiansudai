package com.tuotiansudai.api.service.v3_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v3_0.UserInvestListRequestDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestListResponseDataDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestRecordResponseDataDto;
import com.tuotiansudai.api.service.v3_0.MobileAppInvestListsV3Service;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.UserInvestRecordDataDto;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MobileAppInvestListsV3ServiceImpl implements MobileAppInvestListsV3Service {

    @Autowired
    private InvestService investService;

    @Override
    public BaseResponseDto<UserInvestListResponseDataDto> generateUserInvestList(UserInvestListRequestDto userInvestListRequestDto) {
        String loginName = LoginUserInfo.getLoginName();
        BasePaginationDataDto<UserInvestRecordDataDto> basePaginationDataDto = investService.generateUserInvestList(loginName,
                userInvestListRequestDto.getIndex(),
                userInvestListRequestDto.getPageSize(),
                userInvestListRequestDto.getStatus());

        UserInvestListResponseDataDto dtoData = new UserInvestListResponseDataDto();
        dtoData.setInvestList(basePaginationDataDto.getRecords().stream().map(UserInvestRecordResponseDataDto::new).collect(Collectors.toList()));
        dtoData.setIndex(userInvestListRequestDto.getIndex());
        dtoData.setPageSize(userInvestListRequestDto.getPageSize());

        dtoData.setTotalCount((int) basePaginationDataDto.getCount());

        BaseResponseDto<UserInvestListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);

        return dto;
    }
}
