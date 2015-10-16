package com.ttsd.api.dao.impl;

import com.esoft.archer.config.model.Config;
import com.esoft.jdp2p.invest.InvestConstants;
import com.esoft.jdp2p.invest.model.Invest;
import com.ttsd.api.dao.MobileAppInvestListDao;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MobileAppInvestListDaoImpl implements MobileAppInvestListDao {
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
        sqlQuery.setParameter(0, loanId);
        return ((Number) sqlQuery.uniqueResult()).intValue();
    }

    @Override
    public List<Invest> getInvestList(Integer index, Integer pageSize, String loanId) {

        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(investListSql);
        sqlQuery.addEntity(Invest.class);
        sqlQuery.setParameter(0, loanId);
        sqlQuery.setParameter(1, (index - 1) * pageSize);
        sqlQuery.setParameter(2, pageSize);
        List<Invest> investList = sqlQuery.list();

        return investList;
    }

    @Override
    public Integer getUserInvestTotalCount(String userId, String[] status) {
        String hql = buildInvestQueryHql(status, true);
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(hql);
        sqlQuery.setParameter(0, userId);
        return ((Number) sqlQuery.uniqueResult()).intValue();
    }

    @Override
    public List<Invest> getUserInvestList(Integer index, Integer pageSize, String userId, String[] status) {
        String hql = buildInvestQueryHql(status,false);
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(hql);
        sqlQuery.addEntity(Invest.class);
        sqlQuery.setFirstResult((index - 1) * pageSize);
        sqlQuery.setMaxResults(pageSize);
        sqlQuery.setParameter(0, userId);
        List<Invest> investList = sqlQuery.list();
        return investList;
    }

    private String buildInvestQueryHql(String[] status, boolean forCount) {
        StringBuffer sb = new StringBuffer("select ");
        if(forCount){
            sb.append(" count(*) ");
        }else {
            sb.append(" * ");
        }
        sb.append(" from invest ");
        sb.append(" where user_id = ? ");
        if (status != null && status.length > 0) {
            if (status.length == 1) {
                sb.append(" and invest.status = '" + status[0] + "' ");
            } else {
                sb.append(" and invest.status in ('" + StringUtils.join(status, "','") + "') ");
            }
        }
        if(!forCount){
            sb.append(" order by invest.time desc");
        }
        return sb.toString();
    }
    @Override
    public int getConfigIntValue(String configId) {
        Config config = ht.get(Config.class, configId);

        if (config != null) {
            return Integer.parseInt(config.getValue());
        }

        return 0;
    }
}
