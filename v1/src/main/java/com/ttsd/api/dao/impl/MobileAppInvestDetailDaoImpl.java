package com.ttsd.api.dao.impl;

import com.esoft.jdp2p.invest.model.Invest;
import com.ttsd.api.dao.MobileAppInvestDetailDao;
import org.hibernate.SQLQuery;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MobileAppInvestDetailDaoImpl implements MobileAppInvestDetailDao {

    private static  String INVEST_DETAIL_SQL = "select * from invest where id = ? and user_id = ?";

    @Resource
    private HibernateTemplate ht;

    @Override
    public Invest getInvest(String investId, String userId) {

        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(INVEST_DETAIL_SQL);
        sqlQuery.addEntity(Invest.class);
        sqlQuery.setParameter(0, investId);
        sqlQuery.setParameter(1, userId);

        List<Invest> investList = sqlQuery.list();
        if(investList == null || investList.size() ==0){
            return null;
        }else{
            return investList.get(0);
        }
    }
}
