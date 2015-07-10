package com.ttsd.mobile.dao.impl;

import com.ttsd.mobile.service.IMobileRegisterService;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by tuotian on 15/7/9.
 */
@Repository(name="com.ttsd.mobile.dao.impl.MobileRegisterDaoImpl")
public class MobileRegisterDaoImpl implements IMobileRegisterService {
    @Resource
    private HibernateTemplate hibernateTemplate;

    public Integer getUserCountByUserName(){
        Session session = this.getSession();
        String sql = "select count(1) from USER where username=?";
        SQLQuery sqlQuery = session.createSQLQuery(sql);
        sqlQuery.setParameter(0, 1);
        return ((Number)sqlQuery.uniqueResult()).intValue();
    }


    public Session getSession(){
        return hibernateTemplate.getSessionFactory().getCurrentSession();
    }
    /************setter注入方法*************/
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
}
