package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.service.InvestService;
import com.tuotiansudai.paywrapper.service.InvestSuccessService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.util.AmountTransfer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvestSuccessServiceImpl implements InvestSuccessService{

    static Logger logger = Logger.getLogger(InvestSuccessServiceImpl.class);

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestService investService;

    /**
     * 投资成功处理：冻结资金＋更新invest状态
     *
     * @param orderId
     * @param investModel
     * @param loginName
     */
    @Override
    public void investSuccess(long orderId, InvestModel investModel, String loginName) {
        try {
            // 冻结资金
            amountTransfer.freeze(loginName, orderId, investModel.getAmount(), UserBillBusinessType.INVEST_SUCCESS, null, null);
        } catch (AmountTransferException e) {
            // 记录日志，发短信通知管理员
            investService.fatalLog("invest success, but freeze account fail", String.valueOf(orderId), investModel.getAmount(), loginName, investModel.getLoanId(), e);
        }
        // 改invest 本身状态为投资成功
        investMapper.updateStatus(investModel.getId(), InvestStatus.SUCCESS);
    }

}
