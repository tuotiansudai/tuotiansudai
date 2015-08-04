package com.ttsd.api.dao.impl;

import com.esoft.jdp2p.loan.model.Loan;
import com.ttsd.api.dao.LoanListDao;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.SQLQuery;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LoanListDaoImpl implements LoanListDao {
    @Resource
    private HibernateTemplate ht;

    private static String loanListSql = "select * from loan where "
            + " status <> 'test' "
            + " AND status in ('raising','complete','recheck','repaying')"
            + " order by case status when 'raising' then 1 "
            + " when 'recheck' then 2 when 'repaying' then 3 else 4 end asc,"
            + " case loan_activity_type when 'xs' then 1 else 2 end asc, "
            + " commit_time desc limit ?,? ";

    @Override
    public boolean isHasNextPage(Integer index, Integer pageSize) {
        int indexInt = index.intValue();
        int pageSizeInt = pageSize.intValue();

        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(loanListSql);
        sqlQuery.addEntity(Loan.class);
        sqlQuery.setParameter(0, indexInt * pageSizeInt);
        sqlQuery.setParameter(1, pageSizeInt);
        List<Loan> investList = sqlQuery.list();

        return CollectionUtils.isNotEmpty(investList);
    }

    @Override
    public List<Loan> getInvestList(Integer index, Integer pageSize) {
        int indexInt = index.intValue();
        int pageSizeInt = pageSize.intValue();


        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(loanListSql);
        sqlQuery.addEntity(Loan.class);
        sqlQuery.setParameter(0, (indexInt - 1) * pageSizeInt);
        sqlQuery.setParameter(1, pageSizeInt);
        List<Loan> investList = sqlQuery.list();

        return investList;
    }


}
