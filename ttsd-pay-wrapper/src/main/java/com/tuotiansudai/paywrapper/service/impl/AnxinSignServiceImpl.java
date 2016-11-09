package com.tuotiansudai.paywrapper.service.impl;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.cs.SignInfoVO;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.tuotiansudai.cfca.dto.AnxinContractType;
import com.tuotiansudai.cfca.dto.ContractResponseView;
import com.tuotiansudai.cfca.service.AnxinSignConnectService;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.sms.GenerateContractErrorNotifyDto;
import com.tuotiansudai.paywrapper.service.AnxinSignService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnxinSignServiceImpl implements AnxinSignService {

    static Logger logger = Logger.getLogger(AnxinSignServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AnxinSignConnectService anxinSignConnectService;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanerDetailsMapper loanerDetailsMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;
    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private TransferRuleMapper transferRuleMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String LOAN_CONTRACT_AGENT_SIGN = "agentUserName";

    private static final String LOAN_CONTRACT_INVESTOR_SIGN = "investorUserName";

    private static final String TRANSFER_LOAN_CONTRACT_AGENT_SIGN = "transferUserName";

    private static final String TRANSFER_LOAN_CONTRACT_INVESTOR_SIGN = "transfereeUserName";

    @Value(value = "${anxin.contract.batch.num}")
    private int batchNum;

    @Value(value = "${anxin.loan.contract.template}")
    private String loanTemplate;

    @Value(value = "${anxin.transfer.contract.template}")
    private String transferTemplate;

    @Value("#{'${anxin.contract.notify.mobileList}'.split('\\|')}")
    private List<String> mobileList;

    @Override
    public BaseDto createContracts(long loanId) {
        List<CreateContractVO> createContractVOs = Lists.newArrayList();
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanId);
        InvestModel investModel;
        BaseDto baseDto = new BaseDto();
        for (int i = 0; i < investModels.size(); i++) {
            investModel = investModels.get(i);
            createContractVOs.add(collectInvestorContractModel(investModel.getLoginName(), loanId, investModel.getId()));
            if (createContractVOs.size() == batchNum || investModels.size() == i) {
                String batchNo = UUIDGenerator.generate();
                try {
                    logger.debug(MessageFormat.format("[安心签] create contract begin , loanId:{0}, batchNo{1}", loanId, batchNo));
                    //创建合同
                    anxinSignConnectService.generateContractBatch3202(loanId, batchNo, AnxinContractType.LOAN_CONTRACT,createContractVOs);

                    baseDto.setSuccess(true);
                } catch (PKIException e) {
                    smsWrapperClient.sendGenerateContractErrorNotify(new GenerateContractErrorNotifyDto(mobileList, ImmutableList.<String>builder().add(String.valueOf(loanId)).add(batchNo).build()));

                    baseDto.setSuccess(false);
                    logger.error(MessageFormat.format("[安心签] create contract error , loanId:{0}", loanId), e);

                }
                createContractVOs.clear();
            }
        }
        return baseDto;
    }

    @Override
    public BaseDto createTransferContracts(long transferApplicationId) {
        List<CreateContractVO> createContractVOs = Lists.newArrayList(collectTransferContractModel(transferApplicationId));
        BaseDto baseDto = new BaseDto();
        String batchNo = UUIDGenerator.generate();
        try {
            logger.debug(MessageFormat.format("[安心签] create transfer contract begin , loanId:{0}, batchNo{1}", transferApplicationId, batchNo));
            //创建合同
            anxinSignConnectService.generateContractBatch3202(transferApplicationId, batchNo,AnxinContractType.TRANSFER_CONTRACT, createContractVOs);

            baseDto.setSuccess(true);
        } catch (PKIException e) {
            smsWrapperClient.sendGenerateContractErrorNotify(new GenerateContractErrorNotifyDto(mobileList, ImmutableList.<String>builder().add(String.valueOf(transferApplicationId)).add(batchNo).build()));

            baseDto.setSuccess(false);
            logger.error(MessageFormat.format("[安心签] create transfer contract error , loanId:{0}", transferApplicationId), e);

        }
        createContractVOs.clear();
        return baseDto;
    }

    private CreateContractVO collectTransferContractModel(long transferApplicationId) {
        CreateContractVO createContractVO = new CreateContractVO();
        Map<String, String> dataModel = new HashMap<>();
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);

        UserModel transfereeUserModel = userMapper.findByLoginName(transferApplicationModel.getLoginName());
        AnxinSignPropertyModel agentAnxinProp = anxinSignPropertyMapper.findByLoginName(transferApplicationModel.getLoginName());
        if (transfereeUserModel != null) {
            dataModel.put("transferUserName", transfereeUserModel.getUserName());
            dataModel.put("transferMobile", transfereeUserModel.getMobile());
            dataModel.put("transferIdentity", transfereeUserModel.getIdentityNumber());
        }

        InvestModel investModel = investMapper.findById(transferApplicationModel.getInvestId());
        UserModel investAccountModel = userMapper.findByLoginName(investModel.getLoginName());
        AnxinSignPropertyModel investorAnxinProp = anxinSignPropertyMapper.findByLoginName(investModel.getLoginName());
        if (investAccountModel != null) {
            dataModel.put("transfereeUserName", investAccountModel.getUserName());
            dataModel.put("transfereeMobile", userMapper.findByLoginName(investAccountModel.getLoginName()).getMobile());
            dataModel.put("transfereeIdentity", investAccountModel.getIdentityNumber());
        }

        LoanModel loanModel = loanMapper.findById(transferApplicationModel.getLoanId());
        if (null != loanModel) {
            dataModel.put("userName", loanerDetailsMapper.getByLoanId(loanModel.getId()).getUserName());
            dataModel.put("identity", loanModel.getLoanerIdentityNumber());
            dataModel.put("amount", AmountConverter.convertCentToString(loanModel.getLoanAmount()));
            dataModel.put("totalRate", String.valueOf((loanModel.getBaseRate() + loanModel.getActivityRate()) * 100));
            dataModel.put("periods", String.valueOf(loanModel.getPeriods() * 30));
        }

        if (transferApplicationModel.getPeriod() != 1) {
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), transferApplicationModel.getPeriod() - 1);
            dataModel.put("transferStartTime", simpleDateFormat.format(new LocalDate(investRepayModel.getRepayDate()).plusDays(1).toDate()));
        } else {
            if (loanModel.getType().equals(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY) || loanModel.getType().equals(LoanType.INVEST_INTEREST_MONTHLY_REPAY)) {
                dataModel.put("transferStartTime", simpleDateFormat.format(investModel.getInvestTime()));
            } else if (loanModel.getType().equals(LoanType.LOAN_INTEREST_MONTHLY_REPAY) || loanModel.getType().equals(LoanType.LOAN_INTEREST_LUMP_SUM_REPAY)) {
                dataModel.put("transferStartTime", simpleDateFormat.format(loanModel.getRecheckTime()));
            }
        }

        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), loanModel.getPeriods());
        dataModel.put("transferEndTime", simpleDateFormat.format(investRepayModel.getRepayDate()));

        dataModel.put("investAmount", AmountConverter.convertCentToString(transferApplicationModel.getInvestAmount()));
        dataModel.put("transferTime", simpleDateFormat.format(transferApplicationModel.getTransferTime()));
        dataModel.put("leftPeriod", String.valueOf(transferApplicationModel.getLeftPeriod()));
        dataModel.put("orderId", String.valueOf(transferApplicationModel.getId()));

        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        String msg1;
        String msg2;
        String msg3;
        if (transferRuleModel.getLevelOneFee() != 0) {
            msg1 = MessageFormat.format("甲方持有债权30天以内的，收取转让本金的{0}%作为服务费用。", transferRuleModel.getLevelOneFee());
        } else {
            msg1 = "甲方持有债权30天以内的，暂不收取转服务费用。";
        }

        if (transferRuleModel.getLevelTwoFee() != 0) {
            msg2 = MessageFormat.format("甲方持有债权30天以上，90天以内的，收取转让本金的{0}%作为服务费用。", transferRuleModel.getLevelOneFee());
        } else {
            msg2 = "甲方持有债权30天以上，90天以内的，暂不收取转服务费用。";
        }

        if (transferRuleModel.getLevelThreeFee() != 0) {
            msg3 = MessageFormat.format("甲方持有债权90天以上的，收取转让本金的{0}%作为服务费用。", transferRuleModel.getLevelOneFee());
        } else {
            msg3 = "甲方持有债权90天以上的，暂不收取转服务费用。";
        }
        dataModel.put("msg1", msg1);
        dataModel.put("msg2", msg2);
        dataModel.put("msg3", msg3);

        createContractVO.setInvestmentInfo(dataModel);

        SignInfoVO agentSignInfo = new SignInfoVO();
        agentSignInfo.setUserId(agentAnxinProp.getAnxinUserId());
        agentSignInfo.setAuthorizationTime(new DateTime(agentAnxinProp.getAuthTime()).toString("yyyyMMddHHmmss"));
        agentSignInfo.setLocation(agentAnxinProp.getAuthIp());
        agentSignInfo.setSignLocation(TRANSFER_LOAN_CONTRACT_AGENT_SIGN);
        agentSignInfo.setProjectCode(agentAnxinProp.getProjectCode());
        agentSignInfo.setIsProxySign(1);

        SignInfoVO investorSignInfo = new SignInfoVO();
        investorSignInfo.setUserId(investorAnxinProp.getAnxinUserId());
        investorSignInfo.setAuthorizationTime(new DateTime(investorAnxinProp.getAuthTime()).toString("yyyyMMddHHmmss"));
        investorSignInfo.setLocation(investorAnxinProp.getAuthIp());
        investorSignInfo.setSignLocation(TRANSFER_LOAN_CONTRACT_INVESTOR_SIGN);
        investorSignInfo.setProjectCode(investorAnxinProp.getProjectCode());
        investorSignInfo.setIsProxySign(1);

        createContractVO.setSignInfos(new SignInfoVO[]{agentSignInfo, investorSignInfo});
        createContractVO.setTemplateId(transferTemplate);
        createContractVO.setIsSign(1);

        return createContractVO;
    }

    private CreateContractVO collectInvestorContractModel(String investorLoginName, long loanId, long investId) {
        CreateContractVO createContractVO = new CreateContractVO();
        Map<String, String> dataModel = new HashMap<>();

        // 标的
        LoanModel loanModel = loanMapper.findById(loanId);

        // 借款人（代理人 or 企业借款人）
        UserModel agentModel = userMapper.findByLoginName(loanModel.getAgentLoginName());
        AnxinSignPropertyModel agentAnxinProp = anxinSignPropertyMapper.findByLoginName(loanModel.getAgentLoginName());

        // 投资人
        UserModel investorModel = userMapper.findByLoginName(investorLoginName);
        AnxinSignPropertyModel investorAnxinProp = anxinSignPropertyMapper.findByLoginName(investorLoginName);

        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investId, loanModel.getPeriods());
        LoanerDetailsModel loanerDetailsModel = loanerDetailsMapper.getByLoanId(loanId);

        dataModel.put("agentUserName", agentModel.getUserName());
        dataModel.put("investorUserName", investorModel.getUserName());
        dataModel.put("agentMobile", agentModel.getMobile());
        dataModel.put("agentIdentityNumber", agentModel.getIdentityNumber());
        dataModel.put("investorMobile", investorModel.getMobile());
        dataModel.put("investorIdentityNumber", investorModel.getIdentityNumber());
        dataModel.put("loanerUserName", loanerDetailsModel.getUserName());
        dataModel.put("loanerIdentityNumber", loanerDetailsModel.getIdentityNumber());
        dataModel.put("loanAmount1", AmountConverter.convertCentToString(loanModel.getLoanAmount()));
        dataModel.put("loanAmount2", AmountConverter.convertCentToString(loanModel.getLoanAmount()));
        dataModel.put("periods1", String.valueOf(loanModel.getPeriods() * 30));
        dataModel.put("periods2", String.valueOf(loanModel.getPeriods() * 30));
        dataModel.put("totalRate", String.valueOf(loanModel.getBaseRate() * 100));
        dataModel.put("recheckTime1", new DateTime(loanModel.getRecheckTime()).toString("yyyy-MM-dd"));
        dataModel.put("recheckTime2", new DateTime(loanModel.getRecheckTime()).toString("yyyy-MM-dd"));
        dataModel.put("endTime1", new DateTime(investRepayModel.getRepayDate()).toString("yyyy-MM-dd"));
        dataModel.put("endTime2", new DateTime(investRepayModel.getRepayDate()).toString("yyyy-MM-dd"));
        dataModel.put("orderId", String.valueOf(investId));
        if (loanModel.getPledgeType().equals(PledgeType.HOUSE)) {
            dataModel.put("pledge", "房屋");
        } else if (loanModel.getPledgeType().equals(PledgeType.VEHICLE)) {
            dataModel.put("pledge", "车辆");
        }
        createContractVO.setInvestmentInfo(dataModel);

        SignInfoVO agentSignInfo = new SignInfoVO();
        agentSignInfo.setUserId(agentAnxinProp.getAnxinUserId());
        agentSignInfo.setAuthorizationTime(new DateTime(agentAnxinProp.getAuthTime()).toString("yyyyMMddHHmmss"));
        agentSignInfo.setLocation(agentAnxinProp.getAuthIp());
        agentSignInfo.setSignLocation(LOAN_CONTRACT_AGENT_SIGN);
        agentSignInfo.setProjectCode(agentAnxinProp.getProjectCode());
        agentSignInfo.setIsProxySign(1);

        SignInfoVO investorSignInfo = new SignInfoVO();
        investorSignInfo.setUserId(investorAnxinProp.getAnxinUserId());
        investorSignInfo.setAuthorizationTime(new DateTime(investorAnxinProp.getAuthTime()).toString("yyyyMMddHHmmss"));
        investorSignInfo.setLocation(investorAnxinProp.getAuthIp());
        investorSignInfo.setSignLocation(LOAN_CONTRACT_INVESTOR_SIGN);
        investorSignInfo.setProjectCode(investorAnxinProp.getProjectCode());
        investorSignInfo.setIsProxySign(1);

        createContractVO.setSignInfos(new SignInfoVO[]{agentSignInfo, investorSignInfo});
        createContractVO.setTemplateId(loanTemplate);
        createContractVO.setIsSign(1);
        return createContractVO;
    }

    @Override
    public BaseDto updateContractResponse(long businessId,AnxinContractType anxinContractType) {
        BaseDto baseDto = new BaseDto(true);
        try {
            //查询合同创建结果并更新invest
            List<ContractResponseView> contractResponseViews = anxinSignConnectService.updateContractResponse(businessId,anxinContractType);

            if(CollectionUtils.isEmpty(contractResponseViews)){
                return new BaseDto(false);
            }

            contractResponseViews.forEach(contractResponseView -> {
                if (contractResponseView.getRetCode().equals("60000000")) {
                    if(anxinContractType.equals(AnxinContractType.LOAN_CONTRACT)){
                        investMapper.updateContractNoById(contractResponseView.getInvestId(), contractResponseView.getContractNo());
                    }else{
                        transferApplicationMapper.updateContractNoById(contractResponseView.getInvestId(),contractResponseView.getContractNo());
                    }
                }
            });
        } catch (PKIException e) {
            baseDto.setSuccess(false);
            logger.error(MessageFormat.format("[安心签] update contract response error , loanId:{0}", businessId), e);
        }

        return baseDto;
    }


}
