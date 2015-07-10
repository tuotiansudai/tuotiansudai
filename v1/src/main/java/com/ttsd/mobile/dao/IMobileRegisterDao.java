package com.ttsd.mobile.dao;

import org.hibernate.annotations.Source;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Created by tuotian on 15/7/9.
 */
public interface IMobileRegisterDao {
    /**
     * @function  根据用户名查找用户
     * @param userName
     * @return Integer 用户数量
     */
    Integer getUserCountByUserName(String userName);

    /**
     * @function 根据手机号查找用户
     * @param userCellphone
     * @return Integer 用户数量
     */
    Integer getUserCountByCellphone(String userCellphone);
}
