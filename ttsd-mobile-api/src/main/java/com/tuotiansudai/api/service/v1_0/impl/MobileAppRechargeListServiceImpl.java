package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppRechargeListService;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.RechargeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MobileAppRechargeListServiceImpl implements MobileAppRechargeListService {
    @Autowired
    private RechargeMapper rechargeMapper;
    @Override
    public BaseResponseDto generateRechargeList(RechargeListRequestDto requestDto) {
        Integer index = requestDto.getIndex();
        Integer pageSize = requestDto.getPageSize();
        String userId = requestDto.getBaseParam().getUserId();
        Integer offset = (index-1)*pageSize;
        List<RechargeModel> rechargeModels = rechargeMapper.findRechargePagination(null, userId, null, null, null, offset, pageSize, null, null);
        int count = rechargeMapper.findRechargeCount(null, userId, null, null, null, null, null);

        List<RechargeDetailResponseDataDto> rechargeResponseList = Lists.newArrayList();
        if(rechargeModels != null) {
            for (RechargeModel recharge : rechargeModels) {
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
