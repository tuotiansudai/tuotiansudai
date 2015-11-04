package com.ttsd.api.dao.impl;

import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.loan.model.Loan;
import com.ttsd.api.dao.MobileAppLoanListDao;
import org.hibernate.SQLQuery;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MobileAppLoanListDaoImpl implements MobileAppLoanListDao {
    @Resource
    private HibernateTemplate ht;

    private static String loanListSql = "select * from loan where "
            + " status in ('raising','complete','recheck','repaying')"
            + " order by case status when 'raising' then 1 "
            + " when 'recheck' then 2 when 'repaying' then 3 else 4 end asc,"
            + " case loan_activity_type when 'xs' then 1 else 2 end asc, "
            + " commit_time desc limit ?,? ";

    private static String loanListCountSql = "select count(*) from loan where "
            + " status in ('raising','complete','recheck','repaying') ";

    private static String loanCompleteSql = "select * from loan where status = 'complete' "
            + " and loan_activity_type = 'xs' order by commit_time desc limit 1 ";

    private static String raiseCompletedTimeSql = "select max(time) from invest where loan_id = ? "
            + " and status in ('" + InvestConstants.InvestStatus.BID_SUCCESS+"','"+ InvestConstants.InvestStatus.COMPLETE+"','"+InvestConstants.InvestStatus.REPAYING+"')";



    @Override
    public Integer getTotalCount() {
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(loanListCountSql);

        return ((Number)sqlQuery.uniqueResult()).intValue();
    }

    @Override
    public List<Loan> getLoanList(Integer index, Integer pageSize) {
        int indexInt = index.intValue();
        int pageSizeInt = pageSize.intValue();


        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(loanListSql);
        sqlQuery.addEntity(Loan.class);
        sqlQuery.setParameter(0, (indexInt - 1) * pageSizeInt);
        sqlQuery.setParameter(1, pageSizeInt);
        List<Loan> investList = sqlQuery.list();

        return investList;
    }

    @Override
    public List<Loan> getCompletedXsInvest() {
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(loanCompleteSql);
        sqlQuery.addEntity(Loan.class);
        List<Loan> loans = sqlQuery.list();
        return loans;
    }

    @Override
    public Date getRaiseCompletedTime(String loanId) {
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(raiseCompletedTimeSql);
        sqlQuery.setParameter(0,loanId);
        return  sqlQuery.list() != null?(Date)sqlQuery.list().get(0):null;
    }


}
