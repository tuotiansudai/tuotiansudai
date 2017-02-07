package com.tuotiansudai.anxin.service.impl;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.cs.SignInfoVO;
import cfca.trustsign.common.vo.response.tx3.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.anxin.service.AnxinSignService;
import com.tuotiansudai.cfca.constant.AnxinRetCode;
import com.tuotiansudai.cfca.dto.AnxinContractType;
import com.tuotiansudai.cfca.dto.ContractResponseView;
import com.tuotiansudai.cfca.service.AnxinSignConnectService;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.contract.service.ContractService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.ContractNoStatus;
import com.tuotiansudai.dto.sms.GenerateContractErrorNotifyDto;
import com.tuotiansudai.job.DelayMessageDeliveryJob;
import com.tuotiansudai.job.DelayMessageDeliveryJobCreator;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.message.AnxinContractQueryMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.AnxinSignPropertyMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AnxinSignPropertyModel;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.TransferApplicationModel;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.*;

@Service
public class AnxinSignServiceImpl implements AnxinSignService {

    static Logger logger = Logger.getLogger(AnxinSignServiceImpl.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private AnxinSignConnectService anxinSignConnectService;

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private ContractService contractService;

    private static final String LOAN_CONTRACT_AGENT_SIGN = "agentUserName";

    private static final String LOAN_CONTRACT_INVESTOR_SIGN = "investorUserName";

    private static final String TRANSFER_LOAN_CONTRACT_AGENT_SIGN = "transferUserName";

    private static final String TRANSFER_LOAN_CONTRACT_INVESTOR_SIGN = "transfereeUserName";

    private static final String TEMP_PROJECT_CODE_KEY = "temp_project_code:";

    public static final String LOAN_CONTRACT_IN_CREATING_KEY = "loanContractInCreating:";

    public static final String LOAN_BATCH_NO_LIST_KEY = "loanBathNoList:";

    public static final String TRANSFER_CONTRACT_IN_CREATING_KEY = "transferContractInCreating:";

    public static final String TRANSFER_BATCH_NO_LIST_KEY = "transferBathNoList:";

    private static final int TEMP_PROJECT_CODE_EXPIRE_TIME = 60 * 30; // 验证码30分钟过期

    private static final int BATCH_NO_LIFT_TIME = 60 * 60 * 24 * 7; // bath_NO 在redis里保存7天

    private static final int CREATE_CONTRACT_MAX_IN_DOING_TIME = 60 * 90; // 给创建合同预留90分钟，如果job出现问题，没能删除InCreating key, 90分钟后，可以再次手动创建合同

    private static final String CONTRACT_TIME_FORMAT = "yyyyMMddHHmmss";

    @Value(value = "${anxin.contract.batch.num}")
    private int batchSize;

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

    @Override
    public boolean isAuthenticationRequired(String loginName) {
        boolean anxinSwitch = Strings.isNullOrEmpty(redisWrapperClient.hget("anxin-sign:switch", "switch")) ? true : Boolean.valueOf(redisWrapperClient.hget("anxin-sign:switch", "switch"));
        String whitelist = Strings.isNullOrEmpty(redisWrapperClient.hget("anxin-sign:switch", "whitelist")) ? "" : redisWrapperClient.hget("anxin-sign:switch", "whitelist");
        if (!anxinSwitch && !whitelist.contains(userMapper.findByLoginName(loginName).getMobile())) {
            return false;
        }
        AnxinSignPropertyModel model = anxinSignPropertyMapper.findByLoginName(loginName);
        return model == null || !model.isSkipAuth();
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
    @Transactional
    @Override
    public BaseDto createAccount3001(String loginName) {

        userMapper.lockByLoginName(loginName);

        try {
            UserModel userModel = userMapper.findByLoginName(loginName);

            if (userModel == null) {
                logger.error("create anxin user not exist, loginName:" + loginName);
                return failBaseDto("请求参数错误：用户不存在");
            }

            if (hasAnxinAccount(loginName)) {
                logger.error(loginName + " already have anxin-sign account. can't create anymore.");
                return new BaseDto();
            }


            Tx3001ResVO tx3001ResVO = anxinSignConnectService.createAccount3001(userModel);

            if (isSuccess(tx3001ResVO)) {
                AnxinSignPropertyModel anxinProp = new AnxinSignPropertyModel();
                anxinProp.setLoginName(loginName);
                Date now = new Date();
                anxinProp.setCreatedTime(now);
                anxinProp.setAnxinUserId(tx3001ResVO.getPerson().getUserId());
                anxinSignPropertyMapper.create(anxinProp);
                return new BaseDto();
            } else {
                if (tx3001ResVO == null) {
                    logger.error("create anxin sign account failed. result is null.");
                    return failBaseDto("连接超时");
                }
                String retMessage = tx3001ResVO.getHead().getRetMessage();
                logger.error("create anxin sign account failed. " + retMessage);
                return failBaseDto(retMessage);
            }
        } catch (PKIException e) {
            logger.error("create anxin sign account failed. ", e);
            return new BaseDto(false);
        }
    }


    /**
     * 发送验证码
     */
    @Transactional
    @Override
    public BaseDto sendCaptcha3101(String loginName, boolean isVoice) {

        userMapper.lockByLoginName(loginName);

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

            Tx3101ResVO tx3101ResVO = anxinSignConnectService.sendCaptcha3101(anxinUserId, projectCode, isVoice);

            if (isSuccess(tx3101ResVO)) {
                redisWrapperClient.setex(TEMP_PROJECT_CODE_KEY + loginName, TEMP_PROJECT_CODE_EXPIRE_TIME, projectCode);
                return new BaseDto();
            } else {
                if (tx3101ResVO == null) {
                    logger.error("send anxin captcha code failed. result is null.");
                    return failBaseDto("连接超时");
                }
                String retMessage = tx3101ResVO.getHead().getRetMessage();
                logger.error("send anxin captcha code failed. " + retMessage);
                return failBaseDto(retMessage);
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
    public BaseDto verifyCaptcha3102(String loginName, String captcha, boolean skipAuth, String ip) {

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
                logger.warn("project code is expired. loginName:" + loginName + ", anxinUserId:" + anxinUserId);
                return failBaseDto("验证码已过期，请重新获取");
            }

            Tx3102ResVO tx3102ResVO = anxinSignConnectService.verifyCaptcha3102(anxinUserId, projectCode, captcha);

            if (isSuccess(tx3102ResVO)) {
                // 更新projectCode 和 skipAuth
                anxinProp.setProjectCode(projectCode);
                anxinProp.setSkipAuth(skipAuth);
                anxinProp.setAuthTime(new Date());
                anxinProp.setAuthIp(ip);
                anxinSignPropertyMapper.update(anxinProp);
                BaseDto<BaseDataDto> baseDto = new BaseDto<>();
                baseDto.setData(new BaseDataDto(true, skipAuth ? "skipAuth" : ""));
                return baseDto;
            } else {
                if (tx3102ResVO == null) {
                    logger.error("verify anxin captcha code failed. result is null.");
                    return failBaseDto("连接超时");
                }
                String retMessage = tx3102ResVO.getHead().getRetMessage();
                logger.info("verify anxin captcha code failed. " + retMessage);
                return failBaseDto(retMessage);
            }

        } catch (PKIException e) {
            logger.error("verify anxin captcha code failed. ", e);
            return new BaseDto(false);
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
        return tx3ResVO != null && tx3ResVO.getHead() != null && AnxinRetCode.SUCCESS.equals(tx3ResVO.getHead().getRetCode());
    }

    private boolean hasAnxinAccount(String loginName) {
        AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);
        return anxinProp != null && StringUtils.isNotEmpty(anxinProp.getAnxinUserId());
    }

    @Override
    public byte[] downContractByContractNo(String contractNo) {
        byte[] contract = null;
        try {
            contract = anxinSignConnectService.downLoanContractByBatchNo(contractNo);
        } catch (PKIException | FileNotFoundException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return contract;
    }


    @Override
    public BaseDto createLoanContracts(long loanId) {
        redisWrapperClient.setex(LOAN_CONTRACT_IN_CREATING_KEY + loanId, CREATE_CONTRACT_MAX_IN_DOING_TIME, "1");

        LoanModel loanModel = loanMapper.findById(loanId);
        AnxinSignPropertyModel agentAnxinProp = anxinSignPropertyMapper.findByLoginName(loanModel.getAgentLoginName());
        if (agentAnxinProp == null || Strings.isNullOrEmpty(agentAnxinProp.getProjectCode())) {
            // 如果 代理人/借款人 未授权安心签，该标的所有投资都使用旧版合同，更新contractNo为OLD
            investMapper.updateAllContractNoByLoanId(loanId, ContractNoStatus.OLD.name());
            logger.error(MessageFormat.format("[安心签] create contract error, agent has not signed, loanid:{0}, userId:{1}",
                    String.valueOf(loanId), loanModel.getAgentLoginName()));
            return new BaseDto(false);
        }

        List<String> batchNoList = new ArrayList<>();
        boolean processResult = true;

        List<InvestModel> investModels = investMapper.findNoContractNoInvest(loanId);
        List<CreateContractVO> createContractVOs = new ArrayList<>();

        for (int i = 0; i < investModels.size(); i++) {
            InvestModel investModel = investModels.get(i);
            CreateContractVO createContractVO = createInvestorContractVo(loanId, investModel);
            if (createContractVO == null) {
                continue;
            }
            createContractVOs.add(createContractVO);
            if (createContractVOs.size() == batchSize) {
                if (!createContractBatch(loanId, createContractVOs, batchNoList)) {
                    processResult = false;
                }
                createContractVOs.clear();
            }
        }
        // 循环结束，如果列表里还有未处理的，则给它们创建合同
        if (!createContractVOs.isEmpty()) {
            if (!createContractBatch(loanId, createContractVOs, batchNoList)) {
                processResult = false;
            }
        }

        if (!processResult) {
            logger.error("[安心签]: create contract error. loanId:" + String.valueOf(loanId));
            smsWrapperClient.sendGenerateContractErrorNotify(new GenerateContractErrorNotifyDto(mobileList, loanId));
        }

        if (CollectionUtils.isNotEmpty((batchNoList))) {
            logger.info("[安心签]: 创建job，稍后查询并更新合同状态。loanId:" + String.valueOf(loanId));
            DelayMessageDeliveryJobCreator.createAnxinContractQueryDelayJob(jobManager,
                    loanId, AnxinContractType.LOAN_CONTRACT.name(), batchNoList);
        }

        redisWrapperClient.setex(LOAN_BATCH_NO_LIST_KEY + loanId, BATCH_NO_LIFT_TIME, String.join(",", batchNoList));
        return new BaseDto(true);
    }

    private Boolean createContractBatch(long loanId, List<CreateContractVO> createContractVOs, List<String> batchNoList) {
        String batchNo = UUIDGenerator.generate();
        try {
            logger.info(MessageFormat.format("[安心签] create contract begin, loanId:{0}, batchNo:{1}", String.valueOf(loanId), batchNo));
            //创建合同
            Tx3202ResVO tx3202ResVO = anxinSignConnectService.createContractBatch3202(loanId, batchNo, AnxinContractType.LOAN_CONTRACT, createContractVOs);

            if (!isSuccess(tx3202ResVO)) {
                return false;
            }
            batchNoList.add(batchNo);
        } catch (PKIException e) {
            logger.error(MessageFormat.format("[安心签] create contract error, loanId:{0}, batchNo:{1}", String.valueOf(loanId), batchNo), e);
            return false;
        }
        return true;
    }

    @Override
    public BaseDto createTransferContracts(long transferApplicationId) {
        redisWrapperClient.setex(TRANSFER_CONTRACT_IN_CREATING_KEY + transferApplicationId, CREATE_CONTRACT_MAX_IN_DOING_TIME, "1");

        CreateContractVO createContractVO = createTransferContractVo(transferApplicationId);
        if (createContractVO == null) {
            return new BaseDto(false);
        }
        List<CreateContractVO> createContractVOs = Lists.newArrayList(createContractVO);
        BaseDto baseDto = new BaseDto();
        String batchNo = UUIDGenerator.generate();
        try {
            logger.info(MessageFormat.format("[安心签] create transfer contract begin, transferId:{0}, batchNo:{1}", transferApplicationId, batchNo));
            //创建合同
            Tx3202ResVO tx3202ResVO = anxinSignConnectService.createContractBatch3202(transferApplicationId, batchNo, AnxinContractType.TRANSFER_CONTRACT, createContractVOs);

            baseDto.setSuccess(isSuccess(tx3202ResVO));
        } catch (PKIException e) {
            smsWrapperClient.sendGenerateContractErrorNotify(new GenerateContractErrorNotifyDto(mobileList, transferApplicationId));

            baseDto.setSuccess(false);
            logger.error(MessageFormat.format("[安心签] create transfer contract error, transferId:{0}", transferApplicationId), e);
        }
        createContractVOs.clear();

        redisWrapperClient.setex(TRANSFER_BATCH_NO_LIST_KEY + transferApplicationId, BATCH_NO_LIFT_TIME, batchNo);

        if (baseDto.isSuccess()) {
            logger.info("[安心签]: 创建job，稍后查询并更新合同状态。债权ID:" + transferApplicationId);
            DelayMessageDeliveryJobCreator.createAnxinContractQueryDelayJob(jobManager,
                    transferApplicationId, AnxinContractType.TRANSFER_CONTRACT.name(), Collections.singletonList(batchNo));
        } else {
            logger.error("[安心签]: create transfer contract error, ready send sms. transferId:" + transferApplicationId);
            smsWrapperClient.sendGenerateContractErrorNotify(new GenerateContractErrorNotifyDto(mobileList, transferApplicationId));
        }

        return baseDto;
    }

    private CreateContractVO createTransferContractVo(long transferApplicationId) {
        CreateContractVO createContractVO = new CreateContractVO();
        Map<String, String> dataModel = new HashMap<>();
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);

        AnxinSignPropertyModel agentAnxinProp = anxinSignPropertyMapper.findByLoginName(transferApplicationModel.getLoginName());
        if (agentAnxinProp == null || Strings.isNullOrEmpty(agentAnxinProp.getProjectCode())) {
            // 如果转让人未授权安心签，则将该投资的合同号设置为OLD，使用旧版合同：
            investMapper.updateContractNoById(transferApplicationModel.getInvestId(), ContractNoStatus.OLD.name());
            logger.warn(MessageFormat.format("[安心签] create transfer contract fail, agent has not signed, transferApplicationId:{0}, userId:{1}", transferApplicationId, transferApplicationModel.getLoginName()));
            return null;
        }

        InvestModel investModel = investMapper.findById(transferApplicationModel.getInvestId());
        AnxinSignPropertyModel investorAnxinProp = anxinSignPropertyMapper.findByLoginName(investModel.getLoginName());
        if (investorAnxinProp == null || Strings.isNullOrEmpty(investorAnxinProp.getProjectCode())) {
            // 如果承接人未授权安心签，则将该投资的合同号设置为OLD，使用旧版合同：
            investMapper.updateContractNoById(investModel.getId(), ContractNoStatus.OLD.name());
            logger.warn(MessageFormat.format("[安心签] create transfer contract fail, investor has not signed, transferApplicationId:{0}, userId:{1}", transferApplicationId, investModel.getLoginName()));
            return null;
        }

        Map<String, String> transferMap = contractService.collectTransferContractModel(transferApplicationId);
        dataModel.put("transferMobile", transferMap.get("transferMobile"));
        dataModel.put("transferIdentity", transferMap.get("transferIdentityNumber"));
        dataModel.put("transfereeMobile", transferMap.get("transfereeMobile"));
        dataModel.put("transfereeIdentity", transferMap.get("transfereeIdentityNumber"));
        dataModel.put("userName", transferMap.get("loanerUserName"));
        dataModel.put("identity", transferMap.get("loanerIdentityNumber"));
        dataModel.put("amount", transferMap.get("loanAmount"));
        dataModel.put("totalRate", transferMap.get("totalRate"));
        dataModel.put("periods", transferMap.get("periods"));
        dataModel.put("transferStartTime", transferMap.get("transferStartTime"));
        dataModel.put("transferEndTime", transferMap.get("transferEndTime"));
        dataModel.put("investAmount", transferMap.get("investAmount"));
        dataModel.put("transferTime", transferMap.get("transferTime"));
        dataModel.put("leftPeriod", transferMap.get("leftPeriod"));
        dataModel.put("orderId", String.valueOf(transferApplicationModel.getInvestId()));
        dataModel.put("msg1", transferMap.get("msg1"));
        dataModel.put("msg2", transferMap.get("msg2"));
        dataModel.put("msg3", transferMap.get("msg3"));

        createContractVO.setInvestmentInfo(dataModel);

        SignInfoVO agentSignInfo = new SignInfoVO();
        agentSignInfo.setUserId(agentAnxinProp.getAnxinUserId());
        agentSignInfo.setAuthorizationTime(new DateTime(agentAnxinProp.getAuthTime()).toString(CONTRACT_TIME_FORMAT));
        agentSignInfo.setLocation(agentAnxinProp.getAuthIp());
        agentSignInfo.setSignLocation(TRANSFER_LOAN_CONTRACT_AGENT_SIGN);
        agentSignInfo.setProjectCode(agentAnxinProp.getProjectCode());
        agentSignInfo.setIsProxySign(1);

        SignInfoVO investorSignInfo = new SignInfoVO();
        investorSignInfo.setUserId(investorAnxinProp.getAnxinUserId());
        investorSignInfo.setAuthorizationTime(new DateTime(investorAnxinProp.getAuthTime()).toString(CONTRACT_TIME_FORMAT));
        investorSignInfo.setLocation(investorAnxinProp.getAuthIp());
        investorSignInfo.setSignLocation(TRANSFER_LOAN_CONTRACT_INVESTOR_SIGN);
        investorSignInfo.setProjectCode(investorAnxinProp.getProjectCode());
        investorSignInfo.setIsProxySign(1);

        createContractVO.setSignInfos(new SignInfoVO[]{agentSignInfo, investorSignInfo});
        createContractVO.setTemplateId(transferTemplate);
        createContractVO.setIsSign(1);

        return createContractVO;
    }

    private CreateContractVO createInvestorContractVo(long loanId, InvestModel investModel) {
        CreateContractVO createContractVO = new CreateContractVO();
        Map<String, String> dataModel = new HashMap<>();

        // 标的
        LoanModel loanModel = loanMapper.findById(loanId);

        // 借款人（代理人 or 企业借款人）
        AnxinSignPropertyModel agentAnxinProp = anxinSignPropertyMapper.findByLoginName(loanModel.getAgentLoginName());

        // 投资人
        long investId = investModel.getId();
        String investLoginName = investModel.getLoginName();
        AnxinSignPropertyModel investorAnxinProp = anxinSignPropertyMapper.findByLoginName(investLoginName);

        if (investorAnxinProp == null || Strings.isNullOrEmpty(investorAnxinProp.getProjectCode())) {
            // 如果投资人未授权安心签，则将该笔投资的合同号设置为OLD，使用旧版合同：
            investMapper.updateContractNoById(investModel.getId(), ContractNoStatus.OLD.name());
            logger.warn(MessageFormat.format("[安心签] create contract fail, investor has not signed. loanid:{0}, investId:{1}, userId:{2}",
                    String.valueOf(loanId), String.valueOf(investId), investLoginName));
            return null;
        }

        Map<String, String> investMap = contractService.collectInvestorContractModel(investModel.getLoginName(), loanId, investModel.getId());
        dataModel.put("agentMobile", investMap.get("agentMobile"));
        dataModel.put("agentIdentityNumber", investMap.get("agentIdentityNumber"));
        dataModel.put("investorMobile", investMap.get("investorMobile"));
        dataModel.put("investorIdentityNumber", investMap.get("investorIdentityNumber"));
        dataModel.put("loanerUserName", investMap.get("loanerUserName"));
        dataModel.put("loanerIdentityNumber", investMap.get("loanerIdentityNumber"));
        dataModel.put("loanAmount1", investMap.get("loanAmount"));
        dataModel.put("loanAmount2", investMap.get("investAmount"));
        dataModel.put("periods1", investMap.get("agentPeriods"));
        dataModel.put("periods2", investMap.get("leftPeriods"));
        dataModel.put("totalRate", investMap.get("totalRate"));
        dataModel.put("recheckTime1", investMap.get("recheckTime"));
        dataModel.put("recheckTime2", investMap.get("recheckTime"));
        dataModel.put("endTime1", investMap.get("endTime"));
        dataModel.put("endTime2", investMap.get("endTime"));
        dataModel.put("orderId", String.valueOf(investId));
        dataModel.put("pledge", investMap.get("pledge"));
        createContractVO.setInvestmentInfo(dataModel);

        SignInfoVO agentSignInfo = new SignInfoVO();
        agentSignInfo.setUserId(agentAnxinProp.getAnxinUserId());
        agentSignInfo.setAuthorizationTime(new DateTime(agentAnxinProp.getAuthTime()).toString(CONTRACT_TIME_FORMAT));
        agentSignInfo.setLocation(agentAnxinProp.getAuthIp());
        agentSignInfo.setSignLocation(LOAN_CONTRACT_AGENT_SIGN);
        agentSignInfo.setProjectCode(agentAnxinProp.getProjectCode());
        agentSignInfo.setIsProxySign(1);

        SignInfoVO investorSignInfo = new SignInfoVO();
        investorSignInfo.setUserId(investorAnxinProp.getAnxinUserId());
        investorSignInfo.setAuthorizationTime(new DateTime(investorAnxinProp.getAuthTime()).toString(CONTRACT_TIME_FORMAT));
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
    public List<String> queryContract(long businessId, List<String> batchNoList, AnxinContractType anxinContractType) {

        //查询合同创建结果，以及处理中的batchNo
        List[] lists = anxinSignConnectService.queryContract(businessId, batchNoList, anxinContractType);

        // 合同还没有生成完毕的batchNo
        List<String> waitingBatchNoList = lists[0];

        // 处理结果
        List<ContractResponseView> contractResponseViews = lists[1];

        // 把合同号更新到 invest 或 transferApplication 表
        contractResponseViews.stream().filter(resView -> resView.getRetCode().equals(AnxinRetCode.SUCCESS))
                .forEach(resView -> investMapper.updateContractNoById(resView.getInvestId(), resView.getContractNo()));

        return waitingBatchNoList;
    }


}
