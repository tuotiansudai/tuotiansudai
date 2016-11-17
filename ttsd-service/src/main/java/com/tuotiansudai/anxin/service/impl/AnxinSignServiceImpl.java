package com.tuotiansudai.anxin.service.impl;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.cs.SignInfoVO;
import cfca.trustsign.common.vo.response.tx3.Tx3001ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3202ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3ResVO;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.anxin.service.AnxinSignService;
import com.tuotiansudai.cfca.constant.AnxinRetCode;
import com.tuotiansudai.cfca.dto.AnxinContractType;
import com.tuotiansudai.cfca.dto.ContractResponseView;
import com.tuotiansudai.cfca.service.AnxinSignConnectService;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.ContractNoStatus;
import com.tuotiansudai.dto.sms.GenerateContractErrorNotifyDto;
import com.tuotiansudai.job.AnxinQueryContractJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.JobManager;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
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
    private LoanerDetailsMapper loanerDetailsMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private TransferRuleMapper transferRuleMapper;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

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

    private static final int CREATE_CONTRACT_MAX_IN_DOING_TIME = 60 * 30; // 创建合同的时间在半个小时内应该可以完成，如果job出现问题，没能删除InCreating key, 半个小时后，可以再次手动创建合同

    private static final String CONTRACT_TIME_FORMAT = "yyyyMMddHHmmss";

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

            UserModel userModel = userMapper.findByLoginName(loginName);

            Tx3ResVO tx3001ResVO = anxinSignConnectService.createAccount3001(userModel);
            String retMessage = tx3001ResVO.getHead().getRetMessage();

            if (isSuccess(tx3001ResVO)) {
                AnxinSignPropertyModel anxinProp = new AnxinSignPropertyModel();
                anxinProp.setLoginName(loginName);
                Date now = new Date();
                anxinProp.setCreatedTime(now);
                anxinProp.setAnxinUserId(((Tx3001ResVO) tx3001ResVO).getPerson().getUserId());
                anxinSignPropertyMapper.create(anxinProp);
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
            logger.error(MessageFormat.format("[安心签] create contract error, agent has not signed, loanid:{0}, userId:{1}",
                    String.valueOf(loanId), loanModel.getAgentLoginName()));
            return new BaseDto(false);
        }

        List<String> batchNoList = new ArrayList<>();
        boolean processResult = true;

        List<InvestModel> investModels = investMapper.findContractFailInvest(loanId);
        List<CreateContractVO> createContractVOs = new ArrayList<>();

        for (int i = 0; i < investModels.size(); i++) {
            InvestModel investModel = investModels.get(i);
            CreateContractVO createContractVO = createInvestorContractVo(loanId, investModel);
            if (createContractVO == null) {
                continue;
            }
            createContractVOs.add(createContractVO);
            if (createContractVOs.size() == batchNum) {
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
            logger.debug("[安心签]: 创建job，十分钟后，查询并更新合同状态。loanId:" + String.valueOf(loanId));
            updateContractResponseHandleJob(batchNoList, loanId, AnxinContractType.LOAN_CONTRACT);
        }

        redisWrapperClient.setex(LOAN_BATCH_NO_LIST_KEY + loanId, BATCH_NO_LIFT_TIME, String.join(",", batchNoList));
        return new BaseDto(true);
    }

    private Boolean createContractBatch(long loanId, List<CreateContractVO> createContractVOs, List<String> batchNoList) {
        String batchNo = UUIDGenerator.generate();
        try {
            logger.debug(MessageFormat.format("[安心签] create contract begin, loanId:{0}, batchNo:{1}", String.valueOf(loanId), batchNo));
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

    private void updateContractResponseHandleJob(List<String> batchNoList, long businessId, AnxinContractType contractType) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AnxinQueryContractJob.HANDLE_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.ContractResponse, AnxinQueryContractJob.class)
                    .addJobData(AnxinQueryContractJob.BUSINESS_ID, businessId)
                    .addJobData(AnxinQueryContractJob.BATCH_NO_LIST, batchNoList)
                    .addJobData(AnxinQueryContractJob.ANXIN_CONTRACT_TYPE, contractType)
                    .withIdentity(JobType.ContractResponse.name(), "businessId-" + businessId)
                    .replaceExistingJob(true)
                    .runOnceAt(triggerTime)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create query contract job for loan/transfer[" + businessId + "] fail", e);
        }
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
            logger.debug(MessageFormat.format("[安心签] create transfer contract begin, transferId:{0}, batchNo:{1}", transferApplicationId, batchNo));
            //创建合同
            Tx3202ResVO tx3202ResVO = anxinSignConnectService.createContractBatch3202(transferApplicationId, batchNo, AnxinContractType.TRANSFER_CONTRACT, createContractVOs);

            baseDto.setSuccess(isSuccess(tx3202ResVO));
        } catch (PKIException e) {
            smsWrapperClient.sendGenerateContractErrorNotify(new GenerateContractErrorNotifyDto(mobileList, transferApplicationId));

            baseDto.setSuccess(false);
            logger.error(MessageFormat.format("[安心签] create transfer contract error, transferId:{0}", transferApplicationId), e);
        }
        createContractVOs.clear();

        if (baseDto.isSuccess()) {
            logger.debug("[安心签]:创建job，十分钟后，查询并更新合同状态。债权ID:" + transferApplicationId);
            updateContractResponseHandleJob(Arrays.asList(batchNo), transferApplicationId, AnxinContractType.TRANSFER_CONTRACT);
        } else {
            logger.error("[安心签]: create transfer contract error, ready send sms. transferId:" + transferApplicationId);
            smsWrapperClient.sendGenerateContractErrorNotify(new GenerateContractErrorNotifyDto(mobileList, transferApplicationId));
        }

        redisWrapperClient.setex(TRANSFER_BATCH_NO_LIST_KEY + transferApplicationId, BATCH_NO_LIFT_TIME, batchNo);
        return baseDto;
    }

    private CreateContractVO createTransferContractVo(long transferApplicationId) {
        CreateContractVO createContractVO = new CreateContractVO();
        Map<String, String> dataModel = new HashMap<>();
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);

        AnxinSignPropertyModel agentAnxinProp = anxinSignPropertyMapper.findByLoginName(transferApplicationModel.getLoginName());
        if (agentAnxinProp == null || Strings.isNullOrEmpty(agentAnxinProp.getProjectCode())) {
            logger.error(MessageFormat.format("[安心签] create transfer contract error, agent has not signed, transferApplicationId:{0}, userId:{1}", transferApplicationId, transferApplicationModel.getLoginName()));
            return null;
        }

        InvestModel investModel = investMapper.findById(transferApplicationModel.getInvestId());
        AnxinSignPropertyModel investorAnxinProp = anxinSignPropertyMapper.findByLoginName(investModel.getLoginName());
        if (investorAnxinProp == null || Strings.isNullOrEmpty(investorAnxinProp.getProjectCode())) {
            logger.error(MessageFormat.format("[安心签] create transfer contract error, investor has not signed, transferApplicationId:{0}, userId:{1}", transferApplicationId, investModel.getLoginName()));
            return null;
        }

        Map<String, String> transferMap = collectTransferContractModel(transferApplicationId);
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
            logger.error(MessageFormat.format("[安心签] create contract error, investor has not signed. loanid:{0}, investId:{1}, userId:{2}",
                    String.valueOf(loanId), String.valueOf(investId), investLoginName));
            return null;
        }

        Map<String, String> investMap = collectInvestorContractModel(investModel.getLoginName(), loanId, investModel.getId());
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
        contractResponseViews.stream().filter(contractResponseView -> contractResponseView.getRetCode().equals(AnxinRetCode.SUCCESS)).forEach(contractResponseView -> {
            investMapper.updateContractNoById(contractResponseView.getInvestId(), contractResponseView.getContractNo());
        });

        return waitingBatchNoList;
    }

    /**
     * 将合同编号更新为OLD或WAITING
     *
     * @param loanId
     * @return
     */
    @Override
    public BaseDto updateLoanInvestContractNo(long loanId) {

        LoanModel loanModel = loanMapper.findById(loanId);
        String agentLoginName = loanModel.getAgentLoginName();
        AnxinSignPropertyModel agentAnxinProp = anxinSignPropertyMapper.findByLoginName(agentLoginName);

        if (agentAnxinProp == null || StringUtils.isEmpty(agentAnxinProp.getProjectCode())) {
            // update all invest contractNo to "OLD"
            investMapper.updateAllContractNoByLoanId(loanId, ContractNoStatus.OLD.name());
            return new BaseDto();
        }

        List<InvestModel> successInvestList = investMapper.findSuccessInvestsByLoanId(loanId);
        for (InvestModel investModel : successInvestList) {
            String investLoginName = investModel.getLoginName();
            AnxinSignPropertyModel investAnxinProp = anxinSignPropertyMapper.findByLoginName(investLoginName);

            if (investAnxinProp == null || StringUtils.isEmpty(investAnxinProp.getProjectCode())) {
                // update contractNo to "OLD"
                investMapper.updateContractNoById(investModel.getId(), ContractNoStatus.OLD.name());
            } else {
                // update contractNo to "WAITING"
                investMapper.updateContractNoById(investModel.getId(), ContractNoStatus.WAITING.name());
            }
        }

        return new BaseDto();
    }

    /**
     * 将债权转让投资的合同编号更新为OLD或WAITING
     *
     * @param investId
     * @return
     */
    @Override
    public BaseDto updateTransferInvestContractNo(long investId) {

        InvestModel investModel = investMapper.findById(investId);
        String investLoginName = investModel.getLoginName();
        AnxinSignPropertyModel investAnxinProp = anxinSignPropertyMapper.findByLoginName(investLoginName);

        InvestModel transferInvestModel = investMapper.findById(investModel.getTransferInvestId());
        String tranferLoginName = transferInvestModel.getLoginName();
        AnxinSignPropertyModel tranferAnxinProp = anxinSignPropertyMapper.findByLoginName(tranferLoginName);

        if (investAnxinProp == null || StringUtils.isEmpty(investAnxinProp.getProjectCode())
                || tranferAnxinProp == null || StringUtils.isEmpty(tranferAnxinProp.getProjectCode())) {
            // update contractNo to "OLD"
            investMapper.updateContractNoById(investId, ContractNoStatus.OLD.name());
        } else {
            // update contractNo to "WAITING"
            investMapper.updateContractNoById(investId, ContractNoStatus.WAITING.name());
        }

        return new BaseDto();
    }

    @Override
    public Map<String, String> collectTransferContractModel(long transferApplicationId) {
        Map<String, String> dataModel = new HashMap<>();

        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);
        if (null == transferApplicationModel) {
            return dataModel;
        }

        LoanModel loanModel = loanMapper.findById(transferApplicationModel.getLoanId());
        if (loanModel == null) {
            return dataModel;
        }

        UserModel transferUserModel = userMapper.findByLoginName(transferApplicationModel.getLoginName());
        dataModel.put("transferUserName", transferUserModel.getUserName());
        dataModel.put("transferMobile", transferUserModel.getMobile());
        dataModel.put("transferIdentityNumber", transferUserModel.getIdentityNumber());

        InvestModel investModel = investMapper.findById(transferApplicationModel.getInvestId());
        UserModel investUserModel = userMapper.findByLoginName(investModel.getLoginName());
        dataModel.put("transfereeUserName", investUserModel.getUserName());
        dataModel.put("transfereeMobile", investUserModel.getMobile());
        dataModel.put("transfereeIdentityNumber", investUserModel.getIdentityNumber());

        dataModel.put("loanerUserName", loanerDetailsMapper.getByLoanId(loanModel.getId()).getUserName());
        dataModel.put("loanerIdentityNumber", loanModel.getLoanerIdentityNumber());
        dataModel.put("loanAmount", AmountConverter.convertCentToString(loanModel.getLoanAmount()) + "元");
        dataModel.put("totalRate", String.valueOf((loanModel.getBaseRate() + loanModel.getActivityRate() * 100)) + "%");
        dataModel.put("periods", String.valueOf(loanModel.getPeriods() * 30) + "天");

        if (transferApplicationModel.getPeriod() != 1) {
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(transferApplicationModel.getTransferInvestId(), transferApplicationModel.getPeriod() - 1);
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

        dataModel.put("investAmount", AmountConverter.convertCentToString(transferApplicationModel.getInvestAmount()) + "元");
        dataModel.put("transferTime", simpleDateFormat.format(transferApplicationModel.getTransferTime()));
        dataModel.put("leftPeriod", String.valueOf(transferApplicationModel.getLeftPeriod()));

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

        return dataModel;
    }

    @Override
    public Map<String, String> collectInvestorContractModel(String investorLoginName, long loanId, long investId) {
        Map<String, String> dataModel = new HashMap<>();
        LoanModel loanModel = loanMapper.findById(loanId);
        UserModel agentModel = userMapper.findByLoginName(loanModel.getAgentLoginName());
        UserModel investorModel = userMapper.findByLoginName(investorLoginName);
        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investId, loanModel.getPeriods());
        LoanerDetailsModel loanerDetailsModel = loanerDetailsMapper.getByLoanId(loanId);
        InvestModel investModel = investMapper.findById(investId);
        dataModel.put("agentMobile", agentModel.getMobile());
        dataModel.put("agentIdentityNumber", agentModel.getIdentityNumber());
        dataModel.put("investorMobile", investorModel.getMobile());
        dataModel.put("investorIdentityNumber", agentModel.getIdentityNumber());
        dataModel.put("loanerUserName", loanerDetailsModel == null ? "" : loanerDetailsModel.getUserName());
        dataModel.put("loanerIdentityNumber", loanerDetailsModel == null ? "" : loanerDetailsModel.getIdentityNumber());
        dataModel.put("loanAmount", AmountConverter.convertCentToString(loanModel.getLoanAmount()));
        dataModel.put("investAmount", AmountConverter.convertCentToString(investModel.getAmount()));
        dataModel.put("agentPeriods", String.valueOf(loanModel.getPeriods() * 30) + "天");
        dataModel.put("leftPeriods", String.valueOf(loanModel.getPeriods()) + "期");
        dataModel.put("totalRate", String.valueOf((loanModel.getBaseRate() + loanModel.getActivityRate()) * 100) + "%");
        dataModel.put("recheckTime", simpleDateFormat.format(loanModel.getRecheckTime()));
        dataModel.put("endTime", simpleDateFormat.format(investRepayModel.getRepayDate()));
        if (loanModel.getPledgeType().equals(PledgeType.HOUSE)) {
            dataModel.put("pledge", "房屋");
        } else if (loanModel.getPledgeType().equals(PledgeType.VEHICLE)) {
            dataModel.put("pledge", "车辆");
        }
        return dataModel;
    }
}
