package com.tuotiansudai.service.impl;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.cs.SignInfoVO;
import cfca.trustsign.common.vo.response.tx3.Tx3001ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3101ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3102ResVO;
import cfca.trustsign.common.vo.response.tx3.Tx3ResVO;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.cfca.service.AnxinSignConnectService;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.AnxinSignService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private AnxinSignConnectService anxinSignConnectService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanerDetailsMapper loanerDetailsMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AnxinSignService anxinSignService;

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    private static final String TEMP_PROJECT_CODE_KEY = "temp_project_code:";

    private static final int TEMP_PROJECT_CODE_EXPIRE_TIME = 1800; // 半小时

    private static final String SIGN_LOCATION_AGENT_LOGIN_NAME = "agentLoginName";

    private static final String SIGN_LOCATION_INVESTOR_LOGIN_NAME = "investorLoginName";


    /**
     * 以前是否授权过
     */
    @Override
    public boolean hasAuthedBefore(String loginName) {
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
            AccountModel accountModel = accountMapper.findByLoginName(loginName);
            UserModel userModel = userMapper.findByLoginName(loginName);

            Tx3001ResVO tx3001ResVO = anxinSignConnectService.createAccount3001(accountModel, userModel);
            String retMessage = tx3001ResVO.getHead().getRetMessage();

            if (isSuccess(tx3001ResVO)) {

                AnxinSignPropertyModel anxinProp = new AnxinSignPropertyModel();
                anxinProp.setLoginName(loginName);
                Date now = new Date();
                anxinProp.setCreatedTime(now);
                anxinProp.setAnxinUserId(tx3001ResVO.getPerson().getUserId());
                anxinSignPropertyMapper.create(anxinProp);

                accountMapper.update(accountModel);
                return new BaseDto();
            } else {
                logger.error("create anxin sign account failed. " + retMessage);
                return failBaseDto();
            }

        } catch (PKIException e) {
            logger.error("create anxin sign account failed. ", e);
            return failBaseDto();
        }
    }


    @Override
    public BaseDto sendCaptcha3101(String loginName, boolean isVoice) {
        try {
            // 如果用户没有开通安心签账户，则先开通账户，再进行授权（发送验证码）
            if (!hasAnxinAccount(loginName)) {
                BaseDto createAccountRet = this.createAccount3001(loginName);
                if (!createAccountRet.isSuccess()) {
                    return failBaseDto();
                }
            }

            String anxinUserId = anxinSignPropertyMapper.findByLoginName(loginName).getAnxinUserId();

            String projectCode = UUIDGenerator.generate();

            Tx3101ResVO tx3101ResVO = anxinSignConnectService.sendCaptcha3101(anxinUserId, projectCode, isVoice);

            String retMessage = tx3101ResVO.getHead().getRetMessage();

            if (isSuccess(tx3101ResVO)) {
                redisWrapperClient.setex(TEMP_PROJECT_CODE_KEY + loginName, TEMP_PROJECT_CODE_EXPIRE_TIME, projectCode);
                return new BaseDto();
            } else {
                logger.error("send anxin captcha code failed. " + retMessage);
                return failBaseDto();
            }

        } catch (PKIException e) {
            logger.error("send anxin captcha code failed. ", e);
            return failBaseDto();
        }
    }

    /**
     * 确认验证码 （授权）
     *
     * @param loginName
     * @param captcha
     * @param skipAuth
     * @return
     */
    @Override
    public BaseDto verifyCaptcha3102(String loginName, String captcha, boolean skipAuth, String ip) {

        try {
            // 如果用户没有开通安心签账户，则返回失败
            if (!hasAnxinAccount(loginName)) {
                logger.error("user has not create anxin account yet. loginName: " + loginName);
                return failBaseDto();
            }

            AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);

            String anxinUserId = anxinProp.getAnxinUserId();

            String projectCode = redisWrapperClient.get(TEMP_PROJECT_CODE_KEY + loginName);

            if (StringUtils.isEmpty(projectCode)) {
                logger.error("project code is expired. loginName:" + loginName + ", anxinUserId:" + anxinUserId);
                return failBaseDto();
            }

            Tx3102ResVO tx3101ResVO = anxinSignConnectService.verifyCaptcha3102(anxinUserId, projectCode, captcha);

            String retMessage = tx3101ResVO.getHead().getRetMessage();

            if (isSuccess(tx3101ResVO)) {
                // 更新projectCode 和 skipAuth
                anxinProp.setProjectCode(projectCode);
                anxinProp.setSkipAuth(skipAuth);
                anxinProp.setAuthTime(new Date());
                anxinProp.setAuthIp(ip);
                anxinSignPropertyMapper.update(anxinProp);
                return new BaseDto();
            } else {
                logger.error("verify anxin captcha code failed. " + retMessage);
                return failBaseDto();
            }

        } catch (PKIException e) {
            logger.error("verify anxin captcha code failed. ", e);
            return failBaseDto();
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
            return failBaseDto();
        }
        return new BaseDto();
    }


    private BaseDto failBaseDto() {
        BaseDto baseDto = new BaseDto<>();
        baseDto.setSuccess(false);
        return baseDto;
    }

    private boolean isSuccess(Tx3ResVO tx3ResVO) {
        return tx3ResVO.getHead().getRetCode().equals("60000000");
    }

    private boolean hasAnxinAccount(String loginName) {
        AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loginName);
        return anxinProp != null && StringUtils.isNotEmpty(anxinProp.getAnxinUserId());
    }


    @Override
    public BaseDto createContracts(long loanId) {
        List<CreateContractVO> createContractVOs = Lists.newArrayList();
        investMapper.findSuccessInvestsByLoanId(loanId).forEach(investModel -> createContractVOs.add(collectInvestorContractModel(investModel.getLoginName(), loanId, investModel.getId())));
        try {
            anxinSignConnectService.generateContractBatch3202(loanId,UUIDGenerator.generate(), createContractVOs);
        } catch (PKIException e) {
            e.printStackTrace();
        }
        return new BaseDto();
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

        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investId, loanModel.getPeriods());
        LoanerDetailsModel loanerDetailsModel = loanerDetailsMapper.getByLoanId(loanId);

        dataModel.put("agentMobile", agentModel.getMobile());
        dataModel.put("agentIdentityNumber", agentAccount.getIdentityNumber());
        dataModel.put("investorMobile", investorModel.getMobile());
        dataModel.put("investorIdentityNumber", investorAccount.getIdentityNumber());
        dataModel.put("loanerUserName", loanerDetailsModel.getUserName());
        dataModel.put("loanerIdentityNumber", loanerDetailsModel.getIdentityNumber());
        dataModel.put("loanAmount", AmountConverter.convertCentToString(loanModel.getLoanAmount()));
        dataModel.put("periods", String.valueOf(loanModel.getPeriods()));
        dataModel.put("totalRate", String.valueOf(loanModel.getBaseRate()));
        dataModel.put("recheckTime", new DateTime(loanModel.getRecheckTime()).toString("yyyy-MM-dd"));
        dataModel.put("endTime", new DateTime(investRepayModel.getRepayDate()).toString("yyyy-MM-dd"));
        dataModel.put("investId", String.valueOf(investorModel.getId()));
        if (loanModel.getPledgeType().equals(PledgeType.HOUSE)) {
            dataModel.put("pledge", "房屋");
        } else if (loanModel.getPledgeType().equals(PledgeType.VEHICLE)) {
            dataModel.put("pledge", "车辆");
        }
        createContractVO.setInvestmentInfo(dataModel);

        AnxinSignPropertyModel agentSignModel = anxinSignPropertyMapper.findByLoginName(agentModel.getLoginName());
        SignInfoVO agentSignInfo = new SignInfoVO();
        agentSignInfo.setUserId("4C6E9CB56A6F7EE05311016B0A6939A19C");
        agentSignInfo.setAuthorizationTime(new DateTime(agentSignModel.getAuthTime()).toString("yyyyMMddHHmmss"));
        agentSignInfo.setLocation(agentSignModel.getAuthIp());
        agentSignInfo.setSignLocation(SIGN_LOCATION_AGENT_LOGIN_NAME);
        agentSignInfo.setProjectCode(String.valueOf(loanModel.getId()));
        agentSignInfo.setIsProxySign(1);

        AnxinSignPropertyModel investorSignModel = anxinSignPropertyMapper.findByLoginName(investorModel.getLoginName());
        SignInfoVO investorSignInfo = new SignInfoVO();
        investorSignInfo.setUserId("27A45BC4BC29E9E05311016B0AA19C6939");
        investorSignInfo.setAuthorizationTime(new DateTime(investorSignModel.getAuthTime()).toString("yyyyMMddHHmmss"));
        investorSignInfo.setLocation(Strings.isNullOrEmpty(investorModel.getCity()) ? "北京" : investorModel.getCity());
        investorSignInfo.setSignLocation(SIGN_LOCATION_INVESTOR_LOGIN_NAME);
        investorSignInfo.setProjectCode(String.valueOf(loanModel.getId()));
        investorSignInfo.setIsProxySign(1);

        createContractVO.setSignInfos(new SignInfoVO[]{agentSignInfo, investorSignInfo});
        createContractVO.setTemplateId("JK_108");
        return createContractVO;
    }


}
