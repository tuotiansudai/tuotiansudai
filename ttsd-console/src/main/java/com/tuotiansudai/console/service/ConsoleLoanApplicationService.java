package com.tuotiansudai.console.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.LoanApplicationStatus;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ConsoleLoanApplicationService {

    private static Logger logger = Logger.getLogger(ConsoleLoanApplicationService.class);

    private static final String DEFAULT_CONTRACT_ID = "789098123"; // 四方合同

    @Autowired
    private LoanApplicationMapper loanApplicationMapper;

    @Autowired
    private LoanRiskManagementTitleMapper loanRiskManagementTitleMapper;

    @Autowired
    private LoanRiskManagementTitleRelationMapper loanRiskManagementTitleRelationMapper;

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

    public LoanApplicationConsumeDetailDto consumeDetail(long id){
        LoanApplicationConsumeDetailDto loanApplicationConsumeDetailDto = new LoanApplicationConsumeDetailDto();
        loanApplicationConsumeDetailDto.setLoanApplicationModel(loanApplicationMapper.findById(id));
        LoanApplicationMaterialsModel materialsModel = loanApplicationMapper.findMaterialsByLoanApplicationId(id);
        loanApplicationConsumeDetailDto.setMaterialsList(Maps.newHashMap(ImmutableMap.<String, List<String>>builder()
                .put("identityProveUrls", materialsModel == null || Strings.isNullOrEmpty(materialsModel.getIdentityProveUrls())? new ArrayList<>() : Arrays.asList(materialsModel.getIdentityProveUrls().split(",")))
                .put("incomeProveUrls", materialsModel == null ||  Strings.isNullOrEmpty(materialsModel.getIncomeProveUrls())? new ArrayList<>() : Arrays.asList(materialsModel.getIncomeProveUrls().split(",")))
                .put("creditProveUrls", materialsModel == null ||  Strings.isNullOrEmpty(materialsModel.getCreditProveUrls())? new ArrayList<>() : Arrays.asList(materialsModel.getCreditProveUrls().split(",")))
                .put("marriageProveUrls", materialsModel == null ||  Strings.isNullOrEmpty(materialsModel.getMarriageProveUrls())? new ArrayList<>() : Arrays.asList(materialsModel.getMarriageProveUrls().split(",")))
                .put("propertyProveUrls", materialsModel == null ||  Strings.isNullOrEmpty(materialsModel.getPropertyProveUrls())? new ArrayList<>() : Arrays.asList(materialsModel.getPropertyProveUrls().split(",")))
                .put("togetherProveUrls", materialsModel == null ||  Strings.isNullOrEmpty(materialsModel.getTogetherProveUrls())? new ArrayList<>() : Arrays.asList(materialsModel.getTogetherProveUrls().split(",")))
                .put("driversLicense", materialsModel == null ||  Strings.isNullOrEmpty(materialsModel.getDriversLicense())? new ArrayList<>() : Arrays.asList(materialsModel.getDriversLicense().split(",")))
                .build()));
        return loanApplicationConsumeDetailDto;
    }

    @Transactional
    public BaseDto<BaseDataDto> consumeSave(long id, LoanApplicationUpdateDto loanApplicationUpdateDto, String loginName){
        LoanApplicationModel loanApplicationModel = loanApplicationMapper.findById(id);
        if (loanApplicationModel == null){
            return new BaseDto<>(new BaseDataDto(false, "借款申请不存在"));
        }
        loanApplicationModel.setAddress(loanApplicationUpdateDto.getAddress());
        loanApplicationModel.setLoanUsage(loanApplicationUpdateDto.getLoanUsage());
        loanApplicationModel.setUpdatedBy(loginName);
        loanApplicationModel.setUpdatedTime(new Date());
        loanApplicationMapper.update(loanApplicationModel);

        loanRiskManagementTitleRelationMapper.deleteByLoanApplication(loanApplicationModel.getId());
        List<LoanRiskManagementTitleRelationModel> relationModels = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(loanApplicationUpdateDto.getRelationModels())) {
            for (LoanRiskManagementTitleRelationCreateDto dto : loanApplicationUpdateDto.getRelationModels()) {
                relationModels.add(new LoanRiskManagementTitleRelationModel(loanApplicationModel.getLoanId(), id, dto.getTitleId(), dto.getDetail()));
            }
            loanRiskManagementTitleRelationMapper.create(relationModels);
        }
        return new BaseDto<>(new BaseDataDto(true));
    }

    public LoanApplicationModel findLoanerDetail(long loanApplicationId){
        return loanApplicationMapper.findById(loanApplicationId);
    }

    public BaseDto<BaseDataDto> consumeReject(long loanApplicationId){
        loanApplicationMapper.updateStatus(loanApplicationId, LoanApplicationStatus.REJECT);
        return new BaseDto<>(new BaseDataDto(true));
    }

    public BaseDto<BaseDataDto> applyAuditLoanApplication(long loanApplicationId){
        return new BaseDto<>(new BaseDataDto(true));
    }

    public BaseDto<BaseDataDto> consumeApprove(long loanApplicationId, String ip){
        LoanApplicationModel loanApplicationModel = loanApplicationMapper.findById(loanApplicationId);
        if (loanApplicationModel == null || loanApplicationModel.getLoanId() == null){
            return new BaseDto<>(new BaseDataDto(false, "审核通过失败"));
        }

        if (userRoleMapper.findByLoginNameAndRole(loanApplicationModel.getLoginName(), Role.LOANER) == null){
            userRoleMapper.create(Lists.newArrayList(new UserRoleModel(loanApplicationModel.getLoginName(), Role.LOANER)));
        }

        loanApplicationMapper.updateStatus(loanApplicationId, LoanApplicationStatus.APPROVE);
        loanMapper.updateStatus(loanApplicationModel.getLoanId(), LoanStatus.WAITING_VERIFY);
        return new BaseDto<>(new BaseDataDto(true));
    }

    public LoanRiskManagementTitleModel createTitle(String title) {
        LoanRiskManagementTitleModel loanRiskManagementTitleModel = new LoanRiskManagementTitleModel();
        loanRiskManagementTitleModel.setTitle(title);
        loanRiskManagementTitleMapper.create(loanRiskManagementTitleModel);
        return loanRiskManagementTitleModel;
    }

    public List<LoanRiskManagementDetailDto> findAllTitleDetail(long loanApplicationId) {
        List<LoanRiskManagementTitleModel> titleModels = loanRiskManagementTitleMapper.findAll();
        List<LoanRiskManagementDetailDto> detailDtos = new ArrayList<>();
        for(LoanRiskManagementTitleModel model : titleModels){
            LoanRiskManagementTitleRelationModel relationModel = loanRiskManagementTitleRelationMapper.findByLoanApplicationIdAndTitleId(loanApplicationId, model.getId());
            detailDtos.add(new LoanRiskManagementDetailDto(model.getId(), model.getTitle(), relationModel != null, relationModel != null ? relationModel.getDetail() : null));
        }
        return detailDtos;
    }

    public Map<String, Object> loanParams(){
        return Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("loanTypes", Lists.newArrayList(LoanType.LOAN_INTEREST_MONTHLY_REPAY, LoanType.LOAN_INTEREST_LUMP_SUM_REPAY))
                .put("activityTypes", Lists.newArrayList(ActivityType.NORMAL, ActivityType.NEWBIE))
                .put("extraSources",  Lists.newArrayList(Source.WEB, Source.MOBILE))
                .put("contractId", DEFAULT_CONTRACT_ID)
                .put("pledgeType", PledgeType.NONE)
                .put("productTypes", Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180, ProductType._360))
                .build());
    }
}
