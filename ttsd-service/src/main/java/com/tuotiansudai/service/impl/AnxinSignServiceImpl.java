package com.tuotiansudai.service.impl;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.cs.SignInfoVO;
import cfca.trustsign.common.vo.response.tx3.Tx3001ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3ResVO;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.tuotiansudai.cfca.dto.AnxinContractType;
import com.tuotiansudai.cfca.dto.ContractResponseView;
import com.tuotiansudai.cfca.service.AnxinSignConnectService;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.sms.GenerateContractErrorNotifyDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.AnxinSignService;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnxinSignServiceImpl implements AnxinSignService {

    static Logger logger = Logger.getLogger(AnxinSignServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;
    @Autowired
    private LoanerDetailsMapper loanerDetailsMapper;

    @Autowired
    private AnxinSignConnectService anxinSignConnectService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private TransferRuleMapper transferRuleMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String TEMP_PROJECT_CODE_KEY = "temp_project_code:";

    private static final int TEMP_PROJECT_CODE_EXPIRE_TIME = 1800; // 半小时

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

    /**
     * 以前是否授权过
     */
    @Override
    public boolean hasAuthed(String loginName) {
        AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);
        return anxinProp != null && anxinProp.getAnxinUserId() != null && anxinProp.getProjectCode() != null;
    }


    /**
     * 获取用户的安心签相关属性
     */
    @Override
    public AnxinSignPropertyModel getAnxinSignProp(String loginName) {
        return anxinSignPropertyMapper.findByLoginName(loginName);
    }

    /**
     * 创建安心签账户
     */
    @Override
    public BaseDto createAccount3001(String loginName) {

        try {
            if (hasAnxinAccount(loginName)) {
                logger.error(loginName + " already have anxin-sign account. can't create anymore.");
                return failBaseDto("用户已有安心签账户，不能重复开户");
            }

            AccountModel accountModel = accountMapper.findByLoginName(loginName);
            UserModel userModel = userMapper.findByLoginName(loginName);

            Tx3ResVO tx3001ResVO = anxinSignConnectService.createAccount3001(accountModel, userModel);
            String retMessage = tx3001ResVO.getHead().getRetMessage();

            if (isSuccess(tx3001ResVO)) {

                AnxinSignPropertyModel anxinProp = new AnxinSignPropertyModel();
                anxinProp.setLoginName(loginName);
                Date now = new Date();
                anxinProp.setCreatedTime(now);
                anxinProp.setAnxinUserId(((Tx3001ResVO) tx3001ResVO).getPerson().getUserId());
                anxinSignPropertyMapper.create(anxinProp);

                accountMapper.update(accountModel);
                return new BaseDto();
            } else {
                logger.error("create anxin sign account failed. " + retMessage);
                return new BaseDto(false);
            }

        } catch (PKIException e) {
            logger.error("create anxin sign account failed. ", e);
            return new BaseDto(false);
        }
    }


    /**
     * 发送验证码
     */
    @Override
    public BaseDto sendCaptcha3101(String loginName, boolean isVoice) {
        try {
            // 如果用户没有开通安心签账户，则先开通账户，再进行授权（发送验证码）
            if (!hasAnxinAccount(loginName)) {
                BaseDto createAccountRet = this.createAccount3001(loginName);
                if (!createAccountRet.isSuccess()) {
                    return createAccountRet;
                }
            }

            String anxinUserId = anxinSignPropertyMapper.findByLoginName(loginName).getAnxinUserId();

            String projectCode = UUIDGenerator.generate();

            Tx3ResVO tx3101ResVO = anxinSignConnectService.sendCaptcha3101(anxinUserId, projectCode, isVoice);

            if (isSuccess(tx3101ResVO)) {
                redisWrapperClient.setex(TEMP_PROJECT_CODE_KEY + loginName, TEMP_PROJECT_CODE_EXPIRE_TIME, projectCode);
                return new BaseDto();
            } else {
                logger.error("send anxin captcha code failed. " + tx3101ResVO.getHead().getRetMessage());
                return new BaseDto(false);
            }

        } catch (PKIException e) {
            logger.error("send anxin captcha code failed. ", e);
            return new BaseDto(false);
        }
    }

    /**
     * 确认验证码 （授权）
     */
    @Override
    public BaseDto<BaseDataDto> verifyCaptcha3102(String loginName, String captcha, boolean skipAuth, String ip) {

        try {
            // 如果用户没有开通安心签账户，则返回失败
            if (!hasAnxinAccount(loginName)) {
                logger.error("user has not create anxin account yet. loginName: " + loginName);
                return failBaseDto("用户还未开通安心签账户");
            }

            AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);

            String anxinUserId = anxinProp.getAnxinUserId();

            String projectCode = redisWrapperClient.get(TEMP_PROJECT_CODE_KEY + loginName);

            if (StringUtils.isEmpty(projectCode)) {
                logger.error("project code is expired. loginName:" + loginName + ", anxinUserId:" + anxinUserId);
                return failBaseDto("验证码已过期，请重新获取");
            }

            Tx3ResVO tx3101ResVO = anxinSignConnectService.verifyCaptcha3102(anxinUserId, projectCode, captcha);

            if (isSuccess(tx3101ResVO)) {
                // 更新projectCode 和 skipAuth
                anxinProp.setProjectCode(projectCode);
                anxinProp.setSkipAuth(skipAuth);
                anxinProp.setAuthTime(new Date());
                anxinProp.setAuthIp(ip);
                anxinSignPropertyMapper.update(anxinProp);
                BaseDto baseDto = new BaseDto();
                baseDto.setData(new BaseDataDto(true, skipAuth ? "skipAuth" : ""));
                return baseDto;
            } else {
                String retMessage = tx3101ResVO.getHead().getRetMessage();
                logger.error("verify anxin captcha code failed. " + retMessage);
                return failBaseDto(retMessage);
            }

        } catch (PKIException e) {
            logger.error("verify anxin captcha code failed. ", e);
            return new BaseDto<>(false);
        }
    }

    /**
     * 打开/关闭 免验开关
     */
    @Override
    public BaseDto switchSkipAuth(String loginName, boolean open) {
        try {
            logger.info(loginName + " is switching anxin-sign skip-auth " + (open ? "on." : "off."));
            AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);
            anxinProp.setSkipAuth(open);
            anxinSignPropertyMapper.update(anxinProp);
        } catch (Exception e) {
            logger.error("switch anxin-sign skip-auth " + (open ? "on " : "off ") + "failed.", e);
            return new BaseDto(false);
        }
        return new BaseDto();
    }

    private BaseDto<BaseDataDto> failBaseDto(String errorMessage) {
        BaseDataDto dataDto = new BaseDataDto(false, errorMessage);
        return new BaseDto<>(false, dataDto);
    }

    private boolean isSuccess(Tx3ResVO tx3ResVO) {
        return tx3ResVO != null && tx3ResVO.getHead() != null && "60000000".equals(tx3ResVO.getHead().getRetCode());
    }

    private boolean hasAnxinAccount(String loginName) {
        AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);
        return anxinProp != null && StringUtils.isNotEmpty(anxinProp.getAnxinUserId());
    }


    @Override
    public BaseDto createContracts(long loanId) {
        List<CreateContractVO> createContractVOs = Lists.newArrayList();
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanId);
        InvestModel investModel;
        BaseDto baseDto = new BaseDto();
        LoanModel loanModel = loanMapper.findById(loanId);
        AnxinSignPropertyModel agentAnxinProp = anxinSignPropertyMapper.findByLoginName(loanModel.getAgentLoginName());
        if(Strings.isNullOrEmpty(agentAnxinProp.getProjectCode())){
            logger.error(MessageFormat.format("[安心签] create contract error,agentModel is not anxin sign , loanid:{0} , userId:{1}", loanId,loanModel.getAgentLoginName()));
            return new BaseDto(true);
        }
        for (int i = 0; i < investModels.size(); i++) {
            investModel = investModels.get(i);
            CreateContractVO createContractVO = collectInvestorContractModel(investModel.getLoginName(), loanId, investModel.getId());
            if(createContractVO == null){
                continue;
            }
            createContractVOs.add(createContractVO);
            if (createContractVOs.size() == batchNum || investModels.size() == (i + 1)) {
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
        CreateContractVO createContractVO = collectTransferContractModel(transferApplicationId);
        if(createContractVO == null){
            logger.error(MessageFormat.format("[安心签] create transfer contract error,users is not anxin sign , transferApplicationId:{0}", transferApplicationId));
            return new BaseDto(true);
        }
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

        AccountModel transfereeAccountModel = accountMapper.findByLoginName(transferApplicationModel.getLoginName());
        AnxinSignPropertyModel agentAnxinProp = anxinSignPropertyMapper.findByLoginName(transferApplicationModel.getLoginName());
        if (transfereeAccountModel != null) {
            dataModel.put("transferMobile", userMapper.findByLoginName(transfereeAccountModel.getLoginName()).getMobile());
            dataModel.put("transferIdentity", transfereeAccountModel.getIdentityNumber());
        }

        InvestModel investModel = investMapper.findById(transferApplicationModel.getInvestId());
        AccountModel investAccountModel = accountMapper.findByLoginName(investModel.getLoginName());
        AnxinSignPropertyModel investorAnxinProp = anxinSignPropertyMapper.findByLoginName(investModel.getLoginName());
        if (investAccountModel != null) {
            dataModel.put("transferreMobile", userMapper.findByLoginName(investAccountModel.getLoginName()).getMobile());
            dataModel.put("transferreIdentity", investAccountModel.getIdentityNumber());
        }

        if(Strings.isNullOrEmpty(agentAnxinProp.getProjectCode())){
            logger.error(MessageFormat.format("[安心签] create transfer contract error,agentAnxinProp is not anxin sign , transferApplicationId:{0} , userId:{1}", transferApplicationId,transferApplicationModel.getLoginName()));
            return null;
        }

        if(Strings.isNullOrEmpty(investorAnxinProp.getProjectCode()) ){
            logger.error(MessageFormat.format("[安心签] create transfer contract error,investorAnxinProp is not anxin sign , transferApplicationId:{0} , userId:{1}", transferApplicationId,investModel.getLoginName()));
            return null;
        }

        LoanModel loanModel = loanMapper.findById(transferApplicationModel.getLoanId());
        if (null != loanModel) {
            dataModel.put("userName", loanerDetailsMapper.getByLoanId(loanModel.getId()).getUserName());
            dataModel.put("identity", loanModel.getLoanerIdentityNumber());
            dataModel.put("amount", AmountConverter.convertCentToString(loanModel.getLoanAmount()));
            dataModel.put("totalRate", String.valueOf((loanModel.getBaseRate() + loanModel.getActivityRate()) * 100));
            dataModel.put("periods", String.valueOf(loanModel.getPeriods()));
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
        AccountModel agentAccount = accountMapper.findByLoginName(loanModel.getAgentLoginName());
        AnxinSignPropertyModel agentAnxinProp = anxinSignPropertyMapper.findByLoginName(loanModel.getAgentLoginName());

        // 投资人
        UserModel investorModel = userMapper.findByLoginName(investorLoginName);
        AccountModel investorAccount = accountMapper.findByLoginName(investorLoginName);
        AnxinSignPropertyModel investorAnxinProp = anxinSignPropertyMapper.findByLoginName(investorLoginName);

        if(Strings.isNullOrEmpty(investorAnxinProp.getProjectCode())){
            logger.error(MessageFormat.format("[安心签] create contract error,investorAnxinProp is not anxin sign , loanid:{0} , investId:{1} ,userId:{2}", loanId,investId,investorLoginName));
            return null;
        }
        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investId, loanModel.getPeriods());
        LoanerDetailsModel loanerDetailsModel = loanerDetailsMapper.getByLoanId(loanId);

        dataModel.put("agentMobile", agentModel.getMobile());
        dataModel.put("agentIdentityNumber", agentAccount.getIdentityNumber());
        dataModel.put("investorMobile", investorModel.getMobile());
        dataModel.put("investorIdentityNumber", investorAccount.getIdentityNumber());
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
    public byte[] downContractByContractNo(String contractNo) {
        byte[] contract = null;
        try {
            contract = anxinSignConnectService.downLoanContractByBatchNo(contractNo);
        } catch (PKIException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return contract;
    }

    @Override
    public BaseDto updateContractResponse(long businessId,AnxinContractType anxinContractType) {
        BaseDto baseDto = new BaseDto(true);
        try {
            //查询合同创建结果并更新invest
            List<ContractResponseView> contractResponseViews = anxinSignConnectService.updateContractResponse(businessId,anxinContractType);

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
