package com.ttsd.api.service.impl;

import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.invest.model.Invest;
import com.ttsd.api.dao.InvestListDao;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileInvestListAppService;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileInvestListAppServiceImpl implements MobileInvestListAppService {
    @Logger
    static Log log;
    @Resource
    private InvestListDao investListDao;

    @Override
    public BaseResponseDto generateInvestList(InvestListRequestDto investListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        Integer index = investListRequestDto.getIndex();
        Integer pageSize = investListRequestDto.getPageSize();
        String loanId = investListRequestDto.getLoanId();
        String returnCode = ReturnMessage.SUCCESS.getCode();
        List<InvestRecordDto> investRecordDtoList = null;
        if (index == null || pageSize == null || index.intValue() <=0 || pageSize.intValue() <=0) {
            returnCode = ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode();
            log.info("成交纪录" + ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode() + ":" + ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {

            List<Invest> investList = investListDao.getInvestList(index, pageSize,loanId);

            investRecordDtoList = convertInvestRecordDto(investList);
        }

        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode) && investRecordDtoList != null) {
            InvestListResponseDataDto investListResponseDataDto = new InvestListResponseDataDto();
            investListResponseDataDto.setInvestRecord(investRecordDtoList);
            investListResponseDataDto.setIndex(index);
            investListResponseDataDto.setPageSize(pageSize);
            investListResponseDataDto.setTotalCount(investListDao.getTotalCount(loanId));
            dto.setData(investListResponseDataDto);
        }

        return dto;
    }

    @Override
    public List<InvestRecordDto> convertInvestRecordDto(List<Invest> investList) {
        List<InvestRecordDto> investRecordDtoList = new ArrayList<InvestRecordDto>();
        for (Invest invest : investList) {
            InvestRecordDto investRecordDto = new InvestRecordDto();
            String userNameTemp = invest.getUser().getUsername();
            if(userNameTemp.length() > 3){
                userNameTemp = userNameTemp.substring(0,3) + "***";
            }else{
                userNameTemp +="***";
            }
            investRecordDto.setUserName(userNameTemp);
            investRecordDto.setInvestTime(invest.getTime().toString());
            investRecordDto.setInvestMoney(invest.getInvestMoney());
            investRecordDtoList.add(investRecordDto);
        }
        return investRecordDtoList;
    }
}
