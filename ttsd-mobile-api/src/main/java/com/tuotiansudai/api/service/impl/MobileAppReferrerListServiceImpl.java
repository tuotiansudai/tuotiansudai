package com.tuotiansudai.api.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppReferrerListService;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.model.ReferrerRelationView;
import com.tuotiansudai.service.ReferrerManageService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppReferrerListServiceImpl implements MobileAppReferrerListService {
    @Autowired
    private ReferrerManageMapper referrerManageMapper;
    @Autowired
    private ReferrerManageService referrerManageService;

    @Override
    public BaseResponseDto generateReferrerList(ReferrerListRequestDto referrerListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        String referrerId = referrerListRequestDto.getReferrerId();
        Integer index = referrerListRequestDto.getIndex();
        Integer pageSize = referrerListRequestDto.getPageSize();
        if(index == null || index.intValue() <= 0){
            index = 1;
        }
        if(pageSize == null || pageSize.intValue() <= 0){
            pageSize = 10;
        }
        String level = referrerManageService.getUserRewardDisplayLevel(referrerId);
        List<ReferrerRelationView> referrerRelationDtos = referrerManageMapper.findReferRelationList(referrerId,null,null,null,level,(index - 1) * pageSize,pageSize);
        int count = referrerManageMapper.findReferRelationCount(referrerId, null, null, null,level);
        List<ReferrerResponseDataDto> referrerResponseDataDtos = Lists.transform(referrerRelationDtos, new Function<ReferrerRelationView, ReferrerResponseDataDto>() {
            @Override
            public ReferrerResponseDataDto apply(ReferrerRelationView input) {
                return new ReferrerResponseDataDto(input);
            }
        });
        ReferrerListResponseDataDto referrerListResponseDataDto = new ReferrerListResponseDataDto();
        referrerListResponseDataDto.setReferrerList(referrerResponseDataDtos);
        referrerListResponseDataDto.setIndex(index);
        referrerListResponseDataDto.setPageSize(pageSize);
        referrerListResponseDataDto.setTotalCount(count);
        dto.setData(referrerListResponseDataDto);

        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return dto;
    }
}
