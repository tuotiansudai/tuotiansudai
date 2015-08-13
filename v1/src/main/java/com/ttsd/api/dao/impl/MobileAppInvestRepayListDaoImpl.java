package com.ttsd.api.dao.impl;

import com.esoft.jdp2p.repay.model.InvestRepay;
import com.ttsd.api.dao.MobileAppInvestRepayListDao;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MobileAppInvestRepayListDaoImpl implements MobileAppInvestRepayListDao {
    @Resource
    private HibernateTemplate ht;

    @Override
    public Integer getUserInvestRepayTotalCount(String userId, String[] status) {
        String hql = buildInvestRepayQueryCountHql(status);
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(hql);
        sqlQuery.setParameter(0, userId);
        return ((Number) sqlQuery.uniqueResult()).intValue();
    }

    @Override
    public List<InvestRepay> getUserInvestRepayList(Integer index, Integer pageSize, String userId, String[] status, boolean isOrderByTimeAsc) {
        String hql = buildInvestRepayQueryListHql(status, isOrderByTimeAsc);
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(hql);
        sqlQuery.addEntity(InvestRepay.class);
        sqlQuery.setFirstResult((index - 1) * pageSize);
        sqlQuery.setMaxResults(pageSize);
        sqlQuery.setParameter(0, userId);
        List<InvestRepay> investRepayList = sqlQuery.list();
        return investRepayList;
    }


    private String buildInvestRepayQueryCountHql(String[] status) {
        return buildInvestRepayQueryHql(status, true, false);
    }

    private String buildInvestRepayQueryListHql(String[] status, boolean isOrderByTimeAsc) {
        return buildInvestRepayQueryHql(status, false, isOrderByTimeAsc);
    }

    private String buildInvestRepayQueryHql(String[] status, boolean forCount, boolean isOrderByTimeAsc) {
        StringBuffer sb = new StringBuffer("select ");
        if (forCount) {
            sb.append(" count(*) ");
        } else {
            sb.append(" r.* ");
        }
        sb.append(" from invest_repay r");
        sb.append(" inner join invest i on r.invest_id=i.id ");
        sb.append(" where i.user_id = ? ");
        if (status != null && status.length > 0) {
            if (status.length == 1) {
                sb.append(" and r.status = '" + status[0] + "' ");
            } else {
                sb.append(" and r.status in ('" + StringUtils.join(status, "','") + "') ");
            }
        }
        if (!forCount) {
            if (isOrderByTimeAsc) {
                sb.append(" order by time asc ");
            } else {
                sb.append(" order by time desc ");
            }
        }
        return sb.toString();
    }
}
