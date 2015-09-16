package com.ttsd.special.dao.impl;

import com.esoft.archer.system.controller.LoginUserInfo;
import com.ttsd.special.dao.InvestLotteryDao;
import com.ttsd.special.model.InvestLottery;
import com.ttsd.special.model.InvestLotteryType;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InvestLotteryDaoImpl implements InvestLotteryDao {
    @Autowired
    private LoginUserInfo loginUserInfo;

    @Autowired
    private HibernateTemplate ht;

    private static final String INVEST_LOTTERY_BY_TYPE_SQL = "select * from invest_lottery "
                                                             + "where type = ? and user_id = ? and is_valid = '1' order by create_time" ;

    @Override
    public List<InvestLottery> findInvestLotteryByType(InvestLotteryType type) {
        String userId = loginUserInfo.getLoginUserId();
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(INVEST_LOTTERY_BY_TYPE_SQL);
        sqlQuery.setParameter(1,type);
        sqlQuery.setParameter(2,userId);
        List<InvestLottery> list = sqlQuery.list();
        return list;
    }
}
