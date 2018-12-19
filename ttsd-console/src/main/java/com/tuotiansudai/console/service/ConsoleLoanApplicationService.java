package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.LoanApplicationStatus;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.CalculateUtil;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;


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

    @Transactional
    public BaseDto<BaseDataDto> consumeSave(LoanApplicationConsumeDto loanApplicationConsumeDto){
        LoanApplicationModel loanApplicationModel = loanApplicationMapper.findById(loanApplicationConsumeDto.getLoanApplicationModel().getId());
        if (loanApplicationModel == null){
            return new BaseDto<>(new BaseDataDto(false, "借款申请不存在"));
        }

        loanApplicationConsumeDto.getLoan().setStatus(LoanStatus.DRAFT);
        LoanCreateRequestDto loanCreateRequestDto = new LoanCreateRequestDto();
        loanCreateRequestDto.setLoan(loanApplicationConsumeDto.getLoan());
        loanCreateRequestDto.setLoanerDetails(loanApplicationConsumeDto.getLoanerDetails());
        loanCreateRequestDto.setLoanDetails(loanApplicationConsumeDto.getLoanDetails());

        if (loanApplicationModel.getLoanId() != null){
            return consoleLoanCreateService.updateLoan(loanCreateRequestDto);
        }

        BaseDto<BaseDataDto> baseDataDtoBaseDto = consoleLoanCreateService.checkCreateLoanData(loanCreateRequestDto);
        if (baseDataDtoBaseDto.getData().getStatus()){
            long loanId = consoleLoanCreateService.createLoan(loanCreateRequestDto);
            loanApplicationModel.setLoanId(Long.parseLong(baseDataDtoBaseDto.getData().getMessage()));
            loanApplicationModel.setId(loanApplicationConsumeDto.getLoanApplicationModel().getId());
            loanApplicationModel.setAddress(loanApplicationConsumeDto.getLoanApplicationModel().getAddress());
            loanApplicationModel.setLoanUsage(loanApplicationConsumeDto.getLoanApplicationModel().getLoanUsage());
            loanApplicationModel.setLoanId(loanId);
            loanApplicationMapper.update(loanApplicationModel);
            loanRiskManagementTitleRelationMapper.create(loanApplicationConsumeDto.getLoanRiskManagementTitleRelationModelList());
        }
        return baseDataDtoBaseDto;
    }

    public BaseDto<BaseDataDto> consumeReject(long loanApplicationId){
        loanApplicationMapper.updateStatus(loanApplicationId, LoanApplicationStatus.REJECT);
        return new BaseDto<>(new BaseDataDto(true));
    }

    public BaseDto<BaseDataDto> consumeApprove(long loanApplicationId){
        LoanApplicationModel loanApplicationModel = loanApplicationMapper.findById(loanApplicationId);
        if (loanApplicationModel == null || loanApplicationModel.getLoanId() == null){
            return new BaseDto<>(new BaseDataDto(false, "审核通过失败"));
        }

        if (userRoleMapper.findByLoginNameAndRole(loanApplicationModel.getLoginName(), Role.LOANER) == null){
            userRoleMapper.create(Lists.newArrayList(new UserRoleModel(loanApplicationModel.getLoginName(), Role.LOANER)));
        }

        loanApplicationMapper.updateStatus(loanApplicationId, LoanApplicationStatus.APPROVE);
        loanMapper.updateStatus(loanApplicationModel.getLoanId(), LoanStatus.WAITING_VERIFY);
        consoleLoanCreateService.applyAuditLoan(loanApplicationModel.getLoanId());
        return new BaseDto<>(new BaseDataDto(true));
    }

    public LoanRiskManagementTitleModel createTitle(String title) {
        LoanRiskManagementTitleModel loanRiskManagementTitleModel = new LoanRiskManagementTitleModel();
        loanRiskManagementTitleModel.setTitle(title);
        loanRiskManagementTitleMapper.create(loanRiskManagementTitleModel);
        return loanRiskManagementTitleModel;
    }

    public List<LoanRiskManagementTitleModel> findAllTitles() {
        return loanRiskManagementTitleMapper.findAll();
    }
}
