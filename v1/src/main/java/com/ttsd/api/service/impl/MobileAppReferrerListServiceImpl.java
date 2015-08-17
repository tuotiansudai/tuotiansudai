package com.ttsd.api.service.impl;

import com.esoft.core.annotations.Logger;
import com.ttsd.api.dao.MobileAppReferrerListDao;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppReferrerListService;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MobileAppReferrerListServiceImpl implements MobileAppReferrerListService {

    @Logger
    static Log log;
    @Resource
    private MobileAppReferrerListDao mobileAppReferrerListDao;


    @Override
    public BaseResponseDto generateReferrerList(ReferrerListRequestDto referrerListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        ReferrerListResponseDataDto referrerListResponseDataDto = null;
        String referrerId = referrerListRequestDto.getReferrerId();
        Integer index = referrerListRequestDto.getIndex();
        Integer pageSize = referrerListRequestDto.getPageSize();
        String returnCode = ReturnMessage.SUCCESS.getCode();
        List<ReferrerResponseDataDto> referrerResponseDataDtos = null;
        if (index == null || pageSize == null || index.intValue() <= 0 || pageSize.intValue() <= 0) {
            returnCode = ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode();
        }
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {

            referrerResponseDataDtos = mobileAppReferrerListDao.getReferrerRelationList(index, pageSize, referrerId);
            referrerListResponseDataDto = new ReferrerListResponseDataDto();
            referrerListResponseDataDto.setReferrerList(referrerResponseDataDtos);
            referrerListResponseDataDto.setIndex(index);
            referrerListResponseDataDto.setPageSize(pageSize);
            referrerListResponseDataDto.setTotalCount(mobileAppReferrerListDao.getTotalCount(referrerId));
            dto.setData(referrerListResponseDataDto);

        }
        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        return dto;
    }

}
