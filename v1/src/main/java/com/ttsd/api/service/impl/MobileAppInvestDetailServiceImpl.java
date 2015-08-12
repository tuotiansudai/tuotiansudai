package com.ttsd.api.service.impl;

import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.model.Invest;
import com.ttsd.api.dao.MobileAppInvestDetailDao;
import com.ttsd.api.dao.MobileAppInvestListDao;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppInvestDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppInvestDetailServiceImpl implements MobileAppInvestDetailService {

    @Resource
    private MobileAppInvestDetailDao mobileAppInvestDetailDao;

    @Override
    public BaseResponseDto generateUserInvestDetail(InvestDetailRequestDto requestDto) {
        String userId = requestDto.getBaseParam().getUserId();
        String investId = requestDto.getInvestId();
        BaseResponseDto dto = new BaseResponseDto();

        Invest invest= mobileAppInvestDetailDao.getInvest(investId, userId);
        if(invest == null){
            dto.setCode(ReturnMessage.INVEST_CAN_NOT_BE_FOUND.getCode());
            dto.setMessage(ReturnMessage.INVEST_CAN_NOT_BE_FOUND.getMsg());
        }else {
            InvestDetailResponseDataDto dtoData = new InvestDetailResponseDataDto(invest);
            dto.setCode(ReturnMessage.SUCCESS.getCode());
            dto.setMessage(ReturnMessage.SUCCESS.getMsg());
            dto.setData(dtoData);
        }
        return dto;
    }
}
