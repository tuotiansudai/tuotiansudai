package com.ttsd.api.service.impl;

import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.repay.model.InvestRepay;
import com.ttsd.api.dao.MobileAppInvestRepayListDao;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppInvestRepayListService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppInvestRepayListServiceImpl implements MobileAppInvestRepayListService {
    private static String QueryInvestRepayStatus_Paid = "paid";
    private static String QueryInvestRepayStatus_UnPaid = "unpaid";

    @Resource
    private MobileAppInvestRepayListDao mobileAppInvestRepayListDao;

    @Override
    public BaseResponseDto generateUserInvestRepayList(InvestRepayListRequestDto requestDto) {
        String userId = requestDto.getBaseParam().getUserId();
        Integer index = requestDto.getIndex();
        Integer pageSize = requestDto.getPageSize();
        String requestStatus = requestDto.getStatus();
        String[] status = null;
        String orderBy = null;
        if (QueryInvestRepayStatus_UnPaid.equalsIgnoreCase(requestStatus)) {
            status = new String[]{LoanConstants.RepayStatus.OVERDUE, LoanConstants.RepayStatus.REPAYING, LoanConstants.RepayStatus.WAIT_REPAY_VERIFY};
            orderBy = "repay_day asc";
        } else if (QueryInvestRepayStatus_Paid.equalsIgnoreCase(requestStatus)) {
            status = new String[]{LoanConstants.RepayStatus.COMPLETE};
            orderBy = "time desc";
        }
        return generateUserInvestRepayList(index, pageSize, userId, status, orderBy);
    }

    private BaseResponseDto generateUserInvestRepayList(Integer index, Integer pageSize, String userId, String[] status, String orderBy) {
        List<InvestRepay> repayList = mobileAppInvestRepayListDao.getUserInvestRepayList(index, pageSize, userId, status, orderBy);
        int repayTotalCount = mobileAppInvestRepayListDao.getUserInvestRepayTotalCount(userId, status);

        // build InvestList
        InvestRepayListResponseDataDto dtoData = new InvestRepayListResponseDataDto();
        dtoData.setRecordList(convertResponseData(repayList));
        dtoData.setIndex(index);
        dtoData.setPageSize(pageSize);
        dtoData.setTotalCount(repayTotalCount);

        // BaseDto
        BaseResponseDto dto = new BaseResponseDto();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);

        return dto;
    }

    private List<InvestRepayRecordResponseDataDto> convertResponseData(List<InvestRepay> investRepayList) {
        List<InvestRepayRecordResponseDataDto> list = new ArrayList<>();
        if (investRepayList != null) {
            for (InvestRepay repay : investRepayList) {
                list.add(new InvestRepayRecordResponseDataDto((repay)));
            }
        }
        return list;
    }
}
