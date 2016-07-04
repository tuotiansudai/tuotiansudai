package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipService;
import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppMembershipServiceImpl implements MobileAppMembershipService {


    @Autowired
    private MembershipExperienceBillMapper membershipExperienceBillMapper;

    @Override
    public BaseResponseDto getMembershipExperienceBill(MembershipRequestDto requestDto) {
        String loginName = requestDto.getBaseParam().getUserId();

        int index = requestDto.getIndex();
        int pageSize = requestDto.getPageSize();

        MembershipResponseDataDto dataDto = fillMembershipDataDto(loginName, index, pageSize);
        BaseResponseDto responseDto = new BaseResponseDto<>();
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        responseDto.setData(dataDto);
        return responseDto;
    }

    private MembershipResponseDataDto fillMembershipDataDto(String loginName, Integer index, Integer pageSize) {
        MembershipResponseDataDto dataDto = new MembershipResponseDataDto();
        List<MembershipExperienceBillModel> membershipExperienceBillModels = membershipExperienceBillMapper.findMembershipExperienceBillByLoginName(loginName, null, null, (index - 1) * pageSize, pageSize);
        List<MembershipExperienceBillDataDto> membershipExperienceBillDtos = CollectionUtils.isEmpty(membershipExperienceBillModels) ? new ArrayList<MembershipExperienceBillDataDto>() :
                Lists.transform(membershipExperienceBillModels, new Function<MembershipExperienceBillModel, MembershipExperienceBillDataDto>() {
                    @Override
                    public MembershipExperienceBillDataDto apply(MembershipExperienceBillModel model) {
                        return new MembershipExperienceBillDataDto(model);
                    }
                });
        long count = membershipExperienceBillMapper.findMembershipExperienceBillCountByLoginName(loginName, null, null);
        dataDto.setIndex(index);
        dataDto.setPageSize(pageSize);
        dataDto.setTotalCount(count);
        dataDto.setMembershipExperienceBill(membershipExperienceBillDtos);
        return dataDto;
    }
}
