package com.ttsd.api.dao.impl;

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
            + " order by seq_Num desc,case status when 'raising' then 1 "
            + " when 'recheck' then 2 when 'repaying' then 3 else 4 end asc,"
            + " commit_time desc limit ?,? ";

    private static String loanListCountSql = "select count(1) from loan where "
            + " status <> 'test' and (status='raising' "
            + " or status='complete' "
            + " or status='recheck' "
            + " or status='repaying')"
            + " order by seq_Num desc,case status when 'raising' then 1 "
            + " when 'recheck' then 2 when 'repaying' then 3 else 4 end asc,"
            + " commit_time desc limit ?,? ";


    @Override
    public String getLimitCondition(String index, String pageSize) {
        return null;
    }

    @Override
    public boolean isHasNextPage(Integer index, Integer pageSize) {
        int indexInt;
        int pageSizeInt;
        if (index == null) {
            indexInt = 1;
        } else {
            indexInt = index.intValue();
        }

        if (pageSize == null) {
            pageSizeInt = 0;
        } else {
            pageSizeInt = pageSize.intValue();
        }
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(loanListCountSql);
        sqlQuery.setParameter(0, indexInt * pageSizeInt);
        sqlQuery.setParameter(1, pageSizeInt);
        return ((Number) sqlQuery.uniqueResult()).intValue() > 0;
    }

    @Override
    public List<InvestDto> getInvestList(Integer index, Integer pageSize) {
        int indexInt;
        int pageSizeInt;
        if (index == null) {
            indexInt = 1;
        } else {
            indexInt = index.intValue();
        }

        if (pageSize == null) {
            pageSizeInt = 0;
        } else {
            pageSizeInt = pageSize.intValue();
        }

        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(loanListSql);
        sqlQuery.setParameter(0, (indexInt - 1) * pageSizeInt);
        sqlQuery.setParameter(1, pageSizeInt);
        List<InvestDto> investList = sqlQuery.list();

        return investList;
    }


}
