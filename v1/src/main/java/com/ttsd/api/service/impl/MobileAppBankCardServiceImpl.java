package com.ttsd.api.service.impl;

import com.ttsd.api.dao.MobileAppBankCardDao;
import com.ttsd.api.service.MobileAppBankCardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service(value = "mobileAppBankCardServiceImpl")
public class MobileAppBankCardServiceImpl implements MobileAppBankCardService {

    @Resource(name = "mobileAppBankCardDaoImpl")
    private MobileAppBankCardDao mobileAppBankCardDao;
    /**
     * @function 查询绑卡状态
     * @param userId 绑卡或签约用户ID
     * @return boolean
     */
    @Override
    public boolean queryBindAndSginStatus(String userId, String operationType) {
        int count = mobileAppBankCardDao.queryBindAndSginStatus(userId,operationType);
        if (count > 0){
            return true;
        }else {
            return false;
        }
    }
}
