package com.ttsd.mobile.dao.impl;

import com.esoft.archer.common.model.AuthInfo;
import com.esoft.archer.user.model.User;
import com.esoft.jdp2p.message.MessageConstants;
import com.esoft.jdp2p.message.model.UserMessageTemplate;
import com.ttsd.mobile.dao.IMobileRegisterDao;
import com.ttsd.mobile.service.IMobileRegisterService;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by tuotian on 15/7/9.
 */
@Repository(value = "mobileRegisterDaoImpl")
public class MobileRegisterDaoImpl implements IMobileRegisterDao {
    @Resource
    private HibernateTemplate hibernateTemplate;

    public Integer getUserCountByUserName(String userName){
        Session session = this.getSession();
        String sql = "select count(1) from USER where username=?";
        SQLQuery sqlQuery = session.createSQLQuery(sql);
        sqlQuery.setParameter(1,userName);
        return ((Number)sqlQuery.uniqueResult()).intValue();
    }

    public Integer getUserCountByCellphone(String userCellphone){
        Session session = this.getSession();
        String sql = "select count(1) from USER where mobile_number=?";
        SQLQuery sqlQuery = session.createSQLQuery(sql);
        sqlQuery.setParameter(1,userCellphone);
        return ((Number)sqlQuery.uniqueResult()).intValue();
    }
    public void persistentUserRegistInfo(User user){
        Session session = this.getSession();
        session.save(user);
    }

    @Override
    public UserMessageTemplate getMessageTemplate(Class clazz, String templateName) {
        return hibernateTemplate.get(UserMessageTemplate.class,templateName);
    }

    @Override
    public int getAuthInfo(String phoneNum, String vCode, String vCodeStatus) {
        Session session = getSession();
        String sql = "select count(1) from auth_info where auth_target=? and auth_code=? and status=? and deadline<=?";
        SQLQuery sqlQuery = session.createSQLQuery(sql);
        sqlQuery.setString(1, phoneNum);
        sqlQuery.setParameter(2,vCode);
        sqlQuery.setParameter(3,new Date());
        return ((Integer)sqlQuery.uniqueResult()).intValue();
    }

    public Session getSession(){
        return hibernateTemplate.getSessionFactory().getCurrentSession();
    }
    /************setter注入方法*************/
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
}
