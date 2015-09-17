package com.ttsd.special.dao.impl;

import com.ttsd.special.dao.InvestLotteryDao;
import com.ttsd.special.model.InvestLottery;
import com.ttsd.special.model.InvestLotteryType;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class InvestLotteryDaoImpl implements InvestLotteryDao {
    @Autowired
    private HibernateTemplate ht;

    private static final String INVEST_LOTTERY_BY_TYPE_SQL = "select * from invest_lottery "
                                                             + " where user_id = ? and type = ? "
                                                             + " and created_time = ? and is_valid != '1' order by created_time" ;

    @Override
    public List<InvestLottery> findInvestLotteryByType(InvestLotteryType type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        userId = "sidneygao";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String createdTime = simpleDateFormat.format(new Date());
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(INVEST_LOTTERY_BY_TYPE_SQL);
        sqlQuery.addEntity(InvestLottery.class);
        sqlQuery.setParameter(0,userId);
        sqlQuery.setParameter(1,type.name());
        sqlQuery.setParameter(2,createdTime);
        List<InvestLottery> list = sqlQuery.list();
        return list;
    }

    @Override
    public void updateInvestLottery(InvestLottery investLottery) {
        ht.save(investLottery);
    }
}
