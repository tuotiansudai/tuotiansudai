package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.MobileAppReferrerInvestService;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.service.ReferrerManageService;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppReferrerInvestServiceImpl implements MobileAppReferrerInvestService {
    @Autowired
    private ReferrerManageMapper referrerManageMapper;
    @Autowired
    private ReferrerManageService referrerManageService;


    @Override
    public BaseResponseDto generateReferrerInvestList(ReferrerInvestListRequestDto referrerInvestListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        ReferrerInvestListResponseDataDto referrerInvestListResponseDataDto = null;

        Integer index = referrerInvestListRequestDto.getIndex();
        Integer pageSize = referrerInvestListRequestDto.getPageSize();
        String referrerId = referrerInvestListRequestDto.getReferrerId();
        if (index == null || index <= 0) {
            index = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        String level = referrerManageService.getUserRewardDisplayLevel(referrerId);
        List<ReferrerManageView> referrerManageViewList = referrerManageMapper.findReferInvestList(referrerId, null, null, null,level, (index - 1) * pageSize, pageSize);
        List<ReferrerInvestResponseDataDto> referrerInvestResponseDataDtos = Lists.transform(referrerManageViewList, new Function<ReferrerManageView, ReferrerInvestResponseDataDto>() {
            @Override
            public ReferrerInvestResponseDataDto apply(ReferrerManageView input) {
                return new ReferrerInvestResponseDataDto(input);
            }
        });
        int count = referrerManageMapper.findReferInvestCount(referrerId, null, null, null,level);
        referrerInvestListResponseDataDto = new ReferrerInvestListResponseDataDto();
        referrerInvestListResponseDataDto.setIndex(index);
        referrerInvestListResponseDataDto.setPageSize(pageSize);
        referrerInvestListResponseDataDto.setTotalCount(count);
        referrerInvestListResponseDataDto.setRewardTotalMoney(AmountConverter.convertCentToString(referrerManageMapper.findReferInvestTotalAmount(referrerId, null, null, null,level)));
        referrerInvestListResponseDataDto.setReferrerInvestList(referrerInvestResponseDataDtos);
        dto.setData(referrerInvestListResponseDataDto);
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return dto;
    }
}
