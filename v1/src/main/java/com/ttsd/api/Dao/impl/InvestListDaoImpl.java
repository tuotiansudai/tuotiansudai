package com.ttsd.api.dao.impl;

import com.esoft.jdp2p.loan.model.Loan;
import com.ttsd.api.dao.InvestListDao;
import com.ttsd.api.dto.InvestDto;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class InvestListDaoImpl implements InvestListDao {
    @Resource
    private HibernateTemplate ht;

    private static String loanListSql = "select * from loan where "
            + " status <> 'test' and (status='raising' "
            + " or status='complete' "
            + " or status='recheck' "
            + " or status='repaying')"
            + " order by case status when 'raising' then 1 "
            + " when 'recheck' then 2 when 'repaying' then 3 else 4 end asc,"
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

        return investList != null && investList.size() > 0;
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
