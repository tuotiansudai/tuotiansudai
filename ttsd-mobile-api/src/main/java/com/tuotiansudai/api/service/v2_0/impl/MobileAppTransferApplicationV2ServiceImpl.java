package com.tuotiansudai.api.service.v2_0.impl;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v1_0.UserInvestListResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.UserInvestRecordResponseDataDto;
import com.tuotiansudai.api.dto.v2_0.TransferableInvestListRequestDto;
import com.tuotiansudai.api.service.v2_0.MobileAppTransferApplicationV2Service;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.repository.model.TransferApplicationModel;
import com.tuotiansudai.repository.model.TransferRuleModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.CalculateLeftDays;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MobileAppTransferApplicationV2ServiceImpl implements MobileAppTransferApplicationV2Service {

    static Logger logger = Logger.getLogger(MobileAppTransferApplicationV2ServiceImpl.class);
    @Autowired
    private TransferApplicationMapper transferApplicationMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private TransferRuleMapper transferRuleMapper;
    @Autowired
    private InvestRepayMapper investRepayMapper;
    @Autowired
    private InvestService investService;
    @Autowired
    private LoanRepayMapper loanRepayMapper;
    @Autowired
    private PageValidUtils pageValidUtils;

    @Override
    public BaseResponseDto<UserInvestListResponseDataDto> generateTransferableInvest(TransferableInvestListRequestDto requestDto) {
        String loginName = requestDto.getBaseParam().getUserId();
        Integer pageSize = pageValidUtils.validPageSizeLimit(requestDto.getPageSize());
        Integer index = requestDto.getIndex();
        if (index == null || index <= 0) {
            index = 1;
        }
        List<InvestModel> transferableInvestList = investMapper.findTransferableApplicationPaginationByLoginName(loginName, (index - 1) * pageSize, pageSize);
        UserInvestListResponseDataDto dtoData = new UserInvestListResponseDataDto();
        dtoData.setInvestList(convertResponseData(transferableInvestList));
        dtoData.setIndex(requestDto.getIndex());
        dtoData.setPageSize(requestDto.getPageSize());
        dtoData.setTotalCount((int) investMapper.findCountTransferableApplicationPaginationByLoginName(loginName));

        BaseResponseDto<UserInvestListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);

        return dto;
    }

    private List<UserInvestRecordResponseDataDto> convertResponseData(List<InvestModel> investList) {
        List<UserInvestRecordResponseDataDto> list = Lists.newArrayList();
        Map<Long, LoanModel> loanMapCache = Maps.newHashMap();
        if (investList != null) {
            for (InvestModel invest : investList) {
                TransferRuleModel transferRuleModel = transferRuleMapper.find();
                if (!transferRuleModel.isMultipleTransferEnabled()) {
                    TransferApplicationModel transfereeApplicationModel = transferApplicationMapper.findByInvestId(invest.getId());
                    if (transfereeApplicationModel != null) {
                        logger.info(MessageFormat.format("{0} MultipleTransferEnabled is false ", invest.getId()));
                        continue;
                    }

                }
                long loanId = invest.getLoanId();
                LoanModel loanModel;
                if (loanMapCache.containsKey(loanId)) {
                    loanModel = loanMapCache.get(loanId);
                } else {
                    loanModel = loanMapper.findById(loanId);
                    loanMapCache.put(loanId, loanModel);
                }
                UserInvestRecordResponseDataDto dto = new UserInvestRecordResponseDataDto(invest, loanModel);

                long amount = 0;
                List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(invest.getId());
                for (InvestRepayModel investRepayModel : investRepayModels) {
                    amount += investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee();
                }

                if (CollectionUtils.isEmpty(investRepayModels)) {
                    amount = investService.estimateInvestIncome(invest.getLoanId(), invest.getInvestFeeRate(), invest.getLoginName(), invest.getAmount(), new Date());
                }

                dto.setInvestInterest(AmountConverter.convertCentToString(amount));
                dto.setTransferStatus(invest.getTransferStatus().name());
                LoanRepayModel loanRepayModel = loanRepayMapper.findCurrentLoanRepayByLoanId(invest.getLoanId());
                dto.setLeftPeriod(loanRepayModel == null ? "0" : String.valueOf(investRepayMapper.findLeftPeriodByTransferInvestIdAndPeriod(invest.getId(), loanRepayModel.getPeriod())));
                InvestRepayModel currentInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(invest.getId(), loanModel.getPeriods());
                Date repayDate = currentInvestRepayModel == null ? new Date() : currentInvestRepayModel.getRepayDate() == null ? new Date() : currentInvestRepayModel.getRepayDate();
                dto.setLeftDays(CalculateLeftDays.calculateTransferApplicationLeftDays(repayDate));
                list.add(dto);
            }
        }
        return list;

    }
}
