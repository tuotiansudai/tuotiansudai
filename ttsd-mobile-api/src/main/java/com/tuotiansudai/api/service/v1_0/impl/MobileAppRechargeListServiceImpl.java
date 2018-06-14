package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppRechargeListService;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.repository.mapper.BankRechargeMapper;
import com.tuotiansudai.repository.model.BankRechargeModel;
import com.tuotiansudai.repository.model.BankRechargePaginationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppRechargeListServiceImpl implements MobileAppRechargeListService {

    @Autowired
    private BankRechargeMapper rechargeMapper;

    @Autowired
    private PageValidUtils pageValidUtils;

    @Override
    public BaseResponseDto<RechargeListResponseDataDto> generateRechargeList(RechargeListRequestDto requestDto) {
        Integer index = requestDto.getIndex();
        Integer pageSize = pageValidUtils.validPageSizeLimit(requestDto.getPageSize());

        if (index == null || index <= 0) {
            index = 1;
        }
        Integer offset = (index - 1) * pageSize;

        List<BankRechargePaginationView> rechargeModels = rechargeMapper.findRechargePagination(null, requestDto.getBaseParam().getPhoneNum(), null, null, null, offset, pageSize, null, null, "");
        int count = rechargeMapper.findRechargeCount(null, requestDto.getBaseParam().getPhoneNum(), null, null, null, null, null, "");

        List<RechargeDetailResponseDataDto> rechargeResponseList = Lists.newArrayList();
        if (rechargeModels != null) {
            for (BankRechargeModel recharge : rechargeModels) {
                rechargeResponseList.add(new RechargeDetailResponseDataDto(recharge));
            }
        }

        RechargeListResponseDataDto dataDto = new RechargeListResponseDataDto();
        dataDto.setPageSize(pageSize);
        dataDto.setIndex(index);
        dataDto.setTotalCount(count);
        dataDto.setRechargeList(rechargeResponseList);

        BaseResponseDto<RechargeListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dataDto);

        return dto;
    }
}
