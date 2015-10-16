package com.ttsd.api.dao.impl;

import com.esoft.archer.user.model.UserBill;
import com.esoft.jdp2p.loan.model.Loan;
import com.ttsd.api.dao.MobileAppLoanListDao;
import com.ttsd.api.dao.MobileAppUserBillListDao;
import org.hibernate.SQLQuery;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MobileAppUserBillListDaoImpl implements MobileAppUserBillListDao {
    @Resource
    private HibernateTemplate ht;

    private static String userBilltSql = "select * from user_bill where "
            + " user_id like ? ORDER BY seq_num desc limit ?,?";

    private static String userBillCountSql = "select count(*) from user_bill where "
            + " user_id like ? ";



    @Override
    public Integer getTotalCount(String userId) {
        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(userBillCountSql);
        sqlQuery.setParameter(0,userId);
        return ((Number)sqlQuery.uniqueResult()).intValue();
    }

    @Override
    public List<UserBill> getUserBillList(Integer index, Integer pageSize,String userId) {
        int indexInt = index.intValue();
        int pageSizeInt = pageSize.intValue();


        SQLQuery sqlQuery = ht.getSessionFactory().getCurrentSession().createSQLQuery(userBilltSql);
        sqlQuery.addEntity(UserBill.class);
        sqlQuery.setParameter(0,userId);
        sqlQuery.setParameter(1, (indexInt - 1) * pageSizeInt);
        sqlQuery.setParameter(2, pageSizeInt);
        List<UserBill> userBillList = sqlQuery.list();

        return userBillList;
    }


}
