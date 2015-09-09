package com.ttsd.api.dao.impl;

import com.esoft.jdp2p.loan.model.Loan;
import com.ttsd.api.dao.MobileAppLoanDetailDao;
import org.hibernate.SQLQuery;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MobileAppLoanDetailDaoImpl implements MobileAppLoanDetailDao {
    @Resource
    private HibernateTemplate ht;
    public static String loanDetailSql = "select * from loan where id = ? ";

    @Override
    public Loan getLoanById(String loanId) {
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(loanDetailSql);
        sqlQuery.addEntity(Loan.class);
        sqlQuery.setParameter(0, loanId);

        return (Loan) sqlQuery.uniqueResult();
    }


}
