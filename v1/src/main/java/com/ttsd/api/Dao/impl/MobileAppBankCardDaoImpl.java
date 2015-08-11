package com.ttsd.api.dao.impl;

import com.esoft.jdp2p.bankcard.model.BankCard;
import com.ttsd.api.dao.MobileAppBankCardDao;
import com.ttsd.api.dto.MobileAppCommonConstants;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import weibo4j.model.Query;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tuotian on 15/8/7.
 */
@Repository(value = "mobileAppBankCardDaoImpl")
public class MobileAppBankCardDaoImpl implements MobileAppBankCardDao{
    @Resource
    private HibernateTemplate ht;

    /**
     * @function 查询绑卡或签约状态
     * @param userId 绑卡或签约用户ID
     * @param operationType 操作类型
     * @return int
     */
    @Override
    public int queryBindAndSginStatus(String userId,String operationType) {
        Session session = ht.getSessionFactory().getCurrentSession();
        String sql = "select count(1) from bank_card where user_id=? and status='passed'";
        if (MobileAppCommonConstants.QUERY_SIGN_STATUS.equals(operationType)){
            sql += " and is_open_fastPayment=true";
        }
        SQLQuery sqlQuery = session.createSQLQuery(sql);
        sqlQuery.setParameter(0, userId);
        return ((Number)sqlQuery.uniqueResult()).intValue();
    }
}
