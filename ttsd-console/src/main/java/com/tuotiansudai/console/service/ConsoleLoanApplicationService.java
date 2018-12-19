package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.LoanApplicationStatus;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.LoanApplicationModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanRiskManagementTitleModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.CalculateUtil;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConsoleLoanApplicationService {

    private static Logger logger = Logger.getLogger(ConsoleLoanApplicationService.class);

    @Autowired
    private LoanApplicationMapper loanApplicationMapper;

    @Autowired
    private LoanRiskManagementTitleMapper loanRiskManagementTitleMapper;

    @Autowired
    private LoanRiskManagementTitleRelationMapper loanRiskManagementTitleRelationMapper;

    @Autowired
    private ConsoleLoanCreateService consoleLoanCreateService;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;


    public BaseDto<BaseDataDto> comment(LoanApplicationModel loanApplicationModel) {
        LoanApplicationModel dbLoanApplicationModel = loanApplicationMapper.findById(loanApplicationModel.getId());
        if (null == dbLoanApplicationModel) {
            return new BaseDto<>(new BaseDataDto(false, "该借款申请不存在"));
        }

        dbLoanApplicationModel.setComment(loanApplicationModel.getComment());
        dbLoanApplicationModel.setUpdatedBy(loanApplicationModel.getUpdatedBy());
        dbLoanApplicationModel.setUpdatedTime(new Date());

        loanApplicationMapper.update(dbLoanApplicationModel);

        return new BaseDto<>(new BaseDataDto(true));
    }

    public LoanApplicationModel detail(long id){
        return loanApplicationMapper.findById(id);
    }

    public BasePaginationDataDto<LoanApplicationModel> getPagination(int index, int pageSize) {
        long count = loanApplicationMapper.findCount();
        List<LoanApplicationModel> loanApplicationModels = loanApplicationMapper.findPagination(PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        return new BasePaginationDataDto<>(index, pageSize, count, loanApplicationModels);
    }

    public BasePaginationDataDto<LoanApplicationModel> loanApplicationConsumeList(String keyWord, LoanApplicationStatus status, Date startTime, Date endTime, int index, int pageSize) {
        startTime = startTime == null ? null : new DateTime(startTime).withTimeAtStartOfDay().toDate();
        endTime = endTime == null ? null : new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        long count = loanApplicationMapper.findConsumeApplyCount(keyWord, status, startTime, endTime);
        List<LoanApplicationModel> loanApplicationModels = loanApplicationMapper.findConsumeApply(keyWord, status, startTime, endTime, (index - 1) * pageSize, pageSize);
        return new BasePaginationDataDto<>(index, pageSize, count, loanApplicationModels);
    }

    public LoanApplicationConsumeDto consumeDetail(long id){
        LoanApplicationConsumeDto loanApplicationConsumeDto = new LoanApplicationConsumeDto();
        LoanApplicationModel loanApplicationModel = loanApplicationMapper.findById(id);
        loanApplicationConsumeDto.setLoanApplicationModel(loanApplicationModel);
        loanApplicationConsumeDto.setLoanRiskManagementTitleModelList(loanRiskManagementTitleMapper.findAll());
        loanApplicationConsumeDto.setLoanRiskManagementTitleRelationModelList(loanRiskManagementTitleRelationMapper.findByLoanApplicationId(loanApplicationModel.getId()));
        if (loanApplicationModel.getLoanId() != null){
            LoanCreateRequestDto loanCreateRequestDto = consoleLoanCreateService.getEditLoanDetails(loanApplicationModel.getLoanId());
            loanApplicationConsumeDto.setLoan(loanCreateRequestDto.getLoan());
            loanApplicationConsumeDto.setLoanDetails(loanCreateRequestDto.getLoanDetails());
            loanApplicationConsumeDto.setExtraLoanRateModelList(extraLoanRateMapper.findByLoanId(loanApplicationModel.getLoanId()));
        }

        return loanApplicationConsumeDto;
    }
}
