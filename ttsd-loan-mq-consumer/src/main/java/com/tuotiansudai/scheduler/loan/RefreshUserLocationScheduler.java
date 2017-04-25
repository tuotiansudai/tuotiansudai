package com.tuotiansudai.scheduler.loan;

import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.MobileLocationUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
public class RefreshUserLocationScheduler {
    private static Logger logger = LoggerFactory.getLogger(RefreshUserLocationScheduler.class);
    @Autowired
    private UserMapper userMapper;

    //@Scheduled(cron = "0 0 2 * * ?", zone = "Asia/Shanghai")
    @Scheduled(cron = "0 0 13 * * ?", zone = "Asia/Shanghai")
    public void refreshUserLocation() {
        logger.info("RefreshUserLocationScheduler start ...");
        try {
            while (true) {
                List<UserModel> userModels = userMapper.findUsersByProvince();
                if (CollectionUtils.isEmpty(userModels)) {
                    break;
                }
                ((RefreshUserLocationScheduler) AopContext.currentProxy()).refreshUserLocation(userModels);
            }
        } catch (Exception e) {
            logger.error("[RefreshUserLocationScheduler:] job execution is failed.", e);
        }
        logger.info("RefreshUserLocationScheduler end ...");

    }

    @Transactional
    public void refreshUserLocation(List<UserModel> userModels) {
        for (UserModel userModel : userModels) {
            String phoneMobile = userModel.getMobile();
            if (StringUtils.isNotEmpty(phoneMobile)) {
                String[] provinceAndCity = MobileLocationUtils.locateMobileNumber(phoneMobile);
                if (StringUtils.isEmpty(provinceAndCity[0])) {
                    provinceAndCity[0] = "未知";
                }
                if (StringUtils.isEmpty(provinceAndCity[1])) {
                    provinceAndCity[1] = "未知";
                }
                userModel.setProvince(provinceAndCity[0]);
                userModel.setCity(provinceAndCity[1]);
            } else {
                userModel.setProvince("未知");
                userModel.setCity("未知");
            }
            userModel.setLastModifiedTime(new Date());
            userMapper.updateUser(userModel);
        }
    }
}
