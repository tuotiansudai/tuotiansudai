package com.tuotiansudai.cfca.service;

import cfca.trustsign.common.vo.cs.CreateContractVO;
import cn.jpush.api.utils.StringUtils;
import com.tuotiansudai.cfca.service.impl.AnxinSignServiceImpl;
import com.tuotiansudai.repository.model.InvestModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by qduljs2011 on 2018/9/5.
 */
@Service
public class CreateContractDataContext {
    private static Logger logger = Logger.getLogger(AnxinSignServiceImpl.class);
    @Autowired
    List<CreateContractDataService> createContractDataServiceList;
    @Value(value = "${anxin.loan.contract.template.v1}")
    protected String defaultTemp;

    public CreateContractVO createInvestorContractVo(long loanId, InvestModel investModel, String contractVersion) {
        CreateContractDataService createContractDataService = getCreateContractDataService(contractVersion);
        if (createContractDataService == null) {
            logger.info(MessageFormat.format("[CreateContractDataContext没有找到对应的数据构建类],contractVersion={0}", contractVersion));
            return null;
        }
        return createContractDataService.createInvestorContractVo(loanId, investModel);
    }

    public CreateContractDataService getCreateContractDataService(String contractVersion) {
        if (StringUtils.isEmpty(contractVersion)) {
            contractVersion = defaultTemp;
        }
        if (createContractDataServiceList == null || createContractDataServiceList.size() == 0) {
            logger.info("[CreateContractDataContext没有找到对应的数据构建类集合]");
        }
        for (CreateContractDataService createContractDataService : createContractDataServiceList) {
            if (contractVersion.equals(createContractDataService.getSupportContractVersion())) {
                return createContractDataService;
            }
        }
        return null;
    }
}
