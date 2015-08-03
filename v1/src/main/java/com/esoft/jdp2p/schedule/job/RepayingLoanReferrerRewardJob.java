package com.esoft.jdp2p.schedule.job;

import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.umpay.repay.service.impl.UmPayNormalRepayOperation;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2015/8/2.
 */
@Component
public class RepayingLoanReferrerRewardJob implements Job {

    @Autowired
    private UmPayNormalRepayOperation umPayNormalRepayOperation;

    @Autowired
    private HibernateTemplate hibernateTemplate;

    Log log = LogFactory.getLog(RepayingLoanReferrerRewardJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            String hql = "from Loan t where t.status in (?,?) and t.giveMoneyTime < ?";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Loan> listLoan= hibernateTemplate.find(hql, new Object[]{LoanConstants.LoanStatus.REPAYING,LoanConstants.LoanStatus.COMPLETE, simpleDateFormat.parse("2015-04-27 00:00:00")});
            log.info("四级奖励补发共需要处理" + listLoan.size() + "个标的！");
            for (int i = 0; i < listLoan.size(); i++) {
                try {
                    this.umPayNormalRepayOperation.recommendedIncome(listLoan.get(i));
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
