package com.esoft.archer.user.service.impl;

import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.umpay.repay.service.impl.UmPayNormalRepayOperation;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2015/8/3.
 */
@Service
public class RepayingLoanReferrerRewardService {

    @Autowired
    private UmPayNormalRepayOperation umPayNormalRepayOperation;

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Logger
    Log log;

    public void reward() {
        try {
            String hql = "from Loan t where t.status in (?,?) and t.giveMoneyTime < ? and t.id <> ?";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Loan> listLoan= hibernateTemplate.find(hql, new Object[]{LoanConstants.LoanStatus.REPAYING,LoanConstants.LoanStatus.COMPLETE, simpleDateFormat.parse("2015-07-29 23:00:00"),"20150727000031"});
//            String hql = "from Loan t where t.id = ?";
//            List<Loan> listLoan= hibernateTemplate.find(hql, new Object[]{"20150727000031"});
            log.info("四级奖励补发共需要处理" + listLoan.size() + "个标的！");
            for (int i = 0; i < listLoan.size(); i++) {
                try {
                    this.umPayNormalRepayOperation.recommendedIncomeReward(listLoan.get(i));
                    log.info("四级奖励标的" + listLoan.get(i).getId() + "处理完毕！");
                } catch (IOException | ReqDataException | RetDataException e) {
                    e.printStackTrace();
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
