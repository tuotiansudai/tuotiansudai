package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppReferrerListService;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.ReferrerRelationView;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.ReferrerManageService;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppReferrerListServiceImpl implements MobileAppReferrerListService {
    @Autowired
    private ReferrerManageMapper referrerManageMapper;
    @Autowired
    private ReferrerManageService referrerManageService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PageValidUtils pageValidUtils;

    @Override
    public BaseResponseDto<ReferrerListResponseDataDto> generateReferrerList(ReferrerListRequestDto referrerListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        String referrerId = referrerListRequestDto.getReferrerId();
        String level = referrerManageService.getUserRewardDisplayLevel(referrerId);
        int count = referrerManageMapper.findReferRelationCount(referrerId, null, null, null, level);
        Integer index = referrerListRequestDto.getIndex() <= 0 ? 1 : referrerListRequestDto.getIndex();
        Integer pageSize = pageValidUtils.validPageSizeLimit(referrerListRequestDto.getPageSize());

        List<ReferrerRelationView> referrerRelationDtos = referrerManageMapper.findReferRelationList(referrerId, null, null, null, level, (index - 1) * pageSize, pageSize);

        List<ReferrerResponseDataDto> referrerResponseDataDtos = Lists.transform(referrerRelationDtos, input -> {
            UserModel userModel = userMapper.findByLoginName(input.getLoginName());
            input.setLoginName(userModel.getMobile());
            return new ReferrerResponseDataDto(input);
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
