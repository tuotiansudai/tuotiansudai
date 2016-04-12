package com.ttsd.api.service.impl;

import com.esoft.core.annotations.Logger;
import com.ttsd.api.dao.MobileAppReferrerInvestListDao;
import com.ttsd.api.dao.MobileAppReferrerListDao;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppReferrerInvestService;
import com.ttsd.api.service.MobileAppReferrerListService;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MobileAppReferrerInvestListServiceImpl implements MobileAppReferrerInvestService {

    @Logger
    static Log log;
    @Resource
    private MobileAppReferrerInvestListDao mobileAppReferrerInvestListDao;

    @Override
    public BaseResponseDto generateReferrerInvestList(ReferrerInvestListRequestDto referrerInvestListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        List<ReferrerInvestResponseDataDto> referrerInvestResponseDataDtos = null;
        ReferrerInvestListResponseDataDto referrerInvestListResponseDataDto = null;

        Integer index = referrerInvestListRequestDto.getIndex();
        Integer pageSize = referrerInvestListRequestDto.getPageSize();
        String referrerId = referrerInvestListRequestDto.getReferrerId();
        String returnCode = ReturnMessage.SUCCESS.getCode();
        if (index == null || pageSize == null || index.intValue() <= 0 || pageSize.intValue() <= 0) {
            returnCode = ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode();
        }
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {
            referrerInvestResponseDataDtos = mobileAppReferrerInvestListDao.getReferrerInvestList(index,pageSize,referrerId);
            referrerInvestListResponseDataDto = new ReferrerInvestListResponseDataDto();
            referrerInvestListResponseDataDto.setIndex(index);
            referrerInvestListResponseDataDto.setPageSize(pageSize);
            referrerInvestListResponseDataDto.setTotalCount(mobileAppReferrerInvestListDao.getTotalCount(referrerId));
            referrerInvestListResponseDataDto.setRewardTotalMoney("" + mobileAppReferrerInvestListDao.getRewardTotalMoney(referrerId));
            referrerInvestListResponseDataDto.setReferrerInvestList(referrerInvestResponseDataDtos);
            dto.setData(referrerInvestListResponseDataDto);
        }
        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        return dto;
    }
}
