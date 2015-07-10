package com.ttsd.mobile.service.impl;

import com.ttsd.mobile.dao.IMobileRegisterDao;
import com.ttsd.mobile.service.IMobileRegisterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by tuotian on 15/7/9.
 */
@Service(value = "com.ttsd.mobile.service.impl.MobileRegisterImpl")
public class MobileRegisterImpl implements IMobileRegisterService {
    @Resource(name = "com.ttsd.mobile.dao.impl.MobileRegisterDaoImpl")
    private IMobileRegisterDao mobileRegisterDao;


    /***************************setter注入方法****************************/
    public void setMobileRegisterDao(IMobileRegisterDao mobileRegisterDao) {
        this.mobileRegisterDao = mobileRegisterDao;
    }
}
