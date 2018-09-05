package com.tuotiansudai.cfca.service.impl;

import cfca.trustsign.common.vo.cs.CreateContractVO;
import cfca.trustsign.common.vo.cs.SignInfoVO;
import com.google.common.base.Strings;
import com.tuotiansudai.cfca.service.CreateContractDataService;
import com.tuotiansudai.dto.ContractNoStatus;
import com.tuotiansudai.repository.mapper.AnxinSignPropertyMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.AnxinSignPropertyModel;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qduljs2011 on 2018/9/5.
 */
public abstract class AbstractCreateContractDataService implements CreateContractDataService {
    protected static final String LOAN_CONTRACT_AGENT_SIGN = "agentUserName";

    protected static final String LOAN_CONTRACT_INVESTOR_SIGN = "investorUserName";
    private static Logger logger = Logger.getLogger(AbstractCreateContractDataService.class);
    @Autowired
    protected LoanMapper loanMapper;

    @Autowired
    protected AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Autowired
    protected InvestMapper investMapper;

    @Override
    public CreateContractVO createInvestorContractVo(long loanId, InvestModel investModel) {
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
        Map<String,String> dataMap=getLoanDataMap(investModel.getLoginName(), loanId, investId);

        createContractVO.setInvestmentInfo(dataMap);

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
        createContractVO.setTemplateId(getSupportContractVersion());
        createContractVO.setIsSign(1);
        return createContractVO;
    }

    protected abstract Map<String, String> getLoanDataMap(String investorLoginName, long loanId, long investId);
}
