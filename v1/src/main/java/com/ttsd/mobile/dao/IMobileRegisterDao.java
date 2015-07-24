package com.ttsd.mobile.dao;

import com.esoft.archer.common.model.AuthInfo;
import com.esoft.archer.user.model.User;
import com.esoft.jdp2p.message.MessageConstants;
import com.esoft.jdp2p.message.model.UserMessage;
import com.esoft.jdp2p.message.model.UserMessageTemplate;
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

    /**
     * @function 持久化用户信息到数据库
     * @param user 用户信息
     */
    void persistentUserRegistInfo(User user);

    /**
     * @function 获取短信模版
     * @param clazz
     * @param templateName 短信模版名称
     * @return
     */
    UserMessageTemplate getMessageTemplate(Class clazz,String templateName);

    /**
     * @function 获取用户验证码相关信息
     * @param phoneNum 手机号
     * @param vCode 用户输入的验证码
     * @param authType 授权码类型
     * @return AuthInfo
     */
    int getAuthInfo(String phoneNum,String vCode,String authType);

    /**
     * @function 更新用户授权码状态为：inactive（无效状态）
     * @param phoneNum 手机号
     * @param authType 授权码类型
     */
    void updateUserAuthInfo(String status, String phoneNum, String authType);

}