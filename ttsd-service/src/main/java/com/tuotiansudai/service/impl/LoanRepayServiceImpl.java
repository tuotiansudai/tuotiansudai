package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDto;
import com.tuotiansudai.dto.LoanRepayPaginationDataDto;
import com.tuotiansudai.dto.LoanRepayDto;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.service.LoanRepayService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
@Service
public class LoanRepayServiceImpl implements LoanRepayService {
    static Logger logger = Logger.getLogger(LoanRepayServiceImpl.class);

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Override
    public BasePaginationDto findLoanRepayPagination(LoanRepayDto requestDto) {
        int index = requestDto.getIndex();
        int pageSize = requestDto.getPageSize();
        if( index < 1){
            index = 1;
        }
        if(pageSize < 1){
            pageSize = 10;
        }

        Long loanId = null;
        if(StringUtils.isNotEmpty(requestDto.getLoanId())){
            loanId = Long.parseLong(requestDto.getLoanId());
        }
        ;
        String loginName = requestDto.getLoginName();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDate = requestDto.getRepayStartDate();
        String endDate = requestDto.getRepayEndDate();
        RepayStatus repayStatus = requestDto.getRepayStatus();

        int count = loanRepayMapper.findLoanRepayCount(loanId, loginName, repayStatus, startDate, endDate);
        BasePaginationDto baseDto = new BasePaginationDto(index, pageSize, count);
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findLoanRepayPagination((index - 1) * pageSize, pageSize, loanId, loginName, repayStatus, startDate, endDate);
        List<LoanRepayPaginationDataDto> loanRepayPaginationDataDtos = convertModelToDto(loanRepayModels);
        baseDto.setRecordDtoList(loanRepayPaginationDataDtos);
        baseDto.setStatus(true);
        return baseDto;

    }

    @Override
    public List<RepayStatus> findAllRepayStatus() {

            List<RepayStatus> repayStatusList = new ArrayList<RepayStatus>();
            for (RepayStatus repayStatus : RepayStatus.values()) {
                repayStatusList.add(repayStatus);
            }
            return repayStatusList;

    }

    private List<LoanRepayPaginationDataDto> convertModelToDto(List<LoanRepayModel> loanRepayModels) {
        List<LoanRepayPaginationDataDto> loanRepayPaginationDataDtos = Lists.newArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            LoanRepayPaginationDataDto loanRepayPaginationDataDto = new LoanRepayPaginationDataDto();
            loanRepayPaginationDataDto.setLoginName(loanRepayModel.getLoan().getLoanerLoginName());
            loanRepayPaginationDataDto.setProjectName(loanRepayModel.getLoan().getName());
            loanRepayPaginationDataDto.setCorpus(loanRepayModel.getCorpus());
            loanRepayPaginationDataDto.setInterest(loanRepayModel.getActualInterest());
            loanRepayPaginationDataDto.setRepayDay(sdf.format(loanRepayModel.getRepayDate()));
            loanRepayPaginationDataDto.setPeriod(loanRepayModel.getPeriod());
            loanRepayPaginationDataDto.setTotalAmount(loanRepayModel.getCorpus() + loanRepayModel.getActualInterest());
            loanRepayPaginationDataDto.setRepayStatus(loanRepayModel.getStatus());
            loanRepayPaginationDataDto.setLoanId("" + loanRepayModel.getLoanId());
            loanRepayPaginationDataDtos.add(loanRepayPaginationDataDto);
        }

        return loanRepayPaginationDataDtos;

    }


}
