package com.ttsd.api.service.impl;

import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.invest.model.Invest;
import com.ttsd.api.dao.MobileAppInvestListDao;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppInvestListService;
import com.ttsd.api.util.CommonUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppInvestListServiceImpl implements MobileAppInvestListService {
    @Logger
    static Log log;
    @Resource
    private MobileAppInvestListDao mobileAppInvestListDao;

    @Override
    public BaseResponseDto generateInvestList(InvestListRequestDto investListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        Integer index = investListRequestDto.getIndex();
        Integer pageSize = investListRequestDto.getPageSize();
        String loanId = investListRequestDto.getLoanId();
        String returnCode = ReturnMessage.SUCCESS.getCode();
        List<InvestRecordResponseDataDto> investRecordResponseDataDtoList = null;
        if (index == null || pageSize == null || index.intValue() <=0 || pageSize.intValue() <=0) {
            returnCode = ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode();
            log.info("成交纪录" + ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode() + ":" + ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {

            List<Invest> investList = mobileAppInvestListDao.getInvestList(index, pageSize,loanId);

            investRecordResponseDataDtoList = convertInvestRecordDto(investList);
        }

        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode) && investRecordResponseDataDtoList != null) {
            InvestListResponseDataDto investListResponseDataDto = new InvestListResponseDataDto();
            investListResponseDataDto.setInvestRecord(investRecordResponseDataDtoList);
            investListResponseDataDto.setIndex(index);
            investListResponseDataDto.setPageSize(pageSize);
            investListResponseDataDto.setTotalCount(mobileAppInvestListDao.getTotalCount(loanId));
            dto.setData(investListResponseDataDto);
        }

        return dto;
    }

    @Override
    public List<InvestRecordResponseDataDto> convertInvestRecordDto(List<Invest> investList) {
        List<InvestRecordResponseDataDto> investRecordResponseDataDtoList = new ArrayList<InvestRecordResponseDataDto>();
        for (Invest invest : investList) {
            InvestRecordResponseDataDto investRecordResponseDataDto = new InvestRecordResponseDataDto();
            investRecordResponseDataDto.setUserName(CommonUtils.encryptUserName(invest.getUser().getUsername()));
            investRecordResponseDataDto.setInvestTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(invest.getTime()).toString());
            investRecordResponseDataDto.setInvestMoney(invest.getInvestMoney());
            investRecordResponseDataDtoList.add(investRecordResponseDataDto);
        }
        return investRecordResponseDataDtoList;
    }
}
