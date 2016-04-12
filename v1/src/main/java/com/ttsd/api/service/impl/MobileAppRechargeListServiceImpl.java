package com.ttsd.api.service.impl;

import com.esoft.jdp2p.loan.model.Recharge;
import com.esoft.jdp2p.user.service.RechargeService;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppRechargeListService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppRechargeListServiceImpl implements MobileAppRechargeListService {
    @Resource
    private RechargeService rechargeService;

    @Override
    public BaseResponseDto generateRechargeList(RechargeListRequestDto requestDto) {
        // collect parameters
        Integer index = requestDto.getIndex();
        Integer pageSize = requestDto.getPageSize();
        String userId = requestDto.getBaseParam().getUserId();
        Integer offset = (index-1)*pageSize;

        // search for recharges
        Integer rechargeCount = rechargeService.findUserRechargeCount(userId);
        List<Recharge> rechargeList = rechargeService.findUserRecharge(userId,offset,pageSize);

        // build response dto
        List<RechargeDetailResponseDataDto> rechargeResponseList = new ArrayList<>();
        if(rechargeList != null) {
            for (Recharge recharge : rechargeList) {
                rechargeResponseList.add(new RechargeDetailResponseDataDto(recharge));
            }
        }

        RechargeListResponseDataDto dataDto = new RechargeListResponseDataDto();
        dataDto.setPageSize(pageSize);
        dataDto.setIndex(index);
        dataDto.setTotalCount(rechargeCount);
        dataDto.setRechargeList(rechargeResponseList);

        BaseResponseDto<RechargeListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dataDto);

        return dto;
    }
}
