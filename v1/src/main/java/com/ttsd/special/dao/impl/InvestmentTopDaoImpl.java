package com.ttsd.special.dao.impl;

import com.ttsd.special.dao.InvestmentTopDao;
import com.ttsd.special.dto.InvestTopItem;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InvestmentTopDaoImpl implements InvestmentTopDao {

    @Autowired
    private HibernateTemplate ht;

    private static final String INVEST_TOP_SQL = "select" +
            " stat.user_id, u.mobile_number, stat.corpus, stat.interest " +
            " from(" +
            "   select invest.user_id," +
            "   sum(repay.corpus) as corpus, sum(repay.interest) as interest" +
            "   from invest_repay repay" +
            "   inner join invest on repay.invest_id = invest.id" +
            "   where invest.time >= :beginTime and invest.time < :endTime" +
            "   group by invest.user_id" +
            " ) stat" +
            " inner join user u on u.id = stat.user_id" +
            " order by stat.interest desc";

    @Override
    public List<InvestTopItem> StatInvestmentTop(Date beginTime, Date endTime){
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(INVEST_TOP_SQL);
        sqlQuery.setParameter("beginTime", beginTime);
        sqlQuery.setParameter("endTime", endTime);
        List<Object[]> list = sqlQuery.list();
        List<InvestTopItem> returnList=new ArrayList<>();
        for(Object[] items : list){
            InvestTopItem item = new InvestTopItem();
            item.setUserId(String.valueOf(items[0]));
            item.setPhone(String.valueOf(items[1]));
            item.setCorpus(String.format("%.2f",items[2]));
            item.setInterest(String.format("%.2f",items[3]));
            returnList.add(item);
        }
        return returnList;
    }
}
