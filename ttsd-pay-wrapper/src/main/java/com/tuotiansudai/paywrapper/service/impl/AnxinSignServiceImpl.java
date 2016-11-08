package com.tuotiansudai.paywrapper.service.impl;

import cfca.sadk.algorithm.common.PKIException;
import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.cs.SignInfoVO;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.tuotiansudai.cfca.dto.ContractResponseView;
import com.tuotiansudai.cfca.service.AnxinSignConnectService;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.sms.GenerateContractErrorNotifyDto;
import com.tuotiansudai.paywrapper.service.AnxinSignService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
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

    @Value(value = "${anxin.contract.batch.num}")
    private int batchNum;

    @Value(value = "${anxin.loan.contract.template}")
    private String templateId;

    @Value("#{'${anxin.contract.notify.mobileList}'.split('\\|')}")
    private List<String> mobileList;

    private static final String SIGN_LOCATION_AGENT_LOGIN_NAME = "agentLoginName";

    private static final String SIGN_LOCATION_INVESTOR_LOGIN_NAME = "investorLoginName";

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
                    anxinSignConnectService.generateContractBatch3202(loanId, batchNo, createContractVOs);

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
        dataModel.put("investId", String.valueOf(investId));
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
        agentSignInfo.setSignLocation(SIGN_LOCATION_AGENT_LOGIN_NAME);
        agentSignInfo.setProjectCode(agentAnxinProp.getProjectCode());
        agentSignInfo.setIsProxySign(1);

        SignInfoVO investorSignInfo = new SignInfoVO();
        investorSignInfo.setUserId(investorAnxinProp.getAnxinUserId());
        investorSignInfo.setAuthorizationTime(new DateTime(investorAnxinProp.getAuthTime()).toString("yyyyMMddHHmmss"));
        investorSignInfo.setLocation(investorAnxinProp.getAuthIp());
        investorSignInfo.setSignLocation(SIGN_LOCATION_INVESTOR_LOGIN_NAME);
        investorSignInfo.setProjectCode(investorAnxinProp.getProjectCode());
        investorSignInfo.setIsProxySign(1);

        createContractVO.setSignInfos(new SignInfoVO[]{agentSignInfo, investorSignInfo});
        createContractVO.setTemplateId(templateId);
        createContractVO.setIsSign(1);
        return createContractVO;
    }

    @Override
    public BaseDto updateContractResponse(long loanId) {
        BaseDto baseDto = new BaseDto(true);
        try {
            //查询合同创建结果并更新invest
            List<ContractResponseView> contractResponseViews = anxinSignConnectService.updateContractResponse(loanId);

            contractResponseViews.forEach(contractResponseView -> {
                if (contractResponseView.getRetCode().equals("60000000")) {
                    investMapper.updateContractNoById(contractResponseView.getInvestId(), contractResponseView.getContractNo());
                }
            });
        } catch (PKIException e) {
            baseDto.setSuccess(false);
            logger.error(MessageFormat.format("[安心签] update contract response error , loanId:{0}", loanId), e);
        }

        return baseDto;
    }


}
