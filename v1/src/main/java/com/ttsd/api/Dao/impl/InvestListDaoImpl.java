package com.ttsd.api.dao.impl;

import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.model.Invest;
import com.ttsd.api.dao.InvestListDao;
import org.hibernate.SQLQuery;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class InvestListDaoImpl implements InvestListDao {
    @Resource
    private HibernateTemplate ht;

    private static String investListSql = "select * from invest where "
            + " loan_id = ? and status not in ('" + InvestConstants.InvestStatus.WAIT_AFFIRM + "','"
            + InvestConstants.InvestStatus.CANCEL + "','" + InvestConstants.InvestStatus.UNFINISHED + "') "
            + " order by time desc limit ?,?";

    private static String investListCountSql = "select count(*) from invest where "
            + " loan_id = ? and status not in ('" + InvestConstants.InvestStatus.WAIT_AFFIRM + "','"
            + InvestConstants.InvestStatus.CANCEL + "','" + InvestConstants.InvestStatus.UNFINISHED + "') ";


    @Override
    public Integer getTotalCount(String loanId) {
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(investListCountSql);
        sqlQuery.setParameter(0,loanId);
        return ((Number) sqlQuery.uniqueResult()).intValue();
    }

    @Override
    public List<Invest> getInvestList(Integer index, Integer pageSize,String loanId) {
        int indexInt = index.intValue();
        int pageSizeInt = pageSize.intValue();


        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(investListSql);
        sqlQuery.addEntity(Invest.class);
        sqlQuery.setParameter(0,loanId);
        sqlQuery.setParameter(1, (indexInt - 1) * pageSizeInt);
        sqlQuery.setParameter(2, pageSizeInt);
        List<Invest> investList = sqlQuery.list();

        return investList;
    }


}
